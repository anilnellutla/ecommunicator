package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;

import projects.ecommunicator.internalframes.WhiteBoard;

public class AccessoriesAction extends AbstractAction {
	
	private WhiteBoard whiteBoard;
	
	public AccessoriesAction(WhiteBoard whiteBoard ){
		this.whiteBoard =whiteBoard; 
		
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals("bold")) {
			System.out.println("Bold Button Clicked");
		} else if (evt.getActionCommand().equals("italics")) {
			System.out.println("Bold Button Clicked");
		} else if (evt.getActionCommand().equals("names")) {
			JComboBox nameCombo = (JComboBox) evt.getSource();
			String selectedName = nameCombo.getSelectedItem().toString();
			System.out.println("Selected Item is : " + selectedName);
		} else if (evt.getActionCommand().equals("size")) {
			JComboBox nameCombo = (JComboBox) evt.getSource();
			String selectedName = nameCombo.getSelectedItem().toString();
			System.out.println("Selected Item is : " + selectedName);			
		}
	}
}
