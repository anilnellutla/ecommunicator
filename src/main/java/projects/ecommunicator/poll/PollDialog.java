package projects.ecommunicator.poll;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import projects.ecommunicator.actions.PollAction;
import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.utility.ScoolConstants;

public class PollDialog extends JDialog implements ActionListener {

	private JButton okButton;
	private JButton cancelButton;
	private PollPanel pollPanel;
	private PollOptions pollOptions;
	private WhiteBoardFrame whiteBoardFrame;
	private PollAction pollAction;

	public PollDialog(WhiteBoardFrame frame, PollAction pollAction) {
		super(frame, "Poll Option", true);
		this.whiteBoardFrame = frame;
		this.pollAction = pollAction;
		pollOptions = new PollOptions();
		setSize(300, 400);
		setResizable(false);		
		getContentPane().setLayout(new BorderLayout());
		pollPanel = new PollPanel();
		getContentPane().add(pollPanel, BorderLayout.CENTER);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				dispose();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		okButton = new JButton("Ok");
		okButton.setFont(ScoolConstants.FONT);
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(ScoolConstants.FONT);
		cancelButton.addActionListener(this);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dialogSize = getSize();
		setLocation(
			(screenSize.width - dialogSize.width) / 2,
			(screenSize.height - dialogSize.height) / 2);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == cancelButton) {
			setVisible(false);
		}

		if (evt.getSource() == okButton) {

			//sets the question typed
			if (!(pollPanel.getQuestion().getText()).equals("")) {
				pollOptions.setQuestionTyped(
					pollPanel.getQuestion().getText().toString().trim());
			}

			//adds choices to pollOptions
			if (!(pollPanel.getChoiceOne().getText()).equals("")) {
				pollOptions.addChoice(
					"01" + pollPanel.getChoiceOne().getText().trim());
			}
			if (!(pollPanel.getChoiceTwo().getText()).equals("")) {
				pollOptions.addChoice(
					"02" + pollPanel.getChoiceTwo().getText().trim());
			}
			if (!(pollPanel.getChoiceThree().getText()).equals("")) {
				pollOptions.addChoice(
					"03" + pollPanel.getChoiceThree().getText().trim());
			}
			if (!(pollPanel.getChoiceFour().getText()).equals("")) {
				pollOptions.addChoice(
					"04" + pollPanel.getChoiceFour().getText().trim());
			}
			if (!(pollPanel.getChoiceFive().getText()).equals("")) {
				pollOptions.addChoice(
					"05" + pollPanel.getChoiceFive().getText().trim());
			}
			if (!(pollPanel.getChoiceSix().getText()).equals("")) {
				pollOptions.addChoice(
					"06" + pollPanel.getChoiceSix().getText().trim());
			}
			if (!(pollPanel.getChoiceSeven().getText()).equals("")) {
				pollOptions.addChoice(
					"07" + pollPanel.getChoiceSeven().getText().trim());
			}
			if (!(pollPanel.getChoiceEight().getText()).equals("")) {
				pollOptions.addChoice(
					"08" + pollPanel.getChoiceEight().getText().trim());
			}
			if (!(pollPanel.getChoiceNine().getText()).equals("")) {
				pollOptions.addChoice(
					"09" + pollPanel.getChoiceNine().getText().trim());
			}
			if (!(pollPanel.getChoiceTen().getText()).equals("")) {
				pollOptions.addChoice(
					"10" + pollPanel.getChoiceTen().getText().trim());
			}
			
			//creates a new poll page on the whiteboard

			pollAction.createNewPollPage(pollOptions);

		}
	}
}
