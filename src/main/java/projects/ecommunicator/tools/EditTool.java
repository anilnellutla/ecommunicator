/*
 * Created on Apr 29, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.tools;

import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EditTool extends Edit {

	public static String getCutActionToolString(int selectedToolPos) {
		String toolString =
			Utility.convertTo4Byte(ScoolConstants.EDIT_TOOL)
				+ Utility.convertTo4Byte(ScoolConstants.CUT_ACTION)
				+ Utility.convertTo8Byte(selectedToolPos);
		return toolString;
	}

	public static String getPasteActionToolString(
		int selectedToolPos,
		String translatedToolString) {
		String toolString =
			Utility.convertTo4Byte(ScoolConstants.EDIT_TOOL)
				+ Utility.convertTo4Byte(ScoolConstants.PASTE_ACTION)
				+ Utility.convertTo8Byte(selectedToolPos)
				+ translatedToolString;
		return toolString;
	}

}
