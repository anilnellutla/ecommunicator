package projects.ecommunicator.menus;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import projects.ecommunicator.actions.AboutAction;
import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is a sub class of JMenu that creates the FileMenu    
 * <P> 
 * @see javax.swing.JMenu
 * @see javax.swing.JMenuItem
 * @version 1.0
 */
public class FileMenu extends JMenu{
	
	private JMenuItem menuItem;

	public FileMenu(WhiteBoardFrame frame) {
		//creates Menu titled File		
		super("File");
		setMnemonic(KeyEvent.VK_F);
		setFont(ScoolConstants.FONT);
		
		//creates menuitem for open operation and registers open action
		menuItem = new JMenuItem(Property.getString("school","FileMenu.open_label"));
		addMenuItem(menuItem,null,KeyEvent.VK_O,KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		
		//creates menuitem for save operation and registers save action		
		menuItem = new JMenuItem(Property.getString("school","FileMenu.save_label"));
		addMenuItem(menuItem,null,KeyEvent.VK_S,KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		//creates menuitem for save_as operation and registers save_as action		
		menuItem = new JMenuItem(Property.getString("school","FileMenu.save_as_label"));
		addMenuItem(menuItem,null,KeyEvent.VK_A,KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menuItem.setMnemonic(KeyEvent.VK_A);
		
		addSeparator();

		//creates menuitem for undo operation and registers undo action
		menuItem = new JMenuItem(Property.getString("school","FileMenu.about_label"));
		AboutAction aboutAction = new AboutAction(frame);
		addMenuItem(menuItem,aboutAction,KeyEvent.VK_B,null);		
  }
  /**
   * method that adds the menuitems to the FileMenu
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
}