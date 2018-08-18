package projects.ecommunicator.whiteboard;

/*
 * WhiteboardCanvas is the essence of the Whiteboard applet, it allows user to drawOffScreen, 
 * type, and handles most all events.  It is a child of the Canvas class.
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import projects.ecommunicator.actions.CopyAction;
import projects.ecommunicator.actions.CutAction;
import projects.ecommunicator.actions.DeleteAction;
import projects.ecommunicator.actions.PasteAction;
import projects.ecommunicator.application.DataEvent;
import projects.ecommunicator.internalframes.WhiteBoard;
import projects.ecommunicator.poll.ResultPanel;
import projects.ecommunicator.tools.CircleTool;
import projects.ecommunicator.tools.EditTool;
import projects.ecommunicator.tools.FreeHandTool;
import projects.ecommunicator.tools.LineTool;
import projects.ecommunicator.tools.PPtImageTool;
import projects.ecommunicator.tools.RectTool;
import projects.ecommunicator.tools.RoundRectTool;
import projects.ecommunicator.tools.ScreenShotImageTool;
import projects.ecommunicator.tools.SelectTool;
import projects.ecommunicator.tools.SnapGraphImageTool;
import projects.ecommunicator.tools.TextTool;
import projects.ecommunicator.utility.LoadPage;
import projects.ecommunicator.utility.NewPage;
import projects.ecommunicator.utility.OffScreenBuffer;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.ToolFactory;
import projects.ecommunicator.utility.Utility;

public class WhiteBoardCanvas
	extends JPanel
	implements MouseListener, MouseMotionListener, KeyListener {

	private int drawTool;
	private SelectTool selectTool;
	private CircleTool circleTool;
	private LineTool lineTool;
	private RectTool rectTool;
	private FreeHandTool freeHandTool;
	private RoundRectTool roundRectTool;
	private ScreenShotImageTool screenShotImageTool;
	private SnapGraphImageTool snapGraphImageTool;
	private TextTool textTool;

	private BufferedImage offScreen;
	private Graphics offgc;

	private Color color;
	private BasicStroke stroke;
	private String fontNameId = "00";
	private int fontStyle = Font.PLAIN;
	private int fontSize = 12;

	private int wbPageNo;
	private int pptPageNo;
	private int screenShotPageNo;
	private int pollPageNo;
	private Hashtable pages;
	private Vector page;

	private int currentPageNo = 1;
	private String currentPageName = ScoolConstants.DEFAULT_WHITEBOARD_TITLE;
	private int applicationType;

	private JPopupMenu popupMenu;
	private String editToolToolString;
	private int editToolEvtX;
	private int editToolEvtY;
	//private Rectangle selectedBounds;

	//private PresentationInfo presentationInfo;
	//private String key;
	
	private ResultPanel resultPanel;
	private WhiteBoard whiteBoard;

	public WhiteBoardCanvas(WhiteBoard whiteBoard) {
		
		this.whiteBoard = whiteBoard;
		setLayout(new BorderLayout());
		setBackground(Color.white);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);

		popupMenu = new JPopupMenu("Edit Tools");

		JMenuItem cutMI = new JMenuItem("Cut");
		cutMI.addActionListener(new CutAction(this));
		popupMenu.add(cutMI);

		JMenuItem copyMI = new JMenuItem("Copy");
		copyMI.addActionListener(new CopyAction(this));
		popupMenu.add(copyMI);

		JMenuItem pasteMI = new JMenuItem("Paste");
		pasteMI.addActionListener(new PasteAction(this));
		popupMenu.add(pasteMI);

		JMenuItem deleteMI = new JMenuItem("Delete");
		deleteMI.addActionListener(new DeleteAction(this));
		popupMenu.add(deleteMI);

		init();
	}

	private void init() {
		color = Color.BLACK;
		stroke = new BasicStroke(2.0f);
		pages = new Hashtable(10);
		NewPage newPage = new NewPage(this);
		newPage.createNewWbPage();
	}

	public void setPage(Vector page) {
		this.page = page;
	}

	public Hashtable getPages() {
		return pages;
	}

	public void addPage(String key, Vector page) {
		pages.put(key, page);
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}	

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	

	public String getCurrentPageName() {
		return currentPageName;
	}

	public void setCurrentPageName(String currentPageName) {
		this.currentPageName = currentPageName;
	}

	public int getWbPageNo() {
		return wbPageNo;
	}

	public void setWbPageNo(int wbPageNo) {
		this.wbPageNo = wbPageNo;
	}

	public int getPPtPageNo() {
		return pptPageNo;
	}

	public void setPPtPageNo(int pptPageNo) {
		this.pptPageNo = pptPageNo;
	}

	public int getPollPageNo() {
		return pollPageNo;
	}

	public void setPollPageNo(int pollPageNo){
		this.pollPageNo = pollPageNo;
	}
	public int getScreenShotPageNo() {
		return screenShotPageNo;
	}

	public void setScreenShotPageNo(int screenShotPageNo) {
		this.screenShotPageNo = screenShotPageNo;
	}

	public int getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(int applicationType) {
		this.applicationType = applicationType;
	}

	public int getDrawTool() {
		return drawTool;
	}

	public void setDrawTool(int drawTool) {
		this.drawTool = drawTool;
		if (this.drawTool == ScoolConstants.TEXT_TOOL) {
			requestFocusInWindow(true);
		} else {
			textTool = null;
		}

		if (this.drawTool != ScoolConstants.SELECT_TOOL) {
			selectTool = null;
		}
	}

	public void setOffScreen(BufferedImage offScreen) {
		if (this.offScreen == null) {
			this.offScreen = offScreen;
		} else {
			synchronized (this.offScreen) {
				this.offScreen = offScreen;
			}
		}
	}

	public void setOffScreenGraphics(Graphics offgc) {
		if (this.offgc == null) {
			this.offgc = offgc;
		} else {
			synchronized (this.offgc) {
				this.offgc = offgc;
			}
		}
	}

	public void setBasicStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setFontNameId(String fontNameId) {
		this.fontNameId = fontNameId;
		if (textTool != null) {
			textTool.setFontNameId(fontNameId);
		}
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		if (textTool != null) {
			textTool.setFontSize(fontSize);
		}
	}

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
		if (textTool != null) {
			textTool.setFontStyle(fontStyle);
		}
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public void mousePressed(MouseEvent evt) {
		if (isEnabled()) {

			switch (drawTool) {

				case ScoolConstants.SELECT_TOOL :
					{
						if (selectTool == null) {
							selectTool =
								new SelectTool(this, evt.getX(), evt.getY());
						}
						selectTool.setPage(page);
						selectTool.markPoints(evt.getX(), evt.getY());
						selectTool.markSelectedTool();
						//if (selectTool.isToolSelected()
						//&& selectTool.getSelectedToolId() != -1) {

						OffScreenBuffer offScBuffer =
							new OffScreenBuffer(
								getBackground(),
								getForeground());
						offScBuffer.createOffScreenBuffer();

						setOffScreen(offScBuffer.getOffScreen());
						setOffScreenGraphics(
							offScBuffer.getOffScreenGraphics());
						//}
						repaint();
						if (selectTool.getSelectedToolId() != -1) {
							setCursor(
								Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
						} else {
							setCursor(
								Cursor.getPredefinedCursor(
									Cursor.DEFAULT_CURSOR));
						}
						break;
					}

				case ScoolConstants.CIRCLE_TOOL :
					{
						circleTool = new CircleTool();
						circleTool.color = color;
						circleTool.stroke = stroke;
						circleTool.startX = evt.getX();
						circleTool.startY = evt.getY();
						break;
					}

				case ScoolConstants.LINE_TOOL :
					{
						lineTool = new LineTool();
						lineTool.color = color;
						lineTool.stroke = stroke;
						lineTool.startX = evt.getX();
						lineTool.startY = evt.getY();
						break;
					}

				case ScoolConstants.RECT_TOOL :
					{
						rectTool = new RectTool();
						rectTool.color = color;
						rectTool.stroke = stroke;
						rectTool.startX = evt.getX();
						rectTool.startY = evt.getY();
						break;
					}

				case ScoolConstants.ROUND_RECT_TOOL :
					{
						roundRectTool = new RoundRectTool();
						roundRectTool.color = color;
						roundRectTool.stroke = stroke;
						roundRectTool.startX = evt.getX();
						roundRectTool.startY = evt.getY();
						break;
					}

				case ScoolConstants.FREE_HAND_TOOL :
					{
						freeHandTool = new FreeHandTool();
						freeHandTool.setPoints(new ArrayList());
						freeHandTool.color = color;
						freeHandTool.stroke = stroke;
						freeHandTool.startX = freeHandTool.endX = evt.getX();
						freeHandTool.startY = freeHandTool.endY = evt.getY();
						break;
					}

			}

		}

	}

	public void mouseDragged(MouseEvent evt) {

		if (isEnabled()) {
			switch (drawTool) {

				case ScoolConstants.SELECT_TOOL :
					{
						if (selectTool != null
							&& selectTool.getSelectedToolId() != -1) {
							selectTool.mouseDragged(evt.getX(), evt.getY());
							repaint();
						}
						break;
					}
				case ScoolConstants.CIRCLE_TOOL :
					{
						circleTool.color = Color.LIGHT_GRAY;
						circleTool.mouseDragged(evt.getX(), evt.getY());
						repaint();
						break;
					}

				case ScoolConstants.LINE_TOOL :
					{
						lineTool.color = Color.LIGHT_GRAY;
						lineTool.mouseDragged(evt.getX(), evt.getY());
						repaint();
						break;
					}

				case ScoolConstants.RECT_TOOL :
					{
						rectTool.color = Color.LIGHT_GRAY;
						rectTool.mouseDragged(evt.getX(), evt.getY());
						repaint();
						break;
					}

				case ScoolConstants.ROUND_RECT_TOOL :
					{
						roundRectTool.color = Color.LIGHT_GRAY;
						roundRectTool.mouseDragged(evt.getX(), evt.getY());
						repaint();
						break;
					}

				case ScoolConstants.FREE_HAND_TOOL :
					{
						freeHandTool.mouseDragged(evt.getX(), evt.getY());
						repaint();
						drawOffScreen();
						break;
					}

			}
		}

	}

	public void mouseReleased(MouseEvent evt) {

		if (isEnabled()) {

			if (evt.isPopupTrigger()) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				editToolEvtX = evt.getX();
				editToolEvtY = evt.getY();
				popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
			} else {

				switch (drawTool) {

					case ScoolConstants.SELECT_TOOL :
						{
							repaint();
							drawOffScreen();
							if (selectTool != null
								&& selectTool.getSelectedToolId() != -1) {
								selectTool.mouseReleased(
									evt.getX(),
									evt.getY());

								String commandString =
									selectTool.getToolString();
								DataEvent.notifySender(
									applicationType,
									currentPageNo,
									currentPageName,
									commandString);

								selectTool.setSelectedToolId(-1);
								//selectTool = null;
								setCursor(
									Cursor.getPredefinedCursor(
										Cursor.DEFAULT_CURSOR));
							}

							break;
						}

					case ScoolConstants.CIRCLE_TOOL :
						{
							circleTool.color = color;
							circleTool.mouseReleased(evt.getX(), evt.getY());
							repaint();
							drawOffScreen();

							String commandString = circleTool.getToolString();
							page.add(commandString);
							DataEvent.notifySender(
								applicationType,
								currentPageNo,
								currentPageName,
								commandString);

							break;
						}

					case ScoolConstants.LINE_TOOL :
						{
							lineTool.color = color;
							lineTool.mouseReleased(evt.getX(), evt.getY());
							repaint();
							drawOffScreen();

							String commandString = lineTool.getToolString();
							page.add(commandString);
							DataEvent.notifySender(
								applicationType,
								currentPageNo,
								currentPageName,
								commandString);
							break;
						}

					case ScoolConstants.RECT_TOOL :
						{
							rectTool.color = color;
							rectTool.mouseReleased(evt.getX(), evt.getY());
							repaint();
							drawOffScreen();

							String commandString = rectTool.getToolString();
							page.add(commandString);

							DataEvent.notifySender(
								applicationType,
								currentPageNo,
								currentPageName,
								commandString);
							break;
						}

					case ScoolConstants.ROUND_RECT_TOOL :
						{
							roundRectTool.color = color;
							roundRectTool.mouseReleased(evt.getX(), evt.getY());
							repaint();
							drawOffScreen();

							String commandString =
								roundRectTool.getToolString();
							page.add(commandString);
							DataEvent.notifySender(
								applicationType,
								currentPageNo,
								currentPageName,
								commandString);
							break;
						}

					case ScoolConstants.FREE_HAND_TOOL :
						{
							freeHandTool.color = color;
							freeHandTool.mouseReleased(evt.getX(), evt.getY());
							repaint();
							drawOffScreen();
							String commandString = freeHandTool.getToolString();
							page.add(commandString);
							DataEvent.notifySender(
								applicationType,
								currentPageNo,
								currentPageName,
								commandString);
							break;
						}

					case ScoolConstants.SCREEN_SHOT_TOOL :
						{
							screenShotImageTool.startX = evt.getX();
							screenShotImageTool.startY = evt.getY();
							repaint();
							drawOffScreen();

							String commandString =
								screenShotImageTool.getToolString();
							page.add(commandString);
							DataEvent.notifySender(
								applicationType,
								currentPageNo,
								currentPageName,
								commandString);
							//presentationInfo.getPresentationList().setSelectedValue(key, true);
							break;
						}

					case ScoolConstants.SNAP_GRAPH_SHOT_TOOL :
						{
							snapGraphImageTool.startX = evt.getX();
							snapGraphImageTool.startY = evt.getY();
							repaint();
							drawOffScreen();

							String commandString =
								snapGraphImageTool.getToolString();
							page.add(commandString);
							DataEvent.notifySender(
								applicationType,
								currentPageNo,
								currentPageName,
								commandString);
							break;
						}
				}
			}

		}

	}

	public void mouseMoved(MouseEvent evt) {

		if (isEnabled()) {
			switch (drawTool) {

				case ScoolConstants.SCREEN_SHOT_TOOL :
					{
						//System.out.println("moused moved");
						screenShotImageTool.startX = evt.getX();
						screenShotImageTool.startY = evt.getY();
						repaint();
						break;
					}

				case ScoolConstants.SNAP_GRAPH_SHOT_TOOL :
					{
						//System.out.println("moused moved");
						snapGraphImageTool.startX = evt.getX();
						snapGraphImageTool.startY = evt.getY();
						repaint();
						break;
					}
			}
		}

	}

	public void mouseEntered(MouseEvent evt) {

	}
	public void mouseExited(MouseEvent evt) {

	}
	public void mouseClicked(MouseEvent evt) {
		if (isEnabled()) {
			editToolEvtX = evt.getX();
			editToolEvtY = evt.getY();

			switch (drawTool) {

				case ScoolConstants.TEXT_TOOL :
					{
						if (textTool != null) {
							drawOffScreen();
							textTool.setTextEntered(true);
							repaint();

							String commandString = textTool.getToolString();
							page.add(commandString);
							DataEvent.notifySender(
								applicationType,
								currentPageNo,
								currentPageName,
								commandString);

							textTool.color = color;
							textTool.setText(new StringBuffer());
							textTool.setOrigin(
								(float) evt.getX(),
								(float) evt.getY());
							textTool.setTextEntered(false);
							textTool.resetInsertionIndex();
							repaint();

						} else {

							textTool = new TextTool();
							textTool.color = color;
							textTool.stroke = stroke;
							textTool.setOrigin(
								(float) evt.getX(),
								(float) evt.getY());
							textTool.setFontNameId(fontNameId);
							textTool.setFontSize(fontSize);
							textTool.setFontStyle(fontStyle);
							repaint();
						}
						break;
					}
			}

		}
	}

	public void keyPressed(KeyEvent evt) {

		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			drawOffScreen();
			textTool.setTextEntered(true);
			repaint();

			if (textTool.getText().length() > 0) {
				String commandString = textTool.getToolString();
				page.add(commandString);
				DataEvent.notifySender(
					applicationType,
					currentPageNo,
					currentPageName,
					commandString);
			}

			textTool.color = color;
			textTool.setText(new StringBuffer());
			textTool.computeNextOrigin();
			textTool.setTextEntered(false);
			textTool.resetInsertionIndex();
			repaint();

		} else {
			textTool.keyPressed(evt);
			repaint();
		}

	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}

	public void screenShotEvent(BufferedImage capturedImage) {
		screenShotImageTool = new ScreenShotImageTool();
		screenShotImageTool.bufferedImage = capturedImage;
		//this.presentationInfo = presentationInfo;
		//this.key = key;
	}

	public void snapGraphEvent(BufferedImage capturedImage) {
		snapGraphImageTool = new SnapGraphImageTool();
		snapGraphImageTool.bufferedImage = capturedImage;
	}

	public void cutAction() {
		if (selectTool != null) {
			int selectedToolPos = selectTool.getSelectedToolPos();
			if (selectedToolPos != -1) {
				editToolToolString = (String) page.get(selectedToolPos);
				page.remove(selectTool.getSelectedToolPos());
				LoadPage loadPage = new LoadPage(this, getCurrentPageName());
				loadPage.run();

				String commandString =
					EditTool.getCutActionToolString(selectedToolPos);
				DataEvent.notifySender(
					applicationType,
					currentPageNo,
					currentPageName,
					commandString);
			}
		}
	}

	public void deleteAction() {
		if (selectTool != null) {
			int selectedToolPos = selectTool.getSelectedToolPos();
			if (selectedToolPos != -1) {
				page.remove(selectTool.getSelectedToolPos());
				LoadPage loadPage = new LoadPage(this, getCurrentPageName());
				loadPage.run();

				String commandString =
					EditTool.getCutActionToolString(selectedToolPos);
				DataEvent.notifySender(
					applicationType,
					currentPageNo,
					currentPageName,
					commandString);
			}
		}
	}

	public void copyAction() {
		if (selectTool != null) {
			int selectedToolPos = selectTool.getSelectedToolPos();
			if (selectedToolPos != -1) {
				editToolToolString = (String) page.get(selectedToolPos);
			}
		}
	}

	public void pasteAction() {
		if (editToolToolString != null) {
			String translatedToolString =
				moveOrigin(editToolToolString, editToolEvtX, editToolEvtY);
			page.add(translatedToolString);
			int selectedToolPos = page.size() - 1;
			LoadPage loadPage = new LoadPage(this, getCurrentPageName());
			loadPage.run();

			String commandString =
				EditTool.getPasteActionToolString(
					selectedToolPos,
					translatedToolString);
			DataEvent.notifySender(
				applicationType,
				currentPageNo,
				currentPageName,
				commandString);
		}
	}

	private String moveOrigin(String toolString, int x, int y) {

		int toolType = Integer.parseInt(toolString.substring(0, 4));
		StringBuffer toolStringBuffer = new StringBuffer(toolString);
		switch (toolType) {

			case ScoolConstants.FREE_HAND_TOOL :
				{

					int minStartX =
						Integer.parseInt(toolString.substring(4, 8));
					int minStartY =
						Integer.parseInt(toolString.substring(8, 12));
					Point prevPoint = new Point(minStartX, minStartY);
					minStartX = minStartX + (x - prevPoint.x);
					minStartY = minStartY + (y - prevPoint.y);

					if (minStartX > 0 && minStartY > 0) {

						// get points from toolstring
						int index = 20;
						int size =
							Integer.parseInt(
								toolString.substring(index, index += 8));
						String points =
							toolString.substring(index, index + size);

						// shift new points
						StringBuffer newPoints = new StringBuffer("");
						for (int i = 0; i < points.length(); i += 8) {
							int pointX =
								Integer.parseInt(points.substring(i, i + 4))
									+ (x - prevPoint.x);
							int pointY =
								Integer.parseInt(
									points.substring(i + 4, i + 8))
									+ (y - prevPoint.y);
							newPoints.append(Utility.convertTo4Byte(pointX));
							newPoints.append(Utility.convertTo4Byte(pointY));
						}

						toolStringBuffer.replace(
							4,
							12,
							Utility.convertTo4Byte(minStartX)
								+ Utility.convertTo4Byte(minStartY));

						toolStringBuffer.replace(
							index,
							index + size,
							newPoints.toString());
					}

					prevPoint.x = x;
					prevPoint.y = y;

					break;
				}
			case ScoolConstants.LINE_TOOL :
				{

					int startX = Integer.parseInt(toolString.substring(4, 8));
					int startY = Integer.parseInt(toolString.substring(8, 12));
					int endX = Integer.parseInt(toolString.substring(12, 16));
					int endY = Integer.parseInt(toolString.substring(16, 20));

					int boundsX;
					int boundsY;
					int width;
					int height;
					if (startX < endX && startY < endY) {
						boundsX = startX;
						boundsY = startY;
						width = endX - startX;
						height = endY - startY;

						Point prevPoint = new Point(boundsX, boundsY);
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);

						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY;
							endX = startX + width;
							endY = startY + height;
						}
					} else if (startX > endX && startY > endY) {
						boundsX = endX;
						boundsY = endY;
						width = startX - endX;
						height = startY - endY;

						Point prevPoint = new Point(boundsX, boundsY);
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);

						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX + width;
							startY = boundsY + height;
							endX = boundsX;
							endY = boundsY;
						}

					} else if (startX > endX && startY < endY) {
						boundsX = endX;
						boundsY = startY;
						width = startX - endX;
						height = endY - startY;

						Point prevPoint = new Point(boundsX, boundsY);
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);

						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX + width;
							startY = boundsY;
							endX = boundsX;
							endY = boundsY + height;
						}
					} else if (startX < endX && startY > endY) {
						boundsX = startX;
						boundsY = endY;
						width = endX - startX;
						height = startY - endY;

						Point prevPoint = new Point(boundsX, boundsY);
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);

						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY + height;
							endX = boundsX + width;
							endY = boundsY;
						}

					} else if (startX == endX && startY < endY) {
						boundsX = startX;
						boundsY = startY;
						width = 0;
						height = endY - startY;

						Point prevPoint = new Point(boundsX, boundsY);
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);

						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY;
							endX = boundsX;
							endY = boundsY + height;
						}
					} else if (startX == endX && startY > endY) {
						boundsX = startX;
						boundsY = endY;
						width = 0;
						height = startY - endY;

						Point prevPoint = new Point(boundsX, boundsY);
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);

						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY + height;
							endX = boundsX;
							endY = boundsY;
						}

					} else if (startX < endX && startY == endY) {
						boundsX = startX;
						boundsY = startY;
						width = endX - startX;
						height = 0;

						Point prevPoint = new Point(boundsX, boundsY);
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);

						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX;
							startY = boundsY;
							endX = boundsX + width;
							endY = boundsY;
						}
					} else if (startX > endX && startY == endY) {
						boundsX = endX;
						boundsY = startY;
						width = startX - endX;
						height = 0;

						Point prevPoint = new Point(boundsX, boundsY);
						boundsX = boundsX + (x - prevPoint.x);
						boundsY = boundsY + (y - prevPoint.y);

						if (boundsX > 0 && boundsY > 0) {
							startX = boundsX + width;
							startY = boundsY;
							endX = boundsX;
							endY = boundsY;
						}

					}

					if (startX > 0 && startY > 0) {

						toolStringBuffer.replace(
							4,
							12,
							Utility.convertTo4Byte(startX)
								+ Utility.convertTo4Byte(startY));

						toolStringBuffer.replace(
							12,
							20,
							Utility.convertTo4Byte(endX)
								+ Utility.convertTo4Byte(endY));
					}
					break;
				}

			case ScoolConstants.TEXT_TOOL :
				{
					int originX = Integer.parseInt(toolString.substring(4, 8));
					int originY = Integer.parseInt(toolString.substring(8, 12));
					int startX = Integer.parseInt(toolString.substring(12, 16));
					int startY = Integer.parseInt(toolString.substring(16, 20));
					//int width = Integer.parseInt(toolString.substring(20, 24));
					int height = Integer.parseInt(toolString.substring(24, 28));

					Point prevPoint = new Point(startX, startY);
					startX = startX + (x - prevPoint.x);
					startY = startY + (y - prevPoint.y);

					originX = startX + 4;
					originY = startY + height / 2;

					toolStringBuffer.replace(
						4,
						20,
						Utility.convertTo4Byte(originX)
							+ Utility.convertTo4Byte(originY)
							+ Utility.convertTo4Byte(startX)
							+ Utility.convertTo4Byte(startY));

					prevPoint.x = x;
					prevPoint.y = y;
					break;
				}
			default :
				{
					int startX = Integer.parseInt(toolString.substring(4, 8));
					int startY = Integer.parseInt(toolString.substring(8, 12));

					Point prevPoint = new Point(startX, startY);
					startX = startX + (x - prevPoint.x);
					startY = startY + (y - prevPoint.y);

					toolStringBuffer.replace(
						4,
						12,
						Utility.convertTo4Byte(startX)
							+ Utility.convertTo4Byte(startY));
					prevPoint.x = x;
					prevPoint.y = y;
					break;
				}
		}

		return toolStringBuffer.toString();

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		synchronized (offScreen) {
			g.drawImage(offScreen, 0, 0, null);
		}

		try {

			switch (drawTool) {

				case ScoolConstants.SELECT_TOOL :
					{
						if (selectTool != null) {
							selectTool.drawComponent(g);
						}
						break;
					}

				case ScoolConstants.CIRCLE_TOOL :
					{
						if (circleTool != null) {
							circleTool.drawComponent(g);
						}
						break;
					}

				case ScoolConstants.LINE_TOOL :
					{
						if (lineTool != null) {
							lineTool.drawComponent(g);
						}
						break;
					}

				case ScoolConstants.RECT_TOOL :
					{
						if (rectTool != null) {
							rectTool.drawComponent(g);
						}
						break;
					}

				case ScoolConstants.ROUND_RECT_TOOL :
					{
						if (roundRectTool != null) {
							roundRectTool.drawComponent(g);
						}
						break;
					}

				case ScoolConstants.FREE_HAND_TOOL :
					{

						if (freeHandTool != null) {
							freeHandTool.drawComponent(g);
						}
						break;
					}

				case ScoolConstants.SCREEN_SHOT_TOOL :
					{
						if (screenShotImageTool != null) {
							screenShotImageTool.drawComponent(g);
						}
						break;
					}
				case ScoolConstants.TEXT_TOOL :
					{
						if (textTool != null) {
							textTool.drawComponent(g);
						}
						break;
					}
				case ScoolConstants.SNAP_GRAPH_SHOT_TOOL :
					{
						if (snapGraphImageTool != null) {
							snapGraphImageTool.drawComponent(g);
						}
						break;
					}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void drawOffScreen() {
		synchronized (offgc) {

			switch (drawTool) {

				case ScoolConstants.SELECT_TOOL :
					{
						selectTool.drawComponent(offgc);
						break;
					}

				case ScoolConstants.CIRCLE_TOOL :
					{
						circleTool.drawComponent(offgc);
						break;
					}

				case ScoolConstants.LINE_TOOL :
					{
						lineTool.drawComponent(offgc);
						break;
					}

				case ScoolConstants.RECT_TOOL :
					{
						rectTool.drawComponent(offgc);
						break;
					}

				case ScoolConstants.ROUND_RECT_TOOL :
					{
						roundRectTool.drawComponent(offgc);
						break;
					}

				case ScoolConstants.FREE_HAND_TOOL :
					{
						freeHandTool.drawComponent(offgc);
						break;
					}

				case ScoolConstants.SCREEN_SHOT_TOOL :
					{
						screenShotImageTool.drawComponent(offgc);
						drawTool = 0;
						break;
					}
				case ScoolConstants.TEXT_TOOL :
					{
						if (textTool != null) {
							textTool.draw(offgc);
							break;
						}
					}
				case ScoolConstants.SNAP_GRAPH_SHOT_TOOL :
					{
						snapGraphImageTool.drawComponent(offgc);
						drawTool = 0;
						break;
					}
			}

		}
	}

	public void drawCommandString(String commandString) {

		Object obj = ToolFactory.getTool(commandString);

		synchronized (offgc) {

			if (obj instanceof CircleTool) {
				CircleTool circleTool = (CircleTool) obj;
				circleTool.draw(offgc);
				circleTool = null;
			} else if (obj instanceof LineTool) {
				LineTool lineTool = (LineTool) obj;
				lineTool.draw(offgc);
				lineTool = null;
			} else if (obj instanceof RectTool) {
				RectTool rectTool = (RectTool) obj;
				rectTool.draw(offgc);
				rectTool = null;
			} else if (obj instanceof RoundRectTool) {
				RoundRectTool roundRectTool = (RoundRectTool) obj;
				roundRectTool.draw(offgc);
				roundRectTool = null;
			} else if (obj instanceof FreeHandTool) {
				FreeHandTool freeHandTool = (FreeHandTool) obj;
				freeHandTool.draw(offgc);
				freeHandTool = null;
			} else if (obj instanceof PPtImageTool) {
				PPtImageTool pptImageTool = (PPtImageTool) obj;
				pptImageTool.draw(offgc, this);
				pptImageTool = null;
			} else if (obj instanceof ScreenShotImageTool) {
				ScreenShotImageTool screenShotImageTool =
					(ScreenShotImageTool) obj;
				screenShotImageTool.draw(offgc, this);
				screenShotImageTool = null;
			} else if (obj instanceof TextTool) {
				TextTool textTool = (TextTool) obj;
				textTool.draw(offgc);
				textTool = null;
			} else if (obj instanceof SnapGraphImageTool) {
				SnapGraphImageTool snapGraphImageTool =
					(SnapGraphImageTool) obj;
				snapGraphImageTool.draw(offgc, this);
				snapGraphImageTool = null;
			}
		}
		repaint();
	}
	
	public ResultPanel getResultPanel(){
		return resultPanel;
	}
	
	public void setResultPanel(ResultPanel resultPanel){
		this.resultPanel = resultPanel;
	}
	
	public WhiteBoard getWhiteBoard(){
		return whiteBoard;
	}

}