package projects.ecommunicator.net.client;

import java.util.LinkedList;

/** 
 * Singleton class and extends java.util.LinkedList. Sender Thread and Event-Dispatch
 * Thread wait and notify on this class singleton instance.
 * <p>
 * @author  Anil K Nellutla 
 * @version 1.0
 */
public class SendDataPool extends LinkedList {

	//static singleton instance of this class
	private static SendDataPool singleton;

	/**
	 * Creates an instance of this class but cannot be invoked from another
	 * class since the constructor is private. The constructor is made private 
	 * to have a single instance of this class throughtout the application.
	 */
	private SendDataPool() {
		super();
	}

	/**
	 * Returns singleton instance of this class. This method checks if singleton
	 * instance already exists. If it exists it returns the same else creates one.
	 * @return single instance of this class
	 */
	public static synchronized SendDataPool getInstance() {
		if(singleton!= null) {
			return singleton;
		}else {
			singleton = new SendDataPool();
			return singleton;
		}
	}
}
