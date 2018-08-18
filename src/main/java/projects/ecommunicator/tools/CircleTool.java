package projects.ecommunicator.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;

/**
 * Class that  extends Circle and draws Circle on the whiteboard canvas by getting the 
 * starX and startY on the mouse events of WhiteBoardCanvas for furthur reference 
 * @see java.awt.Graphics.drawOval(int,int,int,int)       
 * <P> 
 * @see java.awt.Graphics
 * @see java.awt.Graphics2D
 * @see java.awt.RenderingHints
 * @see java.awt.geom.Ellipse2D 
 * @version 1.0
 */
public class CircleTool extends Circle {

	//X co-ordinate of the end point
	private int endX;
	//Y co-ordinate of the end point	
	private int endY;

	/**
	 * method that calculates the distance between the starting point and the end point
	 * and it checks for null and swaps the co-ordinates if the user drags the mouse upwards
	 * @param g graphics object that has been used for drawing	  
	 */

	public void drawComponent(Graphics g) {

		// swapping the co-ordinates for upward dragging 
		int x1 = startX;
		int x2 = endX;
		int y1 = startY;
		int y2 = endY;
		int temp;

		if (x2 < x1) {
			temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y2 < y1) {
			temp = y1;
			y1 = y2;
			y2 = temp;
		}

		//calculation of distance between the co-ordiantes (startX,startY) and (endX,endY)
		width =
			(int) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y2) * (y2 - y2)));
		height =
			(int) Math.sqrt(((x1 - x1) * (x1 - x1)) + ((y2 - y1) * (y2 - y1)));

		//Check for null
		if (x1 != 0 && y1 != 0) {
			int tempX = startX;
			int tempY = startY;
			startX = x1;
			startY = y1;
			//method that draws the ellipse on the whiteBoardCanvas
			draw(g);
			startX = tempX;
			startY = tempY;
		}
	}

	/**
	 * Keep on tracking endX,endY on mouseDragged and draw Circle using the
	 * current co-ordinates by repaint() 
	 */
	public void mouseDragged(int endX, int endY) {
		this.endX = endX;
		this.endY = endY;
	}

	//fetch endX,endY on mouseReleased and draw Circle using the current co-ordinates by repaint()
	public void mouseReleased(int endX, int endY) {
		this.endX = endX;
		this.endY = endY;
	}

	/**
	 * method that draws Circle by accepting graphics,co-ordinates startX,startY,height,width and thickness
	 * on the WhiteBoardCanvas on mouseRelesed
	 * @param g object that has been used for creating Graphics2D for drawing	  
	 */
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(stroke);
		g2.setColor(color);
		g2.draw(new Ellipse2D.Double(startX, startY, width, height));
		//g2.setColor(Color.RED);		
		//g2.fill(new Ellipse2D.Double(startX, startY, width, height));
	}

	// comment to be added by Anil

	public String getToolString() {
		if (startX > endX && startY < endY) {
			int tempX = startX;
			startX = endX;
			endX = tempX;
		} else if (startX > endX && startY > endY) {
			int tempX = startX;
			startX = endX;
			endX = tempX;
			
			int tempY = startY;
			startY = endY;
			endY = tempY;
		} else if (startX < endX && startY > endY) {
			int tempY = startY;
			startY = endY;
			endY = tempY;
		}

		String toolString =
			Utility.convertTo4Byte(ScoolConstants.CIRCLE_TOOL)
				+ Utility.convertTo4Byte(startX)
				+ Utility.convertTo4Byte(startY)
				+ Utility.convertTo4Byte(width)
				+ Utility.convertTo4Byte(height)
				+ Utility.convertTo3Byte(color.getRed())
				+ Utility.convertTo3Byte(color.getGreen())
				+ Utility.convertTo3Byte(color.getBlue())
				+ stroke.getLineWidth();
		return toolString;
	}

}