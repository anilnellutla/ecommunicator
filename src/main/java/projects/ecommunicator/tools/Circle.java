package projects.ecommunicator.tools;

import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is the Base Class for the CircleTool class that extends ToolsParams 
 * and implementing the method getToolType() of BasicParams and returns the ToolType as
 * CIRCLE_TOOL        
 * <P> 
 * @version 1.0
 */

public class Circle extends ToolParams {

	/**
	 * implementation of the abstract method getToolType  
	 * @return int that represents the type of the TOOL as CIRCLE_TOOL	 
	 */
    public int getToolType() {
        return ScoolConstants.CIRCLE_TOOL;
    }
}