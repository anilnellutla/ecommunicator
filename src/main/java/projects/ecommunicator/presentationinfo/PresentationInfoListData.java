/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.presentationinfo;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import projects.ecommunicator.utility.Property;

/**
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PresentationInfoListData {
	private Icon icon;
	private int index;
	private boolean selectable;
	private String key;
	private ImageIcon ICON_WHITE_BOARD = new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","Pre_WhiteBoard_Gif")));
	private ImageIcon ICON_SLIDE = new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","Pre_Presentation_Gif")));
	private ImageIcon ICON_POLL = new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","Pre_Poll_Gif")));

	public PresentationInfoListData(int index, boolean selectable, Object data) {
		key = data.toString();
		if(key.startsWith("White")){
			icon = ICON_WHITE_BOARD;
		}else if(key.startsWith("Slide ")){
			icon = ICON_SLIDE;
		}else if(key.startsWith("Poll")){
			icon = ICON_POLL;
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

	public String toString(){
		return key;
	}

	public boolean equals(Object obj) {
		if(key.equals(obj.toString())) {
			return true;
		}else {
			return false;
		}		
	}
}
