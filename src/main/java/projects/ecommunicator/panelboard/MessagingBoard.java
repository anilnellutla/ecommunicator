package projects.ecommunicator.panelboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import projects.ecommunicator.application.DataEvent;

import projects.ecommunicator.messageboard.MessageBoardComboModel;
import projects.ecommunicator.renderer.ComboImageCellRenderer;
import projects.ecommunicator.security.Credentials;
import projects.ecommunicator.utility.ComponentsSizeFactor;
import projects.ecommunicator.utility.Layout;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;

/**
 * This is a sub class of JPanel that contains components relating to Messaging Board   
 * <P> 
 * @see javax.swing.JPanel
 * @see javax.swing.JTextPane
 * @see javax.swing.JScrollPane
 * @version 1.0
 */

public class MessagingBoard extends JPanel {

	//that displays the message got from the server
	private JTextPane chatPane;
	//that provide room for typing messages
	private JTextPane mesPane;
	//that sends the messages to the participants
	private JButton sendButton;
	//that enables to select the user to send message
	private JComboBox messageTo;

	/**
	* Creates a new instance of MessagingBoard	
	*/

	public MessagingBoard() {

		setLayout(new BorderLayout());
		Border etched = BorderFactory.createEtchedBorder();
		setBorder(
			BorderFactory.createTitledBorder(
				etched,
				"Messaging Board",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				new Font("Verdana", Font.PLAIN, 11)));

		//send messages panel

		JPanel toPanel = new JPanel();
		toPanel.setBorder(etched);
		toPanel.setLayout(new BorderLayout());
		JLabel label = new JLabel("Send Message To :");
		label.setFont(new Font("Verdana", Font.PLAIN, 11));
		toPanel.add(label, BorderLayout.WEST);

		MessageBoardComboModel model = new MessageBoardComboModel();
		messageTo = new JComboBox(model);
		messageTo.setEnabled(false);
		messageTo.setRenderer(new ComboImageCellRenderer());
		toPanel.add(messageTo, BorderLayout.CENTER);

		//chat panel

		JPanel chatPanel = new JPanel();
		chatPanel.setBorder(etched);
		chatPanel.setLayout(new BorderLayout());
		chatPane = new JTextPane();
		chatPane.setEnabled(false);
		chatPane.setFont(new Font("Verdana", Font.PLAIN, 11));
		//chatPane.setText("Abc : i got doubt in calculus");
		chatPane.setEditable(false);
		chatPanel.add(new JScrollPane(chatPane), BorderLayout.CENTER);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());

		//msg panel
		JPanel msgPanel = new JPanel();
		msgPanel.setBorder(etched);
		msgPanel.setLayout(new BorderLayout());
		mesPane = new JTextPane();
		mesPane.setEnabled(false);
		mesPane.setFont(new Font("Verdana", Font.PLAIN, 11));

		mesPane.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent evt) {
				if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
					send("");
				}
			}
		});
		sendButton = new JButton("Send");
		//sendButton.setEnabled(false);
		sendButton.setFont(new Font("Verdana", Font.PLAIN, 11));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				send("\n");
			}
		});

		msgPanel.add(new JScrollPane(mesPane), BorderLayout.CENTER);
		msgPanel.add(sendButton, BorderLayout.EAST);

		southPanel.add(msgPanel, BorderLayout.CENTER);

		add(toPanel, BorderLayout.NORTH);
		add(chatPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		Rectangle size =
			Layout.getSize(
				ComponentsSizeFactor.MESSAGING_BOARD_WIDTH,
				ComponentsSizeFactor.MESSAGING_BOARD_HEIGHT);
		setMinimumSize(new Dimension(size.width, size.height));
	}

	private String getToolString(String textMessage) {
		try {
			return Utility.convertTo4Byte(ScoolConstants.MESSAGEBOARD_ACTION)
				+ Utility.convertTo16Byte(Credentials.getInstance().getLoginId())
				+ textMessage;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public JTextPane getChatPane() {
		return chatPane;
	}

	public JTextPane getMesPane() {
		return mesPane;
	}

	public JButton getSendButton() {
		return sendButton;
	}

	public JComboBox getMessageTo() {
		return messageTo;
	}

	public void send(String newLine) {
		String textMessage = mesPane.getText();
		String history = chatPane.getText();
		//chatPane.setText("");

		chatPane.setText(
			history
				+ Credentials.getInstance().getLoginId()
				+ ":"
				+ textMessage
				+ newLine);

		DataEvent.notifySender(
			ScoolConstants.MESSAGE_BOARD_APP,
			0,
			"",
			getToolString(textMessage));
		mesPane.setText("");
	}
}