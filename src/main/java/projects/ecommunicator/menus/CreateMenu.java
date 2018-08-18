package projects.ecommunicator.menus;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import projects.ecommunicator.contentpane.WhiteBoardPanel;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is a sub class of JMenu that creates the CreateMenu    
 * <P> 
 * @see javax.swing.JMenu
 * @see javax.swing.JMenuItem
 * @version 1.0
 */
public class CreateMenu extends JMenu {
	private JMenuItem whiteBoardMenuItem;
	private JMenuItem onlinePresentationMenuItem;
	private JMenuItem screenShotMenuItem;
	private JMenuItem applicationSharingMenuItem;
	private JMenuItem viewDesktopMenuItem;	
	/**
	* Creates a new instance of CreateMenu
	* @param contentPane used to get the WhiteBoardToolBar and actions
	*/
	public CreateMenu(WhiteBoardPanel contentPane) {
		//creates Menu titled Create
		super("Create");
		setFont(ScoolConstants.FONT);
		setMnemonic(KeyEvent.VK_C);
		
		//creates whiteBoard MenuItem and adds it to CreateMenu
		whiteBoardMenuItem = new JMenuItem(Property.getString("school", "CreateMenu.wb_label"));
		whiteBoardMenuItem.setEnabled(false);		
		Action whiteBoardAction = contentPane.getWhiteBoardToolBar().getWhiteBoardAction();				
		addMenuItem(whiteBoardMenuItem,whiteBoardAction,KeyEvent.VK_W,KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		
		//creates online presentation MenuItem and adds it to CreateMenu
		onlinePresentationMenuItem = new JMenuItem(Property.getString("school", "CreateMenu.op_label"));
		onlinePresentationMenuItem.setEnabled(false);
		Action pptAction = contentPane.getWhiteBoardToolBar().getPPtAction();		
		addMenuItem(onlinePresentationMenuItem,pptAction,KeyEvent.VK_P,KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		
		//creates screenshot MenuItem and adds it to CreateMenu
		screenShotMenuItem = new JMenuItem(Property.getString("school", "CreateMenu.ss_label"));		
		Action screenShotAction = contentPane.getWhiteBoardToolBar().getScreenShotAction();		
		addMenuItem(screenShotMenuItem,screenShotAction,KeyEvent.VK_S,KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		//creates application sharing MenuItem and adds it to CreateMenu
		applicationSharingMenuItem = new JMenuItem(Property.getString("school", "CreateMenu.as_label"));	
		applicationSharingMenuItem.setEnabled(false);		
		addMenuItem(applicationSharingMenuItem,null,KeyEvent.VK_A,null);

		//creates view desktop MenuItem and adds it to CreateMenu
		viewDesktopMenuItem = new JMenuItem(Property.getString("school", "CreateMenu.vd_label"));
		viewDesktopMenuItem.setEnabled(false);		
		addMenuItem(viewDesktopMenuItem,null,KeyEvent.VK_D,null);		
	}
	
	/**
	* method that adds the menuitems to the CreateMenu
	* @param menuItem that has to be added Menu
	* @param action to be registered with the menu Item 
	* @param mnemonic that enables ALT+Mnemonic functionality
	* @param stroke that enables CTRL+Mnemonics functionality 
	*/	
	public void addMenuItem(JMenuItem menuItem,Action action,int mnemonic,KeyStroke stroke){		
		menuItem.setFont(ScoolConstants.FONT);
		menuItem.setMnemonic(mnemonic);	
		menuItem.setAccelerator(stroke);	
		menuItem.addActionListener(action);	
		add(menuItem);	
	}
	/**
	 * method that returns WhiteBoardMenuItem
	 * @return  WhiteBoardMenuItem
	 */
	public JMenuItem  getWhiteBoardMenuItem() {
		return whiteBoardMenuItem;
	}
	/**
	 * method that returns OnlinePresentationMenuItem
	 * @return  OnlinePresentationMenuItem
	 */
	public JMenuItem  getOnlinePresentationMenuItem() {
		return onlinePresentationMenuItem;
	}
	/**
	 * method that returns ScreenShotMenuItem
	 * @return  ScreenShotMenuItem
	 */
	public JMenuItem  getScreenShotMenuItem() {
		return screenShotMenuItem;
	}
	/**
	 * method that returns ApplicationSharingMenuItem
	 * @return  ApplicationSharingMenuItem
	 */
	public JMenuItem  getApplicationSharingMenuItem() {
		return applicationSharingMenuItem;
	}
	/**
	 * method that returns ViewDesktopMenuItem
	 * @return  ViewDesktopMenuItem
	 */
	public JMenuItem  getViewDesktopMenuItem() {
		return viewDesktopMenuItem;
	}
}