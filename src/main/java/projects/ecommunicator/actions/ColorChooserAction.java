package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.internalframes.WhiteBoard;
import projects.ecommunicator.whiteboard.ColorChooserThread;

/**
 * This is a sub class of AbstractAction invokes ColorChooserThread to set the Color
 * <P>
 * @see java.lang.Thread
 * @version 1.0
 */
public class ColorChooserAction extends AbstractAction {

	//WhiteBoardCanvas object
	private WhiteBoard whiteBoard;

	/**
	* Creates a new instance of ColorChooserAction
	* @param whiteBoard container that is passed to ColorChooserThreade to get the WhiteBoardCanvas
	*/
	public ColorChooserAction(WhiteBoard whiteBoard) {
		this.whiteBoard = whiteBoard;
	}

	/**
	* method instantiates the ColorChooserThread and runs it in a separate thread
	* @param evt ActionEvent object that has been captured
	*/
	public void actionPerformed(ActionEvent e) {
		//The Thread Class that ivokes the colorchooser dialog
		ColorChooserThread colorChooserThread = new ColorChooserThread(whiteBoard);
		Thread t = new Thread(colorChooserThread);
		t.start();
	}
}