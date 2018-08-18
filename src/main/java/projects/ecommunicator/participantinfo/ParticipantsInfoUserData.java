/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.participantinfo;


/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ParticipantsInfoUserData {

	private String displayValue="";
	private int userId;
	private String loginId;
	private int role;
	private int state;

	public ParticipantsInfoUserData(		
		int userId,
		String loginId,
		int role,
		int state) {
		
		this.userId = userId;
		this.loginId = loginId;
		this.role = role;
		this.state = state;
	}
	
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}	
	
	public String getLoginId() {
		return loginId;
	}

	public String toString() {		
		return displayValue;
	}

}
