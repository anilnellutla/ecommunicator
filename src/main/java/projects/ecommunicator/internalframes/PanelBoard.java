package projects.ecommunicator.internalframes;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;

import projects.ecommunicator.utility.ComponentsSizeFactor;
import projects.ecommunicator.utility.Layout;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.panelboard.MessagingBoard;
import projects.ecommunicator.panelboard.ParticipantsInfo;
import projects.ecommunicator.panelboard.PresentationInfo;

/**
 * This is a sub class of JInternalFrame that has the been set as ContentPane for the Application   
 * <P> 
 * @see javax.swing.JInternalFrame
 * @see javax.swing.JSplitPane
 * @version 1.0
 */

public class PanelBoard extends JInternalFrame {
	/**
	* Creates a new instance of PanelBoard
	* @param messagingBoard object that used to add MessagingBoard to PanelBoard
	* @param presentationInfo object that used to add PresentationInfo to PanelBoard
	* @param participantsInfo object that used to add ParticipantsInfo to PanelBoard
	*/
	public PanelBoard(MessagingBoard messagingBoard, PresentationInfo presentationInfo, ParticipantsInfo participantsInfo) {
		//creates new InternalFrame titled Panel Board
		super("Panel Board");        
		//creates and sets the internal frame's icon
		frameIcon = new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","Frame_PanelBoard_Gif")));
		setFrameIcon((javax.swing.Icon)frameIcon);
		//get the component object of the internal frame
		JComponent component = (JComponent)getContentPane();
		component.setLayout(new BorderLayout());
		
		//splitpane for the upper pane that consists of messaging board and presentation info
		JSplitPane splitPaneTop = new JSplitPane(JSplitPane.VERTICAL_SPLIT,messagingBoard, presentationInfo);
		//sets the splitpane as one touch expandable
		splitPaneTop.setOneTouchExpandable(true);
		//sets the divider location
		splitPaneTop.setDividerLocation(300);
		
		
		//splitpane for the PanelBoard that consists the splitPaneTop and the participantsInfo 
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneTop, participantsInfo);
		//sets the splitpane as one touch expandable
		splitPane.setOneTouchExpandable(true);
		//sets the divider location
		splitPane.setDividerLocation(450);
		splitPane.setResizeWeight(0);
		splitPane.setContinuousLayout(true);	
		
			
		//add splitPane to the internal frame
		component.add(splitPane,BorderLayout.CENTER);
		
		
		//gets and sets the default size from ComponentsSizeFactor
		Rectangle size = Layout.getSize(ComponentsSizeFactor.PANEL_BOARD_WIDTH, ComponentsSizeFactor.PANEL_BOARD_HEIGHT);
		setSize( size.width,size.height );
		//gets and sets the default location from ComponentsSizeFactor
		Rectangle location = Layout.getLocation(ComponentsSizeFactor.PANEL_BOARD_POSITION_X,ComponentsSizeFactor.PANEL_BOARD_POSITION_Y);
		setLocation( location.width,location.height);
		//makes the internalframe visible
		setVisible(true);
	}
}