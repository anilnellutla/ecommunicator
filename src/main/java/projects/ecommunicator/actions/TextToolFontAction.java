package projects.ecommunicator.actions;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;

import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

public class TextToolFontAction extends AbstractAction {

	private WhiteBoardCanvas canvas;

	public TextToolFontAction(WhiteBoardCanvas canvas) {
		this.canvas = canvas;
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("plain")) {
			canvas.setFontStyle(Font.PLAIN);
		} else if (evt.getActionCommand().equals("bold")) {
			canvas.setFontStyle(Font.BOLD);
		} else if (evt.getActionCommand().equals("italic")) {
			canvas.setFontStyle(Font.ITALIC);
		} else if (evt.getActionCommand().equals("names")) {
			JComboBox nameCombo = (JComboBox) evt.getSource();
			int selectedIndex = nameCombo.getSelectedIndex();
			System.out.println("font id:" + selectedIndex);
			canvas.setFontNameId(Utility.convertTo2Byte(selectedIndex));
		} else if (evt.getActionCommand().equals("size")) {
			JComboBox nameCombo = (JComboBox) evt.getSource();
			String selectedName = nameCombo.getSelectedItem().toString();
			System.out.println("font size:" + selectedName);
			canvas.setFontSize(Integer.parseInt(selectedName));
		}
	}
}
