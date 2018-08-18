package projects.ecommunicator.toolbar;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

import projects.ecommunicator.actions.PPtAction;
//import PollAction;
import projects.ecommunicator.actions.GraphAction;
import projects.ecommunicator.actions.PollAction;
import projects.ecommunicator.actions.ScreenShotAction;
import projects.ecommunicator.actions.WhiteBoardAction;
import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is a sub class of JToolBar that has been placed as a container for Accessories Tools   
 * <P> 
 * @see javax.swing.JToolBar
 * @version 1.0
 */
public class WhiteBoardToolBar extends JToolBar{
	//actions to be registered with the tool bar buttons
	private Action whiteBoardAction;
	private Action pptAction;
	private Action screenShotAction;
	private Action navigationAction;
	private Action pollAction;
	private Action graphAction;
	
	//buttons to be added with the tool bar
	private JButton previousButton;
	private JButton nextButton;
	private JButton whiteBoardButton;
	private JButton onlinePresentationButton;
	private JButton screenShotButton;
	private JButton appSharingButton;
	private JButton viewDesktopButton;
	private JButton pollButton;
	private JButton graphButton;


	public WhiteBoardToolBar(WhiteBoardDesktopPane whiteBoardDesktopPane, WhiteBoardFrame whiteBoardFrame){

		setFloatable(false);
		setBorder(ScoolConstants.ETCHED_BORDER);
		
		//previous button that registers navigation action 
		previousButton =  new JButton(Property.getString("school","ToolBar.previous_Label"));
		previousButton.setMargin(ScoolConstants.INSETS);
		previousButton.setFont(ScoolConstants.FONT);
		previousButton.setToolTipText(Property.getString("school","ToolBar.previous_ToolTip"));
		navigationAction = whiteBoardDesktopPane.getPresentationInfo().getNavigationAction();
		previousButton.addActionListener(navigationAction);
		add(previousButton);
		
		//next button that registers navigation action
		nextButton =  new JButton(Property.getString("school","ToolBar.next_Label"));
		nextButton.setMargin(ScoolConstants.INSETS);
		nextButton.setFont(ScoolConstants.FONT);
		nextButton.setToolTipText(Property.getString("school","ToolBar.next_ToolTip"));
		navigationAction = whiteBoardDesktopPane.getPresentationInfo().getNavigationAction();
		nextButton.addActionListener(navigationAction );
		add(nextButton);
		
		//adds separator
		addSeparator();
		
		//button that registers the whiteboard action
		whiteBoardButton =  new JButton(Property.getString("school","ToolBar.wb_Label"));
		whiteBoardButton.setEnabled(false);
		whiteBoardButton.setMargin(ScoolConstants.INSETS);
		whiteBoardButton.setFont(ScoolConstants.FONT);
		whiteBoardButton.setToolTipText(Property.getString("school","ToolBar.wb_ToolTip"));
		whiteBoardAction = new WhiteBoardAction(whiteBoardDesktopPane);
		whiteBoardButton.addActionListener(whiteBoardAction);
		add(whiteBoardButton);
		
		//button that registers online presentation action 
		onlinePresentationButton =  new JButton(Property.getString("school","ToolBar.olp_Label"));
		onlinePresentationButton.setEnabled(false);
		onlinePresentationButton.setMargin(ScoolConstants.INSETS);
		onlinePresentationButton.setFont(ScoolConstants.FONT);
		onlinePresentationButton.setToolTipText(Property.getString("school","ToolBar.olp_ToolTip"));
		pptAction = new PPtAction(whiteBoardDesktopPane);
		onlinePresentationButton.addActionListener(pptAction);
		add(onlinePresentationButton);

		//button that registers ApplicationSharing Action
		appSharingButton =  new JButton(Property.getString("school","ToolBar.as_Label"));
		appSharingButton.setEnabled(false);
		appSharingButton.setMargin(ScoolConstants.INSETS);
		appSharingButton.setFont(ScoolConstants.FONT);
		appSharingButton.setToolTipText(Property.getString("school","ToolBar.as_ToolTip"));		
		add(appSharingButton);
		
		//button that registers view desktop action
		viewDesktopButton =  new JButton(Property.getString("school","ToolBar.vd_Label"));
		viewDesktopButton.setEnabled(false);
		viewDesktopButton.setMargin(ScoolConstants.INSETS);
		viewDesktopButton.setFont(ScoolConstants.FONT);
		viewDesktopButton.setToolTipText(Property.getString("school","ToolBar.vd_ToolTip"));		
		add(viewDesktopButton);
		
		//button that registers poll action
		pollButton = new JButton(Property.getString("school","ToolBar.poll_Label"));
		//pollButton.setEnabled(false);
		pollButton.setMargin(ScoolConstants.INSETS);
		pollButton.setFont(ScoolConstants.FONT);
		pollButton.setToolTipText(Property.getString("school","ToolBar.poll_ToolTip"));
		pollAction = new PollAction(whiteBoardFrame);
		pollButton.addActionListener(pollAction);
		add(pollButton);
		
		//button that registers screen shot action
		screenShotButton =  new JButton(Property.getString("school","ToolBar.ss_Label"));
		//screenShotButton.setEnabled(false);
		screenShotButton.setMargin(ScoolConstants.INSETS);
		screenShotButton.setFont(ScoolConstants.FONT);
		screenShotButton.setToolTipText(Property.getString("school","ToolBar.ss_ToolTip"));
		screenShotAction = new ScreenShotAction(whiteBoardFrame);
		screenShotButton.addActionListener( screenShotAction );
		add(screenShotButton);
		
		//button that registers screen shot action
		graphButton =  new JButton(Property.getString("school","ToolBar.graph_Label"));
		//graphButton.setEnabled(false);
		graphButton.setMargin(ScoolConstants.INSETS);
		graphButton.setFont(ScoolConstants.FONT);
		graphButton.setToolTipText(Property.getString("school","ToolBar.graph_ToolTip"));
		graphAction = new GraphAction(whiteBoardFrame);
		graphButton.addActionListener( graphAction );
		add(graphButton);
	}
	
