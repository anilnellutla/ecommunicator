package projects.ecommunicator.actions;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.internalframes.PanelBoard;
import projects.ecommunicator.internalframes.ToolBoard;
import projects.ecommunicator.internalframes.VoiceBoard;
import projects.ecommunicator.internalframes.WhiteBoard;
import projects.ecommunicator.utility.ComponentsSizeFactor;
import projects.ecommunicator.utility.Layout;

/**
 * This is a sub class of AbstractAction that sets the default layout/location for the internal frames of the application
 * <P>
 * @see javax.swing.AbstractAction
 * @version 1.0
 */
public class LayoutAction extends AbstractAction{

	//internale frame panelboard
	private PanelBoard panelBoard;
	//internale frame whiteboard
	private WhiteBoard whiteBoard;
	//internale frame voiceboard
	private VoiceBoard voiceBoard;
	//internale frame toolboard
	private ToolBoard toolBoard;
	/**
	* Creates a new instance of LayoutAction
	* @param whiteBoardDesktopPane object used to get the internalframes of panelboard,whiteboard,voiceboard,toolboard
	*/
	public LayoutAction( WhiteBoardDesktopPane whiteBoardDesktopPane ){
		panelBoard = whiteBoardDesktopPane.getPanelBoard();
		whiteBoard = whiteBoardDesktopPane.getWhiteBoard();
		voiceBoard = whiteBoardDesktopPane.getVoiceBoard();
		toolBoard = whiteBoardDesktopPane.getToolBoard();
	}
	/**
	* method sets the default layout/locations to the internal frames
	* @param evt ActionEvent object that has been captured
	*/
	public void actionPerformed( ActionEvent evt){
		Rectangle location = Layout.getLocation(ComponentsSizeFactor.WHITE_BOARD_POSITION_X,ComponentsSizeFactor.WHITE_BOARD_POSITION_Y);
		//sets the default location for whiteboard
		whiteBoard.setLocation(location.width, location.height);

		location = Layout.getLocation(ComponentsSizeFactor.VOICE_BOARD_POSITION_X,ComponentsSizeFactor.VOICE_BOARD_POSITION_Y);
		//sets the default location for voiceboard
		voiceBoard.setLocation(location.width, location.height);

		location = Layout.getLocation(ComponentsSizeFactor.PANEL_BOARD_POSITION_X,ComponentsSizeFactor.PANEL_BOARD_POSITION_Y);
		//sets the default location for panelboard
		panelBoard.setLocation( location.width, location.height);

		location = Layout.getLocation(ComponentsSizeFactor.TOOL_BOARD_POSITION_X,ComponentsSizeFactor.TOOL_BOARD_POSITION_Y);
		//sets the default location for toolboard
		toolBoard.setLocation( location.width, location.height);
	}
}