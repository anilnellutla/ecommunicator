package projects.ecommunicator.panelboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import projects.ecommunicator.application.DataEvent;
import projects.ecommunicator.application.PermissionManager;
import projects.ecommunicator.application.WhiteBoardFrame;
import projects.ecommunicator.participantinfo.ParticipantState;
import projects.ecommunicator.participantinfo.ParticipantsInfoTableModel;
import projects.ecommunicator.participantinfo.ParticipantsInfoUserData;
import projects.ecommunicator.security.Credentials;
import projects.ecommunicator.utility.ComponentsSizeFactor;
import projects.ecommunicator.utility.Layout;
import projects.ecommunicator.utility.LoginState;
import projects.ecommunicator.utility.Permission;
import projects.ecommunicator.utility.Property;
import projects.ecommunicator.utility.Role;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

public class ParticipantsInfo extends JPanel {

	private JTable table;
	private ImageIcon disAllowIcon;
	private ImageIcon allowIcon;
	private ImageIcon handRaiseIcon;
	private static int num_participants_logged = 0;
	private ParticipantsInfoTableModel dataModel;
	private boolean isModeratorLoggedIn = false;
	private WhiteBoardFrame frame;

	public ParticipantsInfo() {

		setLayout(new BorderLayout());
		disAllowIcon =
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "Par_DisAllow_Gif")),
				Permission.DISALLOW);
		allowIcon =
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "Par_Allow_Gif")),
				Permission.ALLOW);
		handRaiseIcon =
			new ImageIcon(
				getClass().getClassLoader().getResource(
					Property.getString("images", "Par_HandRaise_Gif")),
				Permission.SEEK);

		table = new JTable(dataModel = new ParticipantsInfoTableModel());

		TableColumn column = null;
		for (int i = 0; i < 5; i++) {
			column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(200);
			} else {
				column.setPreferredWidth(50);
			}
		}

		JTableHeader header = table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setResizingAllowed(false);
		table.setGridColor(Color.blue);
		table.setShowHorizontalLines(false);

		table.setFont(new Font("Verdana", Font.PLAIN, 11));
		// This customized renderer can render objects of the type Icon
		TableCellRenderer iconHeaderRenderer = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(
				JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column) {
				if (value instanceof HeaderIcon) {
					setIcon(((HeaderIcon) value).icon);
				}
				setBorder(UIManager.getBorder("TableHeader.cellBorder"));
				setHorizontalAlignment(JLabel.CENTER);
				return this;
			}
		};

		table.getTableHeader().getColumnModel().getColumn(0).setHeaderRenderer(
			iconHeaderRenderer);
		table.getColumnModel().getColumn(0).setHeaderValue(
			new HeaderIcon(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString(
							"images",
							"Par_Participants_Gif")))));

		table.getTableHeader().getColumnModel().getColumn(1).setHeaderRenderer(
			iconHeaderRenderer);
		table.getColumnModel().getColumn(1).setHeaderValue(
			new HeaderIcon(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "Par_Mic_Gif")))));

		table.getTableHeader().getColumnModel().getColumn(2).setHeaderRenderer(
			iconHeaderRenderer);
		table.getColumnModel().getColumn(2).setHeaderValue(
			new HeaderIcon(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString("images", "Par_WhiteBoard_Gif")))));

		table.getTableHeader().getColumnModel().getColumn(3).setHeaderRenderer(
			iconHeaderRenderer);
		table.getColumnModel().getColumn(3).setHeaderValue(
			new HeaderIcon(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString(
							"images",
							"Par_MessageBoard_Gif")))));

		table.getTableHeader().getColumnModel().getColumn(4).setHeaderRenderer(
			iconHeaderRenderer);
		table.getColumnModel().getColumn(4).setHeaderValue(
			new HeaderIcon(
				new ImageIcon(
					getClass().getClassLoader().getResource(
						Property.getString(
							"images",
							"Par_PresentationInfo_Gif")))));

		table.setCellSelectionEnabled(true);
		//ListSelectionModel colSM = table.getColumnModel().getSelectionModel();
		//colSM.addListSelectionListener(this);

		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent evt) {
				Credentials credentials = Credentials.getInstance();
				String loginId = credentials.getLoginId();
				int role = credentials.getRole();
				ParticipantState participantState =
					ParticipantState.getInstance();
				int participantRow = dataModel.getParticipantRow(loginId);
				//System.out.println("Role=" + role);
				//System.out.println("Participant Row=" + participantRow);

				for (int row = 0; row < table.getRowCount(); row++) {
					for (int col = 1; col < table.getColumnCount(); col++) {
						if (table.isCellSelected(row, col)) {
							//System.out.println("row=" + row + " col=" + col);

							ImageIcon imageIcon = null;
							if (col != 0) {
								try {
									imageIcon =
										(ImageIcon) table.getValueAt(row, col);
								} catch (ClassCastException ex) {
								}

							}

							if (row == participantRow
								&& role == Role.MEMBER
								&& col != 0) {

								if (col == 1) {
									if (imageIcon
										.getDescription()
										.equals(Permission.DISALLOW)) {
										dataModel.setValueAt(
											handRaiseIcon,
											row,
											col);
										participantState.setMicOption(
											Permission.SEEK_MIC);
									} else if (
										imageIcon.getDescription().equals(
											Permission.SEEK)
											|| imageIcon.getDescription().equals(
												Permission.ALLOW)) {

										dataModel.setValueAt(
											disAllowIcon,
											row,
											col);

										checkPermissions(
											Permission.DISALLOW_MIC,
											participantState
												.getMessageBoardOption(),
											participantState
												.getPresentatioInfoOption(),
											participantState
												.getWhiteBoardOption());

										/*
										participantState.setMicOption(
											Permission.DISALLOW_MIC);
										*/
									}
								}
								if (col == 2) {
									if (imageIcon
										.getDescription()
										.equals(Permission.DISALLOW)) {

										dataModel.setValueAt(
											handRaiseIcon,
											row,
											col);
										participantState.setMessagBoardOption(
											Permission.SEEK_MESSAGEBOARD);
									} else if (
										imageIcon.getDescription().equals(
											Permission.SEEK)
											|| imageIcon.getDescription().equals(
												Permission.ALLOW)) {

										dataModel.setValueAt(
											disAllowIcon,
											row,
											col);

										checkPermissions(
											participantState.getMicOption(),
											Permission.DISALLOW_MESSAGEBOARD,
											participantState
												.getPresentatioInfoOption(),
											participantState
												.getWhiteBoardOption());

										/*
										participantState.setMessagBoardOption(
											Permission.DISALLOW_MESSAGEBOARD);
										*/
									}

								} else if (col == 3) {
									if (imageIcon
										.getDescription()
										.equals(Permission.DISALLOW)) {
										dataModel.setValueAt(
											handRaiseIcon,
											row,
											col);
										participantState
											.setPresentationInfoOption(
											Permission.SEEK_PRESENTATIONINFO);
									} else if (
										imageIcon.getDescription().equals(
											Permission.SEEK)
											|| imageIcon.getDescription().equals(
												Permission.ALLOW)) {

										dataModel.setValueAt(
											disAllowIcon,
											row,
											col);

										checkPermissions(
											participantState.getMicOption(),
											participantState
												.getMessageBoardOption(),
											Permission
												.DISALLOW_PRESENTATIONINFO,
											participantState
												.getWhiteBoardOption());

										/*
										participantState
											.setPresentationInfoOption(
											Permission
												.DISALLOW_PRESENTATIONINFO);
										*/

									}
								} else if (col == 4) {
									if (imageIcon
										.getDescription()
										.equals(Permission.DISALLOW)) {
										dataModel.setValueAt(
											handRaiseIcon,
											row,
											col);
										participantState.setWhiteBoardOption(
											Permission.SEEK_WHITEBOARD);
									} else if (
										imageIcon.getDescription().equals(
											Permission.SEEK)
											|| imageIcon.getDescription().equals(
												Permission.ALLOW)) {

										dataModel.setValueAt(
											disAllowIcon,
											row,
											col);

										checkPermissions(
											participantState.getMicOption(),
											participantState
												.getMessageBoardOption(),
											participantState
												.getPresentatioInfoOption(),
											Permission.DISALLOW_WHITEBOARD);

										/*
										participantState.setWhiteBoardOption(
											Permission.DISALLOW_WHITEBOARD);
										*/
									}
								}
								String commandString =
									getParticipantToolString();

								DataEvent.notifySender(
									ScoolConstants.PARTICIPANTS_INFO_APP,
									0,
									"",
									commandString);

							} else if (role == Role.MODERATOR) {
								int micOption = 0;
								int messageBoardOption = 0;
								int presentationInfoOption = 0;
								int whiteBoardOption = 0;

								ParticipantsInfoUserData data =
									(
										ParticipantsInfoUserData) dataModel
											.getValueAt(
										row,
										0);

								if (row != participantRow
									&& data.getState() != LoginState.LOGGED_OUT) {

									if (col == 1) {

										if (imageIcon
											.getDescription()
											.equals(Permission.DISALLOW)
											|| imageIcon.getDescription().equals(
												Permission.SEEK)) {

											resetAccessToMic();

											dataModel.setValueAt(
												allowIcon,
												row,
												col);
											micOption = Permission.ALLOW_MIC;

										} else if (
											imageIcon.getDescription().equals(
												Permission.ALLOW)) {
											dataModel.setValueAt(
												disAllowIcon,
												row,
												col);
											micOption = Permission.DISALLOW_MIC;
										}
									} else if (col == 2) {

										if (imageIcon
											.getDescription()
											.equals(Permission.DISALLOW)
											|| imageIcon.getDescription().equals(
												Permission.SEEK)) {

											dataModel.setValueAt(
												allowIcon,
												row,
												col);
											messageBoardOption =
												Permission.ALLOW_MESSAGEBOARD;
										} else if (
											imageIcon.getDescription().equals(
												Permission.ALLOW)) {
											dataModel.setValueAt(
												disAllowIcon,
												row,
												col);
											messageBoardOption =
												Permission
													.DISALLOW_MESSAGEBOARD;
										}
									} else if (col == 3) {

										if (imageIcon
											.getDescription()
											.equals(Permission.DISALLOW)
											|| imageIcon.getDescription().equals(
												Permission.SEEK)) {

											resetAccessToPresentationInfo();

											dataModel.setValueAt(
												allowIcon,
												row,
												col);
											presentationInfoOption =
												Permission
													.ALLOW_PRESENTATIONINFO;
										} else if (
											imageIcon.getDescription().equals(
												Permission.ALLOW)) {
											dataModel.setValueAt(
												disAllowIcon,
												row,
												col);
											presentationInfoOption =
												Permission
													.DISALLOW_PRESENTATIONINFO;
										}
									} else if (col == 4) {

										if (imageIcon
											.getDescription()
											.equals(Permission.DISALLOW)
											|| imageIcon.getDescription().equals(
												Permission.SEEK)) {

											resetAccessToWhiteBoard();

											dataModel.setValueAt(
												allowIcon,
												row,
												col);
											whiteBoardOption =
												Permission.ALLOW_WHITEBOARD;

										} else if (
											imageIcon.getDescription().equals(
												Permission.ALLOW)) {
											dataModel.setValueAt(
												disAllowIcon,
												row,
												col);
											whiteBoardOption =
												Permission.DISALLOW_WHITEBOARD;
										}
									}

								} else if (row == participantRow) {
									if (col == 1) {

										if (imageIcon
											.getDescription()
											.equals(Permission.DISALLOW)
											|| imageIcon.getDescription().equals(
												Permission.SEEK)) {

											resetAccessToMic();

											dataModel.setValueAt(
												allowIcon,
												row,
												col);
											micOption = Permission.ALLOW_MIC;

											checkPermissions(
												Permission.ALLOW_MIC,
												participantState
													.getMessageBoardOption(),
												participantState
													.getPresentatioInfoOption(),
												participantState
													.getWhiteBoardOption());

										} else if (
											imageIcon.getDescription().equals(
												Permission.ALLOW)) {
											dataModel.setValueAt(
												disAllowIcon,
												row,
												col);
											micOption = Permission.DISALLOW_MIC;

											checkPermissions(
												Permission.DISALLOW_MIC,
												participantState
													.getMessageBoardOption(),
												participantState
													.getPresentatioInfoOption(),
												participantState
													.getWhiteBoardOption());
										}
									} else if (col == 2) {

										if (imageIcon
											.getDescription()
											.equals(Permission.DISALLOW)
											|| imageIcon.getDescription().equals(
												Permission.SEEK)) {
											dataModel.setValueAt(
												allowIcon,
												row,
												col);
											messageBoardOption =
												Permission.ALLOW_MESSAGEBOARD;

											checkPermissions(
												participantState.getMicOption(),
												Permission.ALLOW_MESSAGEBOARD,
												participantState
													.getPresentatioInfoOption(),
												participantState
													.getWhiteBoardOption());

										} else if (
											imageIcon.getDescription().equals(
												Permission.ALLOW)) {
											dataModel.setValueAt(
												disAllowIcon,
												row,
												col);
											messageBoardOption =
												Permission
													.DISALLOW_MESSAGEBOARD;

											checkPermissions(
												participantState.getMicOption(),
												Permission
													.DISALLOW_MESSAGEBOARD,
												participantState
													.getPresentatioInfoOption(),
												participantState
													.getWhiteBoardOption());
										}
									} else if (col == 3) {

										if (imageIcon
											.getDescription()
											.equals(Permission.DISALLOW)
											|| imageIcon.getDescription().equals(
												Permission.SEEK)) {

											resetAccessToPresentationInfo();

											dataModel.setValueAt(
												allowIcon,
												row,
												col);
											presentationInfoOption =
												Permission
													.ALLOW_PRESENTATIONINFO;

											checkPermissions(
												participantState.getMicOption(),
												participantState
													.getMessageBoardOption(),
												Permission
													.ALLOW_PRESENTATIONINFO,
												participantState
													.getWhiteBoardOption());

											notifySelectedPage();

										} else if (
											imageIcon.getDescription().equals(
												Permission.ALLOW)) {
											dataModel.setValueAt(
												disAllowIcon,
												row,
												col);
											presentationInfoOption =
												Permission
													.DISALLOW_PRESENTATIONINFO;

											checkPermissions(
												participantState.getMicOption(),
												participantState
													.getMessageBoardOption(),
												Permission
													.DISALLOW_PRESENTATIONINFO,
												participantState
													.getWhiteBoardOption());

										}
									} else if (col == 4) {

										if (imageIcon
											.getDescription()
											.equals(Permission.DISALLOW)
											|| imageIcon.getDescription().equals(
												Permission.SEEK)) {

											resetAccessToWhiteBoard();

											dataModel.setValueAt(
												allowIcon,
												row,
												col);
											whiteBoardOption =
												Permission.ALLOW_WHITEBOARD;

											checkPermissions(
												participantState.getMicOption(),
												participantState
													.getMessageBoardOption(),
												participantState
													.getPresentatioInfoOption(),
												Permission.ALLOW_WHITEBOARD);

										} else if (
											imageIcon.getDescription().equals(
												Permission.ALLOW)) {
											dataModel.setValueAt(
												disAllowIcon,
												row,
												col);
											whiteBoardOption =
												Permission.DISALLOW_WHITEBOARD;

											checkPermissions(
												participantState.getMicOption(),
												participantState
													.getMessageBoardOption(),
												participantState
													.getPresentatioInfoOption(),
												Permission.DISALLOW_WHITEBOARD);
										}
									}
								}

								String participantLoginId =
									((ParticipantsInfoUserData) dataModel
										.getValueAt(row, 0))
										.getLoginId();

								String commandString =
									getModeratorToolString(
										micOption,
										messageBoardOption,
										presentationInfoOption,
										whiteBoardOption,
										participantLoginId);

								System.out.println(
									"from moderator:" + commandString);

								DataEvent.notifySender(
									ScoolConstants.PARTICIPANTS_INFO_APP,
									0,
									"",
									commandString);

							}

						}
					}
				}
			}

		});

		JScrollPane scrollPane = new JScrollPane(table);
		TitledBorder titled =
			BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				"Participants Info",
				TitledBorder.LEFT,
				TitledBorder.TOP,
				new Font("Verdana", Font.PLAIN, 11));

		//scrollPane.setBorder(titled);
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		tablePanel.setBorder(titled);

		add(tablePanel, BorderLayout.CENTER);

		Rectangle size =
			Layout.getSize(
				ComponentsSizeFactor.PARTICIPANTS_INFO_WIDTH,
				ComponentsSizeFactor.PARTICIPANTS_INFO_HEIGHT);
		setMinimumSize(new Dimension(size.width, size.height));
	}

	public void setWhiteBoardFrame(WhiteBoardFrame frame) {
		this.frame = frame;
	}

	public void addNewParticipant(String toolString) {
		System.out.println(
			"\n^^^^^^^^^^^Adding new participant..." + toolString);

		String loginId = Utility.getLoginId(toolString);
		int row = dataModel.getParticipantRow(loginId);
		int role = Utility.getRole(toolString);
		ParticipantsInfoUserData data =
			new ParticipantsInfoUserData(
				-1,
				loginId,
				role,
				LoginState.LOGGED_IN);

		if (row != -1) {
			if (loginId.equals(Credentials.getInstance().getLoginId())) {
				data.setState(LoginState.LOGGED_IN);
				data.setDisplayValue(loginId + "*");
				dataModel.setValueAt(data, row, 0);
			} else {
				data.setState(LoginState.LOGGED_IN);
				data.setDisplayValue(loginId);
				dataModel.setValueAt(data, row, 0);
			}
		} else {
			row = num_participants_logged;
			if (role == Role.MODERATOR) {
				row = ScoolConstants.MODERATOR_ROW;
				isModeratorLoggedIn = true;
			} else if (role == Role.MEMBER && !isModeratorLoggedIn) {
				row++;
			}
			if (loginId.equals(Credentials.getInstance().getLoginId())) {
				data.setState(LoginState.LOGGED_IN);
				data.setDisplayValue(loginId + "*");
				dataModel.setValueAt(data, row, 0);
			} else {
				data.setState(LoginState.LOGGED_IN);
				data.setDisplayValue(loginId);
				dataModel.setValueAt(data, row, 0);
			}
			num_participants_logged++;
		}

		if (Credentials.getInstance().getRole() == Role.MODERATOR) {
			int micAccessRow = dataModel.getRowAccessTo(1, Permission.ALLOW);
			if (micAccessRow != -1
				&& micAccessRow != ScoolConstants.MODERATOR_ROW) {
				resetAccessToMic();
			}
			int presentationInfoAccessRow =
				dataModel.getRowAccessTo(3, Permission.ALLOW);
			if (presentationInfoAccessRow != -1
				&& presentationInfoAccessRow != ScoolConstants.MODERATOR_ROW) {
				resetAccessToPresentationInfo();
			}
			int whiteBoardAccessRow =
				dataModel.getRowAccessTo(4, Permission.ALLOW);
			if (whiteBoardAccessRow != -1
				&& whiteBoardAccessRow != ScoolConstants.MODERATOR_ROW) {
				resetAccessToWhiteBoard();
			}
		}

		int micOption = Utility.getMicOption(toolString);
		int messageBoardOption = Utility.getMessageBoardOption(toolString);
		int presentationInfoOption =
			Utility.getPresentationInfoOption(toolString);
		int whiteBoardOption = Utility.getWhiteBoardOption(toolString);

		if (micOption == Permission.ALLOW_MIC) {
			dataModel.setValueAt(allowIcon, row, 1);
		} else if (micOption == Permission.DISALLOW_MIC) {
			dataModel.setValueAt(disAllowIcon, row, 1);
		}

		if (messageBoardOption == Permission.ALLOW_MESSAGEBOARD) {
			dataModel.setValueAt(allowIcon, row, 2);
		} else if (messageBoardOption == Permission.DISALLOW_MESSAGEBOARD) {
			dataModel.setValueAt(disAllowIcon, row, 2);
		}

		if (presentationInfoOption == Permission.ALLOW_PRESENTATIONINFO) {
			dataModel.setValueAt(allowIcon, row, 3);
		} else if (
			presentationInfoOption == Permission.DISALLOW_PRESENTATIONINFO) {
			dataModel.setValueAt(disAllowIcon, row, 3);
		}

		if (whiteBoardOption == Permission.ALLOW_WHITEBOARD) {
			dataModel.setValueAt(allowIcon, row, 4);
		} else if (whiteBoardOption == Permission.DISALLOW_WHITEBOARD) {
			dataModel.setValueAt(disAllowIcon, row, 4);
		}

		if (loginId.equals(Credentials.getInstance().getLoginId())) {
			checkPermissions(
				micOption,
				messageBoardOption,
				presentationInfoOption,
				whiteBoardOption);
		}
		System.out.println(
			"no of participants logged in:" + num_participants_logged);
	}

	public void logOffParticipant(String toolString) {
		System.out.println(
			"\n^^^^^^^^^^^Logging off participant..." + toolString);

		String loginId = Utility.getLoginId(toolString);
		int row = dataModel.getParticipantRow(loginId);

		Credentials credentials = Credentials.getInstance();
		int role = credentials.getRole();
		String participantLoginId = credentials.getLoginId();
		if (role == Role.MODERATOR) {
			ParticipantState participantState = ParticipantState.getInstance();
			int participantRow =
				dataModel.getParticipantRow(participantLoginId);

			int micOption = participantState.getMicOption();
			int messageBoardOption = participantState.getMessageBoardOption();
			int presentationInfoOption =
				participantState.getPresentatioInfoOption();
			int whiteBoardOption = participantState.getWhiteBoardOption();

			if (row == dataModel.getRowAccessTo(1, Permission.ALLOW)) {
				System.out.println(
					loginId
						+ " logged off and had access to Mic. Therefore resetting accessing to Mic.");
				resetAccessToMic();
				dataModel.setValueAt(allowIcon, participantRow, 1);
				micOption = Permission.ALLOW_MIC;
				String commandString =
					getModeratorToolString(
						micOption,
						messageBoardOption,
						presentationInfoOption,
						whiteBoardOption,
						participantLoginId);

				DataEvent.notifySender(
					ScoolConstants.PARTICIPANTS_INFO_APP,
					0,
					"",
					commandString);
			}
			if (row == dataModel.getRowAccessTo(3, Permission.ALLOW)) {
				System.out.println(
					loginId
						+ " logged off and had access to PresentationInfo. Therefore resetting accessing to PresentationInfo.");

				resetAccessToPresentationInfo();
				dataModel.setValueAt(allowIcon, participantRow, 3);
				presentationInfoOption = Permission.ALLOW_PRESENTATIONINFO;
				String commandString =
					getModeratorToolString(
						micOption,
						messageBoardOption,
						presentationInfoOption,
						whiteBoardOption,
						participantLoginId);

				DataEvent.notifySender(
					ScoolConstants.PARTICIPANTS_INFO_APP,
					0,
					"",
					commandString);
			}
			if (row == dataModel.getRowAccessTo(4, Permission.ALLOW)) {
				System.out.println(
					loginId
						+ " logged off and had access to WhiteBoard. Therefore resetting accessing to WhiteBoard.");

				resetAccessToWhiteBoard();
				dataModel.setValueAt(allowIcon, participantRow, 4);
				whiteBoardOption = Permission.ALLOW_WHITEBOARD;
				String commandString =
					getModeratorToolString(
						micOption,
						messageBoardOption,
						presentationInfoOption,
						whiteBoardOption,
						participantLoginId);

				DataEvent.notifySender(
					ScoolConstants.PARTICIPANTS_INFO_APP,
					0,
					"",
					commandString);
			}

			checkPermissions(
				micOption,
				messageBoardOption,
				presentationInfoOption,
				whiteBoardOption);
		}

		ParticipantsInfoUserData data =
			(ParticipantsInfoUserData) dataModel.getValueAt(row, 0);
		data.setState(LoginState.LOGGED_OUT);
		data.setDisplayValue(data.toString() + " logged off");

		System.out.println(loginId + " has logged out.");
		dataModel.setValueAt(data, row, 0);

		//num_participants_logged--;
	}

	public void changeState(String toolString) {

		System.out.println(toolString);

		int role = Utility.getRole(toolString);
		int row = -1;
		if (role == Role.MODERATOR) {
			row =
				dataModel.getParticipantRow(
					Utility.getParticipantLoginId(toolString));
		} else if (role == Role.MEMBER) {
			row = dataModel.getParticipantRow(Utility.getLoginId(toolString));
		}

		if (row != -1) {
			int micOption = Utility.getMicOption(toolString);
			int messageBoardOption = Utility.getMessageBoardOption(toolString);
			int presentationInfoOption =
				Utility.getPresentationInfoOption(toolString);
			int whiteBoardOption = Utility.getWhiteBoardOption(toolString);

			if (micOption == Permission.ALLOW_MIC) {
				dataModel.setValueAt(allowIcon, row, 1);
			} else if (micOption == Permission.DISALLOW_MIC) {
				dataModel.setValueAt(disAllowIcon, row, 1);
			} else if (micOption == Permission.SEEK_MIC) {
				dataModel.setValueAt(handRaiseIcon, row, 1);
			}

			if (messageBoardOption == Permission.ALLOW_MESSAGEBOARD) {
				dataModel.setValueAt(allowIcon, row, 2);
			} else if (
				messageBoardOption == Permission.DISALLOW_MESSAGEBOARD) {
				dataModel.setValueAt(disAllowIcon, row, 2);
			} else if (messageBoardOption == Permission.SEEK_MESSAGEBOARD) {
				dataModel.setValueAt(handRaiseIcon, row, 2);
			}

			if (presentationInfoOption == Permission.ALLOW_PRESENTATIONINFO) {
				dataModel.setValueAt(allowIcon, row, 3);

				int participantRow =
					dataModel.getParticipantRow(
						Credentials.getInstance().getLoginId());

				ParticipantsInfoUserData data =
					(ParticipantsInfoUserData) dataModel.getValueAt(
						participantRow,
						0);

				if (row == participantRow
					&& data.getState() != LoginState.LOGGED_OUT) {
					notifySelectedPage();
				}
			} else if (
				presentationInfoOption
					== Permission.DISALLOW_PRESENTATIONINFO) {
				dataModel.setValueAt(disAllowIcon, row, 3);
			} else if (
				presentationInfoOption == Permission.SEEK_PRESENTATIONINFO) {
				dataModel.setValueAt(handRaiseIcon, row, 3);
			}

			if (whiteBoardOption == Permission.ALLOW_WHITEBOARD) {
				dataModel.setValueAt(allowIcon, row, 4);
			} else if (whiteBoardOption == Permission.DISALLOW_WHITEBOARD) {
				dataModel.setValueAt(disAllowIcon, row, 4);
			} else if (whiteBoardOption == Permission.SEEK_WHITEBOARD) {
				dataModel.setValueAt(handRaiseIcon, row, 4);
			}

			if (role == Role.MODERATOR
				&& Utility.getParticipantLoginId(toolString).equals(
					Credentials.getInstance().getLoginId())) {

				checkPermissions(
					micOption,
					messageBoardOption,
					presentationInfoOption,
					whiteBoardOption);
			}
		}
	}

	private void notifySelectedPage() {

		PresentationInfo presentationInfo =
			frame
				.getWhiteBoardPanel()
				.getWhiteBoardDesktopPane()
				.getPresentationInfo();
		presentationInfo.setSynWithSession(true);

		WhiteBoardCanvas canvas =
			frame
				.getWhiteBoardPanel()
				.getWhiteBoardDesktopPane()
				.getWhiteBoard()
				.getCanvas();

		DataEvent.notifySender(
			canvas.getApplicationType(),
			canvas.getCurrentPageNo(),
			canvas.getCurrentPageName(),
			presentationInfo.getToolString());

	}

	private void checkPermissions(
		int micOption,
		int messageBoardOption,
		int presentationInfoOption,
		int whiteBoardOption) {

		ParticipantState participantState = ParticipantState.getInstance();
		synchronized (participantState) {

			PermissionManager permissionManager = new PermissionManager(frame);

			if (micOption != participantState.getMicOption()) {
				if (micOption == Permission.ALLOW_MIC) {
					//permissionManager.allowMic();
				} else if (micOption == Permission.DISALLOW_MIC) {
					permissionManager.disAllowMic();
				}
				participantState.setMicOption(micOption);
				participantState.notifyAll();
			}

			if (messageBoardOption
				!= participantState.getMessageBoardOption()) {

				if (messageBoardOption == Permission.ALLOW_MESSAGEBOARD) {
					permissionManager.allowMessageBoard();
				} else if (
					messageBoardOption == Permission.DISALLOW_MESSAGEBOARD) {
					permissionManager.disAllowMessageBoard();
				}
				participantState.setMessagBoardOption(messageBoardOption);
			}

			if (presentationInfoOption
				!= participantState.getPresentatioInfoOption()) {

				if (presentationInfoOption
					== Permission.ALLOW_PRESENTATIONINFO) {
					permissionManager.allowPresentationInfo();
				} else if (
					presentationInfoOption
						== Permission.DISALLOW_MESSAGEBOARD) {
					permissionManager.disAllowPresentationInfo();
				}
				participantState.setPresentationInfoOption(
					presentationInfoOption);
			}

			if (whiteBoardOption != participantState.getWhiteBoardOption()) {

				if (whiteBoardOption == Permission.ALLOW_WHITEBOARD) {
					permissionManager.allowWhiteBoard();

				} else if (
					whiteBoardOption == Permission.DISALLOW_WHITEBOARD) {
					permissionManager.disAllowWhiteBoard();
				}
				participantState.setWhiteBoardOption(whiteBoardOption);

			}

		}

	}

	private String getParticipantToolString() {
		try {
			int role = Credentials.getInstance().getRole();
			ParticipantState participantState = ParticipantState.getInstance();
			String participantToolString =
				Utility.convertTo4Byte(
					ScoolConstants.PARTICIPANT_STATE_CHANGED_ACTION)
					+ Utility.convertTo16Byte(
						Credentials.getInstance().getLoginId())
					+ Utility.convertTo2Byte(role)
					+ participantState.getMicOption()
					+ participantState.getMessageBoardOption()
					+ participantState.getPresentatioInfoOption()
					+ participantState.getWhiteBoardOption();
			return participantToolString;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	private String getModeratorToolString(
		int micOption,
		int messageBoardOption,
		int presentatioInfoOption,
		int whiteBoardOption,
		String participantLoginId) {
		try {
			int role = Credentials.getInstance().getRole();
			String moderatorToolString =
				Utility.convertTo4Byte(
					ScoolConstants.MODERATOR_CHANGED_STATE_ACTION)
					+ Utility.convertTo16Byte(
						Credentials.getInstance().getLoginId())
					+ Utility.convertTo2Byte(role)
					+ micOption
					+ messageBoardOption
					+ presentatioInfoOption
					+ whiteBoardOption
					+ Utility.convertTo16Byte(participantLoginId);
			return moderatorToolString;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	private void resetAccessToMic() {
		int row = dataModel.getRowAccessTo(1, Permission.ALLOW);

		if (row != -1) {
			dataModel.setValueAt(disAllowIcon, row, 1);

			int micOption = Permission.DISALLOW_MIC;
			int messageBoardOption = 0;
			int presentationInfoOption = 0;
			int whiteBoardOption = 0;

			int participantRow =
				dataModel.getParticipantRow(
					Credentials.getInstance().getLoginId());
			ParticipantState participantState = ParticipantState.getInstance();
			if (row == participantRow) {
				checkPermissions(
					Permission.DISALLOW_MIC,
					participantState.getMessageBoardOption(),
					participantState.getPresentatioInfoOption(),
					participantState.getWhiteBoardOption());
			}

			ImageIcon imageIcon = null;
			String imageDesc = null;

			imageIcon = (ImageIcon) table.getValueAt(row, 2);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				messageBoardOption = Permission.DISALLOW_MESSAGEBOARD;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				messageBoardOption = Permission.ALLOW_MESSAGEBOARD;
			} else if (imageDesc.equals(Permission.SEEK)) {
				messageBoardOption = Permission.SEEK_MESSAGEBOARD;
			}

			imageIcon = (ImageIcon) table.getValueAt(row, 3);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				presentationInfoOption = Permission.DISALLOW_PRESENTATIONINFO;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				presentationInfoOption = Permission.ALLOW_PRESENTATIONINFO;
			} else if (imageDesc.equals(Permission.SEEK)) {
				presentationInfoOption = Permission.SEEK_PRESENTATIONINFO;
			}

			imageIcon = (ImageIcon) table.getValueAt(row, 4);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				whiteBoardOption = Permission.DISALLOW_WHITEBOARD;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				whiteBoardOption = Permission.ALLOW_WHITEBOARD;
			} else if (imageDesc.equals(Permission.SEEK)) {
				whiteBoardOption = Permission.SEEK_WHITEBOARD;
			}

			ParticipantsInfoUserData data =
				(ParticipantsInfoUserData) dataModel.getValueAt(row, 0);

			String participantLoginId = data.getLoginId();

			String commandString =
				getModeratorToolString(
					micOption,
					messageBoardOption,
					presentationInfoOption,
					whiteBoardOption,
					participantLoginId);

			DataEvent.notifySender(
				ScoolConstants.PARTICIPANTS_INFO_APP,
				0,
				"",
				commandString);
			System.out.println(
				participantLoginId + " Mic resetted." + commandString);
		}
	}

	private void resetAccessToPresentationInfo() {
		int row = dataModel.getRowAccessTo(3, Permission.ALLOW);

		if (row != -1) {

			dataModel.setValueAt(disAllowIcon, row, 3);

			int micOption = 0;
			int messageBoardOption = 0;
			int presentationInfoOption = Permission.DISALLOW_PRESENTATIONINFO;
			int whiteBoardOption = 0;

			int participantRow =
				dataModel.getParticipantRow(
					Credentials.getInstance().getLoginId());
			ParticipantState participantState = ParticipantState.getInstance();
			if (row == participantRow) {
				checkPermissions(
					participantState.getMicOption(),
					participantState.getMessageBoardOption(),
					presentationInfoOption,
					participantState.getWhiteBoardOption());
			}

			ImageIcon imageIcon = null;
			String imageDesc = null;

			imageIcon = (ImageIcon) table.getValueAt(row, 1);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				micOption = Permission.DISALLOW_MIC;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				micOption = Permission.ALLOW_MIC;
			} else if (imageDesc.equals(Permission.SEEK)) {
				micOption = Permission.SEEK_MIC;
			}

			imageIcon = (ImageIcon) table.getValueAt(row, 2);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				messageBoardOption = Permission.DISALLOW_MESSAGEBOARD;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				messageBoardOption = Permission.ALLOW_MESSAGEBOARD;
			} else if (imageDesc.equals(Permission.SEEK)) {
				messageBoardOption = Permission.SEEK_MESSAGEBOARD;
			}

			imageIcon = (ImageIcon) table.getValueAt(row, 4);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				whiteBoardOption = Permission.DISALLOW_WHITEBOARD;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				whiteBoardOption = Permission.ALLOW_WHITEBOARD;
			} else if (imageDesc.equals(Permission.SEEK)) {
				whiteBoardOption = Permission.SEEK_WHITEBOARD;
			}

			ParticipantsInfoUserData data =
				(ParticipantsInfoUserData) dataModel.getValueAt(row, 0);

			String participantLoginId = data.getLoginId();

			String commandString =
				getModeratorToolString(
					micOption,
					messageBoardOption,
					presentationInfoOption,
					whiteBoardOption,
					participantLoginId);

			DataEvent.notifySender(
				ScoolConstants.PARTICIPANTS_INFO_APP,
				0,
				"",
				commandString);
			System.out.println(
				participantLoginId
					+ " PresentationInfo resetted."
					+ commandString);
		}
	}

	private void resetAccessToWhiteBoard() {
		int row = dataModel.getRowAccessTo(4, Permission.ALLOW);

		if (row != -1) {

			dataModel.setValueAt(disAllowIcon, row, 4);

			int micOption = 0;
			int messageBoardOption = 0;
			int presentationInfoOption = 0;
			int whiteBoardOption = Permission.DISALLOW_WHITEBOARD;

			int participantRow =
				dataModel.getParticipantRow(
					Credentials.getInstance().getLoginId());
			ParticipantState participantState = ParticipantState.getInstance();
			if (row == participantRow) {
				checkPermissions(
					participantState.getMicOption(),
					participantState.getMessageBoardOption(),
					participantState.getPresentatioInfoOption(),
					whiteBoardOption);
			}

			ImageIcon imageIcon = null;
			String imageDesc = null;

			imageIcon = (ImageIcon) table.getValueAt(row, 1);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				micOption = Permission.DISALLOW_MIC;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				micOption = Permission.ALLOW_MIC;
			} else if (imageDesc.equals(Permission.SEEK)) {
				micOption = Permission.SEEK_MIC;
			}

			imageIcon = (ImageIcon) table.getValueAt(row, 2);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				messageBoardOption = Permission.DISALLOW_MESSAGEBOARD;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				messageBoardOption = Permission.ALLOW_MESSAGEBOARD;
			} else if (imageDesc.equals(Permission.SEEK)) {
				messageBoardOption = Permission.SEEK_MESSAGEBOARD;
			}

			imageIcon = (ImageIcon) table.getValueAt(row, 3);
			imageDesc = imageIcon.getDescription();
			if (imageDesc.equals(Permission.DISALLOW)) {
				presentationInfoOption = Permission.DISALLOW_PRESENTATIONINFO;
			} else if (imageDesc.equals(Permission.ALLOW)) {
				presentationInfoOption = Permission.ALLOW_PRESENTATIONINFO;
			} else if (imageDesc.equals(Permission.SEEK)) {
				presentationInfoOption = Permission.SEEK_PRESENTATIONINFO;
			}

			ParticipantsInfoUserData data =
				(ParticipantsInfoUserData) dataModel.getValueAt(row, 0);

			String participantLoginId = data.getLoginId();

			String commandString =
				getModeratorToolString(
					micOption,
					messageBoardOption,
					presentationInfoOption,
					whiteBoardOption,
					participantLoginId);

			DataEvent.notifySender(
				ScoolConstants.PARTICIPANTS_INFO_APP,
				0,
				"",
				commandString);
			System.out.println(
				participantLoginId + " WhiteBoard resetted." + commandString);
		}
	}

}

class HeaderIcon {
	Icon icon;
	HeaderIcon(Icon icon) {
		this.icon = icon;
	}
}