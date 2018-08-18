/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.security;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Credentials {
	private int userId;
	private String passwd;
	private String loginId;
	private int role;
	private int sessionId;
	private String ipAddress;
	private int port;
	private int audioPort;
	private long trackingId;
	private String captureDeviceURL;
	private static Credentials singleton;

	private Credentials() {
		super();
	}

	public static synchronized Credentials getInstance() {
		if (singleton != null) {
			return singleton;
		} else {
			singleton = new Credentials();
			return singleton;
		}
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTrackingId(long trackingId) {
		this.trackingId = trackingId;
	}

	public void setAudioPort(int audioPort) {
		this.audioPort = audioPort;
	}

	public String getCaptureDeviceURL() {
		return captureDeviceURL;
	}

	public int getUserId() {
		return userId;
	}

	public int getRole() {
		return role;
	}

	public String getPasswd() {
		return passwd;
	}

	public String getLoginId() {
		return loginId;
	}

	public int getSessionId() {
		return sessionId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getPort() {
		return port;
	}

	public long getTrackingId() {
		return trackingId;
	}

	public int getAudioPort() {
		return audioPort;
	}

	public void setCaptureDeviceURL(String captureDeviceURL) {
		this.captureDeviceURL = captureDeviceURL;
	}

}
