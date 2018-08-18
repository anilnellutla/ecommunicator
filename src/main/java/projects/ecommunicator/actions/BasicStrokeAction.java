/*
 * Created on Jan 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.actions;

import java.awt.BasicStroke;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.internalframes.WhiteBoard;

/**
 * This is a sub class of AbstractAction which has been used for setting the BasicStroke width
 * <P> 
 * @see javax.swing.AbstractAction
 * @see java.awt.BasicStroke
 * @version 1.0
 */
public class BasicStrokeAction extends AbstractAction {
	
	//used for setting width
	private BasicStroke stroke = null;
	//whiteBoard
	private WhiteBoard whiteBoard = null;
	
	/**
	* Creates a new instance of BasicStrokeAction()
	* @param whiteBoard container used to get canvas object 
	* @param stroke width of the stroke
	*/	 
	 public BasicStrokeAction(WhiteBoard whiteBoard,float stroke){
	 	this.stroke = new BasicStroke(stroke);	 	
	 	this.whiteBoard = whiteBoard;
	 }
	 
	/**
	* this method sets the width of the BasicStroke 
	* @param evt ActionEvent object that has been captured	
	*/	 
	 public void actionPerformed(ActionEvent evt) {		
		whiteBoard.getCanvas().setBasicStroke(stroke);
	}
}
