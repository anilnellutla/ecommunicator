package projects.ecommunicator.poll;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import projects.ecommunicator.tools.PollTool;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

public class ResultPanel extends JPanel implements ActionListener {

	private JTextArea questionArea;
	private JPanel choicePanel;
	private Color bg = Color.WHITE;

	private JButton buttonOne;
	private JRadioButton choiceRadioOne;
	private JLabel choiceLabelOne;

	private JButton buttonTwo;
	private JRadioButton choiceRadioTwo;
	private JLabel choiceLabelTwo;

	private JButton buttonThree;
	private JRadioButton choiceRadioThree;
	private JLabel choiceLabelThree;

	private JButton buttonFour;
	private JRadioButton choiceRadioFour;
	private JLabel choiceLabelFour;

	private JButton buttonFive;
	private JRadioButton choiceRadioFive;
	private JLabel choiceLabelFive;

	private JButton buttonSix;
	private JRadioButton choiceRadioSix;
	private JLabel choiceLabelSix;

	private JButton buttonSeven;
	private JRadioButton choiceRadioSeven;
	private JLabel choiceLabelSeven;

	private JButton buttonEight;
	private JRadioButton choiceRadioEight;
	private JLabel choiceLabelEight;

	private JButton buttonNine;
	private JRadioButton choiceRadioNine;
	private JLabel choiceLabelNine;

	private JButton buttonTen;
	private JRadioButton choiceRadioTen;
	private JLabel choiceLabelTen;

	private JButton noVoteButton;
	private JRadioButton noVoteRadio;
	private JLabel noVoteLabel;

	private ArrayList buttonList;
	private ArrayList radioButtonList;
	private ArrayList checkBoxList;
	private ArrayList labelList;
	private ArrayList resultList;
	private ArrayList percentageList;

	private JLabel resultOne;
	private JLabel resultTwo;
	private JLabel resultThree;
	private JLabel resultFour;
	private JLabel resultFive;
	private JLabel resultSix;
	private JLabel resultSeven;
	private JLabel resultEight;
	private JLabel resultNine;
	private JLabel resultTen;

	private JPanel resultPanel;

	private ButtonGroup group = new ButtonGroup();
	//private WhiteBoardFrame whiteBoardFrame;

	private JLabel percentageOne;
	private JLabel percentageTwo;
	private JLabel percentageThree;
	private JLabel percentageFour;
	private JLabel percentageFive;
	private JLabel percentageSix;
	private JLabel percentageSeven;
	private JLabel percentageEight;
	private JLabel percentageNine;
	private JLabel percentageTen;
	//private PollDialog pollDialog;

	private JLabel pollStatus;

	private PollTool pollTool;

	/**
	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */

