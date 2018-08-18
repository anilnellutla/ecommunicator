package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.panelboard.PresentationInfo;
import projects.ecommunicator.poll.PollDialog;
import projects.ecommunicator.poll.PollOptions;
import projects.ecommunicator.presentationinfo.PresentationInfoListData;
import projects.ecommunicator.utility.NewPage;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PollAction extends AbstractAction {
	private WhiteBoardFrame frame;
	private WhiteBoardDesktopPane whiteBoardDesktopPane;
	private PresentationInfo presentationInfo;
	private PollDialog pollDialog;

	public PollAction(WhiteBoardFrame frame) {
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent evt) {
		this.whiteBoardDesktopPane =
			frame.getWhiteBoardPanel().getWhiteBoardDesktopPane();
		this.presentationInfo = whiteBoardDesktopPane.getPresentationInfo();		
		pollDialog = new PollDialog(frame, this);
		pollDialog.show();
	}

	public void createNewPollPage(PollOptions pollOptions) {
		if (pollDialog.isVisible()) {
			pollDialog.setVisible(false);
		}
		//creates NewPage object for the WhiteBoardCanvas
		NewPage newPage =
			new NewPage(whiteBoardDesktopPane.getWhiteBoard().getCanvas());
		//creates NewPollPageNo that refers the Poll Board
		newPage.createNewPollPage(pollOptions, presentationInfo);
		//creates PresentationInfoListData to place it in the presentationInfo List
		PresentationInfoListData data =  new PresentationInfoListData(0, false, newPage.getKey());
		//and selects the created pptPage
		presentationInfo.getPresentationList().setSelectedValue(data, true);

	}

}
