package projects.ecommunicator.utility;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;

import projects.ecommunicator.internalframes.WhiteBoard;
import projects.ecommunicator.poll.PollButtonPanel;
import projects.ecommunicator.poll.ResultPanel;
import projects.ecommunicator.tools.CircleTool;
import projects.ecommunicator.tools.FreeHandTool;
import projects.ecommunicator.tools.LineTool;
import projects.ecommunicator.tools.PPtImageTool;
import projects.ecommunicator.tools.PollTool;
import projects.ecommunicator.tools.RectTool;
import projects.ecommunicator.tools.RoundRectTool;
import projects.ecommunicator.tools.ScreenShotImageTool;
import projects.ecommunicator.tools.SnapGraphImageTool;
import projects.ecommunicator.tools.TextTool;
import projects.ecommunicator.whiteboard.WhiteBoardCanvas;

public class LoadPage {

	private WhiteBoardCanvas canvas;
	private String selectedPage;

	public LoadPage(WhiteBoardCanvas canvas, String selectedPage) {
		this.canvas = canvas;
		this.selectedPage = selectedPage;
	}

	public void run() {
		loadPage();
	}

	private void loadPage() {
		Hashtable pages = canvas.getPages();
		Vector page = (Vector) pages.get(selectedPage);
		int drawTool = canvas.getDrawTool();

		if (!selectedPage.startsWith(ScoolConstants.POLL)) {

			//first remove the PollButtonPanel from the WhiteBoard			
			WhiteBoard whiteBoard = canvas.getWhiteBoard();
			PollButtonPanel pollBoardPanel = whiteBoard.getPollButtonPanel();
			JPanel panel = whiteBoard.getWhiteBoardButtonPanel();
			if (pollBoardPanel != null) {
				whiteBoard.remove(pollBoardPanel);
				whiteBoard.validate();
				whiteBoard.setWhiteBoardButtonPanel(panel);
				whiteBoard.getContentPane().add(panel, BorderLayout.NORTH);
				whiteBoard.validate();
			}

			//remove the ResultPanel from the Canvas
			ResultPanel resultPanel = canvas.getResultPanel();
			if (resultPanel != null) {
				canvas.remove(resultPanel);
				canvas.validate();
			}

			if (page != null) {
				OffScreenBuffer offScBuffer =
					new OffScreenBuffer(
						canvas.getBackground(),
						canvas.getForeground());
				offScBuffer.createOffScreenBuffer();
				BufferedImage offScreen = offScBuffer.getOffScreen();
				Graphics offgc = offScBuffer.getOffScreenGraphics();

				for (int i = 0; i < page.size(); i++) {
					String toolString = (String) page.get(i);
					Object obj = ToolFactory.getTool(toolString);

					if (obj instanceof CircleTool) {
						CircleTool circleTool = (CircleTool) obj;
						circleTool.draw(offgc);
						circleTool = null;
						//canvas.setDrawTool(0);
					} else if (obj instanceof LineTool) {
						LineTool lineTool = (LineTool) obj;
						lineTool.draw(offgc);
						lineTool = null;
						//canvas.setDrawTool(0);
					} else if (obj instanceof RectTool) {
						RectTool rectTool = (RectTool) obj;
						rectTool.draw(offgc);
						rectTool = null;
						//canvas.setDrawTool(0);
					} else if (obj instanceof RoundRectTool) {
						RoundRectTool roundRectTool = (RoundRectTool) obj;
						roundRectTool.draw(offgc);
						roundRectTool = null;
						//canvas.setDrawTool(0);
					} else if (obj instanceof FreeHandTool) {
						FreeHandTool freeHandTool = (FreeHandTool) obj;
						freeHandTool.draw(offgc);
						freeHandTool = null;
						//canvas.setDrawTool(0);
					} else if (obj instanceof PPtImageTool) {
						PPtImageTool pptImageTool = (PPtImageTool) obj;
						pptImageTool.draw(offgc, canvas);
						pptImageTool = null;
						//canvas.setDrawTool(0);
					} else if (obj instanceof ScreenShotImageTool) {
						ScreenShotImageTool screenShotImageTool =
							(ScreenShotImageTool) obj;
						screenShotImageTool.draw(offgc, canvas);
						screenShotImageTool = null;
						//canvas.setDrawTool(0);
					} else if (obj instanceof TextTool) {
						TextTool textTool = (TextTool) obj;
						textTool.draw(offgc);
						textTool = null;
						//canvas.setDrawTool(0);
					} else if (obj instanceof SnapGraphImageTool) {
						SnapGraphImageTool snapGraphImageTool =
							(SnapGraphImageTool) obj;
						snapGraphImageTool.draw(offgc, canvas);
						snapGraphImageTool = null;
						//canvas.setDrawTool(0);
					}
					/*
					 else if (obj instanceof PollTool) {
						PollTool pollTool = (PollTool) obj;
						pollTool.showResultPanel(canvas);
						pollTool = null;
					}
					*/
				}
				canvas.setOffScreen(offScreen);
				canvas.setOffScreenGraphics(offgc);
				//canvas.setCurrentPage(selectedPage);
				canvas.setPage(page);
				if (drawTool != ScoolConstants.SELECT_TOOL) {
					canvas.setDrawTool(drawTool);
				}
				canvas.setDrawTool(0);
			}
		} else if (selectedPage.startsWith(ScoolConstants.POLL)) {
			String toolString = (String) page.get(0);
			Object obj = ToolFactory.getTool(toolString);
			PollTool pollTool = (PollTool) obj;
			pollTool.showResultPanel(canvas);
			pollTool = null;

		}

		canvas.repaint();

	}

}