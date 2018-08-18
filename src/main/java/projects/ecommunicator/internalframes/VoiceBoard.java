package projects.ecommunicator.internalframes;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import projects.ecommunicator.utility.ComponentsSizeFactor;
import projects.ecommunicator.utility.Layout;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.voiceboard.VoiceBoardPanel;

/**
 * This is a sub class of JInternalFrame that has the been set as ContentPane for the Application   
 * <P> 
 * @see javax.swing.JInternalFrame
 * @see javax.swing.JSlider
 * @version 1.0
 */

public class VoiceBoard extends JInternalFrame {
	
	private VoiceBoardPanel voiceBoardPanel;
	private JButton talkButton;
	
	
	/**
	* Creates a new instance of VoiceBoard	
	*/
	public VoiceBoard() {
		//creates new InternalFrame titled Voice Board	
		super("Voice Board");
		//creates and sets the internal frame's icon
		frameIcon =
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "Frame_VoiceBoard_Gif")));
		setFrameIcon((Icon) frameIcon);
		//get the component object of the internal frame
		JComponent component = (JComponent) getContentPane();
		component.setLayout(new BorderLayout());
		
		talkButton = new JButton("Talk");

		//panel that is having all the audio control buttons and sliders  
		voiceBoardPanel = new VoiceBoardPanel();
		//add panel to the internal frame
		component.add(voiceBoardPanel, BorderLayout.CENTER);

		//gets and sets the default size from ComponentsSizeFactor
		Rectangle size =
			Layout.getSize(
				ComponentsSizeFactor.VOICE_BOARD_WIDTH,
				ComponentsSizeFactor.VOICE_BOARD_HEIGHT);
		setSize(size.width, size.height);
		//gets and sets the default location from ComponentsSizeFactor
		Rectangle location =
			Layout.getLocation(
				ComponentsSizeFactor.VOICE_BOARD_POSITION_X,
				ComponentsSizeFactor.VOICE_BOARD_POSITION_Y);
		setLocation(location.width, location.height);
		//makes the internalframe visible
		setVisible(true);

	}
	
	public VoiceBoardPanel getVoiceBoardPanel(){
		return voiceBoardPanel;
	}
	
	public JButton getTalkButton(){
		return talkButton;
	}

	
}