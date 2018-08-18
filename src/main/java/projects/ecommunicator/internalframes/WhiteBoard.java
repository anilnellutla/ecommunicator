package projects.ecommunicator.internalframes;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import projects.ecommunicator.actions.CircleAction;
import projects.ecommunicator.actions.CopyAction;
import projects.ecommunicator.actions.CutAction;
import projects.ecommunicator.actions.DeleteAction;
import projects.ecommunicator.actions.FreeHandAction;
import projects.ecommunicator.actions.LineAction;
import projects.ecommunicator.actions.PasteAction;
import projects.ecommunicator.actions.RectAction;
import projects.ecommunicator.actions.RoundRectAction;
import projects.ecommunicator.actions.SelectAction;
import projects.ecommunicator.actions.TextAction;
import projects.ecommunicator.poll.PollButtonPanel;
import projects.ecommunicator.utility.ComponentsSizeFactor;
import projects.ecommunicator.utility.Layout;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

/**
 * This is a sub class of JInternalFrame that has the been set as ContentPane for the Application   
 * <P> 
 * @see javax.swing.JInternalFrame
 * @see javax.swing.JToggleButton
 * @see java.awt.GridBagLayout
 * @see java.awt.GridBagConstraints
 * @version 1.0
 */

public class WhiteBoard extends JInternalFrame implements ItemListener {

	//Canvas Object of the Application
	private WhiteBoardCanvas canvas;
	//border objects used for toggle buttons
	private Border raised = new SoftBevelBorder(SoftBevelBorder.RAISED);
	private Border lowered = new SoftBevelBorder(SoftBevelBorder.LOWERED);

	//button that registers SelectAction
	private JToggleButton selectButton;
	//button that registers CircleAction
	private JToggleButton circleButton;
	//button that registers RectAction
	private JToggleButton rectButton;
	//button that registeres LineAction
	private JToggleButton lineButton;
	//button that registeres RoundRectAction
	private JToggleButton roundRectButton;
	//button that registeres FreeHadAction
	private JToggleButton freeHandButton;
	// button that registers TextAction
	private JToggleButton textButton;

	private JButton cutButton;
	private JButton copyButton;
	private JButton pasteButton;
	private JButton deleteButton;

	private JPanel panel;
	private PollButtonPanel pollButtonPanel;

