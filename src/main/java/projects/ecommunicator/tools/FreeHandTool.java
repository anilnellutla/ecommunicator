package projects.ecommunicator.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;

/**
 * Class that draws Line on the whiteboard canvas which extends class FreeHand and enables 
 * the user to scrible on the whiteBoardCanvas by getting the starX,startY,endX and endY on the 
 * mouse events of WhiteBoardCanvas for furthur reference @see java.awt.Graphics.drawLine(int,int,int,int)       
 * <P> 
 * @see java.awt.Graphics
 * @see java.awt.Graphics2D
 * @see java.awt.RenderingHints
 * @see java.awt.geom.Line2D 
 * @version 1.0
 */
public class FreeHandTool extends FreeHand {

	public int endX;
	public int endY;
	private ArrayList points;
	private Point minPoint;
	private Point maxPoint;

	public FreeHandTool() {
		minPoint = new Point(99999, 99999);
		maxPoint = new Point();
	}

	
	public void setPoints(ArrayList points) {
		this.points = points;
	}
	

	/**
	 * method that calculates the distance between the starting point and the end point
	 * and it checks for null and swaps the co-ordinates if the user drags the mouse upwards
	 * @param g graphics object that has been used for drawing	  
	 */

	public void drawComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(stroke);
		g2.setColor(color);
		g2.draw(new Line2D.Double(startX, startY, endX, endY));
		Point p = new Point(startX, startY);
		points.add(p);
		startX = endX;
		startY = endY;
		if (p.x < minPoint.x) {
			minPoint.x = p.x;
		}
		if (p.y < minPoint.y) {
			minPoint.y = p.y;
		}
		if (p.x > maxPoint.x) {
			maxPoint.x = p.x;
		}
		if (p.y > maxPoint.y) {
			maxPoint.y = p.y;
		}
		/*
		g2.drawRect(
			minPoint.x,
			minPoint.y,
			Math.abs(maxPoint.x - minPoint.x),
			Math.abs(maxPoint.y - minPoint.y));
		*/
	}

	//Keep on tracking endX,endY on mouseDragged and draw Line using the current co-ordinates by repaint()
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
		if (points != null) {
			int i = 1;
			for (; i < points.size() - 1; i++) {
				Point startPoint = (Point) points.get(i - 1);
				Point endPoint = (Point) points.get(i);
				g2.draw(
					new Line2D.Double(
						startPoint.getX(),
						startPoint.getY(),
						endPoint.getX(),
						endPoint.getY()));
			}
		}

	}

	//	comment to be added by Anil

	public String getToolString() {

		StringBuffer points = new StringBuffer("");
		if (this.points != null) {

			for (int i = 0; i < this.points.size(); i++) {
				Point point = (Point) this.points.get(i);
				points.append(Utility.convertTo4Byte(point.x));
				points.append(Utility.convertTo4Byte(point.y));
			}

			String toolString =
				Utility.convertTo4Byte(ScoolConstants.FREE_HAND_TOOL)
					+ Utility.convertTo4Byte(minPoint.x)
					+ Utility.convertTo4Byte(minPoint.y)
					+ Utility.convertTo4Byte(Math.abs(maxPoint.x - minPoint.x))
					+ Utility.convertTo4Byte(Math.abs(maxPoint.y - minPoint.y))
					+ Utility.convertTo8Byte(this.points.size() * 8)
					+ points.toString()
					+ Utility.convertTo3Byte(color.getRed())
					+ Utility.convertTo3Byte(color.getGreen())
					+ Utility.convertTo3Byte(color.getBlue())
					+ stroke.getLineWidth();			
			return toolString;
		}
		return null;
	}

}