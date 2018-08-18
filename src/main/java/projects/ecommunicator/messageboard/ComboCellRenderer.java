/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.messageboard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ComboCellRenderer extends JLabel implements ListCellRenderer {

	public static final int OFFSET = 16;
	protected Color textSelectionColor = Color.white;
	protected Color textNonSelectionColor = Color.black;
	protected Color textNonselectableColor = Color.gray;
	protected Color bkSelectionColor = new Color(0, 0, 128);
	protected Color bkNonSelectionColor = Color.white;
	protected Color borderSelectionColor = Color.yellow;
	protected Color textColor;
	protected Color bkColor;
	protected boolean hasFocus;
	protected Border[] borders;

	public ComboCellRenderer() {
		super();
		textColor = textNonSelectionColor;
		bkColor = bkNonSelectionColor;
		borders = new Border[20];
		for (int k = 0; k < borders.length; k++)
			borders[k] = new EmptyBorder(0, OFFSET * k, 0, 0);
		setOpaque(false);
	}
	public Component getListCellRendererComponent(
		JList list,Object obj,int row,boolean sel,boolean hasFocus) {
			if (obj == null)
				return this;
			setText(obj.toString());
			boolean selectable = true;
			if (obj instanceof MessageBoardListData) {
				MessageBoardListData ldata = (MessageBoardListData) obj;
				selectable = ldata.isSelectable();
				setIcon(ldata.getIcon());
				int index = 0;
				if (row >= 0) // no offset for editor (row=-1)
					index = ldata.getIndex();
				Border b = (index < borders.length ? borders[index] : new EmptyBorder(0, OFFSET * index, 0, 0));
				setBorder(b);
			} else
				setIcon(null);
				setFont(list.getFont());
				textColor = (sel ? textSelectionColor : (selectable ? textNonSelectionColor : textNonselectableColor));
				bkColor = (sel ? bkSelectionColor : bkNonSelectionColor);
				hasFocus = true;
				return this;
	}
	public void paint(Graphics g) {
		Icon icon = getIcon();		
		g.setColor(bkNonSelectionColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(bkColor);
		int offset = 0;
		if (icon != null && getText() != null) {
			Insets ins = getInsets();
			offset = ins.left + icon.getIconWidth() + getIconTextGap();
		}
		g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
		if (hasFocus) {
			g.setColor(borderSelectionColor);
			g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
		}
		setForeground(textColor);
		setBackground(bkColor);
		super.paint(g);

	}
}