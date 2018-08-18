package projects.ecommunicator.tools;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * Abstract Class that contains the Basic Parameters like startX,startY,color and stroke of all the Tools     
 * <P> 
 * @see java.awt.BasicStroke
 * @see java.awt.Color
 * @version 1.0
 */

public abstract class BasicParams {
	//X co-ordinate of the tool's starting point
	public int startX;
	//Y co-ordinate of the tool's starting point
	public int startY;

	//color of the tool 
	public Color color;
	//stroke that represents the width of the tool
	public BasicStroke stroke;

	/**
	 * abstract method that returns the tool type 
	 * @return int that represents the type of the TOOL	 
	 */

	public abstract int getToolType();

}