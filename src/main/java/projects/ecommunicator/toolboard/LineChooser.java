package projects.ecommunicator.toolboard;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import projects.ecommunicator.actions.BasicStrokeAction;
import projects.ecommunicator.internalframes.WhiteBoard;
import projects.ecommunicator.utility.Property;

/**
 * This is a sub class of JPanel that constructs the lineChooser Component   
 * <P> 
 * @see javax.swing.JToggleButton
 * @see javax.swing.border.BevelBorder
 * @see javax.swing.border.EmptyBorder
 * @version 1.0
 */

public class LineChooser extends JPanel implements ItemListener {
	//buttons for various line width starting from 1 to 4
	private JToggleButton line1 = new JToggleButton();
	private JToggleButton line2 = new JToggleButton();
	private JToggleButton line3 = new JToggleButton();
	private JToggleButton line4 = new JToggleButton();

	//application's whiteBoard object
	private WhiteBoard whiteBoard;

	/**
	 * Creates a new instance of LineChooser
	 * @param whiteBoard passed to the BasicStrokeAction to set the line Width
	 */
	public LineChooser(WhiteBoard whiteBoard) {
		this.whiteBoard = whiteBoard;
		setBorder((BevelBorder) BorderFactory.createLoweredBevelBorder());
		setLayout(new GridLayout(4, 1));

		//array of buttons
		JToggleButton buttons[] = { line1, line2, line3, line4 };
		//array of button images
		String buttonImages[] = {
			//"images/LineWidth1.gif",
			//"images/LineWidth2.gif",
			//"images/LineWidth3.gif",
			//"images/LineWidth4.gif"
			Property.getString("images", "TB_LineWidth1_Gif"),
				Property.getString("images", "TB_LineWidth2_Gif"),
				Property.getString("images", "TB_LineWidth3_Gif"),
				Property.getString("images", "TB_LineWidth4_Gif")};
		//addButton that adds the buttons 
		for (int i = 0; i < buttons.length; i++) {
			addButton(buttons[i], buttonImages[i], i + 1);
		}
	}

	/**
	 * method that adds the button to the lineChooserPanel
	 * @param button the to be registered with the BasicStrokeAction and to be added
	 * @param icon image for the Button
	 * @param i the width of the line that has been passed to the BasicStrokeAction	 
	 */

	public void addButton(JToggleButton button, String icon, int i) {
		ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource(icon));
		button.setIcon(image);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setSelected(false);
		button.setBorder((EmptyBorder) BorderFactory.createEmptyBorder());
		button.setSelectedIcon(image);
		button.addActionListener(new BasicStrokeAction(whiteBoard, i));
		button.addItemListener(this);
		add(button);
	}

	/**
	 * method that changes the border of the JToggleButtons on selection
	 * @param evt that has been captured on the occurance of the ItemEvent	 	 
	 */

	public void itemStateChanged(ItemEvent evt) {
		JToggleButton obj = (JToggleButton) evt.getItem();

		if (obj == line1) {
			if (line1.isSelected()) {

				line1.setSelected(true);

				line2.setSelected(false);
				line3.setSelected(false);
				line4.setSelected(false);
			}
		} else if (obj == line2) {
			if (line2.isSelected()) {

				line2.setSelected(true);

				line1.setSelected(false);
				line3.setSelected(false);
				line4.setSelected(false);
			}
		} else if (obj == line3) {
			if (line3.isSelected()) {

				line3.setSelected(true);

				line1.setSelected(false);
				line2.setSelected(false);
				line4.setSelected(false);
			}
		} else if (obj == line4) {
			if (line4.isSelected()) {

				line4.setSelected(true);

				line1.setSelected(false);
				line2.setSelected(false);
				line3.setSelected(false);
			}
		}
	}

}
