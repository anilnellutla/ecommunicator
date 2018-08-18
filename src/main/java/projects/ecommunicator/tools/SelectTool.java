/*
 * Created on Mar 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.ToolFactory;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelectTool {

	//private int toolId;
	private Vector page;
	private Point prevPoint;
	private Point point;
	private WhiteBoardCanvas canvas;
	private int selectedToolPos = -1;
	private int selectedToolId = -1;
	private Rectangle bounds;
	private static BasicStroke stroke =
		new BasicStroke(
			1.0F,
			BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_BEVEL,
			0,
			new float[] { 5, 5 },
			0);

	public SelectTool(WhiteBoardCanvas canvas, int x, int y) {
		this.canvas = canvas;
		prevPoint = new Point(x, y);
	}

	public void setPage(Vector page) {
		this.page = page;
	}

	public void markPoints(int x, int y) {
		this.prevPoint = this.point = new Point(x, y);
	}

	public int getSelectedToolId() {
		return selectedToolId;
	}

	public void setSelectedToolId(int selectedToolId) {
		this.selectedToolId = selectedToolId;
	}

	public int getSelectedToolPos() {
		return selectedToolPos;
	}

	public void markSelectedTool() {
		selectedToolPos = -1;
		bounds = null;
		for (int i = page.size() - 1; i >= 0; i--) {
			String toolString = (String) page.get(i);
			if (calculateSelectedTool(toolString)) {
				selectedToolPos = i;
				System.out.println(
					selectedToolId
						+ " with tool pos"
						+ i
						+ " contains "
						+ point.x
						+ " "
						+ point.y
						+ "\n\n");
				break;
			}
		}
	}

	private boolean calculateSelectedTool(String toolString) {

		int toolType = Integer.parseInt(toolString.substring(0, 4));
		int startX = 0;
		int startY = 0;
		int width = 0;
		int height = 0;

		switch (toolType) {

			case ScoolConstants.CIRCLE_TOOL :
				{
					startX = Integer.parseInt(toolString.substring(4, 8));
					startY = Integer.parseInt(toolString.substring(8, 12));
					width = Integer.parseInt(toolString.substring(12, 16));
					height = Integer.parseInt(toolString.substring(16, 20));

					Ellipse2D ellipse2D =
						new Ellipse2D.Double(startX, startY, width, height);
					if (ellipse2D.contains(point)) {
						selectedToolId = ScoolConstants.CIRCLE_TOOL;
						bounds = new Rectangle(startX, startY, width, height);
						bounds.grow(2, 2);
						System.out.println("CIRCLE_TOOL selected.");
						return true;
					}
					break;
				}

			case ScoolConstants.RECT_TOOL :
				{

					startX = Integer.parseInt(toolString.substring(4, 8));
					startY = Integer.parseInt(toolString.substring(8, 12));
					width = Integer.parseInt(toolString.substring(12, 16));
					height = Integer.parseInt(toolString.substring(16, 20));

					Rectangle2D rectangle2D =
						new Rectangle2D.Double(startX, startY, width, height);
					if (rectangle2D.contains(point)) {
						selectedToolId = ScoolConstants.RECT_TOOL;
						bounds = new Rectangle(startX, startY, width, height);
						bounds.grow(2, 2);
						System.out.println("RECT_TOOL selected.");
						return true;
					}
					break;
				}

			case ScoolConstants.ROUND_RECT_TOOL :
				{
					startX = Integer.parseInt(toolString.substring(4, 8));
					startY = Integer.parseInt(toolString.substring(8, 12));
					width = Integer.parseInt(toolString.substring(12, 16));
					height = Integer.parseInt(toolString.substring(16, 20));

					RoundRectangle2D roundRectangle2D =
						new RoundRectangle2D.Double(
							startX,
							startY,
							width,
							height,
							10,
							10);
					if (roundRectangle2D.contains(point)) {
						selectedToolId = ScoolConstants.ROUND_RECT_TOOL;
						bounds = new Rectangle(startX, startY, width, height);
						bounds.grow(2, 2);
						System.out.println("ROUND_RECT_TOOL selected.");
						return true;
					}
					break;

				}

			case ScoolConstants.FREE_HAND_TOOL :
				{
					startX = Integer.parseInt(toolString.substring(4, 8));
					startY = Integer.parseInt(toolString.substring(8, 12));
					width = Integer.parseInt(toolString.substring(12, 16));
					height = Integer.parseInt(toolString.substring(16, 20));

					Rectangle2D rectangle2D =
						new Rectangle2D.Double(startX, startY, width, height);
					if (rectangle2D.contains(point)) {
						selectedToolId = ScoolConstants.FREE_HAND_TOOL;
						bounds = new Rectangle(startX, startY, width, height);
						System.out.println("FREE_HAND_TOOL selected.");
						return true;
					}
					break;
				}

			case ScoolConstants.LINE_TOOL :
				{
					startX = Integer.parseInt(toolString.substring(4, 8));
					startY = Integer.parseInt(toolString.substring(8, 12));
					int endX = Integer.parseInt(toolString.substring(12, 16));
					int endY = Integer.parseInt(toolString.substring(16, 20));
					Line2D line2D =
						new Line2D.Double(startX, startY, endX, endY);
					Rectangle rect = new Rectangle(point.x, point.y, 2, 2);
					rect.grow(2, 2);
					if (line2D.intersects(rect)) {
						selectedToolId = ScoolConstants.LINE_TOOL;
						if (startX < endX && startY < endY) {
							width = endX - startX;
							height = endY - startY;
							bounds =
								new Rectangle(startX, startY, width, height);
						} else if (startX > endX && startY > endY) {
							width = startX - endX;
							height = startY - endY;
							bounds = new Rectangle(endX, endY, width, height);

						} else if (startX > endX && startY < endY) {
							width = startX - endX;
							height = endY - startY;
							bounds = new Rectangle(endX, startY, width, height);
						} else if (startX < endX && startY > endY) {
							width = endX - startX;
							height = startY - endY;
							bounds = new Rectangle(startX, endY, width, height);

						} else if (startX == endX && startY < endY) {
							width = 0;
							height = endY - startY;
							bounds =
								new Rectangle(startX, startY, width, height);

						} else if (startX == endX && startY > endY) {
							width = 0;
							height = startY - endY;
							bounds = new Rectangle(startX, endY, width, height);

						} else if (startX < endX && startY == endY) {
							width = endX - startX;
							height = 0;
							bounds =
								new Rectangle(startX, startY, width, height);

						} else if (startX > endX && startY == endY) {
							width = startX - endX;
							height = 0;
							bounds = new Rectangle(endX, startY, width, height);
						}

						System.out.println("LINE_TOOL selected.");
						return true;
					}
					break;
				}
			case ScoolConstants.SCREEN_SHOT_TOOL :
				{
					startX = Integer.parseInt(toolString.substring(4, 8));
					startY = Integer.parseInt(toolString.substring(8, 12));
					width = Integer.parseInt(toolString.substring(12, 16));
					height = Integer.parseInt(toolString.substring(16, 20));

					Rectangle2D rectangle2D =
						new Rectangle2D.Double(startX, startY, width, height);
					if (rectangle2D.contains(point)) {
						selectedToolId = ScoolConstants.SCREEN_SHOT_TOOL;
						bounds = new Rectangle(startX, startY, width, height);
						bounds.grow(2, 2);
						System.out.println("SCREEN_SHOT_TOOL selected.");
						return true;
					}
					break;

				}
			case ScoolConstants.TEXT_TOOL :
				{
					startX = Integer.parseInt(toolString.substring(12, 16));
					startY = Integer.parseInt(toolString.substring(16, 20));
					width = Integer.parseInt(toolString.substring(20, 24));
					height = Integer.parseInt(toolString.substring(24, 28));

					Rectangle2D rectangle2D =
						new Rectangle2D.Double(startX, startY, width, height);
					if (rectangle2D.contains(point)) {
						selectedToolId = ScoolConstants.TEXT_TOOL;
						bounds = new Rectangle(startX, startY, width, height);
						System.out.println("TEXT_TOOL selected.");
						return true;
					}
					break;

				}

			case ScoolConstants.SNAP_GRAPH_SHOT_TOOL :
				{
					startX = Integer.parseInt(toolString.substring(4, 8));
					startY = Integer.parseInt(toolString.substring(8, 12));
					width = Integer.parseInt(toolString.substring(12, 16));
					height = Integer.parseInt(toolString.substring(16, 20));

					Rectangle2D rectangle2D =
						new Rectangle2D.Double(startX, startY, width, height);
					if (rectangle2D.contains(point)) {
						selectedToolId = ScoolConstants.SNAP_GRAPH_SHOT_TOOL;
						bounds = new Rectangle(startX, startY, width, height);
						bounds.grow(2, 2);
						System.out.println("SNAP_GRAPH_SHOT_TOOL selected.");
						return true;
					}
					break;

				}
		}
		return false;
	}

	public void drawComponent(Graphics g) {
		//if (selectedToolId != -1) {
		synchronized (g) {
			for (int i = 0; i < page.size(); i++) {
				String toolString = (String) page.get(i);
				Object obj = ToolFactory.getTool(toolString);
				drawToolString(g, obj);

				if (bounds != null) {
					Graphics2D g2 = (Graphics2D) g;
					Color color = g2.getColor();
					if (color != Color.BLACK) {
						g2.setColor(Color.BLACK);
						g2.setStroke(stroke);
						g2.draw(bounds);
						g2.setColor(color);
					} else {
						g2.setStroke(stroke);
						g2.draw(bounds);
					}

				}
			}
		}
	}

	private void drawToolString(Graphics g, Object obj) {
		if (obj instanceof CircleTool) {
			CircleTool circleTool = (CircleTool) obj;
			circleTool.draw(g);
			circleTool = null;
		} else if (obj instanceof LineTool) {
			LineTool lineTool = (LineTool) obj;
			lineTool.draw(g);
			lineTool = null;
		} else if (obj instanceof RectTool) {
			RectTool rectTool = (RectTool) obj;
			rectTool.draw(g);
			rectTool = null;
		} else if (obj instanceof RoundRectTool) {
			RoundRectTool roundRectTool = (RoundRectTool) obj;
			roundRectTool.draw(g);
			roundRectTool = null;
		} else if (obj instanceof FreeHandTool) {
			FreeHandTool freeHandTool = (FreeHandTool) obj;
			freeHandTool.draw(g);
			freeHandTool = null;

		} else if (obj instanceof PPtImageTool) {
			PPtImageTool pptImageTool = (PPtImageTool) obj;
			pptImageTool.draw(g, canvas);
			pptImageTool = null;

		} else if (obj instanceof ScreenShotImageTool) {
			ScreenShotImageTool screenShotImageTool = (ScreenShotImageTool) obj;
			screenShotImageTool.draw(g, canvas);
			screenShotImageTool = null;

		} else if (obj instanceof TextTool) {
			TextTool textTool = (TextTool) obj;
			textTool.draw(g);
			textTool = null;
		} else if (obj instanceof SnapGraphImageTool) {
			SnapGraphImageTool snapGraphImageTool = (SnapGraphImageTool) obj;
			snapGraphImageTool.draw(g, canvas);
			snapGraphImageTool = null;

		}
	}

	public void mouseDragged(int x, int y) {
		moveOrigin(x, y);
	}

	public void mouseReleased(int x, int y) {
		moveOrigin(x, y);
	}

	private void moveOrigin(int x, int y) {

		String toolString = (String) page.get(selectedToolPos);

		int toolType = Integer.parseInt(toolString.substring(0, 4));
		StringBuffer toolStringBuffer = new StringBuffer(toolString);
		switch (toolType) {

			case ScoolConstants.FREE_HAND_TOOL :
				{

					int minStartX =
						Integer.parseInt(toolString.substring(4, 8));
					int minStartY =
						Integer.parseInt(toolString.substring(8, 12));
					minStartX = minStartX + (x - prevPoint.x);
					minStartY = minStartY + (y - prevPoint.y);

					if (minStartX > 0 && minStartY > 0) {
						if (bounds != null) {
							bounds.x = minStartX;
							bounds.y = minStartY;
						}

						// get points from toolstring
						int index = 20;
						int size =
							Integer.parseInt(
								toolString.substring(index, index += 8));
						String points =
							toolString.substring(index, index + size);

						// shift new points
						StringBuffer newPoints = new StringBuffer("");
						for (int i = 0; i < points.length(); i += 8) {
							int pointX =
								Integer.parseInt(points.substring(i, i + 4))
									+ (x - prevPoint.x);
							int pointY =
								Integer.parseInt(
									points.substring(i + 4, i + 8))
									+ (y - prevPoint.y);
							newPoints.append(Utility.convertTo4Byte(pointX));
							newPoints.append(Utility.convertTo4Byte(pointY));
						}

						toolStringBuffer.replace(
							4,
							12,
							Utility.convertTo4Byte(minStartX)
								+ Utility.convertTo4Byte(minStartY));

						toolStringBuffer.replace(
							index,
							index + size,
							newPoints.toString());
					}

					prevPoint.x = x;
					prevPoint.y = y;

					break;
				}
			case ScoolConstants.LINE_TOOL :
				{

					int startX = Integer.parseInt(toolString.substring(4, 8));
					int startY = Integer.parseInt(toolString.substring(8, 12));
					int endX = Integer.parseInt(toolString.substring(12, 16));
					int endY = Integer.parseInt(toolString.substring(16, 20));

					int boundsX;
					int boundsY;
					int width;
					int height;
					if (startX < endX && startY < endY) {
						boundsX = startX;
						boundsY = startY;
						width = endX - startX;
						height = endY - startY;
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);
						if (bounds != null) {
							bounds.x = boundsX;
							bounds.y = boundsY;
						}
						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY;
							endX = startX + width;
							endY = startY + height;
						}
					} else if (startX > endX && startY > endY) {
						boundsX = endX;
						boundsY = endY;
						width = startX - endX;
						height = startY - endY;
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);
						if (bounds != null) {
							bounds.x = boundsX;
							bounds.y = boundsY;
						}
						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX + width;
							startY = boundsY + height;
							endX = boundsX;
							endY = boundsY;
						}

					} else if (startX > endX && startY < endY) {
						boundsX = endX;
						boundsY = startY;
						width = startX - endX;
						height = endY - startY;
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);
						if (bounds != null) {
							bounds.x = boundsX;
							bounds.y = boundsY;
						}
						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX + width;
							startY = boundsY;
							endX = boundsX;
							endY = boundsY + height;
						}
					} else if (startX < endX && startY > endY) {
						boundsX = startX;
						boundsY = endY;
						width = endX - startX;
						height = startY - endY;
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);
						if (bounds != null) {
							bounds.x = boundsX;
							bounds.y = boundsY;
						}
						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY + height;
							endX = boundsX + width;
							endY = boundsY;
						}

					} else if (startX == endX && startY < endY) {
						boundsX = startX;
						boundsY = startY;
						width = 0;
						height = endY - startY;
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);
						if (bounds != null) {
							bounds.x = boundsX;
							bounds.y = boundsY;
						}
						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY;
							endX = boundsX;
							endY = boundsY + height;
						}
					} else if (startX == endX && startY > endY) {
						boundsX = startX;
						boundsY = endY;
						width = 0;
						height = startY - endY;
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);
						if (bounds != null) {
							bounds.x = boundsX;
							bounds.y = boundsY;
						}
						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY + height;
							endX = boundsX;
							endY = boundsY;
						}

					} else if (startX < endX && startY == endY) {
						boundsX = startX;
						boundsY = startY;
						width = endX - startX;
						height = 0;
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);
						if (bounds != null) {
							bounds.x = boundsX;
							bounds.y = boundsY;
						}
						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY;
							endX = boundsX + width;
							endY = boundsY;
						}
					} else if (startX > endX && startY == endY) {
						boundsX = endX;
						boundsY = startY;
						width = startX - endX;
						height = 0;
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);
						if (bounds != null) {
							bounds.x = boundsX;
							bounds.y = boundsY;
						}
						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX + width;
							startY = boundsY;
							endX = boundsX;
							endY = boundsY;
						}

					}

					if (startX > 0 && startY > 0) {

						toolStringBuffer.replace(
							4,
							12,
							Utility.convertTo4Byte(startX)
								+ Utility.convertTo4Byte(startY));

						toolStringBuffer.replace(
							12,
							20,
							Utility.convertTo4Byte(endX)
								+ Utility.convertTo4Byte(endY));
					}

					prevPoint.x = x;
					prevPoint.y = y;

					break;
				}

			case ScoolConstants.TEXT_TOOL :
				{
					int originX = Integer.parseInt(toolString.substring(4, 8));
					int originY = Integer.parseInt(toolString.substring(8, 12));
					int startX = Integer.parseInt(toolString.substring(12, 16));
					int startY = Integer.parseInt(toolString.substring(16, 20));
					//int width = Integer.parseInt(toolString.substring(20, 24));
					int height = Integer.parseInt(toolString.substring(24, 28));

					startX = startX + (x - prevPoint.x);
					startY = startY + (y - prevPoint.y);

					originX = startX + 4;
					originY = startY + height / 2;

					if (bounds != null) {
						bounds.x = startX;
						bounds.y = startY;
					}

					toolStringBuffer.replace(
						4,
						20,
						Utility.convertTo4Byte(originX)
							+ Utility.convertTo4Byte(originY)
							+ Utility.convertTo4Byte(startX)
							+ Utility.convertTo4Byte(startY));

					prevPoint.x = x;
					prevPoint.y = y;
					break;
				}
			default :
				{
					int startX = Integer.parseInt(toolString.substring(4, 8));
					int startY = Integer.parseInt(toolString.substring(8, 12));
					startX = startX + (x - prevPoint.x);
					startY = startY + (y - prevPoint.y);

					if (bounds != null) {
						bounds.grow(-2, -2);
						bounds.x = startX;
						bounds.y = startY;
						bounds.grow(2, 2);
					}

					toolStringBuffer.replace(
						4,
						12,
						Utility.convertTo4Byte(startX)
							+ Utility.convertTo4Byte(startY));
					prevPoint.x = x;
					prevPoint.y = y;
					break;
				}
		}

		page.remove(selectedToolPos);
		page.add(selectedToolPos, toolStringBuffer.toString());
	}

	public String getToolString() {
		String toolString =
			Utility.convertTo4Byte(ScoolConstants.SELECT_TOOL)
				+ Utility.convertTo8Byte(selectedToolPos)
				+ (String) page.get(selectedToolPos);
		return toolString;
	}

}
