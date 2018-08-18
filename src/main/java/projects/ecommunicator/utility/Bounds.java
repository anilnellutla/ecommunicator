package projects.ecommunicator.utility;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

/**
 * this class has been used to get the screen size, the method getScreen() returns 
 * the Rectangle that has the height and width of the screen
 * @see java.awt.GraphicsConfiguration
 * @see java.awt.GraphicsDevice
 * @see java.awt.GraphicsEnvironment
 * @see java.awt.Rectangle
 * @version 1.0
 */

public class Bounds {
	//object holds the Graphics Environment
	private GraphicsEnvironment ge;
	//object holds the available Graphics Devices
	private GraphicsDevice[] gs;
	//object holds the current Graphics Device
	private GraphicsDevice gd;
	//object holds the Configuration
	private GraphicsConfiguration[] gc;
	//object that holds the bounds of the screen
	private Rectangle virtualBounds;

	/**
	 * creates new instance of Bounds by accepting raphicsEnvironment
	 * @param parentGE the graphics Environment variable
	 */

	public Bounds(GraphicsEnvironment parentGE) {
		ge = parentGE;
	}

	/**
	 * method that calculates the avilabel Graphics Devices and returns the virtual bounds of the 
	 * current Graphics Device i.e screen
	 * @return Rectangle that represents the screen width and height
	 */
	public Rectangle getScreen() {
		gs = ge.getScreenDevices();
		gd = gs[0];
		gc = gd.getConfigurations();
		virtualBounds = new Rectangle();
		virtualBounds = virtualBounds.union(gc[0].getBounds());
		return virtualBounds;
	}

}