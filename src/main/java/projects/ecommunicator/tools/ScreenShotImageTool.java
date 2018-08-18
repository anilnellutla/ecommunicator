package projects.ecommunicator.tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * Class that draws captured image of the desktop on the WhiteBoardCanvas by getting the 
 * starX,startY and imagePath on the mouseRelesed event of the WhiteBoardCanvas 
 * for furthur reference @see java.awt.Graphics.drawImage(Image,int,int,ImageObserver)       
 * <P> 
 * @see java.awt.Graphics
 * @see java.awt.MediaTracker
 * @see java.awt.image.BufferedImage
 * @version 1.0
 */
public class ScreenShotImageTool extends ScreenShotImage {

	public BufferedImage bufferedImage;

	/**
	 * method that draws the bufferedImage of the WhiteBoardCanvas i.e previous image	 
	 * @param g object that has been used for drawing	  
	 */
	public void drawComponent(Graphics g) {
		g.drawImage(bufferedImage, startX, startY, null);
	}

	/**
	 * method that draws the iamges by accepting graphics,co-ordinates startX,startY,canvas and imagePath
	 * of the captured image
	 * @param g object that has been used for drawing	  
	 * @param canvas container to be drawn
	 */
	public void draw(Graphics g, WhiteBoardCanvas canvas) {
		//the captured image
		Image image = Toolkit.getDefaultToolkit().getImage(imagePath);
		//tracker for the container
		MediaTracker tracker = new MediaTracker(canvas);		
		tracker.addImage(image, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}		
		//draw image on the canvas				
		g.drawImage(image, startX, startY, null);
		image = null;
		tracker = null;
	}

	//comments to be added by Anil
	public String getToolString() {

		try {
			File tempFile = File.createTempFile("ScreenShot", ".png", null);
			tempFile.deleteOnExit();
			javax.imageio.ImageIO.write(bufferedImage, "PNG", tempFile);
			imagePath = tempFile.getCanonicalPath();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		String toolString =
			Utility.convertTo4Byte(ScoolConstants.SCREEN_SHOT_TOOL)
				+ Utility.convertTo4Byte(startX)
				+ Utility.convertTo4Byte(startY)
				+ Utility.convertTo4Byte(bufferedImage.getWidth())
				+ Utility.convertTo4Byte(bufferedImage.getHeight())
				+ imagePath;
		return toolString;
	}
}