/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 *                            Header
 *                              ^
 *                              |
 * |----------------------------------------------------------------
 * |                                                                |
 * SessionId |TrackingId |UserId |Role |PayloadLength |Dummy1 |Dummy2
 *     8          16         8      2        8            8        8
 *
 *                     Payload
 *                       ^
 *                       |
 * |--------------------------------------------|
 * |                                            |
 * ApplicationType| PageNo| PageName| ToolString
 *         3          3        16        any #of bytes
 *
 */
public class FormatData {

	public static String formatReceivedData(byte[] payLoad) {
		int c;
		StringBuffer command = new StringBuffer();
		for (int i = 0; i < 26; i++) {
			c = payLoad[i];
			c = (c >= 0) ? c : 256 + c;
			command.append((char) c);
		}

		int toolType = Integer.parseInt(command.toString().substring(22, 26));

		if (toolType == ScoolConstants.SELECT_TOOL) {
			StringBuffer selectedCommand = new StringBuffer(command.toString());
			for (int i = 26; i < 38; i++) {
				c = payLoad[i];
				c = (c >= 0) ? c : 256 + c;
				selectedCommand.append((char) c);
			}

			int shiftedToolType =
				Integer.parseInt(selectedCommand.toString().substring(34, 38));
			if (shiftedToolType == ScoolConstants.PPT_IMAGE_TOOL
				|| shiftedToolType == ScoolConstants.SCREEN_SHOT_TOOL
				|| shiftedToolType == ScoolConstants.SNAP_GRAPH_SHOT_TOOL) {

				for (int i = 38; i < 54; i++) {
					c = payLoad[i];
					c = (c >= 0) ? c : 256 + c;
					selectedCommand.append((char) c);
				}
				selectedCommand.append(getFilePath(payLoad, 54));
				String selectedCommandString = selectedCommand.toString();
				return selectedCommandString;
			}
		}

		if (toolType == ScoolConstants.PPT_IMAGE_TOOL
			|| toolType == ScoolConstants.SCREEN_SHOT_TOOL
			|| toolType == ScoolConstants.SNAP_GRAPH_SHOT_TOOL) {

			for (int i = 26; i < 42; i++) {
				c = payLoad[i];
				c = (c >= 0) ? c : 256 + c;
				command.append((char) c);
			}
			command.append(getFilePath(payLoad, 42));
			String commandString = command.toString();
			return commandString;

		} else if (
			toolType == ScoolConstants.SELECT_TOOL
				|| toolType == ScoolConstants.CIRCLE_TOOL
				|| toolType == ScoolConstants.RECT_TOOL
				|| toolType == ScoolConstants.ROUND_RECT_TOOL
				|| toolType == ScoolConstants.LINE_TOOL
				|| toolType == ScoolConstants.FREE_HAND_TOOL
				|| toolType == ScoolConstants.TEXT_TOOL
				|| toolType == ScoolConstants.NEW_WHITEBOARD_ACTION
				|| toolType == ScoolConstants.NAVIGATION_ACTION
				|| toolType == ScoolConstants.MESSAGEBOARD_ACTION
				|| toolType == ScoolConstants.NEW_PARTICIPANT_JOINED_ACTION
				|| toolType == ScoolConstants.PARTICIPANT_LOGGEDOUT_ACTION
				|| toolType == ScoolConstants.PARTICIPANT_DISCONNECT_ACTION
				|| toolType == ScoolConstants.PARTICIPANT_STATE_CHANGED_ACTION
				|| toolType == ScoolConstants.MODERATOR_CHANGED_STATE_ACTION
				|| toolType == ScoolConstants.END_OF_OFFLINE_DATA_ACTION
				|| toolType == ScoolConstants.EDIT_TOOL
				|| toolType == ScoolConstants.POLL_TOOL) {

			for (int i = 26; i < payLoad.length; i++) {
				c = payLoad[i];
				c = (c >= 0) ? c : 256 + c;
				command.append((char) c);
			}
			String commandString = command.toString();
			return commandString;

		} else {

		}

		return null;
	}

