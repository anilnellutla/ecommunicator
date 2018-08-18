package projects.ecommunicator.menus;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is a sub class of JMenu that creates the EditMenu    
 * <P> 
 * @see javax.swing.JMenu
 * @see javax.swing.JMenuItem
 * @version 1.0
 */
public class EditMenu extends JMenu{
	private JMenuItem menuItem;
	/**
	 * Creates a new instance of EditMenu
	 * @param frame Applications parent Frame
	 */
	public EditMenu() {
		//creates Menu titled Edit
		super("Edit");
		setMnemonic(KeyEvent.VK_E);
		setFont(ScoolConstants.FONT);
		
		//creates menuitem for undo operation and registers undo action
		menuItem = new JMenuItem(Property.getString("school","EditMenu.undo_label"));
		addMenuItem(menuItem,null,KeyEvent.VK_U,KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		
		//creates menuitem for cut operation and registers cut action
		menuItem = new JMenuItem(Property.getString("school","EditMenu.cut_label"));
		addMenuItem(menuItem,null,KeyEvent.VK_T,KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));		
		
		//creates menuitem for copy operation and registers copy operation
		menuItem = new JMenuItem(Property.getString("school","EditMenu.copy_label"));
		addMenuItem(menuItem,null,KeyEvent.VK_C,KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));		

		//creates menuitem for paste operation and registers paste action
		menuItem = new JMenuItem(Property.getString("school","EditMenu.paste_label"));
		addMenuItem(menuItem,null,KeyEvent.VK_P,KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));		
	}
	
	/**
	 * method that adds the menuitems to the EditMenu
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