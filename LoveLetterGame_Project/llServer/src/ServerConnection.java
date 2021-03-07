/**
 * @autor Hoang An Tran, Simon Wiethase, Tobias Celik
 * Class ServerConnection
 * This class provides the Server connection
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerConnection implements Runnable {
	private Scanner in;
	private Socket server;
	private Client client;

	/**
	 * ServerConnection()
	 * @param server	server
	 * @param client	client
	 * Instantiates a new server connection.
	 */
	public ServerConnection(Socket server, Client client) {
		this.server = server;
		this.client=client;
		open();
	}

	/**
	 * open()
	 * Getting Input Stream.
	 */
	public void open(){
		try {
			in = new Scanner(server.getInputStream());
		} catch (IOException e) {
			System.out.println("Error getting input stream: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * run()
	 * This thread is responsible for reading server's input.
	 * It runs in an infinite loop until the client disconnects from the server.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				if(in.hasNext()){
					client.handle(in.nextLine());
				}
				else {
					System.exit(0);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Listening error:" + e.getMessage());
			e.printStackTrace();
		}
	}
}
