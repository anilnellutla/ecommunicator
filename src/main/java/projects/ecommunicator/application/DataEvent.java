package projects.ecommunicator.application;

import projects.ecommunicator.net.client.SendDataPool;
import projects.ecommunicator.utility.FormatData;
import projects.ecommunicator.net.client.Sender;

/** 
 * This class is used to put the data captured from AWTEvent Thread in
 * the network and send it to TCP Server.
 * <p>
 * @author  Anil K Nellutla
 * @see com.adaequare.ecommunicator.util.FormatData
 * @see Sender
 * @version 1.0
 */
public class DataEvent {

	// get the singleton instance of SendDataPool object.
	// this is the object on which Sender is waiting to be notified.
	private static SendDataPool pool = SendDataPool.getInstance();

	/**
	 * This method formats the input data based on application protocol
	 * and notifies Sender to send data to TCP Server.
	 * @param applicationType The type of application: 
	 * for example:WHITE_BOARD_APP, PPT_PRESENTATION_APP, MESSAGE_BOARD_APP, PARTICIPANTS_INFO_APP
	 * @param pageNo the current page number in WhiteBoardCanvas
	 * @param currentPageName the current page name in WhiteBoardCanvas
	 * @param toolString tool string based on application protocol
	 */
	public static void notifySender(
		int applicationType,
		int pageNo,
		String currentPageName,
		String toolString) {

		if (ApplicationState.getInstance().isReceivedOfflineData()) {

			/*
			System.out.println(
				applicationType
					+ " "
					+ pageNo
					+ " "
					+ currentPageName
					+ " "
					+ toolString);
			*/

			// format the input data based on application protocol.
			byte[] payLoad =
				FormatData.formatSendData(
					applicationType,
					pageNo,
					currentPageName,
					toolString);

			// notify Sender Thread to put the data in network
			synchronized (pool) {
				pool.add(pool.size(), payLoad);
				pool.notifyAll();
			}
		}
	}

}
