package projects.ecommunicator.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//import AudioServer;

/** 
 * Server for all the eCommunicator clients. Server reads data from the client and 
 * sends to all the clients in the same session. This Server runs on a port specified 
 * by command line arguments. For every new client connected it spawns a new RequestProcessor 
 * Thread. RequestProcessor Thread then takes care of reading and sending data.
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */
public class ECommunicatorServer implements Runnable {

	// ServerSocket object
	private ServerSocket server;
	//private AudioServer audioServer;

	/**
	 * Creates a new instance of this Server
	 * @param port
	 */
	public ECommunicatorServer(int port) {
		// start the Server on the specified port
		try {
			this.server = new ServerSocket(port);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		//this.audioServer = audioServer;
	}

	/** 
	 * Overridden method to accept connections from clients. 
	 * @see java.lang.Runnable#run()
	 */

	public void run() {
		System.out.println(
			"Accepting connections for ECommunicatorServer on port:" + server.getLocalPort());

		// continuously wait for the clients to connect
		while (true) {
			Socket connection = null;
			try {
				// accept incoming connections
				connection = server.accept();
				System.out.println("\nnew connection has arrived.");

				// Spawn a new Thread for each client connected.
				RequestProcessor requestProcessor =
					new RequestProcessor(connection);
				Thread t = new Thread(requestProcessor);
				t.start();
			} catch (IOException ex) {
				if (connection != null) {
					try {
						connection.close();
					} catch (IOException excep) {
						excep.printStackTrace();
					}
				}
			}
		}
	}

}
