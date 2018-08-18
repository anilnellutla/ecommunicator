package projects.ecommunicator.application;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import projects.ecommunicator.contentpane.WhiteBoardPanel;
import projects.ecommunicator.net.client.ECommunicatorClient;
import projects.ecommunicator.security.Credentials;
import projects.ecommunicator.utility.Property;

/** 
 * This is the main class for starting the application. This class is the container
 * for all the components in the application.
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */

public class WhiteBoardFrame extends JFrame {

	// content pane for WhiteBoardFrame to hold all the components
	private WhiteBoardPanel contentPane;

	// menubar for WhiteBoardFrame
	private WhiteBoardMenuBar menuBar;

	/**
	 * Creates an instance of WhiteBoardFrame and initializes all the components.
	 * The GUI is based on the following hierarchy.
	 * 
	 *                           WhiteBoardFrame
	 *                                 |
	 *                                 V
	 *              -----------------------------------------------------
	 *              |                                                   |	
	 *              V													V
	 * 	        WhiteBoardPanel									  WhiteBoardMenuBar
	 *                 |	                                            |
	 *                 |                                                V               
	 *                 |                                  ----------------------------------------------------  
	 *                 |                                  |         |        |              |                |
	 *                 |                                  V         V        V              V                V
	 *                 |                               FileMenu  EditMenu  CreateMenu  LookAndFeelMenu     WindowMenu
	 *                 V
	 *           ------------------------------
	 *           |	                          |
	 *           V                            V
	 *       WhiteBoardToolBar      WhiteBoardDesktopPane
	 *             |                          |
	 *             |                          V
	 *             |         ----------------------------------------
	 *             |         |            |            |            |
	 *             |         V            V            V            V
	 *             |     WhiteBoard    VoiceBoard   PanelBoard    ToolBoard
	 *             |                                   |             |
	 *             |                                   |             V
	 *             |                                   |         -------------------------
	 *             |                                   |         |              |        |
	 *             |                                   |         V              V        V
	 *             |                                   |       Accessories    Symbols  Thumbnails
	 *             |                                   V
	 *             |                   ---------------------------------------
	 *             |                   |                   |                  |
	 *             |                   V                   V                  V
	 *             |                MessagingBoard   PresentationInfo    ParticipantsInfo
	 *             |                                                       
	 *             V
	 * ---------------------------------------------------------------------
	 * |          |     |            |               |           |         |
	 * V          V     V            V               V           V         V
	 * Previous  Next  WhiteBoard   Online         ScreenShot    App      ViewDesktop
	 *                              Presentation                Sharing
	 * 
	 */
	public WhiteBoardFrame() {

		try {
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		// set the Icon Image for WhiteBoardFrame

		ImageIcon frameIcon =
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "WhiteBoardFrame_Gif")));

		Image image = frameIcon.getImage();
		setIconImage(image);

		// set title
		setTitle("eCommunicator");
		// set the default close operation as Exit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);

		// instantiate content pane
		contentPane = new WhiteBoardPanel(this);

		// instantiate WhiteBoardMenuBar and set JMenuBar to the container
		menuBar = new WhiteBoardMenuBar(this);
		setJMenuBar(menuBar);

		// make the content pane as opaque
		contentPane.setOpaque(true);
		// set WhiteBoardPanel as content pane for WhiteBoardFrame
		setContentPane(contentPane);
		// setExtendedState(MAXIMIZED_BOTH);

		// set the size of WhiteBoardFrame to the screen size of Default Toolkit 
		setSize(getToolkit().getScreenSize());
		// make the Container visible
		//show();
	}

	/**
	 * This method returns content pane
	 * @return content pane for WhiteBoardFrame
	 */
	public WhiteBoardPanel getWhiteBoardPanel() {
		return contentPane;
	}

	/**
	 * This method returns menu bar 
	 * @return JMenuBar for WhiteBoardFrame
	 */
	public WhiteBoardMenuBar getWhiteBoardMenuBar() {
		return menuBar;
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// instantiate container and add components to it
		WhiteBoardFrame frame = new WhiteBoardFrame();

		/* Handle the data obtained from network (sent by TCP Server)
		 * in a seperate Thread. WhiteBoardFrame set in the constructor
		 * of the Thread is used for handling GUI operations for the data
		 * obtained from network.
		 */
		DataListener dataListener = new DataListener(frame);
		Thread t1 = new Thread(dataListener);
		t1.start();
	}

	/**
	 * main method to start the application. This method is responsible for
	 * invoking:
	 * 1)network client Thread.
	 * This Thread reads data from the network and sends
	 * data to the network
	 * 
	 * 2)event-dispatching Thread. 
	 * This Thread handles all the GUI related operations.
	 * @param args arguments for main method from command line
	 */

	public static void main(String[] args) {

		/* create singleton instance of Credentials and populate the crendentials
		 * of the user logged in. 
		 */
		Credentials credentials = Credentials.getInstance();
		credentials.setUserId(Integer.parseInt(args[0]));
		credentials.setPasswd(args[1]);
		credentials.setRole(Integer.parseInt(args[2]));
		credentials.setSessionId(Integer.parseInt(args[3]));
		credentials.setIpAddress(args[4]);
		credentials.setPort(Integer.parseInt(args[5]));
		credentials.setAudioPort(Integer.parseInt(args[6]));
	//	credentials.setCaptureDeviceURL(args[7]);

		/* Create a seperate Thread for handling network related data. 
		 * (reade and write data). Creating a seperate Thread this way
		 * improves performance by loosely coupling the GUI and n/w related API.
		 * The server need not wait continuosly for the data from Client even if
		 * it crashes.
		 */
		ECommunicatorClient eCommunicatorClient = new ECommunicatorClient();
		Thread t1 = new Thread(eCommunicatorClient);
		t1.start();
		System.out.println("Invoked client thread");

		/*
		 * Event-dispataching Thread for handling GUI.
		 */
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	
	}

}