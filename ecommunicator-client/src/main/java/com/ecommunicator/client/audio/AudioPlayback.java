package com.ecommunicator.client.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Plays back PCM audio frames received from remote participants.
 * Thread-safe: frames are queued and played on a dedicated thread.
 */
public class AudioPlayback {

    private static final Logger log = LoggerFactory.getLogger(AudioPlayback.class);

    private static final int MAX_QUEUE = 50; // ~1 second of 20ms frames

    private SourceDataLine speaker;
    private Thread playbackThread;
    private volatile boolean running = false;
    private boolean muted = false;

    private final BlockingQueue<byte[]> frameQueue = new LinkedBlockingQueue<>(MAX_QUEUE);

    // ── Public API ────────────────────────────────────────────────────────────

    public void start() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, AudioCapture.FORMAT);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Speaker line not supported: " + AudioCapture.FORMAT);
        }
        speaker = (SourceDataLine) AudioSystem.getLine(info);
        speaker.open(AudioCapture.FORMAT, 3200);  // 100ms buffer
        speaker.start();
        running = true;

        playbackThread = new Thread(this::playLoop, "audio-playback");
        playbackThread.setDaemon(true);
        playbackThread.start();
        log.info("Audio playback started");
    }

    public void stop() {
        running = false;
        frameQueue.clear();
        if (playbackThread != null) {
            playbackThread.interrupt();
        }
        if (speaker != null) {
            speaker.drain();
            speaker.close();
        }
        log.info("Audio playback stopped");
    }

    /**
     * Enqueue a PCM frame for playback.
     * If the queue is full (jitter), the oldest frame is dropped.
     */
    public void enqueue(byte[] pcmFrame) {
        if (muted) return;
        if (!frameQueue.offer(pcmFrame)) {
            frameQueue.poll();  // Drop oldest frame (jitter recovery)
            frameQueue.offer(pcmFrame);
        }
    }

    public void setMuted(boolean m) {
        this.muted = m;
        if (m) frameQueue.clear();
    }

    public boolean isMuted() { return muted; }

    /** Adjust speaker volume [0.0 – 1.0]. */
    public void setVolume(float volume) {
        if (speaker == null) return;
        FloatControl fc = (FloatControl) speaker.getControl(FloatControl.Type.MASTER_GAIN);
        // Convert linear 0..1 to dB
        float dB = volume <= 0.0f ? fc.getMinimum() : (float)(20.0 * Math.log10(volume));
        fc.setValue(Math.max(fc.getMinimum(), Math.min(fc.getMaximum(), dB)));
    }

    // ── Playback loop ─────────────────────────────────────────────────────────

    private void playLoop() {
        while (running) {
            try {
                byte[] frame = frameQueue.take();
                if (speaker != null && speaker.isOpen()) {
                    speaker.write(frame, 0, frame.length);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
