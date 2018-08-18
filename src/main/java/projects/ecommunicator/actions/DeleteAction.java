/*
 * Created on Apr 28, 2004
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
public class DeleteAction extends AbstractAction {

	private WhiteBoardCanvas canvas;

	public DeleteAction(WhiteBoardCanvas canvas) {
		this.canvas = canvas;
	}

	public void actionPerformed(ActionEvent arg0) {		
		System.out.println("DeleteAction...");
		canvas.deleteAction();
	}

}
