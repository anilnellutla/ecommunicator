package projects.ecommunicator.layeredpane;

import javax.swing.JDesktopPane;

import projects.ecommunicator.contentpane.WhiteBoardPanel;
import projects.ecommunicator.internalframes.Graph;
import projects.ecommunicator.internalframes.PanelBoard;
import projects.ecommunicator.internalframes.ToolBoard;
import projects.ecommunicator.internalframes.VoiceBoard;
import projects.ecommunicator.internalframes.WhiteBoard;
import projects.ecommunicator.panelboard.MessagingBoard;
import projects.ecommunicator.panelboard.ParticipantsInfo;
import projects.ecommunicator.panelboard.PresentationInfo;

/**
 * This is a sub class of JDesktopPane which has the been set as ContentPane for the Application   
 * <P> 
 * @see javax.swing.JDesktopPane
 * @version 1.0
 */
public class WhiteBoardDesktopPane extends JDesktopPane {

	//white board internal frame
	private WhiteBoard whiteBoard;
	//voice board internal frame
	private VoiceBoard voiceBoard;
	//presentationinfo container
	private PresentationInfo presentationInfo;
	//participantsinfo container
	private ParticipantsInfo participantsInfo;
	//messaging board container
	private MessagingBoard messagingBoard;
	//panel board internal frame	
	private PanelBoard panelBoard;
	// tool board internal frame
	private ToolBoard toolBoard;
	private Graph graph;
	private WhiteBoardPanel whiteBoardPanel;

	/**
	* Creates a new instance of WhiteBoardDesktopPane
	*/
	public WhiteBoardDesktopPane(WhiteBoardPanel whiteBoardPanel) {

		this.whiteBoardPanel = whiteBoardPanel;

		//sets the drag mode as OUTLINE_DRAG_MODE
		setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		//creates white board
		whiteBoard = new WhiteBoard();
		//adds it to the desktop pane
		add(whiteBoard);

		//cretes voice board
		voiceBoard = new VoiceBoard();
		//adds it to the desktop pane
		add(voiceBoard);

		//creates tool board
		toolBoard = new ToolBoard(this);
		//adds it to the desktop pane
		add(toolBoard);

		//creates messaging board panel
		messagingBoard = new MessagingBoard();
		//creates presentation info panel
		presentationInfo = new PresentationInfo(this);
		//creates Participants info panel
		participantsInfo = new ParticipantsInfo();
		//creates panel board using messaging board,presentation info,participants info
		panelBoard =
			new PanelBoard(messagingBoard, presentationInfo, participantsInfo);
		//adds it to the desktop pane
		add(panelBoard);

		graph = new Graph();
		add(graph);
	}

	public WhiteBoardPanel getWhiteBoardPanel() {
		return whiteBoardPanel;
	}
	/**
	* method that returns the WhiteBoard	
	* @return WhiteBoard object 	
	*/
	public WhiteBoard getWhiteBoard() {
		return whiteBoard;
	}
	/**
	* method that returns the VoiceBoard
	* @return VoiceBoard object		
	*/
	public VoiceBoard getVoiceBoard() {
		return voiceBoard;
	}
	/**
	* method that returns the PresentationInfo
	* @return PresentationInfo object		
	*/
	public PresentationInfo getPresentationInfo() {
		return presentationInfo;
	}
	/**
	* method that returns the ParticipantsInfo
	* @return ParticipantsInfo object		
	*/
	public ParticipantsInfo getParticipantsInfo() {
		return participantsInfo;
	}
	/**
	* method that returns the MessagingBoard
	* @return MessagingBoard object 		
	*/
	public MessagingBoard getMessagingBoard() {
		return messagingBoard;
	}
	/**
	* method that returns the PanelBoard
	* @return PanelBoard		
	*/
	public PanelBoard getPanelBoard() {
		return panelBoard;
	}
	/**
	* method that returns the ToolBoard
	* @return ToolBoard		
	*/
	public ToolBoard getToolBoard() {
		return toolBoard;
	}

	public Graph getGraph() {
		return graph;
	}
}