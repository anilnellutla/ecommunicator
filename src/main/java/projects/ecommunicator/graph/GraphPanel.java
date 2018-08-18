package projects.ecommunicator.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class GraphPanel extends JPanel {

	private double panelHeight;
	private double panelWidth;

	private int mouseX;
	private int mouseY;

	private double midX;
	private double midY;

	private int prevX;
	private int prevY;
	private double coOrdinateX;
	private double coOrdinateY;

	private double gridWidth = 30;
	private double gridHeight = 30;

	private boolean zoomIn;
	private boolean zoomOut;
	private boolean moveArround = false;
	private boolean pointer = false;

	private ComponentPanel componentPanel;

	private double xMinRange;
	private double xMaxRange;
	private double xScale;

	private double yMinRange;
	private double yMaxRange;
	private double yScale;

	public GraphPanel() {
		BevelBorder loweredBorder =
			(BevelBorder) BorderFactory.createLoweredBevelBorder();
		setPreferredSize(new Dimension(480, 480));
		setBorder(loweredBorder);
		setBackground(Color.WHITE);

		addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent evt) {

				mouseX = evt.getX();
				mouseY = evt.getY();
				setCoOrdinates(mouseX, mouseY);
				repaint();

			}

			public void mouseDragged(MouseEvent evt) {

				if (moveArround) {
					mouseX = evt.getX();
					mouseY = evt.getY();
					double midx = getMidX();
					double midy = getMidY();

					if (prevX == 0) {
						prevX = mouseX;
					}
					if (prevY == 0) {
						prevY = mouseY;
					}

					int differenceX = (mouseX - prevX);
					prevX = mouseX;
					midx = midx + differenceX;

					int differenceY = (mouseY - prevY);
					prevY = mouseY;
					midy = midy + differenceY;

					midX = midx;
					midY = midy;

					//repaint();
					setRanges();
					componentPanel.drawFunction();
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				mouseX = evt.getX();
				mouseY = evt.getY();

				if (zoomIn) {

					zoomInGraph(mouseX, mouseY);
					zoomIn();
					setRanges();
					componentPanel.drawFunction();

				} else if (zoomOut) {

					zoomOutGraph(mouseX, mouseY);
					zoomOut();
					setRanges();
					componentPanel.drawFunction();

				} else if (pointer) {
					setCoOrdinates(mouseX, mouseY);
				}
			}
			public void mouseReleased(MouseEvent evt) {
				prevX = 0;
				prevY = 0;
			}
		});

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				panelWidth = getSize().width;
				panelHeight = getSize().height;
				midX = panelWidth / 2;
				midY = panelHeight / 2;
				repaint();
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setColor(Color.BLACK);

		Dimension panelSize = getSize();
		panelHeight = panelSize.height;
		panelWidth = panelSize.width;

		if (midX == 0 && midY == 0) {
			midX = (panelWidth / 2);
			midY = (panelHeight / 2);
		}

		// draw methods
		drawGrids(graphics2D);
		drawOrigin(graphics2D);
		if (componentPanel.getCoOrdinateList().size() > 0) {
			drawGraph(graphics2D);
		}
		drawCoOrdinates(graphics2D);
	}

	private void drawGrids(Graphics2D graphics2D) {
		graphics2D.setStroke(new BasicStroke(1.0f));
		graphics2D.setColor(Color.LIGHT_GRAY);

		//for drawing Horizontal Lines
		for (double i = midY; i > 0; i -= gridHeight) {
			graphics2D.draw(new Line2D.Double(0, i, panelWidth, i));
		}

		for (double i = midY; i < panelHeight; i += gridHeight) {
			graphics2D.draw(new Line2D.Double(0, i, panelWidth, i));
		}

		//for drawing Vertical Lines
		for (double i = midX; i > 0; i -= gridWidth) {
			graphics2D.draw(new Line2D.Double(i, 0, i, panelHeight));
		}

		for (double i = midX; i < panelWidth; i += gridWidth) {
			graphics2D.draw(new Line2D.Double(i, 0, i, panelHeight));
		}
	}

	private void drawOrigin(Graphics2D graphics2D) {
		graphics2D.setStroke(new BasicStroke(2.0f));
		graphics2D.setColor(Color.BLACK);

		graphics2D.draw(new Line2D.Double(midX, 0, midX, panelHeight));
		graphics2D.draw(new Line2D.Double(0, midY, panelWidth, midY));
	}

	private void drawCoOrdinates(Graphics2D graphics2D) {
		//this can be used for drawing the co-ordinates
		graphics2D.setColor(Color.RED);
		graphics2D.drawString(
			Math.rint(coOrdinateX) + "," + Math.rint(coOrdinateY),
			(mouseX + 25),
			(mouseY + 25));

	}

	private void setCoOrdinates(int mouseX, int mouseY) {

		if (midX == 0 && midY == 0) {
			midX = panelWidth / 2;
			midY = panelHeight / 2;
		}

		coOrdinateX = mouseX - midX;
		coOrdinateY = mouseY - midY;

		if (mouseY < midY) {
			coOrdinateY = midY - mouseY;
		} else if (mouseY >= midY) {
			coOrdinateY = midY - mouseY;
		}

		componentPanel.setXRange();
		componentPanel.setYRange();

		coOrdinateX = (coOrdinateX / (gridWidth / xScale));
		coOrdinateY = (coOrdinateY / (gridHeight / yScale));
	}

	/*
	 * method that sets the ranges of minX maxX minY maxY at the componentPanel
	 *
	 */
	public void setRanges() {
		//xMinRange value
		double xMinRange = 0 - midX;
		xMinRange = xMinRange / (gridWidth / xScale);
		componentPanel.getXMin().setText(String.valueOf(xMinRange));

		//xMaxRange value
		double xMaxRange = panelWidth - midX;
		xMaxRange = xMaxRange / (gridWidth / xScale);
		componentPanel.getXMax().setText(String.valueOf(xMaxRange));

		//yMinRange value
		double yMinRange = midY - panelHeight;
		yMinRange = yMinRange / (gridHeight / yScale);
		componentPanel.getYMin().setText(String.valueOf(yMinRange));

		//yMaxRange value
		double yMaxRange = midY;
		yMaxRange = yMaxRange / (gridHeight / yScale);
		componentPanel.getYMax().setText(String.valueOf(yMaxRange));
	}

	/*
	 * method that sets the xValues and yValues and calls the draw method
	 */

	private void drawGraph(Graphics2D graphics2D) {

		draw(graphics2D, componentPanel.getCoOrdinateList());
	}

	/*
	 * method that draws the function on the graphPanel based on the values of xValues and yValues
	 */

	private void draw(Graphics2D graphics2D, ArrayList coOrdinateList) {

		graphics2D.setStroke(new BasicStroke(1.0f));
		graphics2D.setColor(Color.BLUE);

		for (int i = 0; i < coOrdinateList.size(); i++) {

			if ((i + 1) == coOrdinateList.size()) {
				continue;
			}

			double coOrdinateX1 =
				(((CoOrdinates) coOrdinateList.get(i)).xValue).doubleValue();
			double coOrdinateY1 =
				(((CoOrdinates) coOrdinateList.get(i)).yValue).doubleValue();

			double coOrdinateX2 =
				(((CoOrdinates) coOrdinateList.get(i + 1)).xValue)
					.doubleValue();
			double coOrdinateY2 =
				(((CoOrdinates) coOrdinateList.get(i + 1)).yValue)
					.doubleValue();

			double x1 = (coOrdinateX1 * gridWidth) + midX;
			double y1 = (coOrdinateY1 * gridHeight) + midY;

			if (coOrdinateX1 > 0) {
				x1 = midX + (coOrdinateX1 * gridWidth);
			}

			if (coOrdinateY1 >= 0) {
				y1 = midY - (coOrdinateY1 * gridHeight);
			}
			if (coOrdinateY1 < 0) {
				y1 = midY - (coOrdinateY1 * gridHeight);
			}

			double x2 = (coOrdinateX2 * gridWidth) + midX;
			double y2 = (coOrdinateY2 * gridHeight) + midY;

			if (coOrdinateX2 > 0) {
				x2 = midX + (coOrdinateX2 * gridWidth);
			}

			if (coOrdinateY2 > 0) {
				y2 = midY - (coOrdinateY2 * gridHeight);
			}
			if (coOrdinateY2 < 0) {
				y2 = midY - (coOrdinateY2 * gridHeight);
			}

			if ( !(((CoOrdinates) coOrdinateList.get(i)).yValue).isNaN()
				&& !(((CoOrdinates) coOrdinateList.get(i + 1)).yValue).isNaN() ) {
				graphics2D.draw(new Line2D.Double(x1, y1, x2, y2));
			}
		}
	}

	public double getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(double gridWidth) {
		this.gridWidth = gridWidth;
		repaint();
	}

	public double getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(double gridHeight) {
		this.gridHeight = gridHeight;
		repaint();
	}

	public void setZoomIn(boolean state) {
		zoomIn = state;
	}

	public void setZoomOut(boolean state) {
		zoomOut = state;
	}

	private void zoomInGraph(int mouseX, int mouseY) {

		if (mouseX > midX && mouseY < midY) {
			midX -= (mouseX - midX);
			midY += (midY - mouseY);
		}
		if (mouseX < midX && mouseY < midY) {
			midX += (midX - mouseX);
			midY += (midY - mouseY);
		}
		if (mouseX < midX && mouseY > midY) {
			midX += (midX - mouseX);
			midY -= (mouseY - midY);
		}
		if (mouseX > midX && mouseY > midY) {
			midX -= (mouseX - midX);
			midY -= (mouseY - midY);
		}
	}

	private void zoomOutGraph(int mouseX, int mouseY) {

		if (mouseX > midX && mouseY < midY) {
			midX += (mouseX - midX) / 2;
			midY -= (midY - mouseY) / 2;
		}
		if (mouseX < midX && mouseY < midY) {
			midX -= (midX - mouseX) / 2;
			midY -= (midY - mouseY) / 2;
		}
		if (mouseX < midX && mouseY > midY) {
			midX -= (midX - mouseX) / 2;
			midY += (mouseY - midY) / 2;
		}
		if (mouseX > midX && mouseY > midY) {
			midX += (mouseX - midX) / 2;
			midY += (mouseY - midY) / 2;
		}
	}

	private void zoomIn() {
		gridWidth = gridWidth * 2;
		gridHeight = gridHeight * 2;
	}

	private void zoomOut() {
		gridWidth = gridWidth / 2;
		gridHeight = gridHeight / 2;
	}

	public void setMoveArround(boolean state) {
		moveArround = state;
	}

	public void setPointer(boolean state) {
		pointer = state;
	}

	public boolean getZoomIn() {
		return zoomIn;
	}

	public boolean getZoomOut() {
		return zoomOut;
	}

	public boolean getMoveArround() {
		return moveArround;
	}

	public boolean getPointer() {
		return pointer;
	}

	public void resetZoom() {
		zoomIn = false;
		zoomOut = false;
	}

	public void setMidX(double midX) {
		this.midX = midX;
	}

	public void setMidY(double midY) {
		this.midY = midY;
	}

	public double getMidX() {
		return midX;
	}

	public double getMidY() {
		return midY;
	}

	/**
	 * accessor method that returns the panelWidth to be accessed in the componentPanel
	 * @return
	 */

	public double getPanelWidth() {
		return panelWidth;
	}

	/**
	 * accessor method that returns the panelHeight to be accessed in the componentPanel
	 * @return panelHeight
	 */

	public double getPanelHeight() {
		return panelHeight;
	}

	/**
	 * mutator method that returns the componentPanel
	 * @param componentPanel
	 */

	public void setComponentPanel(ComponentPanel componentPanel) {
		this.componentPanel = componentPanel;
	}

	public double getXMinRange() {
		return xMinRange;
	}

	public void setXMinRange(double xMinRange) {
		this.xMinRange = xMinRange;
	}

	public double getXMaxRange() {
		return xMaxRange;
	}

	public void setXMaxRange(double xMaxRange) {
		this.xMaxRange = xMaxRange;
	}

	public double getXScale() {
		return xScale;
	}

	public void setXScale(double xScale) {
		this.xScale = xScale;
	}

	public double getYMinRange() {
		return yMinRange;
	}

	public void setYMinRange(double yMinRange) {
		this.yMinRange = yMinRange;
	}

	public double getYMaxRange() {
		return yMaxRange;
	}

	public void setYMaxRange(double yMaxRange) {
		this.yMaxRange = yMaxRange;
	}

	public double getYScale() {
		return yScale;
	}

	public void setYScale(double yScale) {
		this.yScale = yScale;
	}

}
