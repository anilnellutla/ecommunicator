/*
 * Created on Feb 20, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.application;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ApplicationState {
	
	private boolean receivedOfflineData;	
	private static ApplicationState singleton;	

	private ApplicationState() {
		super();
	}

	public static synchronized ApplicationState getInstance() {
		if (singleton != null) {
			return singleton;
		} else {
			singleton = new ApplicationState();
			return singleton;
		}
	}
	
	public synchronized void setReceivedOfflineData(boolean receivedOfflineData) {
		this.receivedOfflineData = receivedOfflineData;		
	}
	
	public synchronized boolean isReceivedOfflineData() {
		return receivedOfflineData;
	}

}
