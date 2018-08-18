/*
 * Created on Jan 17, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.utility;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface Permission {
	
	public static final String SEEK = "SEEK";
	public static final String ALLOW = "ALLOW";
	public static final String DISALLOW = "DISALLOW";
	
	public static final int SEEK_WHITEBOARD = 1;
	public static final int ALLOW_WHITEBOARD = 2;
	public static final int DISALLOW_WHITEBOARD = 3;

	public static final int SEEK_MIC = 1;
	public static final int ALLOW_MIC = 2;
	public static final int DISALLOW_MIC = 3;

	public static final int SEEK_MESSAGEBOARD = 1;
	public static final int ALLOW_MESSAGEBOARD = 2;
	public static final int DISALLOW_MESSAGEBOARD = 3;

	public static final int SEEK_PRESENTATIONINFO = 1;
	public static final int ALLOW_PRESENTATIONINFO = 2;
	public static final int DISALLOW_PRESENTATIONINFO = 3;
}
