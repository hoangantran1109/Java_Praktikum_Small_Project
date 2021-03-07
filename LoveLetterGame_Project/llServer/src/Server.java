/**
 * @author Alina Klessinger
 * Class Server
 * this class provides the server for the client server based chat
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    /**
     * All client names, so we can check for duplicates upon registration
     */
    private static List<String> names = new LinkedList<>();

    /**
     * The list of all the print writers for all the clients, used for broadcast.
     */
    private static List<PrintWriter> chatUsers = new LinkedList<>();

    /**
     * main()
     * @param args   arguments from the commandline
     */
    public static void main(String[] args) {

        System.out.println("The chat server is running...");

        ExecutorService pool = Executors.newFixedThreadPool(500);

        try (ServerSocket listener = new ServerSocket(8080)) {
            while (true) {
                pool.execute(new ClientHandler(listener.accept(), names, chatUsers));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

