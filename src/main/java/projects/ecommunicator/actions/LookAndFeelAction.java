package projects.ecommunicator.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import projects.ecommunicator.application.WhiteBoardFrame;

/**
 * This is a sub class of AbstractAction that sets the selected LookAndFeel for the application 
 * <P> 
 * @see javax.swing.AbstractAction
 * @version 1.0
 */
public class LookAndFeelAction extends AbstractAction {
	//Parent Frame object of the Application
	private JFrame desktop;
	//popup menu object 
	private JPopupMenu popupMenu;
	//look and feel name
	private String laf;

	/**
	* Creates a new instance of LookAndFeelAction
	* @param desktop object used to set the tool	
	* @param laf the selected look and feel 
	*/
	public LookAndFeelAction(WhiteBoardFrame desktop, String laf) {
		this.desktop = desktop;
		this.laf = laf;
		//gets the popup menu from the presentation info
		this.popupMenu =
			desktop
				.getWhiteBoardPanel()
				.getWhiteBoardDesktopPane()
				.getPresentationInfo()
				.getPopupMenu();
	}
	
	/**
	* method sets the desired look and feel to the application 
	* @param evt ActionEvent object that has been captured	
	*/
	public void actionPerformed(ActionEvent ae) {
		try {
			UIManager.setLookAndFeel(laf);
			if (desktop != null) {
				//sets the look and feel for the application
				javax.swing.SwingUtilities.updateComponentTreeUI(desktop);
				//sets the look and feel for the popup menu
				javax.swing.SwingUtilities.updateComponentTreeUI(popupMenu);
			}
		} catch (Exception ex) {
		}
	}
}