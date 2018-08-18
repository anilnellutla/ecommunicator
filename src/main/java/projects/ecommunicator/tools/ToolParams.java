package projects.ecommunicator.tools;

/**
 * Abstract Class that extends the BasicParams and contains the width and height that are specific to 
 * the tools like Ellipse/CircleTool,RectTool and RoundRectTool     
 * <P> 
 * @see java.awt.BasicStroke
 * @see java.awt.Color
 * @version 1.0
 */

public abstract class ToolParams extends BasicParams {
	//width of the tools like circle,rect,round rect	
	public int width;
	//height of the tools like circle,rect,round rect
	public int height;
}