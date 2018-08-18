/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.messageboard;

import javax.swing.DefaultComboBoxModel;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MessageBoardComboModel extends DefaultComboBoxModel {

	public MessageBoardComboModel() {
		addElement(new MessageBoardListData(0,false,"moderator"));
		addElement(new MessageBoardListData(0, false, "Dhiraj"));
		addElement(new MessageBoardListData(0, false, "Anil"));

	}
}
