/*
 * Created on Apr 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.actions;

import java.awt.AWTException;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;

import projects.ecommunicator.internalframes.Graph;
import projects.ecommunicator.utility.NewPage;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SnapGraphAction extends AbstractAction {

	private Graph graph;

	public SnapGraphAction(Graph graph) {
		this.graph = graph;
	}

	public void actionPerformed(ActionEvent arg0) {
		snapGraph();
		if (graph.isVisible()) {	
			graph.setFocusable(false);		
			graph.setVisible(false);			
		} else {			
			graph.setVisible(true);
			graph.setFocusable(true);
		}
	}

	private void snapGraph() {
		try {
			Robot robot = new Robot();
			Rectangle area =
				new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage screenImage = robot.createScreenCapture(area);

			Point point = graph.getGraphPanel().getLocationOnScreen();

			int x = point.x;
			int y = point.y;
			int width = graph.getWidth() - 20;
			int height = graph.getHeight() - 80;

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

			BufferedImage graphImage =
				screenImage.getSubimage(x, y, width, height);

			NewPage newPage =
				new NewPage(
					graph
						.getWhiteBoardFrame()
						.getWhiteBoardPanel()
						.getWhiteBoardDesktopPane()
						.getWhiteBoard()
						.getCanvas());
			newPage.createNewSnapGraphPage(graphImage);

			/*
			File file = new File("GraphImage.png");
			ImageIO.write(graphImage, "PNG", file);
			*/
		} catch (AWTException exc) {
			exc.printStackTrace();
		} catch (IllegalComponentStateException exc) {
			exc.printStackTrace();
		}

	}

}
