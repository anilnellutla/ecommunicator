package projects.ecommunicator.actions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.utility.Property;

/**
 * This is a sub class of AbstractAction which has been used for About Dialog Box  
 * <P> 
 * @see javax.swing.AbstractAction
 * @see javax.swing.JDialog
 * @version 1.0
 */
public class AboutAction extends AbstractAction{
		//variable for the AboutDialog Box
		private JDialog aboutBox = null;
		//parent frame
		private WhiteBoardFrame desktop;
		
		/**
		* Creates a new instance of AboutAction ()
		* @param desktop Parent Frame in which the dialog to be shown		
		*/
		public AboutAction(WhiteBoardFrame desktop){
			super("AboutAction");
			this.desktop = desktop;
        }
        
		/**
		* method actionPerformed instantiates the dialog object and shows it
		* @param evt the action event that has been fired 		
		* @see java.awt.event.ActionEvent
		* @see org.apache.poi.hssf.record.Record
		*/
        public void actionPerformed(ActionEvent evt) {
			if(aboutBox == null) {
				JPanel panel = new AboutPanel(desktop);
				panel.setLayout(new BorderLayout());

				aboutBox = new JDialog(desktop, "About eCommunicator", false);
				aboutBox.getContentPane().add(panel, BorderLayout.CENTER);

				JPanel buttonpanel = new JPanel();
				buttonpanel.setOpaque(false);
				JButton button = (JButton) buttonpanel.add(new JButton("OK"));
				panel.add(buttonpanel, BorderLayout.SOUTH);
				button.addActionListener(new OkAction(aboutBox));
			}
			aboutBox.pack();
			Point p = desktop.getLocationOnScreen();
			aboutBox.setLocation(p.x + 100, p.y + 100);
			aboutBox.show();
		}
	}

	class OkAction extends AbstractAction {
		private JDialog aboutBox;

		protected OkAction(JDialog aboutBox) {
			super("OkAction");
			this.aboutBox = aboutBox;
		}

		public void actionPerformed(ActionEvent e) {
			aboutBox.setVisible(false);
		}
	}

	class AboutPanel extends JPanel {
		private ImageIcon aboutimage = null;
		private WhiteBoardFrame desktop = null;

		public AboutPanel(WhiteBoardFrame desktop) {
			this.desktop = desktop;
			aboutimage = new ImageIcon(getClass().getClassLoader().getResource(Property.getString("images","About_Gif")));
			setOpaque(false);
		}

		public void paint(Graphics g) {
			aboutimage.paintIcon(this, g, 0, 0);
			super.paint(g);
		}

		public Dimension getPreferredSize() {
			return new Dimension(aboutimage.getIconWidth(),aboutimage.getIconHeight());
		}
	}