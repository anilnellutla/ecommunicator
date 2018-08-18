package projects.ecommunicator.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;

/**
 * Class that draws line on the whiteboard canvas which extends class Line
 * it can be used for drawing lines by getting the starX,startY,endX and endY on mouse events of
 * WhiteBoardCanvas for furthur reference @see java.awt.Graphics.drawLine(int,int,int,int)       
 * <P> 
 * @see java.awt.Graphics
 * @see java.awt.Graphics2D
 * @see java.awt.RenderingHints
 * @see java.awt.geom.Line2D 
 * @version 1.0
 */
public class LineTool extends Line {

	/**
	 * method that calculates the distance between the starting point and the end point
	 * and it checks for null also
	 * @param g graphics object that has been used for drawing	  
	 */
	public void drawComponent(Graphics g) {

		if (startX != 0 && startY != 0) {
			draw(g);
		}
	}
	/**
	 * Keep on tracking endX,endY on mouseDragged and draw Line using the
	 * current co-ordinates by repaint() 
	 */
	public void mouseDragged(int endX, int endY) {
		this.endX = endX;
		this.endY = endY;
	}

	//fetch endX,endY on mouseReleased and draw Line using the current co-ordinates by repaint()
	public void mouseReleased(int endX, int endY) {
		this.endX = endX;
		this.endY = endY;
	}

	/**
	 * method that draws Line by accepting graphics,co-ordinates startX,startY,endX,endY
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
		g2.draw(new Line2D.Double(startX, startY, endX, endY));
	}

	//	comment to be added by Anil
	public String getToolString() {
		String toolString =
			Utility.convertTo4Byte(ScoolConstants.LINE_TOOL)
				+ Utility.convertTo4Byte(startX)
				+ Utility.convertTo4Byte(startY)
				+ Utility.convertTo4Byte(endX)
				+ Utility.convertTo4Byte(endY)
				+ Utility.convertTo3Byte(color.getRed())
				+ Utility.convertTo3Byte(color.getGreen())
				+ Utility.convertTo3Byte(color.getBlue())
				+ stroke.getLineWidth();
		return toolString;
	}

}