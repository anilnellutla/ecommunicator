package projects.ecommunicator.tools;

import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is the Base Class for the RectTool class that extends ToolsParams 
 * and implementing the method getToolType() of BasicParams and returns the ToolType as
 * RECT_TOOL        
 * <P> 
 * @version 1.0
 */
public class Rect extends ToolParams {

	/**
	 * implementation of the abstract method getToolType  
	 * @return int that represents the type of the TOOL as RECT_TOOL	 
	 */
    public int getToolType() {
        return ScoolConstants.RECT_TOOL;
    }
}