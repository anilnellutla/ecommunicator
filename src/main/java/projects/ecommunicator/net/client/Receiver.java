package projects.ecommunicator.net.client;

import java.io.IOException;
import java.io.InputStream;

import projects.ecommunicator.security.Credentials;
import projects.ecommunicator.utility.ScoolConstants;
import projects.ecommunicator.utility.Utility;
import projects.ecommunicator.application.DataListener;

/** 
 * Receiver Thread. This Thread reads data from the network sent by TCP Server.
 * After reading a data packet, this Thread notifies DataListener Thread. The amount
 * of data to read from stream is specified by payload length in Header.
 * @see com.adaequare.ecommunicator.util.FormatData#formatReceivedData(byte[])
 * @see DataListener#run()
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */

public class Receiver implements Runnable {
	
	// raw InputStream to read raw bytes
	private InputStream rawInput;	

	/**
	 * Creates an instance of this Thread.
	 * @param rawInput raw InputStream
	 */
	public Receiver(InputStream rawInput) {
		this.rawInput = rawInput;		
	}

	/** 
	 * Overridden method to read data from network sent by TCP Server.
	 * This method continuously reads the data from the network.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/* DataPool object on which DataListener Thread waits. This Thread notifies
		   DataListener Thread.
		 */
		ReceivedDataPool pool = ReceivedDataPool.getInstance();
		// boolean flag to check if Tracking Id is received.
		boolean receivedTrackingId = false;
		// get singleton instance of Credential class
		Credentials credentials = Credentials.getInstance();

		// continuously read for the data from network
		while (true) {

			try {
				//System.out.println("\nwaiting for data...\n");
				// string buffer to hold Header data
				StringBuffer header = new StringBuffer();
				// read the header string
				int c;				
				for (int i = 0; i < ScoolConstants.HEADER_LENGTH; i++) {
					c = rawInput.read();
					c = (c >= 0) ? c : 256 + c;
					header.append((char) c);
				}
				String headerString = header.toString();
				//System.out.println("received header:" + headerString);

				//get the payload length
				int bytesToRead = Utility.getPayLoadLength(headerString);
				//System.out.println("received payload length:"+bytesToRead);
				// read payload data
				int bytesRead = 0;				
				byte[] payLoad = new byte[bytesToRead];
				while (bytesRead < bytesToRead) {
					int result =
						rawInput.read(
							payLoad,
							bytesRead,
							bytesToRead - bytesRead);
					if (result == -1) {
						break;
					}
					bytesRead += result;
				}

				//System.out.println("received payLoad:" + new String(payLoad,0,26));
				
				if (!receivedTrackingId) {
				    //set tracking id to Credentials instance
					System.out.println("setting tracking id...");
					credentials.setTrackingId(
						Utility.getTrackingId(headerString));
					credentials.setLoginId(Utility.getLoginId(payLoad));
					receivedTrackingId = true;
				} else {
					/* notify DataListener Thread. After being notified DataListener Thread
					 * handles GUI operations. (painting etc)
					 */
					synchronized (pool) {
						pool.add(pool.size(), payLoad);
						pool.notifyAll();
					}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		} //end while
	}
}
