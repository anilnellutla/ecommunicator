package projects.ecommunicator.contentpane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.toolbar.WhiteBoardToolBar;

/**
 * This is a sub class of JPanel that has the been set as ContentPane for the Application   
 * <P> 
 * @see javax.swing.JPanel
 * @see javax.swing.JToolBar
 * @version 1.0
 */
public class WhiteBoardPanel extends JPanel {
	//WhiteBoardDesktopPane that contains all the internal frames
	private WhiteBoardDesktopPane whiteBoardDesktopPane;
	//ToolBar contains all the ToolButtons
	private WhiteBoardToolBar toolBar;
	//Applications Parent Frame Object
	private WhiteBoardFrame whiteBoardFrame;

	/**
	* Creates a new instance of WhiteBoardPanel
	* @param whiteBoardFrame object passed to ToolBar as it required	
	*/
	public WhiteBoardPanel(WhiteBoardFrame whiteBoardFrame) {
		setLayout(new BorderLayout());
		this.whiteBoardFrame = whiteBoardFrame;
		//cretes WhiteBoardDesktopPane and adds it to the Panel
		whiteBoardDesktopPane = new WhiteBoardDesktopPane(this);

		//creates WhiteBoardToolBar and adds it to the panel		
		toolBar = new WhiteBoardToolBar(whiteBoardDesktopPane, whiteBoardFrame);
		add(toolBar, BorderLayout.NORTH);

		add(whiteBoardDesktopPane, BorderLayout.CENTER);
	}

	public WhiteBoardFrame getWhiteBoardFrame() {
		return whiteBoardFrame;
	}
	/**
	* method that returns the WhiteBoardDesktopPane		
	*/
	public WhiteBoardDesktopPane getWhiteBoardDesktopPane() {
		return whiteBoardDesktopPane;
	}
	/**
	* method that returns the WhiteBoardToolBar		
	*/
	public WhiteBoardToolBar getWhiteBoardToolBar() {
		return toolBar;
	}
}