package projects.ecommunicator.net.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.UnknownHostException;

import projects.ecommunicator.security.Credentials;

/** 
 * This class is used for creating connection between the Client and TCP Server.
 * This class invokes two Threads:
 * 1)Receiver Thread
 * This Thread is receives data from the network sent by TCP Server
 * 2)Sender Thread
 * This Thread sends data to TCP Server
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */
public class ECommunicatorClient implements Runnable {

	// socket connection
	private Socket connection;
	// boolean flag to check if the client is connected or not
	private boolean isConnected = false;
	// TCP Server InetAddress
	private InetAddress serverAddress = null;
	// port on which TCP Server is running
	private int port;

	/**
	 * Creates an instance of ECommunicatorClient
	 *
	 */
	public ECommunicatorClient() {
		super();
	}

	/** 
	 * Overridden method to create connection to TCP Server and spawn
	 * Receiver and Sender Threads.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// get the IPAddress and Port number of TCP Server to connect.
		Credentials credentials = Credentials.getInstance();
		String ipAddress = credentials.getIpAddress();
		port = credentials.getPort();
		try {
			serverAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
			System.out.println("Unknown Host. Exiting...");
			//System.exit(0);
		}
		// connect to TCP Server
		connect();
	}

	/**
	 * This method connects to TCP Server and if connected creates Receiver
	 * and Sender Thread.
	 */
	public void connect() {
		try {
			// establish connection if not connected.
			if (!isConnected) {
				connection = new Socket(serverAddress, port);
			}
			isConnected = true;
		} catch (ConnectException ex) {
			ex.printStackTrace();
		} catch (NoRouteToHostException ex) {
			ex.printStackTrace();
		} catch (PortUnreachableException ex) {
			ex.printStackTrace();
		} catch (BindException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// if connected
		if (isConnected) {
			/*
			 * get the InputStream and OutputStream from connection. These streams
			 * can be used to receive and send data respectively.
			 */
			InputStream rawInput = null;
			OutputStream rawOutput = null;
			try {
				// input stream
				rawInput = new BufferedInputStream(connection.getInputStream());
				// output stream
				rawOutput =
					new BufferedOutputStream(connection.getOutputStream());

			} catch (IOException ex) {
			}

			// create Receiver Thread to receive data and pass InputStream as parameter
			Receiver receiver = new Receiver(rawInput);
			Thread t1 = new Thread(receiver);
			t1.start();

			// create Sender Thread to send data and pass OutputStream as parameter.
			Sender sender = new Sender(rawOutput);
			Thread t2 = new Thread(sender);
			t2.start();
			
		}// end if
	}

	/**
	 * This method disconnects client from TCP Server and also sets boolean flag 
	 * isConnected to false.
	 */
	public void disconnect() {
		try {
			connection.close();
			connection = null;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println(
			"#########################  DISCONNECTED  ####################");
		isConnected = false;
	}

}
