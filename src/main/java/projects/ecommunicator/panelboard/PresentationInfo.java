package projects.ecommunicator.panelboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import projects.ecommunicator.actions.NavigationAction;
import projects.ecommunicator.application.ApplicationState;
import projects.ecommunicator.application.DataEvent;
import projects.ecommunicator.layeredpane.WhiteBoardDesktopPane;
import projects.ecommunicator.participantinfo.ParticipantState;
import projects.ecommunicator.presentationinfo.PresentationInfoListData;
import projects.ecommunicator.presentationinfo.PresentationInfoListModel;
import projects.ecommunicator.renderer.ListImageCellRenderer;
import projects.ecommunicator.utility.ComponentsSizeFactor;
import projects.ecommunicator.utility.Layout;
import projects.ecommunicator.utility.LoadPage;
import projects.ecommunicator.utility.Permission;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

public class PresentationInfo extends JPanel implements ListSelectionListener {

	private JList presentationList;
	private PresentationInfoListModel listModel;
	private JPopupMenu popupMenu;
	private WhiteBoardDesktopPane whiteBoardDesktopPane;
	private Action navigationAction;
	private JPopupMenu popup;
	private boolean sendValueChangedEvent = true;
	private boolean syncWithSession = true;
	private String currentSessionPage;
	private JButton syncButton;

	public PresentationInfo(WhiteBoardDesktopPane whiteBoardDesktopPane) {

		setLayout(new BorderLayout());

		this.whiteBoardDesktopPane = whiteBoardDesktopPane;

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		listPanel.setBorder(BorderFactory.createEtchedBorder());

		//add elements to listModel
		listModel = new PresentationInfoListModel();

		//add listModel to list
		presentationList = new JList(listModel);
		presentationList.setCellRenderer(new ListImageCellRenderer());
		presentationList.setFont(new Font("Verdana", Font.PLAIN, 11));
		presentationList.setVisibleRowCount(6);
		presentationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		presentationList.setSelectedIndex(0);

		//create Popup menu and add it to the list
		popupMenu = createPopupMenu();
		presentationList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
			public void mouseReleased(MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(presentationList);
		presentationList.addListSelectionListener(this);
		listPanel.add(scrollPane);

		add(listPanel, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3));
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());

		navigationAction = new NavigationAction(this);

		addButton(
			Property.getString("school", "ToolBar.previous_Label"),
			buttonPanel,
			navigationAction);
		addButton(
			Property.getString("school", "ToolBar.next_Label"),
			buttonPanel,
			navigationAction);

