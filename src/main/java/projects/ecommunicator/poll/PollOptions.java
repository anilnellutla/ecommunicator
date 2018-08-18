package projects.ecommunicator.poll;

import java.util.ArrayList;

public class PollOptions {

	private String questionTyped = "";	
	private ArrayList choices;

	public PollOptions() {
		choices = new ArrayList();
	}

	public void setQuestionTyped(String questionTyped) {
		this.questionTyped = questionTyped;
	}	

	public void addChoice(String choice) {
		choices.add(choice);
	}

	public String getQuestion() {
		return questionTyped;
	}

	public ArrayList getChoices() {
		return choices;
	}
		
}
