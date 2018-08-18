/*
 * Created on Mar 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelectAction extends AbstractAction {

	//WhiteBoardCanvas object
	  private WhiteBoardCanvas canvas = null;

	  /**
	  * Creates a new instance of FreeHandAction
	  * @param canvas object used to set the tool
	  */
	  public SelectAction(WhiteBoardCanvas canvas) {
		  this.canvas = canvas;
	  }

	  /**
	  * method sets the WhiteBoardCanvas Tool as  FREE_HAND_TOOL and sets cursor as CrossHair
	  * @param evt ActionEvent object that has been captured
	  */
	  public void actionPerformed(ActionEvent e) {
		canvas.setCursor(Cursor.getDefaultCursor());		  
		  canvas.setDrawTool(ScoolConstants.SELECT_TOOL);
	  }

}
