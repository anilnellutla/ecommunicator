package projects.ecommunicator.application;

import java.util.Hashtable;
import java.util.Vector;

import projects.ecommunicator.net.client.ReceivedDataPool;
import projects.ecommunicator.panelboard.MessagingBoard;
import projects.ecommunicator.net.client.Receiver;
import projects.ecommunicator.panelboard.ParticipantsInfo;
import projects.ecommunicator.panelboard.PresentationInfo;
import projects.ecommunicator.presentationinfo.PresentationInfoListData;
import projects.ecommunicator.utility.FormatData;
import projects.ecommunicator.utility.LoadPage;
import projects.ecommunicator.utility.NewPage;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/** 
 * This Thread is used to receive the data from the network sent by TCP Server
 * and call appropriate methods on AWTEvent Thread. This Thread waits on 
 * ReceivedDataPool object to be notified by Receiver Thread.
 * <p>
 * @author  Anil K Nellutla
 * @see com.adaequare.ecommunicator.util.FormatData#formatReceivedData(byte[])
 * @see Receiver#run()
 * @version 1.0
 */
public class DataListener implements Runnable {

	// container for all the components. This instance is used to get access to
	// all the components it contains. 
	private WhiteBoardFrame frame;
	// reference used to draw tools or images
	private WhiteBoardCanvas canvas;
	// reference for adding the page
	private Hashtable pages;
	// reference for updating PresentationInfo
	private PresentationInfo presentationInfo;
	// reference for displaying chat messages
	private MessagingBoard messagingBoard;
	// reference for showing the status of Participants
	private ParticipantsInfo participantsInfo;

	/**
	 * Creates an instance of DataListener Thread.
	 * @param frame container for all the components.
	 */
	public DataListener(WhiteBoardFrame frame) {
		// get the reference to the components contained by the WhiteBoardFrame

		this.frame = frame;
		this.canvas =
			frame
				.getWhiteBoardPanel()
				.getWhiteBoardDesktopPane()
				.getWhiteBoard()
				.getCanvas();
		this.pages = this.canvas.getPages();
		this.presentationInfo =
			frame
				.getWhiteBoardPanel()
				.getWhiteBoardDesktopPane()
				.getPresentationInfo();
		this.messagingBoard =
			frame
				.getWhiteBoardPanel()
				.getWhiteBoardDesktopPane()
				.getMessagingBoard();
		this.participantsInfo =
			frame
				.getWhiteBoardPanel()
				.getWhiteBoardDesktopPane()
				.getParticipantsInfo();
		participantsInfo.setWhiteBoardFrame(frame);
	}

	/** 
	 * Overridden method used to wait for the data from the network sent
	 *  by TCP Server.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/* this is the object on which this Thread is waiting to be notified
		   by the Receiver Thread.
		 */
		ReceivedDataPool pool = ReceivedDataPool.getInstance();

