package projects.ecommunicator.poll;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PollButtonPanel
	extends JPanel
	implements ActionListener, ItemListener {

	private JToggleButton openPollButton;
	private JToggleButton closePollButton;
	private JToggleButton editPollButton;
	private JToggleButton erasePollButton;

	private Border raised;
	private Border lowered;

	public PollButtonPanel() {

		setLayout(new BorderLayout());
		setBorder(ScoolConstants.ETCHED_BORDER);

		raised = new SoftBevelBorder(SoftBevelBorder.RAISED);
		lowered = new SoftBevelBorder(SoftBevelBorder.LOWERED);

		JPanel pollGridPanel = new JPanel();
		pollGridPanel.setLayout(new GridBagLayout());

		openPollButton = new JToggleButton();
		closePollButton = new JToggleButton();
		editPollButton = new JToggleButton();
		erasePollButton = new JToggleButton();

		JToggleButton[] pollButtons =
			{
				openPollButton,
				closePollButton,
				editPollButton,
				erasePollButton };

		String pollButtonImages[] =
			{
				Property.getString("images", "PB_Open_Gif"),
				Property.getString("images", "PB_Close_Gif"),
				Property.getString("images", "PB_Edit_Gif"),
				Property.getString("images", "PB_Erase_Gif")};

		String pollButtonsTips[] =
			{
				"Open the Poll",
				"Close the Poll",
				"Edits the Poll",
				"Erase the Poll" };

		//		creates buttons and add it to gridPanel
		for (int i = 0; i < pollButtons.length; i++) {
			addPollButton(
				pollButtons[i],
				pollButtonImages[i],
				pollGridPanel,
				pollButtonsTips[i],
				i);
		}
		
		add(pollGridPanel,BorderLayout.EAST);

	}

	public void addPollButton(
		JToggleButton button,
		String icon,
		JPanel panel,
		String toolTip,
		int index) {
		ImageIcon image =
			new ImageIcon(getClass().getClassLoader().getResource(icon));
		button.setIcon(image);
		button.setSelected(false);
		button.setBorder(button.isSelected() ? lowered : raised);
		button.setRequestFocusEnabled(true);
		button.setSelectedIcon(image);
		button.addItemListener(this);
		button.setToolTipText(toolTip);
		button.addActionListener(this);
		button.setMargin(ScoolConstants.INSETS);

		//that holds the constraints for the layout
		panel.add(button, addConstraints(index));
	}

	public void itemStateChanged(ItemEvent evt) {
		JToggleButton obj = (JToggleButton) evt.getItem();

		if (obj == openPollButton) {
			if (openPollButton.isSelected()) {

				openPollButton.setBorder(lowered);
				openPollButton.setSelected(true);

				closePollButton.setBorder(raised);
				closePollButton.setSelected(false);

			} else {

				openPollButton.setBorder(raised);
				openPollButton.setSelected(false);
			}
		} else if (obj == closePollButton) {
			if (closePollButton.isSelected()) {

				closePollButton.setBorder(lowered);
				closePollButton.setSelected(true);

				openPollButton.setBorder(raised);
				openPollButton.setSelected(false);

			} else {

				openPollButton.setBorder(raised);
				openPollButton.setSelected(false);
			}

		} else if (obj == editPollButton) {
			if (editPollButton.isSelected()) {

				editPollButton.setBorder(lowered);
				editPollButton.setSelected(true);

				erasePollButton.setBorder(raised);
				erasePollButton.setSelected(false);

			} else {

				editPollButton.setBorder(raised);
				editPollButton.setSelected(false);
			}

		} else if (obj == erasePollButton) {
			if (erasePollButton.isSelected()) {

				erasePollButton.setBorder(lowered);
				erasePollButton.setSelected(true);

				editPollButton.setBorder(raised);
				editPollButton.setSelected(false);

			} else {

				erasePollButton.setBorder(raised);
				erasePollButton.setSelected(false);
			}

		}

	}

	public void actionPerformed(ActionEvent evt) {

	}

	public GridBagConstraints addConstraints(int index) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = index;
		constraints.gridy = 0;
		constraints.weightx = 0;
		constraints.weighty = 0;
		return constraints;
	}

}
