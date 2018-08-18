package projects.ecommunicator.menus;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import projects.ecommunicator.actions.LookAndFeelAction;
import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is a sub class of JMenu that creates the LookAndFeelMenu    
 * <P> 
 * @see javax.swing.JMenu
 * @see javax.swing.JMenuItem
 * @version 1.0
 */
public class LookAndFeelMenu extends JMenu{
	private JMenuItem menuItem;
	private WhiteBoardFrame whiteBoardFrame;

	public LookAndFeelMenu(WhiteBoardFrame whiteBoardFrame) {
		//creates menu titled look & feel
		super("Look & Feel");
		setFont(ScoolConstants.FONT);
		setMnemonic(KeyEvent.VK_L);
		
		this.whiteBoardFrame = whiteBoardFrame;
		
		//creates menuitem for metal look&feel and registers the LookAndFeelAction
		menuItem = new JMenuItem(Property.getString("school","LookMenu.metal_label"));
		addMenuItem(menuItem,Property.getString("school","LookMenu.metal_laf"),KeyEvent.VK_M);		
		
		//creates menuitem for windows look&feel and registers the LookAndFeelAction
		menuItem = new JMenuItem(Property.getString("school","LookMenu.liquid_label"));
		addMenuItem(menuItem,Property.getString("school","LookMenu.liquid_laf"),KeyEvent.VK_W);

		//creates menuitem for motif look&feel and registers the LookAndFeelAction		
		menuItem = new JMenuItem(Property.getString("school","LookMenu.macintosh_label"));
		addMenuItem(menuItem,Property.getString("school","LookMenu.macintosh_laf"),KeyEvent.VK_O);	

		//creates menuitem for macintosh look&feel and registers the LookAndFeelAction
		menuItem = new JMenuItem(Property.getString("school","LookMenu.jgoodies_label"));
		addMenuItem(menuItem,Property.getString("school","LookMenu.jgoodies_laf"),KeyEvent.VK_C);		
	}
	/**
	 * method that adds the menuitems to the LookAndFeelMenu
	 * @param menuItem that has to be added Menu
	 * @param laf String that represents the Look&Feel passed to the LookAndFeelAction
	 * @param mnemonic that enables ALT+Mnemonic functionality	 
	 */
	public void addMenuItem(JMenuItem menuItem,String laf,int mnemonic){
		menuItem.setFont(ScoolConstants.FONT);
		menuItem.setMnemonic(mnemonic);		
		menuItem.addActionListener(new LookAndFeelAction(whiteBoardFrame,laf));
		add(menuItem);
	}
}