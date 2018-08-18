package projects.ecommunicator.internalframes;

import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;

import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.toolboard.Accessories;
import projects.ecommunicator.toolboard.Symbols;
import projects.ecommunicator.toolboard.Thumbnails;
import projects.ecommunicator.utility.ComponentsSizeFactor;
import projects.ecommunicator.utility.Layout;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is a sub class of JInternalFrame that has the been set as ContentPane for the Application   
 * <P> 
 * @see javax.swing.JInternalFrame
 * @see javax.swing.JTabbedPane
 * @version 1.0
 */

public class ToolBoard extends JInternalFrame {
	//desktopPane that contains all the internal frames
	private WhiteBoardDesktopPane whiteBoardDesktopPane;
	/**
	* Creates a new instance of ToolBoard
	* @param whiteBoardDesktopPane object that passed to Accessories to get WhiteBoardCanvas
	*/
	public ToolBoard(WhiteBoardDesktopPane whiteBoardDesktopPane) {
		//creates new InternalFrame titled Tool Board
		super("Tool Board");
		//creates and sets the internal frame's icon
		frameIcon = new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","Frame_ToolBoard_Gif")));
		setFrameIcon((Icon) frameIcon);
		
		this.whiteBoardDesktopPane = whiteBoardDesktopPane;
		//get the component object of the internal frame
		JComponent component = (JComponent) getContentPane();
		//creates tabbedPane to place Accessories panel, Symbols panel, Thumbnails Panel
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(ScoolConstants.FONT);
		
		//creates Accessories panel and adds it to the tabbed pane		
		Accessories accessories = new Accessories(whiteBoardDesktopPane);
		tabbedPane.addTab("Accessories",new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","TB_Accessories_Gif"))),accessories,"Accessories Tab");
		//creates Symbols Panel and adds it to the tabbed pane
		Symbols symbols = new Symbols();
		tabbedPane.addTab("Symbols",new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","TB_Symbols_Gif"))), symbols,"Symbols Tab");
		//creates Thumbnails Panel and adds it to the tabbed pane
		Thumbnails thumbnails = new Thumbnails();
		tabbedPane.addTab("Thumbnails",new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","TB_Thumbnails_Gif"))), thumbnails,"Thumbnail view");
		
		
		//adds tabbedPane to the internal frame
		component.add(tabbedPane);
		
		
		//gets and sets the default size from ComponentsSizeFactor
		Rectangle size = Layout.getSize(ComponentsSizeFactor.TOOL_BOARD_WIDTH,ComponentsSizeFactor.TOOL_BOARD_HEIGHT);
		setSize(size.width, size.height);
		//gets and sets the default location from ComponentsSizeFactor
		Rectangle location = Layout.getLocation(ComponentsSizeFactor.TOOL_BOARD_POSITION_X,ComponentsSizeFactor.TOOL_BOARD_POSITION_Y);
		setLocation(location.width, location.height);
		//makes the internalframe visible
		setVisible(true);
	}
}
