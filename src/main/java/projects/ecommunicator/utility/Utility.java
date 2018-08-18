package projects.ecommunicator.utility;

import java.util.ResourceBundle;

public class Utility {

	public static String convertTo16Byte(long arg) {
		if (arg <= 0) {
			return "0000000000000000";
		} else if (arg > 0 && arg <= 9) {
			return "00000000000000" + arg;
		} else if (arg >= 10 && arg <= 99) {
			return "00000000000000" + arg;
		} else if (arg >= 100 && arg <= 999) {
			return "0000000000000" + arg;
		} else if (arg >= 1000 && arg <= 9999) {
			return "000000000000" + arg;
		} else if (arg >= 10000 && arg <= 99999) {
			return "00000000000" + arg;
		} else if (arg >= 100000 && arg <= 999999) {
			return "0000000000" + arg;
		} else if (arg >= 1000000 && arg <= 9999999) {
			return "000000000" + arg;
		} else if (arg >= 10000000 && arg <= 99999999) {
			return "00000000" + arg;
		} else if (arg >= 100000000 && arg <= 999999999) {
			return "0000000" + arg;
		} else if (arg >= 1000000000 && arg <= 9999999999f) {
			return "000000" + arg;
		} else if (arg >= 10000000000f && arg <= 99999999999f) {
			return "00000" + arg;
		} else if (arg >= 100000000000f && arg <= 999999999999f) {
			return "0000" + arg;
		} else if (arg >= 1000000000000f && arg <= 9999999999999f) {
			return "000" + arg;
		} else if (arg >= 10000000000000f && arg <= 99999999999999f) {
			return "00" + arg;
		} else if (arg >= 100000000000000f && arg <= 99999999999999f) {
			return "0" + arg;
		} else if (arg >= 1000000000000000f && arg <= 999999999999999f) {
			return "" + arg;
		}
		return "" + arg;
	}

	public static String convertTo16Byte(String arg) {
		String pad = "";
		for (int i = 0; i < 16 - arg.length(); i++) {
			pad = pad + " ";
		}
		return pad + arg;
	}

	public static String convertTo8Byte(long arg) {

		if (arg <= 0) {
			return "00000000";
		} else if (arg > 0 && arg <= 9) {
			return "0000000" + arg;
		} else if (arg >= 10 && arg <= 99) {
			return "000000" + arg;
		} else if (arg >= 100 && arg <= 999) {
			return "00000" + arg;
		} else if (arg >= 1000 && arg <= 9999) {
			return "0000" + arg;
		} else if (arg >= 10000 && arg <= 99999) {
			return "000" + arg;
		} else if (arg >= 100000 && arg <= 999999) {
			return "00" + arg;
		} else if (arg >= 1000000 && arg <= 9999999) {
			return "0" + arg;
		} else {
			return "" + arg;
		}
	}

	public static String convertTo4Byte(int arg) {

		if (arg <= 0) {
			return "0000";
		} else if (arg > 0 && arg <= 9) {
			return "000" + arg;
		} else if (arg >= 10 && arg <= 99) {
			return "00" + arg;
		} else if (arg >= 100 && arg <= 999) {
			return "0" + arg;
		} else {
			return "" + arg;
		}
	}

	public static String convertTo3Byte(int arg) {

		if (arg <= 0) {
			return "000";
		} else if (arg > 0 && arg <= 9) {
			return "00" + arg;
		} else if (arg >= 10 && arg <= 99) {
			return "0" + arg;
		} else {
			return "" + arg;
		}
	}

	public static String convertTo2Byte(int arg) {
		if (arg <= 0) {
			return "00";
		} else if (arg > 0 && arg <= 9) {
			return "0" + arg;
		} else {
			return "" + arg;
		}
	}

	public static int getPayLoadLength(String headerString) {
		int payLoadLength = 0;
		try {
			payLoadLength = Integer.parseInt(headerString.substring(34, 42));
		} catch (Exception ex) {
		}
		return payLoadLength;
	}

