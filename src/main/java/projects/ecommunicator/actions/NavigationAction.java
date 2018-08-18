package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;
import javax.swing.ListModel;

import projects.ecommunicator.panelboard.PresentationInfo;
import projects.ecommunicator.presentationinfo.PresentationInfoListData;
import projects.ecommunicator.utility.Property;

/**
 * This is a sub class of AbstractAction that enables easy navigation of pages   
 * <P> 
 * @see javax.swing.AbstractAction
 * @version 1.0
 */
public class NavigationAction extends AbstractAction {

	// PresentationInfo
	private PresentationInfo presentationInfo;

	//PresentationInfo list 
	private JList presentationList;

	/**
	* Creates a new instance of NavigationAction
	* @param presentationList object used to get the listModel	
	*/
	public NavigationAction(PresentationInfo presentationInfo) {
		this.presentationInfo = presentationInfo;
		presentationList = presentationInfo.getPresentationList();
	}
	/**
	* method that facilitates the user to navigate the pages by pressing
	* Previous & Next Buttons on presentationInfo
	* @param evt ActionEvent object that has been captured	
	*/
	public void actionPerformed(ActionEvent evt) {
		//actionCommand identifies the button that has been clicked
		String actionCommand = evt.getActionCommand();

		//selectedIndex represents the current selected index
		int selectedIndex = presentationList.getSelectedIndex();
		//happens when there is only one item in the list
		if (selectedIndex == -1) {
			presentationList.setSelectedIndex(0);
		}
		//represents the model of the list
		ListModel listModel = presentationList.getModel();

		/**
		* loop checks which button has been clicked 
		* if it is previousButton gets the selectedIndex and selects the value of selectedIndex-1
		* else if it is a nextButton gets the selectedIndex and selects the value of selectedIndex+1			
		*/
		if (actionCommand
			.equals(Property.getString("school", "ToolBar.previous_Label"))) {
			int previousIndex = selectedIndex - 1;
			if (previousIndex >= 0) {
				//Object that has been placed in the list
				PresentationInfoListData selectedValue =
					(PresentationInfoListData) listModel.getElementAt(
						previousIndex);
				presentationList.setSelectedValue(selectedValue, true);
			}

		} else if (
			actionCommand.equals(
				Property.getString("school", "ToolBar.next_Label"))) {

			int size = listModel.getSize();
			int nextIndex = selectedIndex + 1;
			if (nextIndex < size) {
				//Object that has been placed in the list
				PresentationInfoListData selectedValue =
					(PresentationInfoListData) listModel.getElementAt(
						nextIndex);
				presentationList.setSelectedValue(selectedValue, true);
			}
		} else if (
			actionCommand.equals(Property.getString("school", "sync"))) {
			presentationInfo.setSynWithSession(true);
			PresentationInfoListData data =
				new PresentationInfoListData(
					0,
					false,
					presentationInfo.getCurrentSessionPage());

			presentationInfo.setSendValueChangedEvent(false);
			presentationInfo.getPresentationList().setSelectedValue(data, true);

		}
	}
}