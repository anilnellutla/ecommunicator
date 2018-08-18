package projects.ecommunicator.application;

/** 
 * This class is used to enable/disable the components of WhiteBoardFrame
 * container.
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */
public class PermissionManager {
	// container reference used to access all the components contained by it.
	private WhiteBoardFrame frame;

	/**
	 * Creates an instance of PermissionManager.
	 * @param frame container for all the components
	 */
	public PermissionManager(WhiteBoardFrame frame) {
		this.frame = frame;
	}

	/**
	 * This method enables talk button of VoiceBoard.
	 */
		
	public void allowMic() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getVoiceBoard()
			.getTalkButton()
			.setEnabled(true);
	}
	
	

	/**
	 * This method disables talk button of VoiceBoard.
	 */
	
	public void disAllowMic() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getVoiceBoard()
			.getTalkButton()
			.setEnabled(false);
	}
	

	/**
	 * This method enables all the componenst of MessagingBoard	 
	 */
	public void allowMessageBoard() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getMessagingBoard()
			.getChatPane()
			.setEnabled(true);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getMessagingBoard()
			.getMesPane()
			.setEnabled(true);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getMessagingBoard()
			.getSendButton()
			.setEnabled(true);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getMessagingBoard()
			.getMessageTo()
			.setEnabled(true);
	}

	/**
	 * This method disables all the components of MessagingBoard.	 
	 */
	public void disAllowMessageBoard() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getMessagingBoard()
			.getChatPane()
			.setEnabled(false);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getMessagingBoard()
			.getMesPane()
			.setEnabled(false);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getMessagingBoard()
			.getSendButton()
			.setEnabled(false);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getMessagingBoard()
			.getMessageTo()
			.setEnabled(false);
	}

	/**
	 * This method enables all the components of PresentationInfo.
	 */
	public void allowPresentationInfo() {

	}

	/**
	 * This method disables all the components of PresentationInfo.
	 */
	public void disAllowPresentationInfo() {

	}

	/**
	 * This method enables WhiteBoard, Online Presentation, AppSharing, ViewDesktop	 
	 */

	public void allowWhiteBoard() {
		//allowTools();
		//allowCanvas();
		allowNewWhiteBoard();
		allowOnlinePresentation();
		//allowScreenShot();
		allowAppSharing();
		allowViewDesktop();
	}

	/**
	 * This method disables WhiteBoard, Online Presentation, AppSharing, ViewDesktop	 
	 */
	public void disAllowWhiteBoard() {
		//disAllowTools();
		//disAllowCanvas();
		disAllowNewWhiteBoard();
		disAllowOnlinePresentation();
		//disAllowScreenShot();
		disAllowAppSharing();
		disAllowViewDesktop();
	}

	/*
	private void allowTools() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getCircleButton()
			.setEnabled(true);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getLineButton()
			.setEnabled(true);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getRectButton()
			.setEnabled(true);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getFreeHandButton()
			.setEnabled(true);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getRoundRectButton()
			.setEnabled(true);
	}
	
	private void allowCanvas() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getCanvas()
			.setEnabled(true);
	}
	*/
	/**
	 * This method enables WhiteBoard	 
	 */
	private void allowNewWhiteBoard() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getWhiteBoardButton()
			.setEnabled(true);

		frame.getWhiteBoardMenuBar().getCreateMenu().getWhiteBoardMenuItem().setEnabled(true);
	}

	/**
	 * This method enables Online Presentation	 
	 */
	private void allowOnlinePresentation() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getOnlinePresentationButton()
			.setEnabled(true);

		frame.getWhiteBoardMenuBar().getCreateMenu().getOnlinePresentationMenuItem().setEnabled(true);
	}

	/*
	private void allowScreenShot() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getScreenShotButton()
			.setEnabled(true);
		
		frame.getCreateMenu().getScreenShotMenuItem().setEnabled(true);
	}
	*/
	/**
	 * This method enables App Sharing	 
	 */
	private void allowAppSharing() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getAppSharingButton()
			.setEnabled(true);

		frame.getWhiteBoardMenuBar().getCreateMenu().getApplicationSharingMenuItem().setEnabled(true);
	}

	/**
	 * This method enables ViewDesktop	 
	 */
	private void allowViewDesktop() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getViewDesktopButton()
			.setEnabled(true);

		frame.getWhiteBoardMenuBar().getCreateMenu().getViewDesktopMenuItem().setEnabled(true);
	}

	/*
	private void disAllowTools() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getCircleButton()
			.setEnabled(false);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getLineButton()
			.setEnabled(false);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getRectButton()
			.setEnabled(false);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getFreeHandButton()
			.setEnabled(false);
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getRoundRectButton()
			.setEnabled(false);
	}
	
	
	private void disAllowCanvas() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardDesktopPane()
			.getWhiteBoard()
			.getCanvas()
			.setEnabled(false);
	}
	*/

	/**
	 * This method disables WhiteBoard	 
	 */
	
	private void disAllowNewWhiteBoard() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getWhiteBoardButton()
			.setEnabled(false);

		frame.getWhiteBoardMenuBar().getCreateMenu().getWhiteBoardMenuItem().setEnabled(false);
	}
	

	/**
	 * This method disables OnlinePresentation	 
	 */
	private void disAllowOnlinePresentation() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getOnlinePresentationButton()
			.setEnabled(false);

		frame.getWhiteBoardMenuBar().getCreateMenu().getOnlinePresentationMenuItem().setEnabled(false);
	}

	/*
	private void disAllowScreenShot() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getScreenShotButton()
			.setEnabled(false);
			
		frame.getCreateMenu().getScreenShotMenuItem().setEnabled(false);
	}
	*/

	/**
	 * This method disables AppSharing	 
	 */
	private void disAllowAppSharing() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getAppSharingButton()
			.setEnabled(false);

		frame.getWhiteBoardMenuBar().getCreateMenu().getApplicationSharingMenuItem().setEnabled(false);
	}

	/**
	 * This method disables View Desktop	 
	 */
	private void disAllowViewDesktop() {
		frame
			.getWhiteBoardPanel()
			.getWhiteBoardToolBar()
			.getViewDesktopButton()
			.setEnabled(false);

		frame.getWhiteBoardMenuBar().getCreateMenu().getViewDesktopMenuItem().setEnabled(false);
	}

}
