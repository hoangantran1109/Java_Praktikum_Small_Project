/**
 * @author Alina Klessinger (provided first draft)
 * team finished in multiple coworking sessions
 * Class LoveLetterGame
 * contains the logic of the Love Letter Game
 */

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class LoveLetterGame implements Runnable{
    List<Player> playersInGame;
    List<Player> playersLeftInRound;
    Player currentPlayer;
    public List<String> playerNames = new LinkedList<>();

    /**
     * create()
     * Player Setup
     * to initialize a new game
     */
    public void create(PrintWriter printWriter, String name, BlockingQueue<String> queue){
        Player player = new Player(name, printWriter, queue);
        playersInGame = new ArrayList<Player>();
        playersLeftInRound = new ArrayList<Player>();
        playersInGame.add(player);
        playerNames.add(name);
    }

    /**
     * join()
     * Player Setup
     * to join an existing game
     */
    public void join(PrintWriter printWriter, String name, BlockingQueue<String> queue){
        Player playerToAdd = new Player(name, printWriter, queue);

        for(Player player : playersInGame){
            if(player.getPlayerName().equals(name)){
                sendToSinglePlayer(playerToAdd,"You are already added to the players for the next game\n");
                return;
            }
        }
        if(playersInGame.size() == 4) {
            sendToSinglePlayer(playerToAdd,"There is already the maximum number of four players in the new game\n");
        } else {
            playersInGame.add(playerToAdd);
            playerNames.add(name);
            sendToAll(playerToAdd.getPlayerName() + " joined the game.\n");
        }
    }

    /**
     * run()
     * to start an existing game
     */
    public void run() {
        /**
         * First round starts with the youngest, then winner goes first
         */
        if (playersInGame.size() < 2) {
            sendToAll("You need at least two players to start the game\n");
        } else {
            sendToAll("Usually the player who was last on a date would start the game.\n"
                    + "Since we all study computer science we assume that none was ever on a date and the youngest will start. " +
                    "\nPlease enter your age when asked\n");

            int lowestAge = 99;
            Player startPlayer = null;

            /**
             * identify which player is the youngest
             */
            for (Player player : playersInGame) {

                currentPlayer = player;
                sendToSinglePlayer(player,"What is your age? Enter the age like this:\n/[age]\n");
                int age;

                while (true) {
                    try {
                        String line;

                        line = player.getQueue().take();

                        age = Integer.parseInt(line);
                        if (age < lowestAge) {
                            startPlayer = player;
                            lowestAge = age;
                        }
                        player.getOut().println("Waiting for input from other players...\n");
                        break;
                    }
                    catch(Exception e) {
                        sendToSinglePlayer(player,"Please enter a whole number.\n");
                    }
                }
            }

            /**
             * 1. game loop where each round of a game starts
             * a round ends when all players' turns are finished and a player either wins a point,
             * or wins the game
             */
            while (true) {

                /**
                 * Card Setup
                 * Shuffle card deck
                 */
                Deck deck = new Deck(playersInGame.size());
                Collections.shuffle(playersInGame);

                if(playersInGame.size() == 2){
                    sendToAll("Three extra cards were excluded from the game, they are:\n");
                    for(Card extraDiscardedCard : deck.getDiscardedCardsForTwoPlayers()){
                        sendToAll(extraDiscardedCard.getName() + "\n");
                    }
                }

                /**
                 * Deal out the cards - each player gets one card
                 */
                for (Player player : playersInGame) {
                    player.setCard1(deck.drawCard());
                }

                playersLeftInRound.clear();
                for (Player player : playersInGame) {
                    playersLeftInRound.add(player);
                }

                Collections.swap(playersInGame, 0, playersInGame.indexOf(startPlayer));

                currentPlayer = playersInGame.get(0);

                /**
                 * 2. game loop
                 * start a new round
                 * every turn of one round
                 */
                sendToAll("Let's start a new round!\n");
                /**
                 * set the choice of the player neutral
                 */
                String choice = "";

                while (true) {

                    sendToAll("It's " + currentPlayer.getPlayerName() + "'s turn.\n");

                    /**
                     * protection by the handmaid ends when its players turn
                     */
                    currentPlayer.setPlayedHandmaid(false);

                    /**
                     * deal out the 2nd card to current player
                     */
                    currentPlayer.setCard2(deck.drawCard());

                    Card card1 = currentPlayer.getCard1();
                    Card card2 = currentPlayer.getCard2();

                    while (true) {

                        /**
                         * let the player choose a card to discard
                         */
                        while(true){
                            sendToSinglePlayer(currentPlayer,"Choose a card to play\n!"
                                    + "\nType /1 for "
                                    + card1.getName()
                                    + "\nor /2 for "
                                    + card2.getName());

                            /**
                             * scan player choice
                             */
                            try {
                                choice = currentPlayer.getQueue().take();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            /**
                             *  if player has a countess in hand, check if the same player has also a prince or a king
                             *  if this is true, player has to choose countess to discard
                             */
                            if (card1.getName().equals("countess")) {
                                if (card2.getName().equals("prince") || card2.getName().equals("king")) {
                                    if (choice.equals("2")) {
                                        sendToSinglePlayer(currentPlayer, "You must choose the Countess. \nPlease enter a new choice\n");
                                    } else break;
                                }
                                else break;
                                /**
                                 * same loop again - other way round
                                 */
                            } else if (card2.getName().equals("countess")) {
                                if (card1.getName().equals("prince") || card1.getName().equals("king")) {
                                    if (choice.equals("1")) {
                                        sendToSinglePlayer(currentPlayer,"You must choose the Countess. \nPlease enter a new choice\n");
                                    }
                                    else break;
                                }
                                else break;
                            }
                            else break;
                        }

                        /**
                         * if player doesn't have a countess with either a prince or a king,
                         * discard player's choice and execute the card's effect
                         */
                        if (choice.equals("1")) {
                            try {
                                playersLeftInRound = card1.startEffect(currentPlayer, playersLeftInRound, playersInGame, deck);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            currentPlayer.setCard1(card2);
                            currentPlayer.setCard2(null);
                            break;
                        } else if (choice.equals("2")) {
                            try {
                                playersLeftInRound = card2.startEffect(currentPlayer, playersLeftInRound, playersInGame, deck);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            currentPlayer.setCard2(null);
                            break;
                        }
                    }

                    /**
                     * a player wins the round when the all other player are knocked out
                     * announce the winner
                     * then start a new round with startplayer = winner from last round
                     */
                    if (playersLeftInRound.size() == 1) {
                        sendToAll("The round is over:\nThe following received a token: " + playersLeftInRound.get(0).getPlayerName() + "\n");
                        startPlayer = playersLeftInRound.get(0);
                        playersLeftInRound.get(0).increaseScore();
                        break;
                    }

                    /**
                     * player with the highest card value can receive a token when the card deck is empty
                     */
                    int highestValue = 0;
                    List<Player> winners = playersLeftInRound;

                    if (deck.cards.isEmpty()) {
                        /**
                         * identify the highest card value, increase score and announce winner
                         */
                        for (Player player : winners) {
                            if (player.getCard1().getValue() > highestValue) {
                                highestValue = player.getCard1().getValue();
                            }
                        }
                        for (Player player : winners) {
                            if (player.getCard1().getValue() < highestValue) {
                                removePlayer(winners, getPlayerIndex(player.getPlayerName(), winners));
                            }
                        }
                        if (winners.size() > 1) {
                            int highestValueDiscardPile = 0;
                            for (Player player : winners) {
                                highestValueDiscardPile = player.getDiscardPileTotalValue();
                            }
                            for (Player player : winners) {
                                if (player.getDiscardPileTotalValue() < highestValueDiscardPile) {
                                    removePlayer(winners, getPlayerIndex(player.getPlayerName(), winners));
                                }
                            }
                        }
                        sendToAll("The round is over:\nThe following received a token:");
                        for (Player player : winners) {
                            player.increaseScore();
                            sendToAll("\n" + player.getPlayerName());
                            startPlayer = player;
                        }
                        break;
                    }

                    /**
                     * nobody has won so far, set next player
                     */
                    if (playersLeftInRound.indexOf(currentPlayer) == playersLeftInRound.size() - 1) {
                        currentPlayer = playersLeftInRound.get(0);
                    } else {
                        currentPlayer = playersLeftInRound.get(playersLeftInRound.indexOf(currentPlayer) + 1);
                    }
                }
                for (Player player : playersInGame) {
                    sendToAll(player.getPlayerName() + "'s score is " + player.getScore() + "\n");
                }

                /**
                 * identify the winner of the game
                 * a player has won the game when he reached the score of 4 with 4 players
                 * a player has won the game when he reached the score of 5 with 3 players
                 * a player has won the game when he reached the score of 7 with 2 players
                 */
                String winningMessage = " has won the game, congratulations!!!\n" +
                        "\nType /start to play a new game with the same players" +
                        "\n or type /quit to stop the game";
                boolean foundFinalWinner = false;
                for (Player player : playersInGame) {
                    if (playersInGame.size() == 4 && player.getScore() == 4) {
                        sendToAll(player.getPlayerName() + winningMessage);
                        foundFinalWinner = true;
                        break;
                    }
                    if (playersInGame.size() == 3 && player.getScore() == 5) {
                        sendToAll(player.getPlayerName() + winningMessage);
                        foundFinalWinner = true;
                        break;
                    }
                    if (playersInGame.size() == 2 && player.getScore() == 7) {
                        sendToAll(player.getPlayerName() + winningMessage);
                        foundFinalWinner = true;
                        break;
                    }
                }
                if (foundFinalWinner) {
                    break;
                }
            }

            /**
             * game is over, display the players final score
             * reset all played handmaids
             */
            for (Player player : playersInGame) {
                sendToAll(player.getPlayerName() + "'s score is " + player.getScore() + "\n");
                player.setPlayedHandmaid(false);
            }
        }
    }

    /**
     * sendToAll()
     * sends message to all players
     */
    public void sendToAll(String message) {
        for (Player p : playersInGame) {
            sendToSinglePlayer(p, message);
        }
    }

    /**
     * sendToSinglePlayer()
     * @param messageReceiver   the player who will receive the message
     * @param message           the text body
     * sends a message to one player and prepends [Game] before each message
     */
    public void sendToSinglePlayer(Player messageReceiver, String message) {
        messageReceiver.getOut().println("[Game] " + message);
    }

    /**
     * getCurrentPlayer()
     * @return the player whose turn it is
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * removePlayer()
     * @param playersLeftInRound    player who are left in round
     * @param index                 index of a player who will be removed
     * @return removes player from a list
     */
    public List<Player> removePlayer(List<Player> playersLeftInRound, int index){
        Player enemy = playersLeftInRound.get(index);
        for(Iterator<Player> player = playersLeftInRound.iterator(); player.hasNext();){
            Player checkPlayer= player.next();
            if(checkPlayer.equals(enemy)){
                player.remove();
            }
        }
        return playersLeftInRound;
    }

    /**
     * getPlayerIndex()
     * @param playerName            name of player
     * @param playersLeftInRound    players who are left in round
     * @return the index of player with a certain name
     */
    public int getPlayerIndex(String playerName, List<Player> playersLeftInRound) {
        int playerIndex = 999;
        for (Player player : playersLeftInRound) {
            if (player.getPlayerName().equals(playerName)) {
                playerIndex = playersLeftInRound.indexOf(player);
            }
        }
        return playerIndex;
    }
}
