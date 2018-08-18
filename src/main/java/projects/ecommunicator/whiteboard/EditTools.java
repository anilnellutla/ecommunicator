/*
 * Created on Mar 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.whiteboard;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EditTools {

	private String toolString;


	public void cut(String toolString) {
		this.toolString = toolString;
	}

	public void copy(String toolString) {
		this.toolString = toolString;
	}

	public String paste() {
		return toolString;
	}

}
