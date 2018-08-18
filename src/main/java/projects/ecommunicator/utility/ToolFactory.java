package projects.ecommunicator.utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

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

public class ToolFactory {

	public static Object getTool(String toolString) {

		int toolType = Integer.parseInt(toolString.substring(0, 4));

		switch (toolType) {

			case ScoolConstants.CIRCLE_TOOL :
				{
					CircleTool circleTool = new CircleTool();
					circleTool.startX =
						Integer.parseInt(toolString.substring(4, 8));
					circleTool.startY =
						Integer.parseInt(toolString.substring(8, 12));
					circleTool.width =
						Integer.parseInt(toolString.substring(12, 16));
					circleTool.height =
						Integer.parseInt(toolString.substring(16, 20));
					circleTool.color =
						new Color(
							Integer.parseInt(toolString.substring(20, 23)),
							Integer.parseInt(toolString.substring(23, 26)),
							Integer.parseInt(toolString.substring(26, 29)));
					circleTool.stroke =
						new BasicStroke(
							Float.parseFloat(toolString.substring(29, 32)));
					return circleTool;
				}

			case ScoolConstants.RECT_TOOL :
				{

					RectTool rectTool = new RectTool();
					rectTool.startX =
						Integer.parseInt(toolString.substring(4, 8));
					rectTool.startY =
						Integer.parseInt(toolString.substring(8, 12));
					rectTool.width =
						Integer.parseInt(toolString.substring(12, 16));
					rectTool.height =
						Integer.parseInt(toolString.substring(16, 20));
					rectTool.color =
						new Color(
							Integer.parseInt(toolString.substring(20, 23)),
							Integer.parseInt(toolString.substring(23, 26)),
							Integer.parseInt(toolString.substring(26, 29)));
					rectTool.stroke =
						new BasicStroke(
							Float.parseFloat(toolString.substring(29, 32)));
					return rectTool;
				}

			case ScoolConstants.ROUND_RECT_TOOL :
				{
					RoundRectTool roundRectTool = new RoundRectTool();
					roundRectTool.startX =
						Integer.parseInt(toolString.substring(4, 8));
					roundRectTool.startY =
						Integer.parseInt(toolString.substring(8, 12));
					roundRectTool.width =
						Integer.parseInt(toolString.substring(12, 16));
					roundRectTool.height =
						Integer.parseInt(toolString.substring(16, 20));
					roundRectTool.color =
						new Color(
							Integer.parseInt(toolString.substring(20, 23)),
							Integer.parseInt(toolString.substring(23, 26)),
							Integer.parseInt(toolString.substring(26, 29)));
					roundRectTool.stroke =
						new BasicStroke(
							Float.parseFloat(toolString.substring(29, 32)));
					return roundRectTool;
				}

			case ScoolConstants.LINE_TOOL :
				{

					LineTool lineTool = new LineTool();
					lineTool.startX =
						Integer.parseInt(toolString.substring(4, 8));
					lineTool.startY =
						Integer.parseInt(toolString.substring(8, 12));
					lineTool.endX =
						Integer.parseInt(toolString.substring(12, 16));
					lineTool.endY =
						Integer.parseInt(toolString.substring(16, 20));
					lineTool.color =
						new Color(
							Integer.parseInt(toolString.substring(20, 23)),
							Integer.parseInt(toolString.substring(23, 26)),
							Integer.parseInt(toolString.substring(26, 29)));
					lineTool.stroke =
						new BasicStroke(
							Float.parseFloat(toolString.substring(29, 32)));
					return lineTool;
				}

			case ScoolConstants.FREE_HAND_TOOL :
				{
					FreeHandTool freeHandTool = new FreeHandTool();
					int index = 20;
					int size =
						Integer.parseInt(
							toolString.substring(index, index += 8));
					String points = toolString.substring(index, index += size);

					ArrayList list = new ArrayList();
					for (int i = 0; i < points.length(); i += 8) {
						int startX =
							Integer.parseInt(points.substring(i, i + 4));
						int startY =
							Integer.parseInt(points.substring(i + 4, i + 8));
						list.add(new Point(startX, startY));
					}
					freeHandTool.setPoints(list);

					freeHandTool.color =
						new Color(
							Integer.parseInt(
								toolString.substring(index, index += 3)),
							Integer.parseInt(
								toolString.substring(index, index += 3)),
							Integer.parseInt(
								toolString.substring(index, index += 3)));

					freeHandTool.stroke =
						new BasicStroke(
							Float.parseFloat(
								toolString.substring(
									index,
									toolString.length())));
					return freeHandTool;
				}

			case ScoolConstants.PPT_IMAGE_TOOL :
				{

					PPtImageTool pptImageTool = new PPtImageTool();
					pptImageTool.startX =
						Integer.parseInt(toolString.substring(4, 8));
					pptImageTool.startY =
						Integer.parseInt(toolString.substring(8, 12));
					pptImageTool.width =
						Integer.parseInt(toolString.substring(12, 16));
					pptImageTool.height =
						Integer.parseInt(toolString.substring(16, 20));
					pptImageTool.imagePath =
						toolString.substring(20, toolString.length());
					return pptImageTool;
				}

			case ScoolConstants.SCREEN_SHOT_TOOL :
				{

					ScreenShotImageTool screenShotImageTool =
						new ScreenShotImageTool();
					screenShotImageTool.startX =
						Integer.parseInt(toolString.substring(4, 8));
					screenShotImageTool.startY =
						Integer.parseInt(toolString.substring(8, 12));
					screenShotImageTool.width =
						Integer.parseInt(toolString.substring(12, 16));
					screenShotImageTool.height =
						Integer.parseInt(toolString.substring(16, 20));
					screenShotImageTool.imagePath =
						toolString.substring(20, toolString.length());
					return screenShotImageTool;
				}

			case ScoolConstants.TEXT_TOOL :
				{

					TextTool textTool = new TextTool();
					textTool.setOrigin(
						Float.parseFloat(toolString.substring(4, 8)),
						Float.parseFloat(toolString.substring(8, 12)));
					textTool.startX =
						Integer.parseInt(toolString.substring(12, 16));
					textTool.startY =
						Integer.parseInt(toolString.substring(16, 20));
					textTool.width =
						Integer.parseInt(toolString.substring(20, 24));
					textTool.height =
						Integer.parseInt(toolString.substring(24, 28));
					textTool.color =
						new Color(
							Integer.parseInt(toolString.substring(28, 31)),
							Integer.parseInt(toolString.substring(31, 34)),
							Integer.parseInt(toolString.substring(34, 37)));
					textTool.stroke =
						new BasicStroke(
							Float.parseFloat(toolString.substring(37, 40)));
					textTool.setFontNameId(toolString.substring(40, 42));
					textTool.setFontSize(
						Integer.parseInt(toolString.substring(42, 44)));
					textTool.setFontStyle(
						Integer.parseInt(toolString.substring(44, 46)));
					textTool.setText(
						new StringBuffer(
							toolString.substring(46, toolString.length())));
					return textTool;
				}

			case ScoolConstants.SNAP_GRAPH_SHOT_TOOL :
				{

					SnapGraphImageTool snapGraphImageTool =
						new SnapGraphImageTool();
					snapGraphImageTool.startX =
						Integer.parseInt(toolString.substring(4, 8));
					snapGraphImageTool.startY =
						Integer.parseInt(toolString.substring(8, 12));
					snapGraphImageTool.width =
						Integer.parseInt(toolString.substring(12, 16));
					snapGraphImageTool.height =
						Integer.parseInt(toolString.substring(16, 20));
					snapGraphImageTool.imagePath =
						toolString.substring(20, toolString.length());
					return snapGraphImageTool;
				}

			case ScoolConstants.POLL_TOOL :
				{

					PollTool pollTool = new PollTool();

					pollTool.startX =
						Integer.parseInt(toolString.substring(4, 8));
					pollTool.startY =
						Integer.parseInt(toolString.substring(8, 12));
						
					int index = 12;

					int lengthOfQuestionTyped =
						Integer.parseInt(
							toolString.substring(index, index + 4));
					//move the index to the start of the question typed
					index += 4;
					String questionTyped =
						toolString.substring(
							index,
							index + lengthOfQuestionTyped);
					
					//move the index to the end of question typed 
					index += lengthOfQuestionTyped;
					int lengthOfChoices =
						Integer.parseInt(
							toolString.substring(index, index + 4));
					//move the index to the start of the length choices
					index += 4;
					String lengthChoices =
						toolString.substring(index, index + lengthOfChoices);
					//move the index to the end of the length choice
					index += lengthOfChoices;

					String choices =
						toolString.substring(index, toolString.length());

					ArrayList list = new ArrayList();

					int choiceIndex = 0;
					for (int i = 0; i < lengthChoices.length(); i += 4) {
						int length =
							Integer.parseInt(lengthChoices.substring(i, i + 4));
						String choice =
							choices.substring(
								choiceIndex,
								(choiceIndex + length));
						list.add(choice);
						choiceIndex += length;
					}
					pollTool.setQuestionTyped(questionTyped);
					pollTool.setChoices(list);
					return pollTool;
				}
		}

		return null;
	}
}
