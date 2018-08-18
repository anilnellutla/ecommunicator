package projects.ecommunicator.application;

import javax.swing.JMenuBar;

import projects.ecommunicator.menus.CreateMenu;
import projects.ecommunicator.menus.FileMenu;
import projects.ecommunicator.menus.LookAndFeelMenu;
import projects.ecommunicator.menus.WindowMenu;

/** 
 * This class extends JMenuBar and creates Menus.
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */
public class WhiteBoardMenuBar extends JMenuBar {
	// File Menu
	private FileMenu fileMenu;
	// CreateMenu
	private CreateMenu createMenu;
	// LookAndFeelMenu
	private LookAndFeelMenu lookMenu;
	// Window Menu
	private WindowMenu windowMenu;

	/**
	 *  Creates new instance of WhiteBoardMenuBar
	 *  @param container to add JMenuBar.
	 */
	public WhiteBoardMenuBar(WhiteBoardFrame frame) {
		fileMenu = new FileMenu(frame);		
		// pass content pane to get Whiteboard Action
		createMenu = new CreateMenu(frame.getWhiteBoardPanel());
		lookMenu = new LookAndFeelMenu(frame);
		windowMenu = new WindowMenu(frame.getWhiteBoardPanel());

		// add menus to menu bar
		add(fileMenu);		
		add(createMenu);
		add(lookMenu);
		add(windowMenu);
	}

	/**
	 * Returns FileMenu
	 * @return FileMenu
	 */
	public FileMenu getFileMenu() {
		return fileMenu;
	}	

	/**
	 * Returns CreateMenu
	 * @return CreateMenu
	 */
	public CreateMenu getCreateMenu() {
		return createMenu;
	}

	/**
	 * ReturnsWindowMenu
	 * @return WindoMenu
	 */
	public WindowMenu getWindowMenu() {
		return windowMenu;
	}
}
