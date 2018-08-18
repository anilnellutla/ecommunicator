package projects.ecommunicator.net.client;

import java.io.IOException;
import java.io.OutputStream;

import projects.ecommunicator.security.Credentials;
import projects.ecommunicator.utility.Utility;

/** 
 * Sender Thread. This Thread puts data in the network sent by 
 * event-dispatching. (DataEventThread)
 * This Thread waits on SendDataPool singleton instance to be notified by event-dispatching
 * Thread. After being notified this Thread puts the data in the network. 
 * @see com.adaequare.ecommunicator.util.FormatData#formatSendData(byte[]) 
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */
public class Sender implements Runnable {

	// raw OutputStream
	private OutputStream rawOutput;
	
	/**
	 * Creates an instance of this Thread.
	 * @param rawOutput raw OutputStream
	 */
	public Sender(OutputStream rawOutput) {
		this.rawOutput = rawOutput;
	}
	
	/** 
	 * Overridden method to send data to the network.
	 * This method continuously waits for the data to send to TCP Server.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/* DataPool object on which this Thread waits. This Thread is notified
		 * by event-dispathching Thread.
		 */	 
		SendDataPool pool = SendDataPool.getInstance();
		//get singleton instance of Credential class
		Credentials credentials = Credentials.getInstance();
		//session id
		int sessionId = credentials.getSessionId();
		//user id
		int userId = credentials.getUserId();
		//role
		int role = credentials.getRole();
		//boolean flag to check if Credentials have been sent
		boolean sentCredentials = false;

		//continuous loop to put data in the network
		while (true) {
            //send credentials to TCP Server if not sent
			if (!sentCredentials) {
				// construct header
				StringBuffer header = new StringBuffer();
				header.append(Utility.convertTo8Byte(sessionId));
				header.append(
					Utility.convertTo16Byte(credentials.getTrackingId()));
				header.append(Utility.convertTo8Byte(userId));
				header.append(Utility.convertTo2Byte(role));
				// payload length is 0 bytes
				header.append(Utility.convertTo8Byte(0));
				// these 16 bytes are for future use
				header.append(Utility.convertTo8Byte(0));
				header.append(Utility.convertTo8Byte(0));
				System.out.println("sending credentials:"+header.toString() + "\n");

				// put data in network
				try {
					rawOutput.write(header.toString().getBytes());
					rawOutput.flush();
				} catch (IOException ex) {
					return;
				}
				// credentials sent
				sentCredentials = true;
			} else {
				/* wait for data to put in network. SendDataPool object is notified
				 * by DataEvent Thread.
				 */
				synchronized (pool) {
					while (pool.isEmpty()) {
						try {
							pool.wait();
						} catch (InterruptedException ex) {
						}
					}
				}
				byte[] payLoad = (byte[]) pool.remove(0);

				/* Only payload data is received from DataEvent Thread. Header information
				 * is added and data packet is then put in the network. 
				 */
				StringBuffer header = new StringBuffer();
				header.append(Utility.convertTo8Byte(sessionId));
				header.append(
					Utility.convertTo16Byte(credentials.getTrackingId()));
				header.append(Utility.convertTo8Byte(userId));
				header.append(Utility.convertTo2Byte(role));
				header.append(Utility.convertTo8Byte(payLoad.length));
				// these 16 bytes are for future use
				header.append(Utility.convertTo8Byte(0));
				header.append(Utility.convertTo8Byte(0));

				// Header byte array
				byte[] headerByteArray = (header.toString()).getBytes();
				//System.out.println("header length:" + headerByteArray.length);

				// payload byte array
				byte[] data = new byte[headerByteArray.length + payLoad.length];
				//System.out.println("data packet length:" + data.length);

				System.arraycopy(
					headerByteArray,
					0,
					data,
					0,
					headerByteArray.length);

				System.arraycopy(
					payLoad,
					0,
					data,
					headerByteArray.length,
					payLoad.length);				

				// put data in the network
				try {
					rawOutput.write(data);
					rawOutput.flush();
				} catch (IOException ex) {
					return;
				}
			}
		}
	}
}