	/**
	 * Creates a new instance of WhiteBoard
	 */
	public WhiteBoard() {
		//creates new InternalFrame titled White Board
		super(ScoolConstants.DEFAULT_WHITEBOARD_TITLE);
		//creates and sets the internal frame's icon	

		frameIcon =
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "Frame_WhiteBoard_Gif")));
		setFrameIcon((Icon) frameIcon);

		//get the component object of the internal frame
		JComponent component = (JComponent) getContentPane();
		component.setLayout(new BorderLayout());

		//panel that contains the buttonPanel
		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));

		//panel that contains the gridPanel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		//panel that contains the tool buttons
		JPanel toolGridPanel = new JPanel();
		toolGridPanel.setLayout(new GridBagLayout());

		//panel that contains the edit buttons
		JPanel editGridPanel = new JPanel();
		editGridPanel.setLayout(new GridBagLayout());

		//add canvas to the canvasPanel
		canvas = new WhiteBoardCanvas(this);
		//component.add(panel, BorderLayout.CENTER);
		//canvas.setEnabled(false);

		//Tool Buttons Action Classes
		Action selectAction = new SelectAction(canvas);
		Action circleAction = new CircleAction(canvas);
		Action rectAction = new RectAction(canvas);
		Action lineAction = new LineAction(canvas);
		Action roundRectAction = new RoundRectAction(canvas);
		Action freeHandAction = new FreeHandAction(canvas);
		Action textAction = new TextAction(canvas);

		//Edit Buttons Action Classes
		Action cutAction = new CutAction(canvas);
		Action copyAction = new CopyAction(canvas);
		Action pasteAction = new PasteAction(canvas);
		Action deleteAction = new DeleteAction(canvas);

		//toolPanel Buttons
		selectButton = new JToggleButton();
		circleButton = new JToggleButton();
		//circleButton.setEnabled(false);
		rectButton = new JToggleButton();
		//rectButton.setEnabled(false);
		lineButton = new JToggleButton();
		//lineButton.setEnabled(false);
		roundRectButton = new JToggleButton();
		//roundRectButton.setEnabled(false);
		freeHandButton = new JToggleButton();
		//freeHandButton.setEnabled(false);
		textButton = new JToggleButton();

		//editPanel Buttons
		cutButton = new JButton();
		copyButton = new JButton();
		pasteButton = new JButton();
		deleteButton = new JButton();

		//contains the list of tool buttons
		JToggleButton toolButtons[] =
			{
				selectButton,
				circleButton,
				lineButton,
				rectButton,
				roundRectButton,
				freeHandButton,
				textButton };

		//contains the list of edit buttons
		JButton editButtons[] =
			{ cutButton, copyButton, pasteButton, deleteButton };

		//contains the list of tools actions
		Action toolActions[] =
			{
				selectAction,
				circleAction,
				lineAction,
				rectAction,
				roundRectAction,
				freeHandAction,
				textAction };

		//contains the list of edit actions 
		Action editActions[] =
			{ cutAction, copyAction, pasteAction, deleteAction };

		//contains the images of the tool buttons
		String toolButtonImages[] =
			{
				Property.getString("images", "WB_Select_Gif"),
				Property.getString("images", "WB_Circle_Gif"),
				Property.getString("images", "WB_Line_Gif"),
				Property.getString("images", "WB_Rect_Gif"),
				Property.getString("images", "WB_RoundRect_Gif"),
				Property.getString("images", "WB_FreeHand_Gif"),
				Property.getString("images", "WB_Text_Gif")};

		//contains the images of the edit buttons
		String editButtonImages[] =
			{
				Property.getString("images", "WB_Canvas_Cut_Gif"),
				Property.getString("images", "WB_Canvas_Copy_Gif"),
				Property.getString("images", "WB_Canvas_Paste_Gif"),
				Property.getString("images", "WB_Canvas_Delete_Gif")};

		//creates buttons and add it to gridPanel
		for (int i = 0; i < toolButtons.length; i++) {
			addToolButton(
				toolButtons[i],
				toolButtonImages[i],
				toolGridPanel,
				toolActions[i],
				i);
		}

		for (int i = 0; i < editButtons.length; i++) {
			addEditButton(
				editButtons[i],
				editButtonImages[i],
				editGridPanel,
				editActions[i],
				i);
		}
		/*
		
		JPanel editPanel = new JPanel();
		
		cutButton =
			new JButton(
				new ImageIcon(
					Property.getString("images", "WB_Canvas_Cut_Gif")));
		cutButton.setMargin(ScoolConstants.INSETS);
		editPanel.add(cutButton);
		//Action cutAction = new CutAction(canvas);
		cutButton.addActionListener(cutAction);
		
		copyButton =
			new JButton(
				new ImageIcon(
					Property.getString("images", "WB_Canvas_Copy_Gif")));
		editPanel.add(copyButton);
		//Action copyAction = new CopyAction(canvas);
		copyButton.addActionListener(copyAction);
		
		pasteButton =
			new JButton(
				new ImageIcon(
					Property.getString("images", "WB_Canvas_Paste_Gif")));
		editPanel.add(pasteButton);
		//Action pasteAction = new PasteAction(canvas);
		pasteButton.addActionListener(pasteAction);
		
		deleteButton =
			new JButton(
				new ImageIcon(
					Property.getString("images", "WB_Canvas_Delete_Gif")));
		editPanel.add(deleteButton);
		//Action deleteAction = new DeleteAction(canvas);
		deleteButton.addActionListener(deleteAction);
		*/

		//set borders for the panels		
		buttonPanel.setBorder(ScoolConstants.ETCHED_BORDER);

		//add gridPanel to buttonPanel
		buttonPanel.add(toolGridPanel, BorderLayout.WEST);

		buttonPanel.add(editGridPanel, BorderLayout.EAST);

		//add button panel to panel
		panel.add(buttonPanel);

		//add panel and canvas to the internal frame
		component.add(panel, BorderLayout.NORTH);
		component.add(canvas);

		//gets and sets the default size from ComponentsSizeFactor
		Rectangle size =
			Layout.getSize(
				ComponentsSizeFactor.WHITE_BOARD_WIDTH,
				ComponentsSizeFactor.WHITE_BOARD_HEIGHT);
		setSize(size.width, size.height);
		//gets and sets the default location from ComponentsSizeFactor
		Rectangle location =
			Layout.getLocation(
				ComponentsSizeFactor.WHITE_BOARD_POSITION_X,
				ComponentsSizeFactor.WHITE_BOARD_POSITION_Y);
		setLocation(location.width, location.height);
		//makes the internalframe visible
		setVisible(true);
	}
	/**
	 * method that adds the tool button to the gridPanel and registers their action classes
	 * @param button that has to be added to the gridPanel
	 * @param icon icon for the tool buttons
	 * @param panel buttons to be added with
	 * @param action to be registered with the tool buttons 
	 * @param index position of the buttons 
	 */
	public void addToolButton(
		JToggleButton button,
		String icon,
		JPanel panel,
		Action action,
		int index) {
		ImageIcon image =
			new ImageIcon(getClass().getClassLoader().getResource(icon));
		button.setIcon(image);
		button.setSelected(false);
		button.setBorder(button.isSelected() ? lowered : raised);
		button.setRequestFocusEnabled(true);
		button.setSelectedIcon(image);
		button.addItemListener(this);
		button.addActionListener(action);
		button.setMargin(ScoolConstants.INSETS);

		//that holds the constraints for the layout
		panel.add(button, addConstraints(index));
	}

	public void addEditButton(
		JButton button,
		String icon,
		JPanel panel,
		Action action,
		int index) {
		ImageIcon image =
			new ImageIcon(getClass().getClassLoader().getResource(icon));
		button.setIcon(image);
		button.setSelected(false);
		button.setBorder(circleButton.isSelected() ? lowered : raised);
		button.setRequestFocusEnabled(true);
		button.setSelectedIcon(image);
		button.addItemListener(this);
		button.addActionListener(action);
		button.setMargin(ScoolConstants.INSETS);

		//that holds the constraints for the layout
		panel.add(button, addConstraints(index));
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
	/**
	 * method that listens for the ItemStateChanged of ToggleButtons and sets the border for the tool buttons 	 
	 * @param evt that has been captured on ItemEvent 
	 */
	public void itemStateChanged(ItemEvent evt) {
		JToggleButton obj = (JToggleButton) evt.getItem();

		if (obj == selectButton) {
			if (selectButton.isSelected()) {

				selectButton.setBorder(lowered);
				selectButton.setSelected(true);

				circleButton.setBorder(raised);
				circleButton.setSelected(false);
				lineButton.setBorder(raised);
				lineButton.setSelected(false);
				rectButton.setBorder(raised);
				rectButton.setSelected(false);
				roundRectButton.setBorder(raised);
				roundRectButton.setSelected(false);
				freeHandButton.setBorder(raised);
				freeHandButton.setSelected(false);
				textButton.setBorder(raised);
				textButton.setSelected(false);
			} else {
				selectButton.setBorder(raised);
				selectButton.setSelected(false);
			}
		} else if (obj == circleButton) {
			if (circleButton.isSelected()) {

				circleButton.setBorder(lowered);
				circleButton.setSelected(true);

				selectButton.setBorder(raised);
				selectButton.setSelected(false);
				lineButton.setBorder(raised);
				lineButton.setSelected(false);
				rectButton.setBorder(raised);
				rectButton.setSelected(false);
				roundRectButton.setBorder(raised);
				roundRectButton.setSelected(false);
				freeHandButton.setBorder(raised);
				freeHandButton.setSelected(false);
				textButton.setBorder(raised);
				textButton.setSelected(false);
			} else {
				circleButton.setBorder(raised);
				circleButton.setSelected(false);
			}
		} else if (obj == lineButton) {
			if (lineButton.isSelected()) {

				lineButton.setBorder(lowered);
				lineButton.setSelected(true);

				selectButton.setBorder(raised);
				selectButton.setSelected(false);
				circleButton.setBorder(raised);
				circleButton.setSelected(false);
				rectButton.setBorder(raised);
				rectButton.setSelected(false);
				roundRectButton.setBorder(raised);
				roundRectButton.setSelected(false);
				freeHandButton.setBorder(raised);
				freeHandButton.setSelected(false);
				textButton.setBorder(raised);
				textButton.setSelected(false);
			} else {
				lineButton.setBorder(raised);
				lineButton.setSelected(false);
			}
		} else if (obj == rectButton) {
			if (rectButton.isSelected()) {

				rectButton.setBorder(lowered);
				rectButton.setSelected(true);

				selectButton.setBorder(raised);
				selectButton.setSelected(false);
				circleButton.setBorder(raised);
				circleButton.setSelected(false);
				lineButton.setBorder(raised);
				lineButton.setSelected(false);
				roundRectButton.setBorder(raised);
				roundRectButton.setSelected(false);
				freeHandButton.setBorder(raised);
				freeHandButton.setSelected(false);
				textButton.setBorder(raised);
				textButton.setSelected(false);
			} else {
				rectButton.setBorder(raised);
				rectButton.setSelected(false);
			}
		} else if (obj == roundRectButton) {
			if (roundRectButton.isSelected()) {

				roundRectButton.setBorder(lowered);
				roundRectButton.setSelected(true);

				selectButton.setBorder(raised);
				selectButton.setSelected(false);
				circleButton.setBorder(raised);
				circleButton.setSelected(false);
				lineButton.setBorder(raised);
				lineButton.setSelected(false);
				rectButton.setBorder(raised);
				rectButton.setSelected(false);
				freeHandButton.setBorder(raised);
				freeHandButton.setSelected(false);
				textButton.setBorder(raised);
				textButton.setSelected(false);
			} else {
				roundRectButton.setBorder(raised);
				roundRectButton.setSelected(false);
			}
		} else if (obj == freeHandButton) {
			if (freeHandButton.isSelected()) {

				freeHandButton.setBorder(lowered);
				freeHandButton.setSelected(true);

				selectButton.setBorder(raised);
				selectButton.setSelected(false);
				circleButton.setBorder(raised);
				circleButton.setSelected(false);
				lineButton.setBorder(raised);
				lineButton.setSelected(false);
				roundRectButton.setBorder(raised);
				roundRectButton.setSelected(false);
				rectButton.setBorder(raised);
				rectButton.setSelected(false);
				textButton.setBorder(raised);
				textButton.setSelected(false);
			} else {
				freeHandButton.setBorder(raised);
				freeHandButton.setSelected(false);
			}
		} else if (obj == textButton) {
			if (textButton.isSelected()) {

				textButton.setBorder(lowered);
				textButton.setSelected(true);

				selectButton.setBorder(raised);
				selectButton.setSelected(false);
				circleButton.setBorder(raised);
				circleButton.setSelected(false);
				lineButton.setBorder(raised);
				lineButton.setSelected(false);
				roundRectButton.setBorder(raised);
				roundRectButton.setSelected(false);
				rectButton.setBorder(raised);
				rectButton.setSelected(false);
				freeHandButton.setBorder(raised);
				freeHandButton.setSelected(false);
			} else {
				textButton.setBorder(raised);
				textButton.setSelected(false);
			}
		}
	}

	/**
	 * method that returns WhiteBoardCanvas 	 
	 * @return  WhiteBoardCanvas
	 */
	public WhiteBoardCanvas getCanvas() {
		return canvas;
	}

	/**
	 * method that returns CircleButton 	 
	 * @return  CircleButton
	 */
	public JToggleButton getCircleButton() {
		return circleButton;
	}

	/**
	 * method that returns RectButton 	 
	 * @return  RectButton
	 */
	public JToggleButton getRectButton() {
		return rectButton;
	}

	/**
	 * method that returns LineButton 	 
	 * @return  LineButton
	 */
	public JToggleButton getLineButton() {
		return lineButton;
	}

	/**
	 * method that returns RoundRectButton 	 
	 * @return  RoundRectButton
	 */
	public JToggleButton getRoundRectButton() {
		return roundRectButton;
	}

	/**
	 * method that returns FreeHandButton 	 
	 * @return  FreeHandButton
	 */
	public JToggleButton getFreeHandButton() {
		return freeHandButton;
	}

	public JPanel getWhiteBoardButtonPanel() {
		return panel;
	}
	
	public void setWhiteBoardButtonPanel(JPanel panel){
		this.panel = panel;
	}

	public void setPollButtonPanel(PollButtonPanel pollButtonPanel) {
		this.pollButtonPanel = pollButtonPanel;
	}

	public PollButtonPanel getPollButtonPanel() {
		return pollButtonPanel;
	}

}