/*
 * Created on Jan 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.toolboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Symbols extends JPanel {

	public Symbols(){
		setLayout(new BorderLayout());
		JPanel symbolsPanel = new JPanel();
		symbolsPanel.setLayout(new FlowLayout());

		JButton starButton = new JButton("*");
		JButton slashButton = new JButton("/");
		JButton atButton = new JButton("@");
		JButton hashButton = new JButton("#");
		symbolsPanel.add(starButton);
		symbolsPanel.add(slashButton);
		symbolsPanel.add(atButton);
		symbolsPanel.add(hashButton);
		add(symbolsPanel, BorderLayout.WEST);
	}
}



