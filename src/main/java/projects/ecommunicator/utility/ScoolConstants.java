package projects.ecommunicator.utility;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

public interface ScoolConstants {
	//public static final int INIT_PARAMS = 0;
	public static final int SELECT_TOOL = 1;
	public static final int CIRCLE_TOOL = 2;
	public static final int LINE_TOOL = 3;
	public static final int RECT_TOOL = 4;
	public static final int ROUND_RECT_TOOL = 5;
	public static final int FREE_HAND_TOOL = 6;
	public static final int PPT_IMAGE_TOOL = 7;
	public static final int SCREEN_SHOT_TOOL = 8;
	public static final int TEXT_TOOL = 9;
	public static final int SNAP_GRAPH_SHOT_TOOL = 10;
	public static final int EDIT_TOOL = 11;
	
	//new one 
	public static final int POLL_TOOL = 24;

	public static final int NEW_WHITEBOARD_ACTION = 12;
	public static final int NAVIGATION_ACTION = 13;
	public static final int MESSAGEBOARD_ACTION = 14;
	public static final int NEW_PARTICIPANT_JOINED_ACTION = 15;
	public static final int PARTICIPANT_LOGGEDOUT_ACTION = 16;
	public static final int PARTICIPANT_DISCONNECT_ACTION = 17;
	public static final int PARTICIPANT_STATE_CHANGED_ACTION = 18;
	public static final int MODERATOR_CHANGED_STATE_ACTION = 19;
	public static final int END_OF_OFFLINE_DATA_ACTION = 20;
	public static final int CUT_ACTION = 21;
	public static final int PASTE_ACTION = 22;
	public static final int DELETE_ACTION = 23;

	public static final String WHITE_BOARD = "WhiteBoard ";
	public static final String PPT_PRESENTATION = "Slide ";
	public static final String SCREEN_SHOT = "ScreenShot ";
	
	public static final String POLL = "PollBoard ";

	public static final int COMMAND_LENGTH = 256;
	public static final int HEADER_LENGTH = 58;
	public static final int DEC_BUFFER_INFO_LENGTH = 8;

	public static final int WHITE_BOARD_APP = 1;
	public static final int PPT_PRESENTATION_APP = 2;
	public static final int MESSAGE_BOARD_APP = 3;
	public static final int PARTICIPANTS_INFO_APP = 4;
	public static final int SERVER_APP = 5;
	//public static final int POLL_APP = 6;

	public static final String HANDSHAKE_PAYLOAD = "0000000000000000";
	public static final int HANDSHAKE_PAYLOAD_LENGTH = 32;

	public static final String DEFAULT_WHITEBOARD_TITLE = "WhiteBoard 1";

	public static final Font FONT = new Font("Verdana", Font.PLAIN, 11);
	public static final Font FONT_BOLD = new Font("Verdana",Font.BOLD,11);
	
	public static final EtchedBorder ETCHED_BORDER =
		(EtchedBorder) BorderFactory.createEtchedBorder();
	//public static final BorderLayout BORDER_LAYOUT = new BorderLayout();
	public static final Insets INSETS = new Insets(0, 0, 0, 0);

	public static final int MODERATOR_ROW = 0;

}