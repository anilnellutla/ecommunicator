/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.participantinfo;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ParticipantState {

	private int micOption;
	private int messageBoardOption;
	private int presentationInfoOption;
	private int whiteBoardOption;
	private static ParticipantState singleton;

	private ParticipantState() {
	}

	public synchronized static ParticipantState getInstance() {
		if (singleton == null) {
			singleton = new ParticipantState();
			return singleton;
		}
		return singleton;
	}

	public void setMicOption(int micOption) {
		this.micOption = micOption;
	}

	public void setMessagBoardOption(int messageBoardOption) {
		this.messageBoardOption = messageBoardOption;
	}

	public void setPresentationInfoOption(int presentationInfoOption) {
		this.presentationInfoOption = presentationInfoOption;
	}

	public void setWhiteBoardOption(int whiteBoardOption) {
		this.whiteBoardOption = whiteBoardOption;
	}

	public int getMicOption() {
		return micOption;
	}

	public int getMessageBoardOption() {
		return messageBoardOption;
	}

	public int getPresentatioInfoOption() {
		return presentationInfoOption;
	}

	public int getWhiteBoardOption() {
		return whiteBoardOption;
	}

}
