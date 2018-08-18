package projects.ecommunicator.utility;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * class that has been used to set the size and location to the components precisely internal frames 
 * of the applications, the method getSize() returns the size for the components and the method getLocation()
 * returns the loction for the components to be placed 
 * 
 * @see java.awt.GraphicsEnvironment
 * @see java.awt.Rectangle
 * @see java.awt.Toolkit
 * @version 1.0 
 */

public class Layout {

	//the height of the title bar to be reduced proportionately from the height of the components 
	private static int titleBarHeight = 18;

	/**
	 * static method that returns the size for the components 
	 * @param widthFactor width factor got from ComponentSizeFactor
	 * @param heightFactor height factor got from ComponentSizeFactor
	 * @return Rectangle that represents the height and width of the components 
	 */

	public static Rectangle getSize(float widthFactor, float heightFactor) {

		Object obj =
			Toolkit.getDefaultToolkit().getDesktopProperty(
				"win.frame.captionHeight");
		if (obj != null) {
			try {
				titleBarHeight = Integer.parseInt(obj.toString().trim());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// Get the Screen Size
		Bounds bounds =
			new Bounds(GraphicsEnvironment.getLocalGraphicsEnvironment());
		int screenWidth = (int) ((bounds.getScreen()).getWidth());
		int screenHeight =
			(int) ((bounds.getScreen()).getHeight()) - (75 + titleBarHeight);

		Rectangle size = new Rectangle();
		size.setSize(
			(int) (screenWidth * widthFactor),
			(int) (screenHeight * heightFactor));
		return size;
	}
	
	/**
	 * static method that returns the posiotion for the components 
	 * @param xPosition x co-ordinate of the component
	 * @param yPositon y co-ordinate of the component
	 * @return Rectangle that represents the x & y coordinates of the components
	 */

	public static Rectangle getLocation(float xPosition, float yPositon) {
		Bounds bounds =
			new Bounds(
				java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment());
		int screenWidth = (int) ((bounds.getScreen()).getWidth());
		int screenHeight =
			(int) ((bounds.getScreen()).getHeight()) - (75 + titleBarHeight);

		Rectangle location = new Rectangle();
		location.setSize(
			(int) (screenWidth * xPosition) + (int) (bounds.getScreen()).x,
			(int) (screenHeight * yPositon) + (int) (bounds.getScreen()).y);
		return location;
	}

}