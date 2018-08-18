package projects.ecommunicator.tools;

import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is the Base Class for the LineTool class that extends BasicParams since Line Tool Dosen't 
 * have the width and height parameters and implementing the method getToolType() of BasicParams 
 * and returns the ToolType as LINE_TOOL        
 * <P> 
 * @version 1.0
 */

public class Line extends BasicParams {

	//X co-ordinate of the end point
	public int endX;
	//Y co-ordinate of the end point
	public int endY;

	/**
	 * implementation of the abstract method getToolType  
	 * @return int that represents the type of the TOOL as LINE_TOOL	 
	 */
    public int getToolType() {
        return ScoolConstants.LINE_TOOL;
    }
}