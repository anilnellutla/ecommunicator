package projects.ecommunicator.utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class OffScreenBuffer {

	private Color background;
	private Color foreground;
	private BufferedImage offScreen;
    private Graphics offgc;

    public OffScreenBuffer (Color background, Color foreground) {
	    this.background = background;
	    this.foreground = foreground;
	}

	public void createOffScreenBuffer() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		offScreen = new BufferedImage(dim.width, dim.height,1);
		offgc = offScreen.getGraphics();
		offgc.setColor(background);
		offgc.fillRect(0, 0, dim.width, dim.height);
		offgc.setColor(foreground);
	}

	public BufferedImage getOffScreen() {
		return offScreen;
	}

	public Graphics getOffScreenGraphics () {
		return offgc;
	}
}