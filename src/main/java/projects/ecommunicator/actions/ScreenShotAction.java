package projects.ecommunicator.actions;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;

import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.internalframes.Graph;
import projects.ecommunicator.screenshot.ScreenShotThread;
import projects.ecommunicator.utility.Bounds;
import projects.ecommunicator.utility.NewPage;

/**
 * This is a sub class of AbstractAction used to handle Screen Capture events.
 * This event is occured by clicking Screen Shot button on toolbar or by selecting
 * Screen Shot(ctrl-s) from Create Menu.
 * <P> 
 * @author  Anil K Nellutla
 * @see javax.swing.AbstractAction
 * @version 1.0
 */

public class ScreenShotAction extends AbstractAction {

	// this variable reference is used to minimize 
	// WhiteBoardFrame when user selects Screen Shot
	private WhiteBoardFrame whiteBoardFrame;

	/**
	 * Creates a new instance of ScreenShotAction.
	 * @param whiteBoardFrame variable used to minimize WhiteBoardFrame 
	 * when the user selects Screen Shot action.
	 */
	public ScreenShotAction(WhiteBoardFrame whiteBoardFrame) {
		this.whiteBoardFrame = whiteBoardFrame;
	}

	/**
	 * invoked when an action occurs
	 * @param evt A semantic event which indicates that a component-defined
	 *  action occured.
	 */
	public void actionPerformed(ActionEvent evt) {
		
		Graph graph = whiteBoardFrame.getWhiteBoardPanel().getWhiteBoardDesktopPane().getGraph();
		if (graph.isVisible()) {
			graph.setVisible(false);
		} 

		// get the bounds of the local graphics environment
		Bounds bounds =
			new Bounds(GraphicsEnvironment.getLocalGraphicsEnvironment());
		// get the Rectangulare area bounded by local graphics environment
		Rectangle area = bounds.getScreen();
		// move the WhiteBoardFrame to extreme coordinates of the Desktop
		whiteBoardFrame.setLocation(
			(int) area.getWidth(),
			(int) area.getHeight());

		try {
			// wait till the WhiteBoardFrame is moved to specified cordinates
			Thread.sleep(50);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		// handle Screen Capture by spawing a seperate Thread. The Screen Capture
		// mechanism is carried out in this Thread.
		ScreenShotThread screenShotThread =
			new ScreenShotThread(whiteBoardFrame, this);
		Thread t = new Thread(screenShotThread);
		t.start();
	}

	/**
	 * Callback method used by ScreenShotThread. This method is invoked by the
	 * Thread  after the Screen Capture is over.
	 * @param capturedImage image captured by ScreenShotThread. This image is 
	 * then pasted on WhiteBoardCanvas. 
	 */
	public void createNewScreenShotPage(BufferedImage capturedImage) {
		// paste the captured image on WhiteBoardCanvas.
		NewPage newPage =
			new NewPage(
				whiteBoardFrame
					.getWhiteBoardPanel()
					.getWhiteBoardDesktopPane()
					.getWhiteBoard()
					.getCanvas());		
		newPage.createNewScreenShotPage(capturedImage);
	}
}