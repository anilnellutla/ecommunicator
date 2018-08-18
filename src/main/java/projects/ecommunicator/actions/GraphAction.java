/*
 * Created on Mar 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.internalframes.Graph;

/**
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GraphAction extends AbstractAction {
	// this variable reference is used to minimize 
	// WhiteBoardFrame when user selects Screen Shot
	private WhiteBoardFrame whiteBoardFrame;

	/**
	 * Creates a new instance of ScreenShotAction.
	 * @param whiteBoardFrame variable used to minimize WhiteBoardFrame 
	 * when the user selects Screen Shot action.
	 */
	public GraphAction(WhiteBoardFrame whiteBoardFrame) {
		this.whiteBoardFrame = whiteBoardFrame;
	}

	public void actionPerformed(ActionEvent evt) {
		Graph graph =
			graph =
				whiteBoardFrame
					.getWhiteBoardPanel()
					.getWhiteBoardDesktopPane()
					.getGraph();
					
		if (graph.isVisible()) {
			graph.setVisible(false);
		} else {
			graph.setVisible(true);
		}
	}
}
