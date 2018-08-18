package projects.ecommunicator.screenshot;

import projects.ecommunicator.actions.ScreenShotAction;
import projects.ecommunicator.application.WhiteBoardFrame;

public class ScreenShotThread implements Runnable {
	
	//parent frame of the Application
	private WhiteBoardFrame whiteBoardFrame;
	//action that captures the desktop/screen images
	private ScreenShotAction screenShotAction;
	
	/**
	 * creates new instance of the ScreenShotThread
	 * @param whiteBoardFrame parent frame of the Application
	 * @param screenShotAction action that captures the desktop/screen images 
	 */
	public ScreenShotThread (WhiteBoardFrame whiteBoardFrame, ScreenShotAction screenShotAction) {
		this.whiteBoardFrame = whiteBoardFrame;
		this.screenShotAction = screenShotAction;
	}
	
	/**
	 * call back method of the runnable interface 
	 * it cretes new instanstance of ScreenShot
	 */
	public void run(){
		new ScreenShot(whiteBoardFrame, "Screen Shot", true,screenShotAction );
	}

}