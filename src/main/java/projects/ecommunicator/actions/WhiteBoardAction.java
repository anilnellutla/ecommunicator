package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import projects.ecommunicator.application.DataEvent;
import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.panelboard.PresentationInfo;
import projects.ecommunicator.presentationinfo.PresentationInfoListData;
import projects.ecommunicator.utility.NewPage;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * This is a sub class of AbstractAction used to handle new WhiteBoard events.
 * This event is occured by clicking Whiteboard button on toolbar or by selecting
 * WhiteBoard(ctrl-w) from Create Menu.
 * <P> 
 * @author  Anil K Nellutla
 * @see javax.swing.AbstractAction
 * @version 1.0
 */
public class WhiteBoardAction extends AbstractAction {

	// WhiteBoardCanvas reference used to create new page on WhiteBoard.
	private WhiteBoardCanvas canvas;
	// PresentationInfo reference used to add
	private PresentationInfo presentationInfo;

	/**
	 * Creates a new instance of WhiteBoardAction.
	 * @param whiteBoardDesktopPane reference used to get 
	 * WhiteBoardCanvas and PresentationInfo references.
	 */
	public WhiteBoardAction(WhiteBoardDesktopPane whiteBoardDesktopPane) {
		this.canvas = whiteBoardDesktopPane.getWhiteBoard().getCanvas();
		this.presentationInfo = whiteBoardDesktopPane.getPresentationInfo();
	}

	/**
	 * invoked when an action occurs
	 * @param evt A semantic event which indicates that a component-defined
	 * action occured.
	 */
	public void actionPerformed(ActionEvent e) {
		// create an instance of NewPage
		NewPage newPage = new NewPage(canvas);
		// create new WhiteBoard Page
		newPage.createNewWbPage();
		// create an instance PresentationInfoListData object to 
		//add to PresentationList.
		PresentationInfoListData data =
			new PresentationInfoListData(0, false, newPage.getKey());
		presentationInfo.add(data);
		// notify all the participants that a new WhiteBoard has been opened
		// and selected
		DataEvent.notifySender(
			canvas.getApplicationType(),
			presentationInfo.getListModel().getSize(),
			newPage.getKey(),
			getToolString());
			
		// select the current new WhiteBoard
		presentationInfo.getPresentationList().setSelectedValue(data, true);

	}

	/**
	 * Returns tool String based on applciation protocol for new WhiteBoardAction
	 * @see com.adaequare.eCommunicator.util.FormatData 
	 * @return tool String based application protocol
	 */
	public String getToolString() {
		return Utility.convertTo4Byte(ScoolConstants.NEW_WHITEBOARD_ACTION);
	}
}