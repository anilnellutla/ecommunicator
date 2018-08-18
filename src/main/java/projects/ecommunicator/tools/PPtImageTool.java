package projects.ecommunicator.tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * Class that draws converted power point images on the WhiteBoardCanvas by getting the 
 * starX,startY and imagePath on the mouseRelesed event of the WhiteBoardCanvas 
 * for furthur reference @see java.awt.Graphics.drawImage(Image,int,int,ImageObserver)       
 * <P> 
 * @see java.awt.Graphics
 * @see java.awt.MediaTracker
 * @version 1.0
 */
public class PPtImageTool extends PPtImage {

	/**
	 * method that draws the iamges by accepting graphics,co-ordinates startX,startY,canvas and imagePath
	 * on PPtAction 
	 * @param g object that has been used for drawing	  
	 * @param canvas container to be drawn
	 */
	public void draw(Graphics g, WhiteBoardCanvas canvas) {
		//the converted pptImage
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

		width = image.getWidth(canvas);
		height = image.getHeight(canvas);
		image = null;
		tracker = null;
	}

	//comments to be added by Anil

	public String getToolString() {
		String toolString =
			Utility.convertTo4Byte(ScoolConstants.PPT_IMAGE_TOOL)
				+ Utility.convertTo4Byte(0)
				+ Utility.convertTo4Byte(0)
				+ Utility.convertTo4Byte(width)
				+ Utility.convertTo4Byte(height)
				+ imagePath;
		return toolString;
	}
}