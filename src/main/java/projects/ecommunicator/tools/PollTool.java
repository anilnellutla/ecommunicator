/*
 * Created on Jul 1, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import projects.ecommunicator.internalframes.WhiteBoard;
import projects.ecommunicator.poll.PollButtonPanel;
import projects.ecommunicator.poll.ResultPanel;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PollTool extends Poll {

	private String questionTyped = " ";
	private ArrayList choices;
	private ResultPanel resultPanel;

	public void setChoices(ArrayList choices) {
		this.choices = choices;
	}

	public ArrayList getChoices() {
		return choices;
	}

	public String getQuestionTyped() {
		return questionTyped;
	}

	public void setQuestionTyped(String questionTyped) {
		this.questionTyped = questionTyped;
	}

	public void showResultPanel(WhiteBoardCanvas canvas) {

		//code for result panel generation
		resultPanel = new ResultPanel(this);

		//setting the question typed in the panel
		resultPanel.getQuestion().setText(questionTyped);

		//setting the choices on panel
		int numberOfChoice = choices.size();

		for (int i = 0; i < numberOfChoice; i++) {
			String question = choices.get(i).toString().trim();

			(
				(JRadioButton)
					(resultPanel.getRadioButtonList().get(i))).setVisible(
				true);
			((JLabel) (resultPanel.getLabelList().get(i))).setText(
				question.substring(2, question.length()));
			setButtonImage(
				((JButton) (resultPanel.getButtonList().get(i))),
				question.substring(0, 2));
			((JButton) (resultPanel.getButtonList().get(i))).setVisible(true);
			((JLabel) (resultPanel.getLabelList().get(i))).setVisible(true);
			((JLabel) (resultPanel.getResultList().get(i))).setBorder(
				BorderFactory.createLineBorder(Color.GRAY));
			((JLabel) (resultPanel.getPercentageList().get(i))).setText("0%");
		}

		setNoVoteButton();
		
		WhiteBoard whiteBoard = canvas.getWhiteBoard();
		PollButtonPanel pollButtonPanel = new PollButtonPanel();
		JPanel panel = whiteBoard.getWhiteBoardButtonPanel(); 
		
		if( panel != null){
			whiteBoard.remove(panel);
			whiteBoard.setPollButtonPanel(pollButtonPanel);
			whiteBoard.getContentPane().add(pollButtonPanel,BorderLayout.NORTH);
			whiteBoard.validate();		
		}
		
		canvas.removeAll();
		canvas.setResultPanel(resultPanel);
				
		canvas.add(resultPanel, BorderLayout.CENTER);
		canvas.validate();

	}

	private void setButtonImage(JButton button, String name) {
		String token = name;
		ImageIcon buttonImage;

		int choice = Integer.parseInt(token);

		switch (choice) {

			case 1 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice1_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 2 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice2_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 3 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice3_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 4 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice4_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 5 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice5_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 6 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice6_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 7 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice7_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 8 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice8_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 9 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice9_Gif")));
					button.setIcon(buttonImage);
					break;
				}

			case 10 :
				{
					buttonImage =
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"PB_Choice10_Gif")));
					button.setIcon(buttonImage);
					break;
				}
		}
	}

	private void setNoVoteButton() {
		int size = resultPanel.getRadioButtonList().size() - 1;
		(
			(JRadioButton)
				(resultPanel.getRadioButtonList().get(size))).setVisible(
			true);
		((JButton) (resultPanel.getButtonList().get(size))).setIcon(
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "PB_NoVote_Gif"))));
		((JButton) (resultPanel.getButtonList().get(size))).setVisible(true);
		((JLabel) (resultPanel.getLabelList().get(size))).setText("NoVote");
		((JLabel) (resultPanel.getLabelList().get(size))).setVisible(true);
	}

	// pollTool's ToolString
	public String getToolString() {

		StringBuffer choices = new StringBuffer("");
		StringBuffer lengthOfChoices = new StringBuffer("");

		if (this.choices != null) {

			for (int i = 0; i < this.choices.size(); i++) {
				choices.append(this.choices.get(i).toString());
				lengthOfChoices.append(
					Utility.convertTo4Byte(
						this.choices.get(i).toString().length()));
			}

			String toolString =
				Utility.convertTo4Byte(ScoolConstants.POLL_TOOL)
					+ Utility.convertTo4Byte(0)
					+ Utility.convertTo4Byte(0)
					+ Utility.convertTo4Byte(questionTyped.length())
					+ questionTyped.trim()
					+ Utility.convertTo4Byte(lengthOfChoices.length())
					+ lengthOfChoices.toString()
					+ choices.toString();
			return toolString;
		}
		return null;
	}

}
