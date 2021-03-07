/**
 * @author Hoang An Tran
 * Class Guard
 * This class creates the Guard card which contains functionality and attributes.
 */

import java.util.Arrays;
import java.util.List;

public class Guard extends CardFunctions implements Card {

    String[] card = {"priest", "baron", "handmaid", "prince", "king", "countess", "princess"};

    private int value = 1;
    private String name = "guard";
    private String detailDescription = "When you played the guard, choose a player and guess their card." +
            "\nIf the chosen player has this card in their hand, that player is " +
            "knocked out of the round.";

    /**
     * getValue()
     * @return the card's value (1-8)
     */
    public int getValue() {
        return value;
    }

    /**
     * getName()
     * @return the card name
     */
    public String getName() {
        return name;
    }

    /**
     * getDetailDescription()
     * @return the card effect description
     */
    public String getDetailDescription() {
        return detailDescription;
    }

    /**
     * startEffect()
     * The Special card function of the Guard class lets the Player choose any player (except yourself) and name a card (except Guard).
     * If the chosen player has this card in their hand, that player is knocked out of the round.
     * If the chosen player has played Handmaid, this card is discarded without effect.
     * @param currentPlayer      the current player
     * @param playersLeftInRound the players left in round
     * @param deck               the deck
     * @return List<Player>      the players left in round
     */
    public List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> playersInGame, Deck deck) throws InterruptedException {
        /**
         * while-block runs in an infinite loop until a player has been chosen
         * then starts the guard's card effect
         */
        while (true) {

            sendToSinglePlayer(currentPlayer, getDetailDescription());


            sendToSinglePlayer(currentPlayer, "Name the player you want to target: " +
                            "\n/[player]" );

            /**
             * list of Players except the current player and the player who are protected by the handmaid
             */
            for (Player player : playersLeftInRound) {
                if(!player.getPlayerName().equals(currentPlayer.getPlayerName()))
                    sendToSinglePlayer(currentPlayer, player.getPlayerName());
            }

            String playerChoice = currentPlayer.getQueue().take();

            if (playerChoice.equals(currentPlayer.getPlayerName())) {
                sendToSinglePlayer(currentPlayer, "Please choose another player!");
            } else {
                for (Player targetPlayer : playersLeftInRound) {
                    if (playerChoice.equals(targetPlayer.getPlayerName())) {
                        if (targetPlayer.getPlayedHandmaid()) {
                            sendToSinglePlayer(currentPlayer, "Ups, seems like all players are protected or out of the round"
                                    + "\nUnfortunately, your turn is over without any effect.");
                            return playersLeftInRound;
                        } else {
                            guessCard(currentPlayer,targetPlayer, playersLeftInRound,playersInGame);
                            return playersLeftInRound;
                        }
                    }
                }
                sendToSinglePlayer(currentPlayer, "Name not recognized! " +
                        "\nAre you sure you spelled the name correctly?");
            }
        }
    }

    /**
     * guessCard()
     * @param currentPlayer
     * @param targetPlayer
     * @param playersLeftInRound
     * @param playersInGame
     * The player tries to guess the target player's card.
     * @return list of the players left in the round
     * @throws InterruptedException
     */
    private List<Player> guessCard(Player currentPlayer,Player targetPlayer,List<Player> playersLeftInRound,List<Player> playersInGame) throws InterruptedException {
        int targetPlayerIndex;
        while (true) {
            sendToSinglePlayer(currentPlayer, "Please guess the target player's card:");

            sendToSinglePlayer(currentPlayer, Arrays.toString(card));

            String cardChoice = currentPlayer.getQueue().take();

            if (cardChoice.equals(this.name)) {
                sendToSinglePlayer(currentPlayer, "You cannot choose the guard card.");
            } else if (cardChoice.equals(targetPlayer.getCard1().getName())) {
                sendToSinglePlayer(currentPlayer, "You've guessed correctly");
                sendToAll(playersInGame,"Player " + targetPlayer.getPlayerName()
                        + " is out of the round!");
                targetPlayerIndex = getPlayerIndex(targetPlayer.getPlayerName(),playersLeftInRound);
                removePlayer(playersLeftInRound,targetPlayerIndex);
                break;

            } else if (Arrays.asList(card).contains(cardChoice)){
                sendToSinglePlayer(currentPlayer, "Player " + targetPlayer.getPlayerName()
                        + " does not have that card.");
                break;
            } else {
                sendToSinglePlayer(currentPlayer, "Sorry, please choose again");
            }
        }
        return playersLeftInRound;
    }
}