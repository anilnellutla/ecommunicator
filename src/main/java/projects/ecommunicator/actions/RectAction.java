package projects.ecommunicator.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * This is a sub class of AbstractAction that sets the WhiteBoardCanvas tool as RECT_TOOL
 * <P> 
 * @see javax.swing.AbstractAction
 * @version 1.0
 */
public class RectAction extends AbstractAction {
	//WhiteBoardCanvas object
	private WhiteBoardCanvas canvas = null;
	/**
	* Creates a new instance of RectAction
	* @param canvas object used to set the tool	
	*/
	public RectAction(WhiteBoardCanvas canvas) {
		this.canvas = canvas;
	}
	/**
	* method sets the WhiteBoardCanvas Tool as RECT_TOOL and sets cursor as CrossHair
	* @param evt ActionEvent object that has been captured	
	*/
	public void actionPerformed(ActionEvent e) {
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		canvas.setDrawTool(ScoolConstants.RECT_TOOL);
	}
}