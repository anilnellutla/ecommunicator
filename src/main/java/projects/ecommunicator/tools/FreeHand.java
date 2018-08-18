package projects.ecommunicator.tools;

import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is the Base Class for the FreeHandTool class that extends BasicParams since FreeHand Tool Dosen't 
 * have the width and height parameters and implementing the method getToolType() of BasicParams 
 * and returns the ToolType as FREE_HAND_TOOL        
 * <P> 
 * @version 1.0
 */

public class FreeHand extends BasicParams {

	/**
	 * implementation of the abstract method getToolType  
	 * @return int that represents the type of the TOOL as FREE_HAND_TOOL	 
	 */
    public int getToolType() {
        return ScoolConstants.FREE_HAND_TOOL;
    }
}