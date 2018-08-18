package projects.ecommunicator.tools;

import projects.ecommunicator.utility.ScoolConstants;

/**
 * This is the Base Class for the PPtImageTool class that extends ToolsParams 
 * and implementing the method getToolType() of BasicParams and returns the ToolType as
 * PPT_IMAGE_TOOL        
 * <P> 
 * @version 1.0
 */
public class PPtImage extends ToolParams {
	
	//canonical path of the converted presentation images
	
	public String imagePath;

	/**
	 * implementation of the abstract method getToolType  
	 * @return int that represents the type of the TOOL as PPT_IMAGE_TOOL	 
	 */
    public int getToolType() {
        return ScoolConstants.PPT_IMAGE_TOOL;
    }
}