package projects.ecommunicator.poll;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

public class PollPanel extends JPanel {

	private JTextArea question;
	private JTextField choiceOne;
	private JTextField choiceTwo;
	private JTextField choiceThree;
	private JTextField choiceFour;
	private JTextField choiceFive;
	private JTextField choiceSix;
	private JTextField choiceSeven;
	private JTextField choiceEight;
	private JTextField choiceNine;
	private JTextField choiceTen;

	public PollPanel() {

		setLayout(new BorderLayout());

		///////////////////////////////////////////////////////////////
		// panel that contains the question area and label		

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());

		//labelPanel
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BorderLayout());
		JLabel label = new JLabel("Enter Your Question Here : ");
		label.setFont(ScoolConstants.FONT_BOLD);
		labelPanel.add(new JPanel(), BorderLayout.EAST);
		labelPanel.add(label, BorderLayout.CENTER);
		labelPanel.add(new JPanel(), BorderLayout.WEST);
		labelPanel.add(new JPanel(), BorderLayout.NORTH);
		labelPanel.add(new JPanel(), BorderLayout.SOUTH);

		//question Text Area		
		question = new JTextArea("", 3, 20);
		question.setLineWrap(true);
		question.setFont(ScoolConstants.FONT);
		JScrollPane scrollPane = new JScrollPane(question);

		headerPanel.add(new JPanel(), BorderLayout.EAST);
		headerPanel.add(scrollPane, BorderLayout.CENTER);
		headerPanel.add(labelPanel, BorderLayout.NORTH);
		headerPanel.add(new JPanel(), BorderLayout.WEST);
		headerPanel.add(new JPanel(), BorderLayout.SOUTH);

		//////////////////////////////////////////////////////////////////////////
		//panel that contains the coices

		JPanel choicesPanel = new JPanel();
		choicesPanel.setLayout(new BorderLayout());

		JPanel choicePanel = new JPanel();
		TitledBorder titled =
			BorderFactory.createTitledBorder(
				ScoolConstants.ETCHED_BORDER,
				"Enter your choices : ",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				ScoolConstants.FONT_BOLD);
		choicePanel.setBorder(titled);
		choicePanel.setLayout(new GridLayout(10, 1));

		//choiceOnePanel
		JPanel choiceOnePanel = new JPanel();
		choiceOnePanel.setLayout(new BorderLayout());
		JLabel choiceOneLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice1_Gif"))));
		choiceOneLabel.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceOnePanel.add(choiceOneLabel, BorderLayout.WEST);
		choiceOne = new JTextField();
		choiceOne.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceOne.setFont(ScoolConstants.FONT);
		choiceOnePanel.add(choiceOne, BorderLayout.CENTER);

		choicePanel.add(choiceOnePanel);

		//choiceTwoPanel
		JPanel choiceTwoPanel = new JPanel();
		choiceTwoPanel.setLayout(new BorderLayout());
		JLabel choiceTwoLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice2_Gif"))));
		choiceTwoLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceTwoPanel.add(choiceTwoLabel, BorderLayout.WEST);
		choiceTwo = new JTextField();
		choiceTwo.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceTwo.setFont(ScoolConstants.FONT);
		choiceTwoPanel.add(choiceTwo, BorderLayout.CENTER);

		choicePanel.add(choiceTwoPanel);

		//choiceThreePanel
		JPanel choiceThreePanel = new JPanel();
		choiceThreePanel.setLayout(new BorderLayout());
		JLabel choiceThreeLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice3_Gif"))));
		choiceThreeLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceThreePanel.add(choiceThreeLabel, BorderLayout.WEST);
		choiceThree = new JTextField();
		choiceThree.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceThree.setFont(ScoolConstants.FONT);
		choiceThreePanel.add(choiceThree, BorderLayout.CENTER);

		choicePanel.add(choiceThreePanel);

		//choiceFourPanel
		JPanel choiceFourPanel = new JPanel();
		choiceFourPanel.setLayout(new BorderLayout());
		JLabel choiceFourLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice4_Gif"))));
		choiceFourLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceFourPanel.add(choiceFourLabel, BorderLayout.WEST);
		choiceFour = new JTextField();
		choiceFour.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceFour.setFont(ScoolConstants.FONT);
		choiceFourPanel.add(choiceFour, BorderLayout.CENTER);

		choicePanel.add(choiceFourPanel);

		//choiceFivePanel
		JPanel choiceFivePanel = new JPanel();
		choiceFivePanel.setLayout(new BorderLayout());
		JLabel choiceFiveLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice5_Gif"))));
		choiceFiveLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceFivePanel.add(choiceFiveLabel, BorderLayout.WEST);
		choiceFive = new JTextField();
		choiceFive.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceFive.setFont(ScoolConstants.FONT);
		choiceFivePanel.add(choiceFive, BorderLayout.CENTER);

		choicePanel.add(choiceFivePanel);

		//choiceSixPanel
		JPanel choiceSixPanel = new JPanel();
		choiceSixPanel.setLayout(new BorderLayout());
		JLabel choiceSixLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice6_Gif"))));
		choiceSixLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceSixPanel.add(choiceSixLabel, BorderLayout.WEST);
		choiceSix = new JTextField();
		choiceSix.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceSix.setFont(ScoolConstants.FONT);
		choiceSixPanel.add(choiceSix, BorderLayout.CENTER);

		choicePanel.add(choiceSixPanel);

		//choiceSevenPanel
		JPanel choiceSevenPanel = new JPanel();
		choiceSevenPanel.setLayout(new BorderLayout());
		JLabel choiceSevenLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice7_Gif"))));
		choiceSevenLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceSevenPanel.add(choiceSevenLabel, BorderLayout.WEST);
		choiceSeven = new JTextField();
		choiceSeven.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceSeven.setFont(ScoolConstants.FONT);
		choiceSevenPanel.add(choiceSeven, BorderLayout.CENTER);

		choicePanel.add(choiceSevenPanel);

		//choiceEightPanel
		JPanel choiceEightPanel = new JPanel();
		choiceEightPanel.setLayout(new BorderLayout());
		JLabel choiceEightLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice8_Gif"))));
		choiceEightLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceEightPanel.add(choiceEightLabel, BorderLayout.WEST);
		choiceEight = new JTextField();
		choiceEight.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceEight.setFont(ScoolConstants.FONT);
		choiceEightPanel.add(choiceEight, BorderLayout.CENTER);

		choicePanel.add(choiceEightPanel);

		//choiceNinePanel
		JPanel choiceNinePanel = new JPanel();
		choiceNinePanel.setLayout(new BorderLayout());
		JLabel choiceNineLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice9_Gif"))));
		choiceNineLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceNinePanel.add(choiceNineLabel, BorderLayout.WEST);
		choiceNine = new JTextField();
		choiceNine.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceNine.setFont(ScoolConstants.FONT);
		choiceNinePanel.add(choiceNine, BorderLayout.CENTER);

		choicePanel.add(choiceNinePanel);

		//choiceTenPanel
		JPanel choiceTenPanel = new JPanel();
		choiceTenPanel.setLayout(new BorderLayout());
		JLabel choiceTenLabel =
			new JLabel(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Choice10_Gif"))));
		choiceTenLabel.setBorder(BorderFactory.createEtchedBorder());
		choiceTenPanel.add(choiceTenLabel, BorderLayout.WEST);
		choiceTen = new JTextField();
		choiceTen.setBorder(ScoolConstants.ETCHED_BORDER);
		choiceTen.setFont(ScoolConstants.FONT);
		choiceTenPanel.add(choiceTen, BorderLayout.CENTER);

		choicePanel.add(choiceTenPanel);

		choicesPanel.add(new JPanel(), BorderLayout.NORTH);
		choicesPanel.add(new JPanel(), BorderLayout.SOUTH);
		choicesPanel.add(choicePanel, BorderLayout.CENTER);
		choicesPanel.add(new JPanel(), BorderLayout.EAST);
		choicesPanel.add(new JPanel(), BorderLayout.WEST);

		add(headerPanel, BorderLayout.NORTH);
		add(choicesPanel, BorderLayout.CENTER);

	}

	public JTextArea getQuestion() {
		return question;
	}

	public JTextField getChoiceOne() {
		return choiceOne;
	}

	public JTextField getChoiceTwo() {
		return choiceTwo;
	}

	public JTextField getChoiceThree() {
		return choiceThree;
	}

	public JTextField getChoiceFour() {
		return choiceFour;
	}

	public JTextField getChoiceFive() {
		return choiceFive;
	}

	public JTextField getChoiceSix() {
		return choiceSix;
	}

	public JTextField getChoiceSeven() {
		return choiceSeven;
	}

	public JTextField getChoiceEight() {
		return choiceEight;
	}

	public JTextField getChoiceNine() {
		return choiceNine;
	}

	public JTextField getChoiceTen() {
		return choiceTen;
	}
}