		String buttonName = Property.getString("school", "sync");
		ImageIcon inSyncIcon =
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "Pre_Sync_Gif")));

		syncButton = new JButton(buttonName, inSyncIcon);
		syncButton.setFont(new Font("Verdana", Font.PLAIN, 11));
		syncButton.addActionListener(navigationAction);
		syncButton.setActionCommand(buttonName);
		buttonPanel.add(syncButton);

		add(buttonPanel, BorderLayout.SOUTH);

		TitledBorder titled =
			BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Presentation Info",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				new Font("Verdana", Font.PLAIN, 11));
		setBorder(titled);

		Rectangle size =
			Layout.getSize(
				ComponentsSizeFactor.PRESENTATION_INFO_WIDTH,
				ComponentsSizeFactor.PRESENTATION_INFO_HEIGHT);
		setMinimumSize(new Dimension(size.width, size.height));
	}

	public void addButton(String buttonName, JPanel panel, Action action) {
		JButton button = new JButton(buttonName);
		button.setFont(ScoolConstants.FONT);
		button.addActionListener(action);
		button.setActionCommand(buttonName);
		panel.add(button);
	}

	public JPopupMenu createPopupMenu() {

		popup = new JPopupMenu("JPopupMenu demo");
		createPopupMenuItem(popup, "Delete", null);
		createPopupMenuItem(popup, "Rename", null);
		return popup;
	}

	public JMenuItem createPopupMenuItem(
		JPopupMenu menu,
		String label,
		Action action) {
		JMenuItem mi = menu.add(new JMenuItem(label));
		mi.addActionListener(action);
		return mi;
	}

	public DefaultListModel getDefaultListModel() {
		return listModel;
	}

	public JPopupMenu getPopupMenu() {
		return popup;
	}

	public JList getList() {
		return presentationList;
	}

	public void add(PresentationInfoListData data) {

		listModel.addElement(data);
		//presentationList.setSelectedValue(data, true);
		whiteBoardDesktopPane.getWhiteBoard().setTitle(data.toString());
	}

	//This method is required by ListSelectionListener.
	public void valueChanged(ListSelectionEvent evt) {

		if (evt.getValueIsAdjusting() == false) {
			JList source = (JList) evt.getSource();
			PresentationInfoListData data =
				(PresentationInfoListData) source.getSelectedValue();

			String selectedValue = data.toString();
			whiteBoardDesktopPane.getWhiteBoard().setTitle(selectedValue);

			int selectedIndex = presentationList.getSelectedIndex();
			if (selectedIndex == -1) {
				selectedIndex = 0;
			}
			WhiteBoardCanvas canvas =
				whiteBoardDesktopPane.getWhiteBoard().getCanvas();
			canvas.setCurrentPageNo(selectedIndex + 1);
			canvas.setCurrentPageName(selectedValue);

			if (selectedValue.startsWith(ScoolConstants.WHITE_BOARD)) {
				canvas.setApplicationType(ScoolConstants.WHITE_BOARD_APP);
			} else if (
				selectedValue.startsWith(ScoolConstants.PPT_PRESENTATION)) {
				canvas.setApplicationType(ScoolConstants.WHITE_BOARD_APP);
			} else if (
				selectedValue.startsWith(ScoolConstants.POLL)) {
				canvas.setApplicationType(ScoolConstants.WHITE_BOARD_APP);
			}

			if (sendValueChangedEvent) {

				if (ParticipantState.getInstance().getPresentatioInfoOption()
					== Permission.ALLOW_PRESENTATIONINFO) {

					DataEvent.notifySender(
						canvas.getApplicationType(),
						canvas.getCurrentPageNo(),
						canvas.getCurrentPageName(),
						getToolString());
				} else {
					syncButton.setIcon(
						new ImageIcon(
							getClass().getClassLoader().getResource(
								Property.getString(
									"images",
									"Pre_ASync_Gif"))));
					syncWithSession = false;
				}
			}
			synchronized (this) {
				sendValueChangedEvent = true;
			}

			if (ApplicationState.getInstance().isReceivedOfflineData()) {
				LoadPage loadPage = new LoadPage(canvas, selectedValue);
				/*
				Thread t = new Thread(loadPage);
				t.start();
				*/
				loadPage.run();
			}
		}
	}

	public JList getPresentationList() {
		return presentationList;
	}

	public PresentationInfoListModel getListModel() {
		return listModel;
	}

	public Action getNavigationAction() {
		return navigationAction;
	}

	public String getToolString() {
		return Utility.convertTo4Byte(ScoolConstants.NAVIGATION_ACTION);
	}

	public void setSendValueChangedEvent(boolean sendValueChangedEvent) {
		this.sendValueChangedEvent = sendValueChangedEvent;
	}

	public boolean getSendValueChangedEvent() {
		return sendValueChangedEvent;
	}

	public void setSynWithSession(boolean syncWithSession) {
		syncButton.setIcon(
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "Pre_Sync_Gif"))));
		this.syncWithSession = syncWithSession;
	}

	public boolean isSyncWithSession() {
		return syncWithSession;
	}

	public void setCurrentSessionPage(String currentSessionPage) {
		this.currentSessionPage = currentSessionPage;
	}

	public String getCurrentSessionPage() {
		return currentSessionPage;
	}

}
