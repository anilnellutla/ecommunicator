package projects.ecommunicator.utility;

import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import projects.ecommunicator.application.DataEvent;
import projects.ecommunicator.panelboard.PresentationInfo;
import projects.ecommunicator.poll.PollOptions;
import projects.ecommunicator.presentationinfo.PresentationInfoListData;
import projects.ecommunicator.tools.PPtImageTool;
import projects.ecommunicator.tools.PollTool;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

public class NewPage {

	private WhiteBoardCanvas canvas;
	private int newWbPageNo;
	private int newPPtPageNo;
	private int newPollPageNo;
	//private int newScreenShotPageNo;
	private String key;

	public NewPage(WhiteBoardCanvas canvas) {
		this.canvas = canvas;
	}

	public void createNewWbPage() {
		//canvas.setCursor(Cursor.getDefaultCursor());
		OffScreenBuffer offScBuffer =
			new OffScreenBuffer(canvas.getBackground(), canvas.getForeground());
		offScBuffer.createOffScreenBuffer();

		canvas.setOffScreen(offScBuffer.getOffScreen());
		canvas.setOffScreenGraphics(offScBuffer.getOffScreenGraphics());

		int wbPageNo = canvas.getWbPageNo();
		newWbPageNo = wbPageNo + 1;
		canvas.setWbPageNo(newWbPageNo);

		canvas.setApplicationType(ScoolConstants.WHITE_BOARD_APP);

		key = ScoolConstants.WHITE_BOARD + newWbPageNo;
		Vector page = new Vector();
		canvas.setPage(page);
		canvas.addPage(key, page);
		//canvas.setDrawTool(0);
	}

	public void createNewPPtPage(
		ArrayList slidePaths,
		PresentationInfo presentationInfo) {
		canvas.setCursor(new Cursor(0));
		OffScreenBuffer offScBuffer =
			new OffScreenBuffer(canvas.getBackground(), canvas.getForeground());
		offScBuffer.createOffScreenBuffer();

		canvas.setOffScreen(offScBuffer.getOffScreen());
		canvas.setOffScreenGraphics(offScBuffer.getOffScreenGraphics());
		canvas.setApplicationType(ScoolConstants.PPT_PRESENTATION_APP);

		int pptPageNo = canvas.getPPtPageNo();
		newPPtPageNo = pptPageNo + 1;
		key = ScoolConstants.PPT_PRESENTATION + newPPtPageNo;
		//key = new PresentationInfoListData(0,false,ScoolConstants.PPT_PRESENTATION + newPPtPageNo);

		for (int i = 0; i < slidePaths.size(); i++) {
			PPtImageTool pptImageTool = new PPtImageTool();
			pptImageTool.imagePath = slidePaths.get(i).toString();
			Vector page = new Vector();
			String toolString = pptImageTool.getToolString();
			page.add(toolString);
			String objName = ScoolConstants.PPT_PRESENTATION + newPPtPageNo;
			canvas.addPage(objName, page);
			PresentationInfoListData data =
				new PresentationInfoListData(0, false, objName);
			presentationInfo.add(data);
			newPPtPageNo++;

			DataEvent.notifySender(
				ScoolConstants.WHITE_BOARD_APP,
				canvas.getPages().size(),
				objName,
				toolString);
		}
		canvas.setPPtPageNo(newPPtPageNo - 1);

	}

	public void createNewScreenShotPage(BufferedImage capturedImage) {
		//OffScreenBuffer offScBuffer = new OffScreenBuffer(canvas.getBackground(), canvas.getForeground());
		//offScBuffer.createOffScreenBuffer();

		//canvas.setOffScreen( offScBuffer.getOffScreen() );
		//canvas.setOffScreenGraphics (offScBuffer.getOffScreenGraphics() );

		//int screenShotPageNo = canvas.getScreenShotPageNo();
		//newScreenShotPageNo = screenShotPageNo + 1;
		//canvas.setScreenShotPageNo(newScreenShotPageNo);
		//String objName = ScoolConstants.SCREEN_SHOT+newScreenShotPageNo;
		//key = ScoolConstants.SCREEN_SHOT+newScreenShotPageNo;
		//canvas.setCurrentPage(objName);
		//ArrayList page = new ArrayList();
		//canvas.setPage(page);
		//canvas.addPage(objName, page);
		//presentationInfo.add(objName);
		canvas.screenShotEvent(capturedImage);
		canvas.setDrawTool(ScoolConstants.SCREEN_SHOT_TOOL);
	}

	public void createNewSnapGraphPage(BufferedImage capturedImage) {
		canvas.snapGraphEvent(capturedImage);
		canvas.setDrawTool(ScoolConstants.SNAP_GRAPH_SHOT_TOOL);
	}

	public void createNewPollPage(PollOptions pollOptions, PresentationInfo presentationInfo) {

		canvas.setCursor(new Cursor(0));	
		canvas.setApplicationType(ScoolConstants.WHITE_BOARD_APP);
		
		int pollPageNo = canvas.getPollPageNo();		
		newPollPageNo = pollPageNo + 1;
		key = ScoolConstants.POLL+ newPollPageNo;		
		//key = new PresentationInfoListData(0,false,ScoolConstants.PPT_PRESENTATION + newPPtPageNo);		
		PollTool pollTool = new PollTool();
		pollTool.setQuestionTyped(pollOptions.getQuestion());		
		pollTool.setChoices(pollOptions.getChoices());
		Vector page = new Vector();		
		String toolString = pollTool.getToolString();
		page.add(toolString);
		String objName = ScoolConstants.POLL + newPollPageNo;
		canvas.addPage(objName, page);
		PresentationInfoListData data = new PresentationInfoListData(0, false, objName);
		presentationInfo.add(data);
		canvas.setPollPageNo(newPollPageNo);

		DataEvent.notifySender(ScoolConstants.WHITE_BOARD_APP,canvas.getPages().size(),objName,toolString);		

	}

	public int getNewWbPageNo() {
		return newWbPageNo;
	}

	public int getNewPPtPageNo() {
		return newPPtPageNo;
	}

	public String getKey() {
		return key;
	}
}