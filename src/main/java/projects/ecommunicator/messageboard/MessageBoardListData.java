/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.messageboard;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import projects.ecommunicator.utility.Property;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MessageBoardListData {
	public static final ImageIcon ICON_MODERATOR = new ImageIcon(MessageBoardListData.class.getClassLoader().getResource(Property.getString("images","MB_Moderator_Gif")));
	public static final ImageIcon ICON_PARTICIPANT = new ImageIcon(MessageBoardListData.class.getClassLoader().getResource(Property.getString("images","MB_Participant_Gif")));

	private Icon icon;
	private int index;
	private boolean selectable;
	private String data;

	public MessageBoardListData(int index,boolean selectable,Object data) {
		this.data = (String) data;
		if(data.equals("moderator")){
			this.icon = ICON_MODERATOR;
		}else{
			this.icon = ICON_PARTICIPANT;
		}
		this.index = index;
		this.selectable = selectable;

	}

	public Icon getIcon() {
		return icon;
	}

	public int getIndex() {
		return index;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public Object getObject() {
		return data;
	}

	public String toString() {
		return data.toString();
	}

}

