package projects.ecommunicator.menus;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import projects.ecommunicator.actions.LayoutAction;
import projects.ecommunicator.contentpane.WhiteBoardPanel;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;
/**
 * This is a sub class of JMenu that creates the LookAndFeelMenu    
 * <P> 
 * @see javax.swing.JMenu
 * @see javax.swing.JMenuItem
 * @version 1.0
 */
public class WindowMenu extends JMenu{
	private JMenuItem menuItem;	

	public WindowMenu ( WhiteBoardPanel whiteBoardPanel ) {
		//creates menu titled Window		
		super("Window");
		setFont(ScoolConstants.FONT);
		setMnemonic(KeyEvent.VK_W);
		
		//creates menuitem for previous operation and registers navigation action
		menuItem = new JMenuItem(Property.getString("school","ViewMenu.previous_label"));
		Action navigationAction = whiteBoardPanel.getWhiteBoardToolBar().getNavigationAction();
		addMenuItem(menuItem,navigationAction,KeyEvent.VK_P,KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0));		

		//creates menuitem for next operation and registers navigation action
		menuItem = new JMenuItem(Property.getString("school","ViewMenu.next_label"));
		addMenuItem(menuItem,navigationAction,KeyEvent.VK_N,KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0));
		
		addSeparator();

		//creates menuitem for default Layout operation and registers layoutAction action
		menuItem = new JMenuItem(Property.getString("school","ViewMenu.default_label"));
		Action layoutAction = new LayoutAction(whiteBoardPanel.getWhiteBoardDesktopPane());
		addMenuItem(menuItem,layoutAction,KeyEvent.VK_D,null);

		//creates menuitem for Automatic Cycling operation and registers cycle action
		menuItem = new JMenuItem(Property.getString("school","ViewMenu.cycling_label"));
		addMenuItem(menuItem,null,KeyEvent.VK_U,null);
	}
	 /**
	  * method that adds the menuitems to the WindowMenu
	  * @param menuItem that has to be added Menu
	  * @param laf String that represents the Look&Feel passed to the LookAndFeelAction
	  * @param mnemonic that enables ALT+Mnemonic functionality
	  * @param stroke  that enables CTRL+Mnemonics functionality 
	  */
	  public void addMenuItem(JMenuItem menuItem,Action action,int mnemonic,KeyStroke stroke){
		  menuItem.setFont(ScoolConstants.FONT);
		  menuItem.setMnemonic(mnemonic);	
		  menuItem.setAccelerator(stroke);		
		  menuItem.addActionListener(action);
		  add(menuItem);
	  }
}
	  