package projects.ecommunicator.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * This is a sub class of AbstractAction that sets the WhiteBoardCanvas tool as CIRCLE_TOOL
 * <P> 
 * @see javax.swing.AbstractAction
 * @version 1.0
 */
public class CircleAction extends AbstractAction {

	//WhiteBoardCanvas object
	private WhiteBoardCanvas canvas = null;

	/**
	* Creates a new instance of CircleAction
	* @param canvas object used to set the tool	
	*/
	public CircleAction(WhiteBoardCanvas canvas) {
		this.canvas = canvas;
	}

	/**
	* method sets the WhiteBoardCanvas Tool as  CIRCLE_TOOL and sets cursor as CrossHair
	* @param evt ActionEvent object that has been captured	
	*/
	public void actionPerformed(ActionEvent evt) {
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		canvas.setDrawTool(ScoolConstants.CIRCLE_TOOL);
	}
}