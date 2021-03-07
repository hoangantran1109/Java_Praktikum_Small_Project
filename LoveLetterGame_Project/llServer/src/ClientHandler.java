/**
 * @author the whole team
 * class ClientHandler
 * provides the client's dialogue and lovelettergame-interactions
 */

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class ClientHandler implements Runnable {

    private String name;
    private Socket socket;
    public Scanner in;
    public PrintWriter out;
    public List<String> names;
    public List<PrintWriter> chatUsers;
    private static LoveLetterGame loveLetterGame;
    private BlockingQueue<String> messagesToGame = new SynchronousQueue<>();
    private static Thread gameThread;
    private static String helpMessage =
        "\n- Create a LoveLetterGame with /create" +
        "\n- Join an existing game with /join"+
        "\n- Start an existing game with /start"+
        "\n- Send private messages with /to [name]" +
        "\n- Stop an existing game with /quit";

    /**
     * ClientHandler()
     * @param s
     * @param names
     * @param chatUsers
     * constructor for the ClientHandler
     */
    public ClientHandler(Socket s, List<String> names, List<PrintWriter> chatUsers) {
        socket = s;
        this.names = names;
        this.chatUsers = chatUsers;
    }

    /**
     * run()
     * implements the run method of the Runnable interface
     */
    public void run() {
        try {
            /**
             * instantiate scanner and printwriter (autoFlush) for in and outgoing messages
             */
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            if(!names.isEmpty()){
                sendToOne(out,"Connected ChatUsers:" + names);
            } else {
                sendToOne(out,"No Users online!");
            }

            /**
             * request username and proof uniqueness
             */
            sendToOne(out,"Choose a username: ");

            if (in.hasNextLine()) {
                name = in.nextLine();
                while (name != null) {
                    synchronized (names) {
                        if (name != "" && !names.contains(name) && !name.equals("bye")) {
                            names.add(name);
                            break;
                        } else {
                            sendToOne(out, "Username not valid or already taken. Please choose a unique username: ");
                            if (in.hasNextLine()) {
                                name = in.nextLine();
                            }
                        }
                    }
                }
            }

            /**
             * welcome and information message
             */
            sendToOne(out,"Welcome " + name + "!\n" + helpMessage);
            sendToOne(out, "type /help to show this message again.\n");

            /**
             * tell other chatUsers that a new user has joined
             */
            if(name != null) {
                sendToAll("New chatuser '" + name + "' has joined the room.");
            }
            chatUsers.add(out);

            while (true) {
                if (in.hasNext()) {
                    String input = in.nextLine();
                    if (input.equals("bye")) {
                        break;
                    } else if (input.indexOf("/to ") == 0) {
                        sendMessage(input);
                        continue;
                    } else if (input.equals("/create")) {
                        if(loveLetterGame == null){
                            loveLetterGame = new LoveLetterGame();
                            loveLetterGame.create(out, name, messagesToGame);
                            sendToAll(name + " created a game. Other players can now join with /join");
                        } else {
                            sendToOne(out,"There is already a game created. You can now join with /join");
                        }
                    } else if (input.equals("/start")) {
                        if(loveLetterGame == null){
                            sendToOne(out,"please create a game first with /create");
                        } else {
                            if(loveLetterGame.playersInGame.size() >= 2 && loveLetterGame.playersInGame.size() <= 4){
                            gameThread = new Thread(loveLetterGame);
                            gameThread.start();
                            }
                            else{
                                sendToOne(out,"you need at least two players to start the game");
                                sendToOne(out,"or you can type /join to join");
                            }
                        }
                    } else if (input.equals("/join")){
                        if(loveLetterGame == null){
                            sendToOne(out, "please create a game first with /create");
                        } else {
                            if(gameThread==null){
                            loveLetterGame.join(out, name, messagesToGame);
                            } else {
                                sendToOne(out, "Game is already started.");
                            }

                        }
                    } else if (input.equals("/help")){
                        sendToOne(out, helpMessage);
                    } else if (input.equals("/quit") && loveLetterGame != null) {
                        loveLetterGame = null;
                        gameThread = null;
                        sendToAll("The game has been canceled by " + name);
                    } else if (input.indexOf("/") == 0 && gameThread != null && loveLetterGame.getCurrentPlayer().getPlayerName() == name) {
                        String subInput = input.substring(1);
                        messagesToGame.put(subInput);
                    } else {
                        sendToAll(name + ": " + input);
                    }
                } else {

                    /**
                     * Removing the username when a client is suddenly canceled
                     */
                    if(loveLetterGame.playerNames.contains(name)) {
                        sendToAll("The game has been canceled by " + name);
                        loveLetterGame = null;
                        gameThread = null;
                        names.remove(name);
                        break;
                    }

                    if(name!=null){
                    names.remove(name);
                    break;
                    }
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * if chatUser input is "bye" remove the user from the chat and inform the other users
         */
        finally {
            if(loveLetterGame != null && loveLetterGame.playerNames.contains(name)) {
                sendToAll("The game has been canceled by " + name);
                loveLetterGame = null;
                gameThread = null;
            }
            if (out != null) {
                chatUsers.remove(out);
            }
            if (name != null && !name.equals("bye")) {
                names.remove(name);
                sendToAll(name + " has left the room.");
            }
            try {
                socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sendToAll()
     * @param message
     * sends a message to all Users
     * and puts [Chat] in front of every message
     */
    public void sendToAll(String message){
        for (PrintWriter chatUser : chatUsers) {
            sendToOne(chatUser, message);
        }
    }

    /**
     * sendToOne()
     * @param message
     * sends a message to one User
     * and puts [Chat] in front of every message
     */
    public void sendToOne(PrintWriter out, String message) {
        out.println("[Chat] " + message);
    }

    /**
     * method void sendMessage()
     * Sends a private message to one specified client only.
     * @param command client-transmitted command: /to [Username] [Message]
     */
    public void sendMessage(String command) {
        int start = command.indexOf(" ") +1;
        int end = command.indexOf(" ", start);

        if(end != -1) {
            String recipient = command.substring(start, end);
            if(!names.contains(recipient)) {
                sendToOne(out,"User '" + recipient + "' does not exist. Please choose a valid username.");
                return;
            }
                String message = command.substring(++end);
            if(message.isBlank()) {
                sendToOne(out,"You can only send a non-blank message.");
                return;
            }
                PrintWriter outRecipient = chatUsers.get(names.indexOf(recipient));
                outRecipient.println("User '" + name + "' whispered to you: " + message);
                sendToOne(out,"Message sent to user '" + recipient + "': " + message);
        } else {
            sendToOne(out,"You can only send a non-empty message.");
        }
    }
}