		// continuously wait for the data from the network
		while (true) {
			try {

				// wait on ReceivedDataPool object to be notified by Receiver Thread.
				synchronized (pool) {
					while (pool.isEmpty()) {
						try {
							pool.wait();
						} catch (InterruptedException ex) {
						}
					}
				}
				// get the data obtained from network (sent by TCP Server)
				byte[] payLoad = (byte[]) pool.remove(0);
				//System.out.println("\n received payLoad:"+new String(payLoad));

				/* format received data. The received data is based on application
				   protocol. For example: Header information is filterd out etc.
				 */
				String payLoadString = FormatData.formatReceivedData(payLoad);
				System.out.println(
					"\nreceived payload string:" + payLoadString);

				/* get the Application Type. 
				   for example:WHITE_BOARD_APP, PPT_PRESENTATION_APP, MESSAGE_BOARD_APP, 
				   PARTICIPANTS_INFO_APP
				*/
				int applicatonType = Utility.getApplicatonType(payLoadString);
				//System.out.println("received application type:" + applicatonType);

				// received page number.
				//int pageNo = Utility.getPageNo(payLoadString);
				//System.out.println("received page no:" + pageNo);

				// received page name
				String pageName = Utility.getPageName(payLoadString);
				//System.out.println("received page name:" + pageName);

				// received tool string
				String toolString = Utility.getToolString(payLoadString);
				//System.out.println("received tool string:" + toolString);

				// received tool type
				int toolType = Utility.getToolType(toolString);
				//System.out.println("received tool type:" + toolType);

				/* if the Application Type is WHITE_BOARD_APP i.e, 1, then check for the tooltype
				   and invoke appropriate method on AWTEvent Thread.
				 */
				if (applicatonType == ScoolConstants.WHITE_BOARD_APP) {
					/* if tool type is NAVIGATION_ACTION i.e 9. This tool type indicates
					 * that one of the participant in the same session has navigated 
					 * from one page to another.
					 */
					if (toolType == ScoolConstants.NAVIGATION_ACTION) {

						/* set the current session page. This is the page that is currently
						   been shared by all the participants.
						*/
						presentationInfo.setCurrentSessionPage(pageName);

						/* check if the participant is in sync with the session.
						   if the participant is not in sync with the session, then 
						   current session page should not be selected.
						*/
						if (presentationInfo.isSyncWithSession()) {
							/* if the participant is in sync with the session, select
							   the current session page. Usually, the data is put in
							   the network when the page in PresentaionList is selected.
							   In this case there is no need to send the data again to all 
							   the participatns though the page in Presentation is selected.
							   To avoid this, set sendValueChangedEvent to false.						    
							*/
							presentationInfo.setSendValueChangedEvent(false);
							// create data object to add to PresentationList
							PresentationInfoListData data =
								new PresentationInfoListData(
									0,
									false,
									pageName);
							// select the current page
							presentationInfo
								.getPresentationList()
								.setSelectedValue(
								data,
								true);
						}
					}
					/*  if tool type is NEW_WHITEBOARD_ACTION i.e, 8. This tool type
					 *  indicates that one of the participant with ALLOW_WHITEBOARD 
					 *  permission has created a new WhiteBoard page and selected it.  
					 */
					else if (
						toolType == ScoolConstants.NEW_WHITEBOARD_ACTION) {
						// create an instance of NewPage
						NewPage newPage = new NewPage(canvas);
						// create new WhiteBoard page
						newPage.createNewWbPage();
						// create a data object to add to PresentationList
						PresentationInfoListData data =
							new PresentationInfoListData(0, false, pageName);
						presentationInfo.add(data);
						/* if the participant is in sync with the session, select
						   the current session page. Usually, the data is put in
						   the network when the page in PresentaionList is selected.
						   In this case there is no need to send the data again to all 
						   the participatns though the page in Presentation is selected.
						   To avoid this, set sendValueChangedEvent to false.						    
						*/
						presentationInfo.setSendValueChangedEvent(false);
						/* set the current session page. This is the page that is currently
							   been shared by all the participants.
							   
							   ^^^^ bug to be fixed. The below lines have to be commented since
							   we do not know whether the sender participant has the Permission
							   for ALLOW_PRESENTATIONINFO. Instead, this tooltype can be
							   modified only to create new WhiteBoard. Selecting the new
							   WhiteBoard can be carried out in NAVIGATION_ACTION tool type
						*/

						//presentationInfo.setCurrentSessionPage(pageName);

						/* check if the participant is in sync with the session.
						   if the participant is not in sync with the session, then 
						   current session page should not be selected.
						*/

						/*
						if (presentationInfo.isSyncWithSession()) {
							// select the current page
							presentationInfo
								.getPresentationList()
								.setSelectedValue(
								data,
								true);
						}
						*/
					} else if (
						toolType == ScoolConstants.CIRCLE_TOOL
							|| toolType == ScoolConstants.RECT_TOOL
							|| toolType == ScoolConstants.ROUND_RECT_TOOL
							|| toolType == ScoolConstants.LINE_TOOL
							|| toolType == ScoolConstants.FREE_HAND_TOOL
							|| toolType == ScoolConstants.TEXT_TOOL
							|| toolType == ScoolConstants.SCREEN_SHOT_TOOL
							|| toolType == ScoolConstants.PPT_IMAGE_TOOL
							|| toolType == ScoolConstants.SNAP_GRAPH_SHOT_TOOL
							|| toolType == ScoolConstants.POLL_TOOL ) {
						/* for any other tool type received simply draw on WhiteBoardCanvas.
						 * The other tool types include:
						 * CIRCLE_TOOL 1
						 * LINE_TOOL 2
						 * RECT_TOOL 3
						 * ROUND_RECT_TOOL	4				 
						 * FREE_HAND_TOOL 5					 
						 * SCREEN_SHOT_TOOL 6
						 * PPT_IMAGE_TOOL 7					 
						 */
						Vector page = (Vector) pages.get(pageName);
						if (page != null) {
							page.add(toolString);
							if (pageName.equals(canvas.getCurrentPageName())) {
								canvas.drawCommandString(toolString);
							}
						}
						/*
						 * This scenario usually occurs with PPT_IMAGE_TOOL tool type.
						 * add the tool string to page and page name to pages. 
						 */
						else {
							page = new Vector();
							// add tool string to page
							page.add(toolString);
							// add page name to pages
							canvas.addPage(pageName, page);
							// create data object to add to PresentationList
							PresentationInfoListData data =
								new PresentationInfoListData(
									0,
									false,
									pageName);
							presentationInfo.add(data);
						}
						/* if  tool type is PPT_IMAGE_TOOL increment PPtPageNo count
						   so that Page Name with appropriate number is displayed
						   in Presentation List.
						*/
						if (toolType == ScoolConstants.PPT_IMAGE_TOOL) {
							canvas.setPPtPageNo(canvas.getPPtPageNo() + 1);
						}
					} else if (toolType == ScoolConstants.SELECT_TOOL) {
						int selectedToolPos =
							Integer.parseInt(toolString.substring(4, 12));
						Vector page = (Vector) pages.get(pageName);
						if (page != null) {
							page.remove(selectedToolPos);
							page.add(
								selectedToolPos,
								toolString.substring(12, toolString.length()));
							if (pageName.equals(canvas.getCurrentPageName())) {
								LoadPage loadPage =
									new LoadPage(canvas, pageName);
								/*
								Thread t = new Thread(loadPage);
								t.start();
								*/
								loadPage.run();
							}
						}

					} else if (toolType == ScoolConstants.EDIT_TOOL) {
						int selectedTool =
							Integer.parseInt(toolString.substring(4, 8));

						if (selectedTool == ScoolConstants.CUT_ACTION) {
							int selectedToolPos =
								Integer.parseInt(toolString.substring(8, 16));

							Vector page = (Vector) pages.get(pageName);
							if (page != null) {
								page.remove(selectedToolPos);
								if (pageName
									.equals(canvas.getCurrentPageName())) {
									LoadPage loadPage =
										new LoadPage(canvas, pageName);
									/*
									Thread t = new Thread(loadPage);
									t.start();
									*/
									loadPage.run();
								}
							}

						} else if (
							selectedTool == ScoolConstants.PASTE_ACTION) {
							int selectedToolPos =
								Integer.parseInt(toolString.substring(8, 16));
							Vector page = (Vector) pages.get(pageName);
							if (page != null) {								
								page.add(
									selectedToolPos,
									toolString.substring(
										16,
										toolString.length()));
								if (pageName
									.equals(canvas.getCurrentPageName())) {
									LoadPage loadPage =
										new LoadPage(canvas, pageName);
									/*
									Thread t = new Thread(loadPage);
									t.start();
									*/
									loadPage.run();
								}
							}
						}
					}
				}
				/* if the Application Type is MESSAGE_BOARD_APP i.e 3. Display the
				 * messages sent by other participants in chat area.
				 */
				else if (applicatonType == ScoolConstants.MESSAGE_BOARD_APP) {
					// get the login id of the sender participant
					String fromLoginId = Utility.getLoginId(toolString);
					// get the message sent by sender participant
					String textMessage = Utility.getMessageText(toolString);
					// copy the previous messages in chat area to a temp cache
					String history = messagingBoard.getChatPane().getText();
					// create formatted message to be displayed on chat area
					String message = history + fromLoginId + ":" + textMessage;
					// copy the message to the chat area
					messagingBoard.getChatPane().setText(message);
					// adjust the vertical scrollbar
					messagingBoard.getChatPane().scrollToReference(message);
				}
				/* if the Application Type is PARTICIPANTS_INFO_APP i.e, 4, then check 
				 * for the tooltypes:
				 * NEW_PARTICIPANT_JOINED_ACTION 11
				 * PARTICIPANT_LOGGEDOUT_ACTION 12
				 * PARTICIPANT_DISCONNECT_ACTION 13
				 * PARTICIPANT_STATE_CHANGED_ACTION 14
				 * MODERATOR_CHANGED_STATE_ACTION 15
				 */
				else if (
					applicatonType == ScoolConstants.PARTICIPANTS_INFO_APP) {
					/* if new participant has joined the session add the participant
					 * to ParticipantsInfo table.
					 */
					if (toolType
						== ScoolConstants.NEW_PARTICIPANT_JOINED_ACTION) {
						participantsInfo.addNewParticipant(toolString);
					}
					/*
					 * if the participant has logged out, update ParticipantsInfo table
					 * with with appropriate message.
					 */
					else if (
						toolType
							== ScoolConstants.PARTICIPANT_LOGGEDOUT_ACTION) {
						participantsInfo.logOffParticipant(toolString);
					}
					/*
					 * This message is sent by the TCP Server when the participant tries
					 * to login again while currently being logged on.
					 */
					else if (
						toolType
							== ScoolConstants.PARTICIPANT_DISCONNECT_ACTION) {
						frame.setVisible(false);
						//eCommunicatorClient.disconnect();
						System.out.println(
							"\nYou have been disconnected because you have logged on to another machine or device.");
						/*JOptionPane.showMessageDialog(
							frame,
							"You have been disconnected because you have logged on to another machine or device.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
						*/
						System.exit(0);
					}
					/*
					 * if any participant or moderator changes the state of the Permissions
					 * update ParticipantsInfoTable with new Permission status.
					 */
					else if (
						toolType
							== ScoolConstants.PARTICIPANT_STATE_CHANGED_ACTION
							|| toolType
								== ScoolConstants
									.MODERATOR_CHANGED_STATE_ACTION) {

						participantsInfo.changeState(toolString);
					}
				} else if (applicatonType == ScoolConstants.SERVER_APP) {
					if (toolType
						== ScoolConstants.END_OF_OFFLINE_DATA_ACTION) {
						System.out.println("Received offline data.");
						ApplicationState.getInstance().setReceivedOfflineData(
							true);
						frame.setVisible(true);
						//frame.repaint();			
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