	public static long getTrackingId(String headerString) {
		long trackingId = 0;
		try {
			trackingId = Long.parseLong(headerString.substring(8, 24));
			System.out.println("trackingId:" + trackingId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return trackingId;
	}

	/**
	 * This method creates Tracking Id for the participant.
	 * This id travesl back and forth between the server and participant
	 * @return java.lang.String Tracking Id
	 */
	public static long createTrackingId() {
		return System.currentTimeMillis();
	}

	public static int getApplicatonType(String payLoadString) {
		try {
			return Integer.parseInt(payLoadString.substring(0, 3));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public static int getPageNo(String payLoadString) {
		try {
			return Integer.parseInt(payLoadString.substring(3, 6));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public static String getPageName(String payLoadString) {
		try {
			return payLoadString.substring(6, 22).trim();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getToolString(String payLoadString) {
		try {
			return payLoadString.substring(22, payLoadString.length());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static int getToolType(String toolString) {
		try {
			return Integer.parseInt(toolString.substring(0, 4));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public static String getLoginId(int userId) {
		String loginId = "";
		try {
			loginId =
				ResourceBundle.getBundle("loginids").getString(
					String.valueOf(userId));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return loginId;
	}

	public static String getLoginId(byte[] payLoad) {
		StringBuffer loginId = new StringBuffer(16);
		int c;
		// read the header string
		for (int i = 0; i < 16; i++) {
			c = payLoad[i];
			c = (c >= 0) ? c : 256 + c;
			loginId.append((char) c);
		}
		return loginId.toString().trim();
	}

	public static String getLoginId(String toolString) {
		try {
			return toolString.substring(4, 20).trim();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static int getRole(String toolString) {
		// toolString is related to new participant joined
		try {
			return Integer.parseInt(toolString.substring(20, 22).trim());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public static int getMicOption(String toolString) {
		// toolString is related to new participant joined
		try {
			return Integer.parseInt(toolString.substring(22, 23).trim());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;

	}

	public static int getMessageBoardOption(String toolString) {
		// toolString is related to new participant joined
		try {
			return Integer.parseInt(toolString.substring(23, 24).trim());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;

	}

	public static int getPresentationInfoOption(String toolString) {
		// toolString is related to new participant joined
		try {
			return Integer.parseInt(toolString.substring(24, 25).trim());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;

	}

	public static int getWhiteBoardOption(String toolString) {
		// toolString is related to new participant joined
		try {
			return Integer.parseInt(toolString.substring(25, 26).trim());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;

	}

	public static String getParticipantLoginId(String toolString) {
		// toolString is related to new participant joined
		try {
			return toolString.substring(26, 42).trim();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static String getMessageText(String toolString) {
		try {
			return toolString.substring(20, toolString.length());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static String getNewParticipantJoinedToolString(
		String loginId,
		int role) {
		try {
			String temp =
				convertTo4Byte(ScoolConstants.NEW_PARTICIPANT_JOINED_ACTION)
					+ convertTo16Byte(loginId)
					+ convertTo2Byte(role);

			if (role == Role.MODERATOR) {
				return temp
					+ Permission.ALLOW_MIC
					+ Permission.ALLOW_MESSAGEBOARD
					+ Permission.ALLOW_PRESENTATIONINFO
					+ Permission.ALLOW_WHITEBOARD;
			} else if (role == Role.MEMBER) {
				return temp
					+ Permission.DISALLOW_MIC
					+ Permission.ALLOW_MESSAGEBOARD
					+ Permission.DISALLOW_PRESENTATIONINFO
					+ Permission.DISALLOW_WHITEBOARD;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static String getDeleteParticipantInfoToolString(
		String loginId,
		int role) {
		try {
			String deleteParticipantInfoToolString =
				convertTo4Byte(ScoolConstants.PARTICIPANT_LOGGEDOUT_ACTION)
					+ convertTo16Byte(loginId)
					+ convertTo2Byte(role);
			return deleteParticipantInfoToolString;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static String getDisconnectParticipantToolString(
		String loginId,
		int role) {
		try {
			String disConnectParticipantToolString =
				convertTo4Byte(ScoolConstants.PARTICIPANT_DISCONNECT_ACTION)
					+ convertTo16Byte(loginId)
					+ convertTo2Byte(role);
			return disConnectParticipantToolString;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public static String getEndOfOfflineDataToolString(String loginId) {
		try {
			String endOfOfflineDataToolString =
				convertTo4Byte(ScoolConstants.END_OF_OFFLINE_DATA_ACTION)
					+ convertTo16Byte(loginId);
			return endOfOfflineDataToolString;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
}