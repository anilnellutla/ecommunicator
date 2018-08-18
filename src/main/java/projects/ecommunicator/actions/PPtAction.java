package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;

import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.panelboard.PresentationInfo;
import projects.ecommunicator.pptconverter.PPtConverter;
import projects.ecommunicator.presentationinfo.PresentationInfoListData;
import projects.ecommunicator.utility.NewPage;

/**
 * This is a sub class of AbstractAction that invokes pptConverter and loads the images on WhiteBoard   
 * <P> 
 * @see javax.swing.AbstractAction
 * @version 1.0
 */
public class PPtAction extends AbstractAction {
	
	//layeredPane used to get the presentationInfo
	private WhiteBoardDesktopPane whiteBoardDesktopPane;
	//container that represents the pages opened
	private PresentationInfo presentationInfo;
	/**
	* Creates a new instance of PPtAction
	* @param whiteBoardDesktopPane object used to get the presentationInfo	
	*/
	public PPtAction(WhiteBoardDesktopPane whiteBoardDesktopPane) {
		this.whiteBoardDesktopPane = whiteBoardDesktopPane;
		this.presentationInfo = whiteBoardDesktopPane.getPresentationInfo();
	}
	/**
	* method that sets the pptPageNo and invokes the PPtConvertor to convert presentation into images
	* conversion process has been invoked in a separate thread	
	* @param evt ActionEvent object that has been captured	
	*/
	public void actionPerformed(ActionEvent e) {
		// page no that represents the power point presentation images
		int pptPageNo = whiteBoardDesktopPane.getWhiteBoard().getCanvas().getPPtPageNo();
		//invokation of the image convertor 
		PPtConverter pptConverter = new PPtConverter(whiteBoardDesktopPane, this, pptPageNo + 1);
		Thread t = new Thread(pptConverter);
		//spawns the ppt convertion thread  
		t.start();
	}
	
	/**
	* method that creates a newPPtPage no for the converted images	
	* @param slidePaths 
	*/
	public void createNewPPtPages(ArrayList slidePaths) {
		if (slidePaths != null) {
			//creates NewPage object for the WhiteBoardCanvas
			NewPage newPage = new NewPage(whiteBoardDesktopPane.getWhiteBoard().getCanvas());
			//creates NewPPtPageNo that refers the power point images
			newPage.createNewPPtPage(slidePaths, presentationInfo);
			//creates PresentationInfoListData to place it in the presentationInfo List
			PresentationInfoListData data = new PresentationInfoListData(0, false, newPage.getKey());
			//and selects the created pptPage
			presentationInfo.getPresentationList().setSelectedValue(data, true);
		}
	}
}