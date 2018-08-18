package projects.ecommunicator.toolboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import projects.ecommunicator.actions.TextToolFontAction;
import projects.ecommunicator.actions.ColorChooserAction;
import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is a sub class of JPanel that has been placed as a container for Accessories Tools   
 * <P> 
 * @see javax.swing.JPanel
 * @see java.awt.GridBagLayout
 * @see java.awt.GridBagConstraints
 * @version 1.0
 */

public class Accessories extends JPanel {

	/**
	 * Creates a new instance of Accessories
	 * @param whiteBoardDesktopPane the layeredPane object of the application used to get the WhiteBoard
	 */
	public Accessories(WhiteBoardDesktopPane whiteBoardDesktopPane) {

		setLayout(new BorderLayout());

		//panel for accessories
		JPanel accessoriesPanel = new JPanel();
		accessoriesPanel.setLayout(new GridLayout(1, 1));

		//temprory panel to hold the gridbag panel
		JPanel tempPanel = new JPanel();
		tempPanel.setLayout(new BorderLayout());

		//panel to hold the tools i.e colorChooser and lineChooser
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout());
		//gridPanel.setBorder(ScoolConstants.ETCHED_BORDER);

		//constraints for the gridbaglayout
		GridBagConstraints gbc = new GridBagConstraints();

		//lineChooser panel that registers lineChooserAction 
		LineChooser lineChooser =
			new LineChooser(whiteBoardDesktopPane.getWhiteBoard());

		addConstraints(gbc, 0);
		gbc.ipady = 25;
		gridPanel.add(lineChooser, gbc);

		JPanel fontPanel = new JPanel();
		fontPanel.setLayout(new FlowLayout());

		//colorChooser button that registers the colorChooserAction
		JButton colorButton =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "TB_ColorChooser_Gif"))));
		colorButton.addActionListener(
			new ColorChooserAction(whiteBoardDesktopPane.getWhiteBoard()));
		colorButton.setMargin(ScoolConstants.INSETS);
		fontPanel.add(colorButton);

		Action textToolFontAction =
			new TextToolFontAction(
				whiteBoardDesktopPane.getWhiteBoard().getCanvas());
		//comboBox that enables various font selection
		String[] fontNames =
			{
				"Verdana",
				"Helvetica-bold-italic",
				"Arial",
				"Comic Sans MS",
				"Courier New",
				"Times New Roman",
				"sansserif.plain" };
		JComboBox fontNamesCombo = new JComboBox(fontNames);
		fontNamesCombo.addActionListener(textToolFontAction);
		fontNamesCombo.setActionCommand("names");
		fontNamesCombo.setBackground(Color.WHITE);
		fontNamesCombo.setFont(ScoolConstants.FONT);

		//comboBox that enables various font size selection
		String[] fontSize = { "12", "13", "14", "16", "18", "20", "22", "24" };
		JComboBox fontSizeCombo = new JComboBox(fontSize);
		fontSizeCombo.setBackground(Color.WHITE);
		fontSizeCombo.setActionCommand("size");
		fontSizeCombo.addActionListener(textToolFontAction);
		fontSizeCombo.setFont(ScoolConstants.FONT);
		JSeparator separator = new JSeparator();

		JButton plainButton =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "TB_Plain_Gif"))));
		plainButton.setFont(ScoolConstants.FONT);
		plainButton.setActionCommand("plain");
		plainButton.setMargin(ScoolConstants.INSETS);
		plainButton.addActionListener(textToolFontAction);

		JButton boldButton =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "TB_Bold_Gif"))));
		boldButton.setFont(ScoolConstants.FONT);
		boldButton.setActionCommand("bold");
		boldButton.setMargin(ScoolConstants.INSETS);
		boldButton.addActionListener(textToolFontAction);

		JButton italicsButton =
			new JButton(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "TB_Italics_Gif"))));
		italicsButton.setFont(ScoolConstants.FONT);
		italicsButton.setActionCommand("italic");
		italicsButton.setMargin(ScoolConstants.INSETS);
		italicsButton.addActionListener(textToolFontAction);

		fontPanel.add(fontNamesCombo);
		fontPanel.add(fontSizeCombo);
		fontPanel.add(separator);
		fontPanel.add(plainButton);
		fontPanel.add(boldButton);
		fontPanel.add(italicsButton);

		addConstraints(gbc, 2);
		gbc.ipady = 0;
		gridPanel.add(fontPanel, gbc);

		tempPanel.add(gridPanel, BorderLayout.WEST);
		accessoriesPanel.add(tempPanel);

		//add accessories tools to accessoriesPanel
		add(accessoriesPanel, BorderLayout.NORTH);
	}

	/**
	 * method that adds the constraints to the GridBagConstraints Object
	 * @param gbc the Constraint object with which the constraint are to be set
	 * @param index position ot the components in the layout	 
	 */
	public void addConstraints(GridBagConstraints gbc, int index) {
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx = index;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
	}
}