	public static byte[] formatSendData(
		int applicationType,
		int pageNo,
		String currentPageName,
		String commandString) {

		int toolType = Integer.parseInt(commandString.substring(0, 4));
		byte[] payLoad;

		if (toolType == ScoolConstants.SELECT_TOOL) {
			int shiftedToolType =
				Integer.parseInt(commandString.substring(12, 16));
			if (shiftedToolType == ScoolConstants.PPT_IMAGE_TOOL
				|| shiftedToolType == ScoolConstants.SCREEN_SHOT_TOOL
				|| shiftedToolType == ScoolConstants.SNAP_GRAPH_SHOT_TOOL) {

				String imagePath =
					commandString.substring(32, commandString.length());
				byte[] imageData = getFileData(imagePath);
				//System.out.println("image data length:" + imageData.length);

				byte[] commandByteArray =
					(Utility.convertTo3Byte(applicationType)
						+ Utility.convertTo3Byte(pageNo)
						+ Utility.convertTo16Byte(currentPageName)
						+ commandString.substring(0, 32))
						.getBytes();
				//System.out.println("command length:" + commandByteArray.length);

				payLoad = new byte[commandByteArray.length + imageData.length];

				System.arraycopy(
					commandByteArray,
					0,
					payLoad,
					0,
					commandByteArray.length);

				System.arraycopy(
					imageData,
					0,
					payLoad,
					commandByteArray.length,
					imageData.length);

				return payLoad;
			}
		}

		if (toolType == ScoolConstants.PPT_IMAGE_TOOL
			|| toolType == ScoolConstants.SCREEN_SHOT_TOOL
			|| toolType == ScoolConstants.SNAP_GRAPH_SHOT_TOOL) {

			String imagePath =
				commandString.substring(20, commandString.length());
			byte[] imageData = getFileData(imagePath);
			//System.out.println("image data length:" + imageData.length);

			byte[] commandByteArray =
				(Utility.convertTo3Byte(applicationType)
					+ Utility.convertTo3Byte(pageNo)
					+ Utility.convertTo16Byte(currentPageName)
					+ commandString.substring(0, 20))
					.getBytes();
			//System.out.println("command length:" + commandByteArray.length);

			payLoad = new byte[commandByteArray.length + imageData.length];

			System.arraycopy(
				commandByteArray,
				0,
				payLoad,
				0,
				commandByteArray.length);

			System.arraycopy(
				imageData,
				0,
				payLoad,
				commandByteArray.length,
				imageData.length);

			return payLoad;

		} else if (
			toolType == ScoolConstants.SELECT_TOOL
				|| toolType == ScoolConstants.CIRCLE_TOOL
				|| toolType == ScoolConstants.RECT_TOOL
				|| toolType == ScoolConstants.ROUND_RECT_TOOL
				|| toolType == ScoolConstants.LINE_TOOL
				|| toolType == ScoolConstants.FREE_HAND_TOOL
				|| toolType == ScoolConstants.TEXT_TOOL
				|| toolType == ScoolConstants.NEW_WHITEBOARD_ACTION
				|| toolType == ScoolConstants.NAVIGATION_ACTION
				|| toolType == ScoolConstants.MESSAGEBOARD_ACTION
				|| toolType == ScoolConstants.NEW_PARTICIPANT_JOINED_ACTION
				|| toolType == ScoolConstants.PARTICIPANT_LOGGEDOUT_ACTION
				|| toolType == ScoolConstants.PARTICIPANT_DISCONNECT_ACTION
				|| toolType == ScoolConstants.PARTICIPANT_STATE_CHANGED_ACTION
				|| toolType == ScoolConstants.MODERATOR_CHANGED_STATE_ACTION
				|| toolType == ScoolConstants.EDIT_TOOL
				|| toolType == ScoolConstants.POLL_TOOL) {

			payLoad =
				(Utility.convertTo3Byte(applicationType)
					+ Utility.convertTo3Byte(pageNo)
					+ Utility.convertTo16Byte(currentPageName)
					+ commandString)
					.getBytes();

			return payLoad;
		} else {
			payLoad = "555555INVALIDPAGE5555INVALIDCOMMAND".getBytes();
			return payLoad;
		}
		//System.out.println("\npayload length:" + payLoad.length);

	}

	private static byte[] getFileData(String path) {
		try {

			File file = new File(path);
			if (file.canRead()) {
				DataInputStream dis =
					new DataInputStream(
						new BufferedInputStream(new FileInputStream(file)));

				byte[] fileData = new byte[(int) file.length()];
				dis.readFully(fileData);
				return fileData;
			}
		} catch (FileNotFoundException ex) {

		} catch (IOException ex) {
		}
		return null;
	}

	private static String getFilePath(byte[] payLoad, int readIndex) {

		DataOutputStream dos = null;
		try {

			File tempFile = File.createTempFile("ScreenShot", ".png", null);
			tempFile.deleteOnExit();

			dos =
				new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(tempFile)));

			for (int i = readIndex; i < payLoad.length; i++) {
				//System.out.println(i);
				dos.writeByte(payLoad[i]);
			}
			dos.flush();
			dos.close();

			String imagePath = tempFile.getCanonicalPath();
			return imagePath;

		} catch (FileNotFoundException ex) {

		} catch (IOException ex) {
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException ex) {
				}

			}
		}

		return null;
	}

	public static void main(String[] args) {

		//String commandString ="1 1 WhiteBoard 1 000700210030C:\\Documents and Settings\\anil\\Local Settings\\Temp\\ScreenShot2106.png";

		//String commandString = "000100470136040102570000000002.0";
		//System.out.println(commandString);
		byte[] payLoad =
			FormatData.formatSendData(
				1,
				1,
				"WhiteBoard 1",
				"000700210030C:\\Documents and Settings\\anil\\Local Settings\\Temp\\ScreenShot2106.png");
		//System.out.println("payload length:"+payLoad.length);

		System.out.println(formatReceivedData(payLoad));

	}

}
