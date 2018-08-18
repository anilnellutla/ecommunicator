package projects.ecommunicator.audio.server;

import java.net.Socket;

/** 
 * This class holds participant details like user id, role, tracking id, connection,
 * and boolean flag to identify if the participant is connected.
 * Participant objects are pushed into Hastable sessions. RequestProcessor Thread
 * retrieves Vector containing Participant objects for a given sessionid and then
 * sends data to all the Participants. 
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */
public class Participant {

	// user id
	private int userId;
	private String loginId;
	// role
	private int role;
	// tracking id
	private long trackingId;
	// socket connection
	private Socket connection;
	// boolean flag to determine if the Participant is already connected
	private boolean connected;

	/**
	 * Creates an instance of this class.
	 * @param userId user id of the participant
	 * @param role role of the participant
	 * @param trackingId tracking id of the participant
	 * @param connection socket conenction
	 */
	public Participant(
		int userId,
		String loginId,
		int role,
		long trackingId,
		Socket connection) {
		this.userId = userId;
		this.loginId = loginId;
		this.role = role;
		this.trackingId = trackingId;
		this.connection = connection;

	}

	public String getLoginId() {
		return loginId;
	}

	/**
	 * Returns user id
	 * @return
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Returns role
	 * @return
	 */
	public int getRole() {
		return role;
	}

	/**
	 * Returns tracking id
	 * @return
	 */
	public long getTrackingId() {
		return trackingId;
	}

	/**
	 * Returns socket connection
	 * @return
	 */
	public Socket getConnection() {
		return connection;
	}

	/**
	 * true if connected else false
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * sets connected boolean flag
	 * @param connected
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

}
