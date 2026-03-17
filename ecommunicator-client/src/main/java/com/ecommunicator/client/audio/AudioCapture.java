package com.ecommunicator.client.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Captures microphone audio and delivers 20 ms PCM frames (s16le, 16 kHz, mono).
 *
 * Each frame is 640 bytes: 16000 samples/sec × 0.020 sec × 2 bytes/sample = 640 bytes.
 * Frames are delivered to a callback for transmission.
 */
public class AudioCapture {

    private static final Logger log = LoggerFactory.getLogger(AudioCapture.class);

    /** 16 kHz, 16-bit, mono — matches the PROTOCOL.md spec. */
    public static final AudioFormat FORMAT =
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);

    private static final int FRAME_MS    = 20;
    private static final int FRAME_BYTES = (int)(FORMAT.getSampleRate() * FORMAT.getFrameSize() * FRAME_MS / 1000);

    private TargetDataLine microphone;
    private ExecutorService executor;
    private volatile boolean running = false;

    private Consumer<byte[]> frameCallback;   // receives 640-byte PCM frames
    private boolean muted = false;

    // ── Public API ────────────────────────────────────────────────────────────

    public void start(Consumer<byte[]> callback) throws LineUnavailableException {
        this.frameCallback = callback;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, FORMAT);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Microphone line not supported with format: " + FORMAT);
        }
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(FORMAT);
        microphone.start();
        running = true;

        executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "audio-capture");
            t.setDaemon(true);
            return t;
        });
        executor.execute(this::captureLoop);
        log.info("Audio capture started ({})", FORMAT);
    }

    public void stop() {
        running = false;
        if (microphone != null) {
            microphone.stop();
            microphone.close();
        }
        if (executor != null) {
            executor.shutdownNow();
        }
        log.info("Audio capture stopped");
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        log.debug("Audio capture muted: {}", muted);
    }

    public boolean isMuted() { return muted; }

    // ── Capture loop ──────────────────────────────────────────────────────────

    private void captureLoop() {
        byte[] buffer = new byte[FRAME_BYTES];
        while (running) {
            int read = microphone.read(buffer, 0, FRAME_BYTES);
            if (read > 0 && !muted && frameCallback != null) {
                byte[] frame = new byte[read];
                System.arraycopy(buffer, 0, frame, 0, read);
                frameCallback.accept(frame);
            }
        }
    }

    public static int frameBytesPerMs(int ms) {
        return (int)(FORMAT.getSampleRate() * FORMAT.getFrameSize() * ms / 1000);
    }
}
