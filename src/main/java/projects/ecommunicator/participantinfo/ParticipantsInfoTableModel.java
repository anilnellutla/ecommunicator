/*
 * Created on Jan 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.participantinfo;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ParticipantsInfoTableModel extends DefaultTableModel {

	private Vector particpants = null;

	private static final int DEFAULT_NUM_ROWS = 8;
	private static final int DEFAULT_NUM_COLOUMNS = 5;

	public ParticipantsInfoTableModel() {
		particpants = new Vector();
		for (int i = 0; i < DEFAULT_NUM_ROWS; i++) {
			Vector participant = new Vector();
			ParticipantsInfoUserData data =
				new ParticipantsInfoUserData(-1, "", -1, -1);
			participant.add(0, data);
			for (int j = 1; j < DEFAULT_NUM_COLOUMNS; j++) {
				participant.add(j, new Object());
			}
			particpants.add(participant);
		}
	}

	public int getColumnCount() {
		return DEFAULT_NUM_COLOUMNS;
	}

	public Class getColumnClass(int col) {

		switch (col) {

			case 0 :
				return ParticipantsInfoUserData.class;

			default :
				return ImageIcon.class;
		}
	}

	public int getRowCount() {
		if (particpants == null) {
			return DEFAULT_NUM_ROWS;
		}
		return particpants.size();
	}

	public Object getValueAt(int row, int col) {
		if (particpants == null) {
			return null;
		}
		try {
			return ((Vector) particpants.get(row)).get(col);
		} catch (ArrayIndexOutOfBoundsException ex) {
		}
		return null;
	}

	public void setValueAt(Object obj, int row, int col) {
		if (particpants != null) {
			Vector participant = null;
			try {
				participant = (Vector) particpants.get(row);
			} catch (ArrayIndexOutOfBoundsException ex) {
			}

			if (participant != null) {
				participant.remove(col);
				participant.add(col, obj);
			} else {
				participant = new Vector();
				participant.add(col, obj);
				particpants.add(row, participant);
			}
		} else {
			particpants = new Vector();

			Vector participant = new Vector();
			participant.add(col, obj);

			particpants.add(row, participant);
		}
		fireTableDataChanged();
		fireTableCellUpdated(row, col);
	}

	public int getParticipantRow(String loginId) {
		int i;
		boolean foundRow = false;
		for (i = 0; i < particpants.size() && !foundRow; i++) {
			ParticipantsInfoUserData data =
				(ParticipantsInfoUserData) getValueAt(i, 0);
			if (data.getLoginId().equals(loginId)) {
				foundRow = true;
				break;
			}
		}
		if (foundRow) {
			return i;
		} else {
			return -1;
		}
	}

	public int getRowAccessTo(int col, String permission) {
		int i;
		boolean foundRow = false;
		for (i = 0; i < particpants.size() && !foundRow; i++) {
			//if (!getValueAt(i, 0).toString().endsWith("logged off")) {

			ImageIcon imageIcon = null;
			try {
				imageIcon = (ImageIcon) getValueAt(i, col);
				if (imageIcon.getDescription().equals(permission)) {
					foundRow = true;
					break;
				}
			} catch (ClassCastException ex) {
			}
			//}
		}
		if (foundRow) {
			return i;
		} else {
			return -1;
		}
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

}