	//public ResultPanel(WhiteBoardFrame frame, PollDialog pollDialog) {
	public ResultPanel(PollTool pollTool) {

		this.pollTool = pollTool;

		setLayout(new BorderLayout());
		//this.whiteBoardFrame = frame;
		//this.pollDialog = pollDialog;
		buttonList = new ArrayList();
		radioButtonList = new ArrayList();
		checkBoxList = new ArrayList();
		labelList = new ArrayList();
		resultList = new ArrayList();
		percentageList = new ArrayList();

		////////////////////////////////////////////////////////
		//headerPanel that contains the Question typed
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		headerPanel.setBackground(bg);

		JPanel headerNorth = new JPanel();
		headerNorth.setLayout(new BorderLayout());

		JPanel questionLabelPanel = new JPanel();
		questionLabelPanel.setBackground(bg);
		questionLabelPanel.setLayout(new BorderLayout());
		JLabel questionLabel = new JLabel("Poll Query :");
		questionLabel.setFont(ScoolConstants.FONT_BOLD);
		questionLabelPanel.add(questionLabel, BorderLayout.WEST);
		JPanel tempQuestionLabelPanel = new JPanel();
		tempQuestionLabelPanel.setBackground(bg);
		headerNorth.add(tempQuestionLabelPanel, BorderLayout.WEST);
		headerNorth.add(questionLabelPanel, BorderLayout.CENTER);

		headerNorth.setBackground(bg);
		JPanel headerSouth = new JPanel();
		headerSouth.setBackground(bg);
		JPanel headerWest = new JPanel();
		headerWest.setBackground(bg);
		JPanel headerEast = new JPanel();
		headerEast.setBackground(bg);

		//JTextArea that contains the Question Typed
		questionArea = new JTextArea("", 6, 15);
		questionArea.setBorder(
			BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
		questionArea.setEditable(false);
		questionArea.setForeground(Color.BLUE);
		questionArea.setLineWrap(true);
		questionArea.setBackground(bg);
		questionArea.setFont(ScoolConstants.FONT_BOLD);

		JPanel questionAreaPanel = new JPanel();
		questionAreaPanel.setLayout(new BorderLayout());
		questionAreaPanel.setBackground(bg);
		questionAreaPanel.add(questionArea, BorderLayout.NORTH);

		headerPanel.add(questionAreaPanel, BorderLayout.CENTER);
		headerPanel.add(headerNorth, BorderLayout.NORTH);
		headerPanel.add(headerSouth, BorderLayout.SOUTH);
		headerPanel.add(headerEast, BorderLayout.EAST);
		headerPanel.add(headerWest, BorderLayout.WEST);

		//////////////////////////////////////////////////////////////////////////////
		//panel that contains the left and right panel		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(bg);
		centerPanel.setLayout(new BorderLayout());

		//////////////////////////////////////////////////////////////////
		//panel that contains the button panel and the choice panel
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(bg);
		leftPanel.setLayout(new BorderLayout());

		JPanel leftNorth = new JPanel();
		leftNorth.setLayout(new BorderLayout());

		JPanel optionsLabelPanel = new JPanel();
		optionsLabelPanel.setBackground(bg);
		optionsLabelPanel.setLayout(new BorderLayout());
		JLabel optionsLabel = new JLabel("Poll Options :");
		optionsLabel.setFont(ScoolConstants.FONT_BOLD);
		optionsLabelPanel.add(optionsLabel, BorderLayout.WEST);
		JPanel optionsTempLabelPanel = new JPanel();
		optionsTempLabelPanel.setBackground(bg);
		leftNorth.add(optionsTempLabelPanel, BorderLayout.WEST);
		leftNorth.add(optionsLabelPanel, BorderLayout.CENTER);

		leftNorth.setBackground(bg);
		JPanel leftSouth = new JPanel();
		leftSouth.setBackground(bg);

		JPanel leftEast = new JPanel();
		leftEast.setBackground(bg);
		JPanel leftWest = new JPanel();
		leftWest.setBackground(bg);

		choicePanel = new JPanel();
		choicePanel.setLayout(new GridLayout(11, 1));
		choicePanel.setBorder(
			BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
		choicePanel.setBackground(bg);

		choiceRadioOne = new JRadioButton();
		choiceRadioTwo = new JRadioButton();
		choiceRadioThree = new JRadioButton();
		choiceRadioFour = new JRadioButton();
		choiceRadioFive = new JRadioButton();
		choiceRadioSix = new JRadioButton();
		choiceRadioSeven = new JRadioButton();
		choiceRadioEight = new JRadioButton();
		choiceRadioNine = new JRadioButton();
		choiceRadioTen = new JRadioButton();
		noVoteRadio = new JRadioButton();

		JRadioButton radioButtons[] =
			{
				choiceRadioOne,
				choiceRadioTwo,
				choiceRadioThree,
				choiceRadioFour,
				choiceRadioFive,
				choiceRadioSix,
				choiceRadioSeven,
				choiceRadioEight,
				choiceRadioNine,
				choiceRadioTen,
				noVoteRadio };

		buttonOne = new JButton();
		buttonTwo = new JButton();
		buttonThree = new JButton();
		buttonFour = new JButton();
		buttonFive = new JButton();
		buttonSix = new JButton();
		buttonSeven = new JButton();
		buttonEight = new JButton();
		buttonNine = new JButton();
		buttonTen = new JButton();
		noVoteButton = new JButton();

		JButton buttons[] =
			{
				buttonOne,
				buttonTwo,
				buttonThree,
				buttonFour,
				buttonFive,
				buttonSix,
				buttonSeven,
				buttonEight,
				buttonNine,
				buttonTen,
				noVoteButton };

		JLabel labels[] =
			{
				choiceLabelOne,
				choiceLabelTwo,
				choiceLabelThree,
				choiceLabelFour,
				choiceLabelFive,
				choiceLabelSix,
				choiceLabelSeven,
				choiceLabelEight,
				choiceLabelNine,
				choiceLabelTen,
				noVoteLabel };

		for (int i = 0; i < 11; i++) {
			addComponentsToChoicePanel(
				radioButtons[i],
				buttons[i],
				labels[i],
				i);
		}

		leftPanel.add(choicePanel, BorderLayout.CENTER);
		leftPanel.add(leftNorth, BorderLayout.NORTH);
		leftPanel.add(leftEast, BorderLayout.EAST);
		leftPanel.add(leftWest, BorderLayout.WEST);
		leftPanel.add(leftSouth, BorderLayout.SOUTH);

		////////////////////////////////////////////////////////////////////
		//right panel that contains the result panel		
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(bg);
		rightPanel.setLayout(new BorderLayout());

		JPanel rightNorth = new JPanel();
		rightNorth.setLayout(new BorderLayout());

		JPanel resultsLabelPanel = new JPanel();
		resultsLabelPanel.setBackground(bg);
		resultsLabelPanel.setLayout(new BorderLayout());
		JLabel resultsLabel = new JLabel("Poll Results :");
		resultsLabel.setFont(ScoolConstants.FONT_BOLD);
		resultsLabelPanel.add(resultsLabel, BorderLayout.WEST);
		JPanel resultsTempLabelPanel = new JPanel();
		resultsTempLabelPanel.setBackground(bg);
		rightNorth.add(resultsTempLabelPanel, BorderLayout.WEST);
		rightNorth.add(resultsLabelPanel, BorderLayout.CENTER);

		rightNorth.setBackground(bg);
		JPanel rightSouth = new JPanel();
		rightSouth.setBackground(bg);
		JPanel rightEast = new JPanel();
		rightEast.setBackground(bg);
		JPanel rightWest = new JPanel();
		rightWest.setBackground(bg);

		resultPanel = new JPanel();
		resultPanel.setBackground(bg);
		resultPanel.setBorder(
			BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
		resultPanel.setLayout(new GridLayout(10, 1));

		ImageIcon labelImage =
			new ImageIcon(
				getClass().getClassLoader().getResource("images/Result.gif"));
		resultOne = new JLabel(labelImage);
		resultTwo = new JLabel(labelImage);
		resultThree = new JLabel(labelImage);
		resultFour = new JLabel(labelImage);
		resultFive = new JLabel(labelImage);
		resultSix = new JLabel(labelImage);
		resultSeven = new JLabel(labelImage);
		resultEight = new JLabel(labelImage);
		resultNine = new JLabel(labelImage);
		resultTen = new JLabel(labelImage);

		JLabel resultLabels[] =
			{
				resultOne,
				resultTwo,
				resultThree,
				resultFour,
				resultFive,
				resultSix,
				resultSeven,
				resultEight,
				resultNine,
				resultTen };

		percentageOne = new JLabel();
		percentageTwo = new JLabel();
		percentageThree = new JLabel();
		percentageFour = new JLabel();
		percentageFive = new JLabel();
		percentageSix = new JLabel();
		percentageSeven = new JLabel();
		percentageEight = new JLabel();
		percentageNine = new JLabel();
		percentageTen = new JLabel();

		JLabel percentageLabels[] =
			{
				percentageOne,
				percentageTwo,
				percentageThree,
				percentageFour,
				percentageFive,
				percentageSix,
				percentageSeven,
				percentageEight,
				percentageNine,
				percentageTen };

		for (int i = 0; i < resultLabels.length; i++) {
			addComponentsToResultPanel(resultLabels[i], percentageLabels[i], i);
		}

		rightPanel.add(resultPanel, BorderLayout.CENTER);
		rightPanel.add(rightNorth, BorderLayout.NORTH);
		rightPanel.add(rightSouth, BorderLayout.SOUTH);
		rightPanel.add(rightEast, BorderLayout.EAST);
		rightPanel.add(rightWest, BorderLayout.WEST);

		////////////////////////////////////////////
		// panel that contains the summary of the poll event

		JPanel summaryPanel = new JPanel();
		summaryPanel.setLayout(new BorderLayout());

		JPanel summaryNorth = new JPanel();
		summaryNorth.setLayout(new BorderLayout());
		summaryNorth.setBackground(bg);

		JPanel summaryLabelPanel = new JPanel();
		summaryLabelPanel.setBackground(bg);
		summaryLabelPanel.setLayout(new BorderLayout());
		JLabel summaryLabel = new JLabel("Summary :");
		summaryLabel.setFont(ScoolConstants.FONT_BOLD);
		summaryLabelPanel.add(summaryLabel, BorderLayout.WEST);
		JPanel tempSummaryLabelPanel = new JPanel();
		tempSummaryLabelPanel.setBackground(bg);
		summaryNorth.add(tempSummaryLabelPanel, BorderLayout.WEST);
		summaryNorth.add(summaryLabelPanel, BorderLayout.CENTER);

		JPanel summarySouth = new JPanel();
		summarySouth.setBackground(bg);

		//////////////////////////////////////
		// panel that contains the polled percentage and the unpolled percentage

		JPanel summaryCenter = new JPanel();
		summaryCenter.setLayout(new BorderLayout());
		summaryCenter.setBackground(bg);
		summaryCenter.setBorder(
			BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));

		JPanel percentagePanel = new JPanel();
		percentagePanel.setBackground(bg);
		percentagePanel.setLayout(new GridLayout(4, 4));

		JLabel noOfParticipantsLabel = new JLabel("No Of Participant(s) : ");
		noOfParticipantsLabel.setBackground(bg);
		noOfParticipantsLabel.setFont(ScoolConstants.FONT);

		JLabel noOfPartcipants = new JLabel(" 1 ");
		noOfPartcipants.setBackground(bg);
		noOfPartcipants.setFont(ScoolConstants.FONT_BOLD);

		JLabel polledLabel = new JLabel("Percentage of Polled Votes :");
		polledLabel.setBackground(bg);
		polledLabel.setFont(ScoolConstants.FONT);

		JLabel polledPercentage = new JLabel(" 0 %");
		polledPercentage.setBackground(bg);
		polledPercentage.setFont(ScoolConstants.FONT_BOLD);

		JLabel unPolledLabel = new JLabel("Percentage of Un-Polled Votes :");
		unPolledLabel.setBackground(bg);
		unPolledLabel.setFont(ScoolConstants.FONT);

		JLabel unPolledPercentage = new JLabel(" 0 %");
		unPolledPercentage.setBackground(bg);
		unPolledPercentage.setFont(ScoolConstants.FONT_BOLD);

		JLabel pollStatusLabel = new JLabel("Poll Status : ");
		pollStatusLabel.setFont(ScoolConstants.FONT);
		pollStatusLabel.setBackground(bg);

		pollStatus = new JLabel("Polls are open.....");
		pollStatus.setFont(ScoolConstants.FONT);
		pollStatus.setBackground(bg);

		percentagePanel.add(pollStatusLabel);
		percentagePanel.add(pollStatus);
		percentagePanel.add(noOfParticipantsLabel);
		percentagePanel.add(noOfPartcipants);
		percentagePanel.add(polledLabel);
		percentagePanel.add(polledPercentage);
		percentagePanel.add(unPolledLabel);
		percentagePanel.add(unPolledPercentage);

		JPanel tempNorth = new JPanel();
		tempNorth.setBackground(bg);
		JPanel tempSouth = new JPanel();
		tempSouth.setBackground(bg);
		JPanel tempEast = new JPanel();
		tempEast.setBackground(bg);
		JPanel tempWest = new JPanel();
		tempWest.setBackground(bg);

		summaryCenter.add(tempNorth, BorderLayout.NORTH);
		summaryCenter.add(tempSouth, BorderLayout.SOUTH);
		summaryCenter.add(percentagePanel, BorderLayout.CENTER);
		summaryCenter.add(tempEast, BorderLayout.EAST);
		summaryCenter.add(tempWest, BorderLayout.WEST);

		JPanel summaryEast = new JPanel();
		summaryEast.setBackground(bg);
		JPanel summaryWest = new JPanel();
		summaryWest.setBackground(bg);

		summaryPanel.add(summaryNorth, BorderLayout.NORTH);
		summaryPanel.add(summarySouth, BorderLayout.SOUTH);
		summaryPanel.add(summaryCenter, BorderLayout.CENTER);
		summaryPanel.add(summaryEast, BorderLayout.EAST);
		summaryPanel.add(summaryWest, BorderLayout.WEST);

		////////////////////////////////
		//add panels 		
		centerPanel.add(leftPanel, BorderLayout.CENTER);
		centerPanel.add(rightPanel, BorderLayout.EAST);

		add(headerPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(summaryPanel, BorderLayout.SOUTH);
	}

	private void addComponentsToChoicePanel(
		JRadioButton radioButton,
		JButton button,
		JLabel label,
		int index) {

		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BorderLayout());
		tempPanel.setBackground(bg);

		JPanel panel = new JPanel();
		panel.setBackground(bg);

		radioButton.addActionListener(this);
		if(radioButton == noVoteRadio){
			radioButton.setSelected(true);
		}else{
			radioButton.setSelected(false);
		}		
		radioButton.setBackground(bg);
		radioButton.setVisible(false);
		group.add(radioButton);
		radioButtonList.add(index, radioButton);

		button.addActionListener(this);
		buttonList.add(index, button);
		button.setMargin(ScoolConstants.INSETS);
		button.setVisible(false);

		label = new JLabel("");
		label.setBackground(bg);
		label.setFont(ScoolConstants.FONT);
		label.setVisible(false);
		labelList.add(index, label);

		panel.add(radioButton);
		panel.add(button);
		panel.add(label);

		tempPanel.add(panel, BorderLayout.WEST);

		choicePanel.add(tempPanel);
	}

	private void addComponentsToResultPanel(
		JLabel label,
		JLabel percentage,
		int index) {
		JPanel panel = new JPanel();
		panel.setBackground(bg);
		resultList.add(index, label);
		panel.add(label);
		panel.add(percentage);
		percentageList.add(index, percentage);
		resultPanel.add(panel);
	}

	public JTextArea getQuestion() {
		return questionArea;
	}

	public ArrayList getButtonList() {
		return buttonList;
	}

	public ArrayList getRadioButtonList() {
		return radioButtonList;
	}

	public ArrayList getCheckBoxList() {
		return checkBoxList;
	}

	public ArrayList getLabelList() {
		return labelList;
	}

	public ArrayList getResultList() {
		return resultList;
	}

	public ArrayList getPercentageList() {
		return percentageList;
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == buttonOne
			|| evt.getSource() == choiceRadioOne) {
			selectButtons(choiceRadioOne);
			deSelectAll();
			setResultImage(
				resultOne,
				pollTool.getChoices().get(0).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonTwo
				|| evt.getSource() == choiceRadioTwo) {
			selectButtons(choiceRadioTwo);
			deSelectAll();
			setResultImage(
				resultTwo,
				pollTool.getChoices().get(1).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonThree
				|| evt.getSource() == choiceRadioThree) {
			selectButtons(choiceRadioThree);
			deSelectAll();
			setResultImage(
				resultThree,
				pollTool.getChoices().get(2).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonFour
				|| evt.getSource() == choiceRadioFour) {
			selectButtons(choiceRadioFour);
			deSelectAll();
			setResultImage(
				resultFour,
				pollTool.getChoices().get(3).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonFive
				|| evt.getSource() == choiceRadioFive) {
			selectButtons(choiceRadioFive);
			deSelectAll();
			setResultImage(
				resultFive,
				pollTool.getChoices().get(4).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonSix
				|| evt.getSource() == choiceRadioSix) {
			selectButtons(choiceRadioSix);
			deSelectAll();
			setResultImage(
				resultSix,
				pollTool.getChoices().get(5).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonSeven
				|| evt.getSource() == choiceRadioSeven) {
			selectButtons(choiceRadioSeven);
			deSelectAll();
			setResultImage(
				resultSeven,
				pollTool.getChoices().get(6).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonEight
				|| evt.getSource() == choiceRadioEight) {
			selectButtons(choiceRadioEight);
			deSelectAll();
			setResultImage(
				resultEight,
				pollTool.getChoices().get(7).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonNine
				|| evt.getSource() == choiceRadioNine) {
			selectButtons(choiceRadioNine);
			deSelectAll();
			setResultImage(
				resultNine,
				pollTool.getChoices().get(8).toString().substring(0, 2));
			//freezeButtons();

		} else if (
			evt.getSource() == buttonTen
				|| evt.getSource() == choiceRadioTen) {
			selectButtons(choiceRadioTen);
			deSelectAll();
			setResultImage(
				resultTen,
				pollTool.getChoices().get(9).toString().substring(0, 2));
			//freezeButtons();
		} else if (
			evt.getSource() == noVoteButton
				|| evt.getSource() == noVoteRadio) {
			selectButtons(noVoteRadio);
			deSelectAll();
		}
	}

	private void deSelectAll() {
		for (int i = 0; i < resultList.size(); i++) {
			((JLabel) resultList.get(i)).setIcon(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "PB_Result_Gif"))));
		}
	}

	private void setResultImage(JLabel resultLabel, String name) {
		String token = name;
		ImageIcon resultImage;

		int choice = Integer.parseInt(token);

		switch (choice) {

			case 1 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result1_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 2 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result2_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 3 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result3_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 4 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result4_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 5 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result5_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 6 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result6_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 7 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result7_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 8 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result8_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 9 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result9_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}

			case 10 :
				{
					resultImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Result10_Gif")));
					resultLabel.setIcon(resultImage);
					break;
				}
		}
	}

	private void selectButtons(JRadioButton radioButton) {
		if (!radioButton.isSelected()) {
			radioButton.setSelected(true);
		}
	}

	private void freezeButtons() {
		for (int i = 0; i < radioButtonList.size(); i++) {
			((JRadioButton) radioButtonList.get(i)).setEnabled(false);
			noVoteRadio.removeActionListener(this);
		}
		for (int i = 0; i < buttonList.size(); i++) {
			((JButton) buttonList.get(i)).removeActionListener(this);
			noVoteButton.removeActionListener(this);
		}
	}
}
