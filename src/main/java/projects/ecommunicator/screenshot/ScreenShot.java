package projects.ecommunicator.screenshot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import projects.ecommunicator.actions.ScreenShotAction;
import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.utility.Bounds;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is a sub class of JDialog which has been used for capturing Screen Images  
 * <P> 
 * @see javax.swing.JDialog
 * @version 1.0
 */

public class ScreenShot extends JDialog implements ActionListener {

	//parent panel for the Screen Shot Frame
	private JPanel panel;
	//panel to hold the buttons
	private JPanel buttonPanel;
	//button that registers the ScreenShotAction and captures the image
	private JButton capture;
	//button that registers the ScreenShotAction and refresh the Frame
	private JButton refresh;
	//button that registers the ScreenShotAction and cancels the action
	private JButton cancel;
	//panel that holds the subImage of the Screen Shot
	private ScreenShotImagePanel imagePanel;
	//area of the screen
	private Rectangle area;
	//parent frame of the application
	private WhiteBoardFrame whiteBoardFrame;
	//action that captures the image
	private ScreenShotAction screenShotAction;
	//buffered image used as a temprory container
	private BufferedImage capturedImage;

	/**
	* Creates a new instance of ScreenShot
	* @param whiteBoardFrame Parent Frame in which the dialog to be shown
	* @param title title of the screen shot frame
	* @param modal state of the dialog either true or false
	* @param screenShotAction action that captures the image from the screen
	*/
	public ScreenShot(
		WhiteBoardFrame whiteBoardFrame,
		String title,
		boolean modal,
		ScreenShotAction screenShotAction) {
		super(whiteBoardFrame, title, modal);

		this.whiteBoardFrame = whiteBoardFrame;
		this.screenShotAction = screenShotAction;

		//parent panel
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(58, 110, 165));

		//captures the image		
		capture = new JButton("Capture");
		capture.setMargin(ScoolConstants.INSETS);
		capture.setFont(ScoolConstants.FONT);
		capture.addActionListener(this);
		buttonPanel.add(capture);

		//refreshes the sub image view port
		refresh = new JButton("Refresh");
		refresh.setMargin(ScoolConstants.INSETS);
		refresh.setFont(ScoolConstants.FONT);
		refresh.addActionListener(this);
		buttonPanel.add(refresh);

		//cancels the operation
		cancel = new JButton("Cancel");
		cancel.setMargin(ScoolConstants.INSETS);
		cancel.setFont(ScoolConstants.FONT);
		cancel.addActionListener(this);
		buttonPanel.add(cancel);

		buttonPanel.setBorder(ScoolConstants.ETCHED_BORDER);

		imagePanel = new ScreenShotImagePanel();
		//add panels to the parent panel		
		panel.add(imagePanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		//add parent panel to frame
		getContentPane().add(panel, "Center");

		Bounds bounds =
			new Bounds(GraphicsEnvironment.getLocalGraphicsEnvironment());
		area = bounds.getScreen();
		imagePanel.setArea(area);

		//sets the size for the screen shot frame
		int screenWidth = (int) (area.getWidth());
		int screenHeight = (int) (area.getHeight());
		setSize(
			(int) (screenWidth * 0.74) - 10,
			(int) (screenHeight * 0.72) - 10);
		int imgWidth = getWidth();
		int imgHeight = getHeight();

		//creates the image
		imagePanel.createDesktopImage();

		/**
		 * anonymous method call that adds component listener to the screen shot dialog 
		 */
		addComponentListener(new ComponentAdapter() {
			public void componentMoved(ComponentEvent evt) {
				imagePanel.repaint();
			}

			public void componentShown(ComponentEvent evt) {
				imagePanel.repaint();
			}
		});

		/**
		 * anonymous method call that adds window listener to the Screen shot dialog
		 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				disposeFrame();
			}
		});
		
		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
					disposeFrame();
				}
			}
		});

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocation(
			(screenWidth - imgWidth) / 2,
			(screenHeight - imgHeight) / 2);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * method that disposes the dialog and shows the Applications Parent Frame
	 */
	public void disposeFrame() {
		setVisible(false);
		whiteBoardFrame.setLocation(0, 0);
		whiteBoardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * method that creates DesktopImage
	 * @param evt object that has been captured on ActionEvent 
	 */
	public void actionPerformed(ActionEvent evt) {

		//action to be done on clicking capture button		
		if (evt.getSource() == capture) {
			capturedImage = imagePanel.image;
			/*java.io.File file = new java.io.File("CapturedImage");
			try {
				javax.imageio.ImageIO.write(capturedImage, "PNG", file);
			}catch(IOException ex) {
				ex.printStackTrace();
			}
			*/
			screenShotAction.createNewScreenShotPage(capturedImage);
			disposeFrame();
		}

		//action to be done on clicking refresh button		
		if (evt.getSource() == refresh) {
			setVisible(false);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			imagePanel.createDesktopImage();
			setVisible(true);
		}

		//action to be done on clicking cancel button
		if (evt.getSource() == cancel) {
			disposeFrame();
		}
	}
}