	/**
	* method returns the WhiteBoardAction
	* @return Action that refers the WhiteBoardAction	
	*/
	public Action getWhiteBoardAction() {
		return whiteBoardAction;
	}

	/**
	* method returns the PPtAction
	* @return Action that refers the PPtAction	
	*/
	public Action getPPtAction(){
		return pptAction;
	}

	/**
	* method returns the ScreenShotAction
	* @return Action that refers the ScreenShotAction	
	*/
	public Action getScreenShotAction(){
			return screenShotAction;
	}

	/**
	* method returns the NavigationAction
	* @return Action that refers the NavigationAction	
	*/
	public Action getNavigationAction(){
		return navigationAction;
	}

	/**
	* method returns the PreviouButton
	* @return JButton that refers the PreviouButton	
	*/
	public JButton getPreviouButton() {
		return previousButton;
	}

	/**
	* method returns the NextButton
	* @return JButton that refers the NextButton	
	*/
	public JButton getNextButton() {
		return nextButton;
	}

	/**
	* method returns the WhiteBoardButton
	* @return JButton that refers the WhiteBoardButton	
	*/
	public JButton getWhiteBoardButton() {
		return whiteBoardButton;
	}

	/**
	* method returns the OnlinePresentationButton
	* @return JButton that refers the OnlinePresentationButton	
	*/
	public JButton getOnlinePresentationButton() {
		return onlinePresentationButton;
	}

	/**
	* method returns the ScreenShotButton
	* @return JButton that refers the ScreenShotButton	
	*/
	public JButton getScreenShotButton() {
		return screenShotButton;
	}

	/**
	* method returns the AppSharingButton
	* @return JButton that refers the AppSharingButton	
	*/
	public JButton getAppSharingButton() {
		return appSharingButton;
	}

	/**
	* method returns the ViewDesktopButton
	* @return JButton that refers the ViewDesktopButton	
	*/
	public JButton getViewDesktopButton() {
		return viewDesktopButton;
	}
}