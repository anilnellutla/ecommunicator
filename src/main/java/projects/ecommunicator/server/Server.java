/*
 * Created on Feb 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package projects.ecommunicator.server;

import projects.ecommunicator.audio.server.AudioServer;
import projects.ecommunicator.net.server.ECommunicatorServer;

/**
 * @author anil
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Server {

	public static void main(String[] args) {
		int port;
		int audioPort;
		try {
			port = Integer.parseInt(args[0]);
			if (port < 0 || port > 65535) {
				port = 8186;
			}
			audioPort = Integer.parseInt(args[1]);
			if (audioPort < 0 || audioPort > 65535) {
				audioPort = 8187;
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			port = 8186;
			audioPort = 4152;
		}
		
		ECommunicatorServer eCommunicatorServer = new ECommunicatorServer(port);
		Thread t1 = new Thread(eCommunicatorServer);
		t1.start();		

		AudioServer audioServer = new AudioServer(audioPort);
		Thread t2 = new Thread(audioServer);
		t2.start();
	}
}
