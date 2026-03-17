package com.ecommunicator.client.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Captures the primary display at a configurable frame rate and delivers
 * JPEG-compressed frames as byte arrays.
 *
 * Frames are delivered to a callback, which typically calls
 * {@code EcpClient.sendMediaFrame(FRAME_SCREEN, jpegBytes)}.
 */
public class ScreenCapture {

    private static final Logger log = LoggerFactory.getLogger(ScreenCapture.class);

    private static final byte FRAME_TYPE_SCREEN = 0x01;

    private final int fps;
    private final float jpegQuality;
    private final Rectangle captureArea;

    private ScheduledExecutorService scheduler;
    private volatile boolean running = false;
    private Consumer<byte[]> frameCallback;

    private Robot robot;

    public ScreenCapture() {
        this(15, 0.5f, null);   // 15 fps, 50% JPEG quality, full screen
    }

    /**
     * @param fps         target frames per second (5–30 recommended)
     * @param jpegQuality JPEG quality 0.0–1.0
     * @param area        screen region to capture (null = entire primary screen)
     */
    public ScreenCapture(int fps, float jpegQuality, Rectangle area) {
        this.fps          = fps;
        this.jpegQuality  = jpegQuality;
        this.captureArea  = area != null ? area : getFullScreenBounds();
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void start(Consumer<byte[]> callback) throws AWTException {
        this.frameCallback = callback;
        this.robot = new Robot();
        running = true;

        long intervalMs = 1000L / fps;
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "screen-capture");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(this::captureFrame, 0, intervalMs, TimeUnit.MILLISECONDS);
        log.info("Screen capture started: {} fps, quality={}, area={}", fps, jpegQuality, captureArea);
    }

    public void stop() {
        running = false;
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        log.info("Screen capture stopped");
    }

    public boolean isRunning() { return running; }

    // ── Capture ───────────────────────────────────────────────────────────────

    private void captureFrame() {
        if (!running || frameCallback == null) return;
        try {
            BufferedImage screenshot = robot.createScreenCapture(captureArea);
            byte[] jpeg = toJpeg(screenshot);
            frameCallback.accept(jpeg);
        } catch (Exception e) {
            log.warn("Screen capture failed: {}", e.getMessage());
        }
    }

    private byte[] toJpeg(BufferedImage img) throws Exception {
        // Optionally scale down to reduce bandwidth
        BufferedImage scaled = img;
        if (img.getWidth() > 1280) {
            int targetW = 1280;
            int targetH = (int)(img.getHeight() * (1280.0 / img.getWidth()));
            java.awt.Image tmp = img.getScaledInstance(targetW, targetH, java.awt.Image.SCALE_FAST);
            scaled = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
            scaled.getGraphics().drawImage(tmp, 0, 0, null);
        }

        // Encode as JPEG with configurable quality
        ByteArrayOutputStream baos = new ByteArrayOutputStream(65536);
        ImageWriter writer = ImageIO.getImageWritersByFormatName("JPEG").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(jpegQuality);
        try (var out = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(out);
            writer.write(null, new IIOImage(scaled, null, null), param);
        }
        writer.dispose();
        return baos.toByteArray();
    }

    private static Rectangle getFullScreenBounds() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return new Rectangle(0, 0, screen.width, screen.height);
    }
}
