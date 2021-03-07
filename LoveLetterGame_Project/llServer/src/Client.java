/**
 * @author Hoang An Tran, Tobias Celik, Simon Wiethase
 * Class Client
 * creates a client for client-server based chat
 */

import java.io.*;
import java.net.*;

public class Client {

	private Socket socket;

	/**
	 * Instantiates a new client.
	 */
	public Client()  {
		try {
			this.socket = new Socket("localhost", 8080);
		} catch (IOException e) {
			System.out.println("[Chat] Could not connect to Server, exiting program..");
			System.exit(0);
			e.printStackTrace();
		}
	}

	/**
	 * main()
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client();
		client.setup();
	}

	/**
	 * setup()
	 * Sends the client's message to server.
	 * Type 'bye' to terminate the program
	 */
	public void setup() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			ServerConnection serverCnn = new ServerConnection(socket, this);
			Thread handleThread = new Thread(serverCnn);
			handleThread.start();

			while (true) {
				BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
				String message;
				do {
					message = console.readLine();
					if(message != null) {
						out.println(message);
					} else break;
				} while (!message.equals("bye"));
			}
		} catch (IOException e) {
			System.out.println("I/O Error in Client :" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * handle()
	 * Prints the server's message.
	 * @param msg the server's message.
	 */
	public void handle(String msg){
		if(msg != null){
			System.out.println(msg);
		}else System.exit(0);
	}
}
