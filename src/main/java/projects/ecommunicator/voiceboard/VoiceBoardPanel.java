package projects.ecommunicator.voiceboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class VoiceBoardPanel
	extends JPanel
	implements ItemListener, ActionListener {

	//	button that allows the user to talk
	private JToggleButton playButton;
	private JToggleButton pauseButton;
	private JToggleButton stopButton;
	private JSlider playback;

	private Border raised;
	private Border lowered;

	public VoiceBoardPanel() {

		setLayout(new BorderLayout());

		raised = new SoftBevelBorder(SoftBevelBorder.RAISED);
		lowered = new SoftBevelBorder(SoftBevelBorder.LOWERED);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setBorder(ScoolConstants.ETCHED_BORDER);

		//panel that contains the volume slider
		JPanel playBackPanel = new JPanel();
		playBackPanel.setLayout(new BorderLayout());
		playback = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		playback.setValue(0);
		playback.setPaintTrack(true);
		playback.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		playback.setToolTipText("Session Seeking bar");

		TitledBorder titled =
			BorderFactory.createTitledBorder(
				ScoolConstants.ETCHED_BORDER,
				"Session Seeking Bar",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				ScoolConstants.FONT);
		playBackPanel.add(playback, BorderLayout.CENTER);
		playBackPanel.setBorder(titled);

		controlPanel.add(playBackPanel, BorderLayout.CENTER);

		//panel that contains the talk button
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		titled =
			BorderFactory.createTitledBorder(
				ScoolConstants.ETCHED_BORDER,
				"PlayBack",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				ScoolConstants.FONT);
		buttonPanel.setBorder(titled);

		JPanel tempButtonPanel = new JPanel();
		tempButtonPanel.setLayout(new FlowLayout());

		playButton =
			new JToggleButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "VB_Play_Gif"))));
		playButton.setFont(ScoolConstants.FONT);
		//playButton.setEnabled(false);
		playButton.setMargin(ScoolConstants.INSETS);
		playButton.setToolTipText("Plays the session");
		playButton.setBorder(raised);
		playButton.addItemListener(this);
		playButton.addActionListener(this);

		pauseButton =
			new JToggleButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "VB_Pause_Gif"))));
		pauseButton.setFont(ScoolConstants.FONT);
		//pauseButton.setEnabled(false);
		pauseButton.setMargin(ScoolConstants.INSETS);
		pauseButton.setToolTipText("Pauses the session");
		pauseButton.setBorder(raised);
		pauseButton.addItemListener(this);
		playButton.addActionListener(this);

		stopButton =
			new JToggleButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "VB_Stop_Gif"))));
		stopButton.setFont(ScoolConstants.FONT);
		//stopButton.setEnabled(false);
		stopButton.setMargin(ScoolConstants.INSETS);
		stopButton.setToolTipText("Stops the session");
		stopButton.addItemListener(this);
		stopButton.setBorder(raised);

		tempButtonPanel.add(playButton);
		tempButtonPanel.add(pauseButton);
		tempButtonPanel.add(stopButton);

		buttonPanel.add(tempButtonPanel, BorderLayout.CENTER);

		controlPanel.add(buttonPanel, BorderLayout.EAST);

		add(controlPanel, BorderLayout.CENTER);

	}

	public void itemStateChanged(ItemEvent evt) {
		JToggleButton obj = (JToggleButton) evt.getItem();

		if (obj == playButton) {
			if (playButton.isSelected()) {

				playButton.setBorder(lowered);
				playButton.setSelected(true);

				pauseButton.setBorder(raised);
				pauseButton.setSelected(false);

				stopButton.setBorder(raised);
				stopButton.setSelected(false);

			} else {

				playButton.setBorder(raised);
				playButton.setSelected(false);

			}

		} else if (obj == pauseButton) {
			if (pauseButton.isSelected()) {

				pauseButton.setBorder(lowered);
				pauseButton.setSelected(true);

				playButton.setBorder(raised);
				playButton.setSelected(false);

				stopButton.setBorder(raised);
				stopButton.setSelected(false);

			} else {

				pauseButton.setBorder(raised);
				pauseButton.setSelected(false);

			}

		} else if (obj == stopButton) {
			if (stopButton.isSelected()) {

				stopButton.setBorder(raised);
				stopButton.setSelected(false);

				playButton.setBorder(raised);
				playButton.setSelected(false);

				pauseButton.setBorder(raised);
				pauseButton.setSelected(false);

			} else {

				stopButton.setBorder(raised);
				stopButton.setSelected(false);

			}

		}

	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == playButton) {

			// write code for playback
		}

		if (evt.getSource() == pauseButton) {

			//write code for pause the playback
		}

		if (evt.getSource() == stopButton) {
			//write code for stopping the playback
		}

	}

	/**
		 * method that returns the TalkButton	
		 * @return TalkButton	
		 */
	public JToggleButton getPlayButton() {
		return playButton;
	}

	public JToggleButton getStopButton() {
		return stopButton;
	}

	public JToggleButton getPauseButton() {
		return pauseButton;
	}

	public JSlider getPlayback() {
		return playback;
	}

}
