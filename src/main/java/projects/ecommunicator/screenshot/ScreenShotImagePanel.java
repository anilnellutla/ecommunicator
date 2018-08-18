package projects.ecommunicator.screenshot;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
/**
 * This is a sub class of JPanel which has been used for creating sub image of the Screen  
 * <P> 
 * @see javax.swing.JDialog
 * @see java.awt.Robot
 * @see java.awt.image.BufferedImage
 * @see java.awt.IllegalComponentStateException
 * @version 1.0
 */
public class ScreenShotImagePanel extends JPanel {
	
	//variable used for holding the desktop/screen image 
	private BufferedImage desktopImage;
	//variable used for holding the sub image i.e the captured image
	protected BufferedImage image;
	//X position of the Screen capture Dialog
	private int x;
	//Y position of the Screen capture Dialog
	private int y;
	//width of the Screen capture Dialog
	private int width;
	//height of the Screen capture Dialog
	private int height;
	//object used to get the screen capture	
	private Robot robot;
	//object that holds the area of the Screen Capture Panel
	private Rectangle area;

	/**
	 * creates a new instance of ScreenShotImagePanel
	 */
	public ScreenShotImagePanel() {
		setDoubleBuffered(true);
	}
	
	/**
	 * method that sets the area of the ScreenShotImagePanel
	 * @param area the area of the sub image panel
	 */
	public void setArea(Rectangle area) {
		this.area = area;
	}
	
	/**
	 * method that captures the image from the desktop/Screen image
	 */
	public void getSubImageOnPanel() {
		try {
			//returns the current loacation of the Screen shot tool
			Point p = getLocationOnScreen();
			x = p.x;
			y = p.y;
			width = getWidth();
			height = getHeight();
			
			//repositioning the X value with respect to screen width
			if ((x + width) > area.getWidth()) {
				width = (int) (area.getWidth() - x);
			}
			
			//repositioning  Y value with respect to screen height
			if ((y + height) > area.getHeight()) {
				height = (int) (area.getHeight() - y);
			}
			
			//repositioning of x if the user moves the dialog beyond the left extreem  
			if (x < 0) {
				width = width + x;
				x = 0;
			}
			
			//repositioning of x if the user moves the dialog beyond the upper 
			if (y < 0) {
				height = height - y;
				y = 0;
			}
			
			//captures the subImage from the deskto/screen Image
			image = desktopImage.getSubimage(x, y, width, height);
		} catch (IllegalComponentStateException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * method that cretes the desktop/screen Image
	 * for capturing the Screen it uses java.awt.Robot.createScreenCapture(Rectangle) method 
	 */

	public void createDesktopImage() {
		if (robot == null) {
			try {
				robot = new Robot();
			} catch (AWTException ex) {
				ex.printStackTrace();
			}
		}
		desktopImage = robot.createScreenCapture(area);
	}
	
	/**
	 * method that gets the subImage on the panel and paint it on the ScreenShotImagePanel
	 */
	public void paintComponent(Graphics g) {
		getSubImageOnPanel();
		super.paintComponent(g);

		if (image != null) {
			Point p = getLocation();
			if (getLocationOnScreen().x < 0) {
				p.x = p.x - getLocationOnScreen().x;
			}
			g.drawImage(image, p.x, p.y, width, height, this);
		}

	}
}