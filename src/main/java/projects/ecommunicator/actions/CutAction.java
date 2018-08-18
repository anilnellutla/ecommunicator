/*
 * Created on Apr 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CutAction extends AbstractAction {

	private WhiteBoardCanvas canvas;

	public CutAction(WhiteBoardCanvas canvas) {
		this.canvas = canvas;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		System.out.println("CutAction...");
		canvas.cutAction();
	}

}
