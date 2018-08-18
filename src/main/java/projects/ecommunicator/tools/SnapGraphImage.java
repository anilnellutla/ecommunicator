package projects.ecommunicator.tools;

import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is the Base Class for the ScreenShotImageTool class that extends ToolsParams 
 * and implementing the method getToolType() of BasicParams and returns the ToolType as
 * PPT_SCREEN_SHOT_TOOL        
 * <P> 
 * @version 1.0
 */
public class SnapGraphImage extends ToolParams {
	//canonical path of the image		
	public String imagePath;

	/**
	 * implementation of the abstract method getToolType  
	 * @return int that represents the type of the TOOL as SCREEN_SHOT_TOOL	 
	 */
    public int getToolType() {
        return ScoolConstants.SCREEN_SHOT_TOOL;
    }
}