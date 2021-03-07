/**
 * Class Baron
 *
 * @author Tobias Celik
 * class creates the Baron Card using the Card Interface
 */
import java.util.*;

public class Baron extends CardFunctions implements Card {

    private int value = 3;
    private String name = "baron";
    private String detailDescription = "When you discard the Baron, choose another player still in the round. You and that player secretly\n" +
            "compare your hands. The player with the lower number is knocked out of the round. In case of a\n" +
            "tie, nothing happens";


    /**
     * getValue()
     * @return the card's value (1-8)
     */
    public int getValue() {
        return this.value;
    }

    /**
     * getName()
     * @return the name of this card
     */
    public String getName() {
        return this.name;
    }

    /**
     * getDetailDescription
     * @return the detailed description of this card
     */
    public String getDetailDescription() {
        return this.detailDescription;
    }

    /**
     * startEffect() implements Baron's card ability
     * Method lets the Player choose an Enemy out of a List
     * They secretly compare Cards. The Player whose Card has the higher value wins.
     * The Loser will be thrown out of the List of playersLeftInRound
     *
     * @param currentPlayer      the current player
     * @param playersLeftInRound list of all players who are in the round
     * @param allPlayers         list of all players
     * @param deck               card deck of the current game round
     * @return playersLeftInRound  list of all players who are in the round, excluding the loser of this round
     */
    public List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> allPlayers, Deck deck) throws InterruptedException {

        sendToSinglePlayer(currentPlayer, "You played the Baron. Choose a player." +
                "\nYou and the other Player secretly compare the cards on your hands." +
                "\nThe Player whose card has a higher value wins, the loser has to leave the round.");
        sendToSinglePlayer(currentPlayer, "You can only target a player who is not protected by the handmaid's effect.");

        /**
         * Asking the player which other player he or she wants to target
         * Looping until valid choice is made
         */
        String targetPlayerName;
        int targetPlayerIndex = 999;
        Player targetPlayer = null;
        List<Player> protectedPlayer = new ArrayList<>();
        int currentPlayerIndex = getPlayerIndex(currentPlayer.getPlayerName(), playersLeftInRound);
        int numberTargetPlayers = playersLeftInRound.size() - protectedPlayer.size() - 1;

        while (0 < numberTargetPlayers) {
            askForTargetPlayer(currentPlayer, playersLeftInRound);
            targetPlayerName = currentPlayer.getQueue().take();

            /**
             * checking if the the input equals the name of one of the players left in the round
             */
            if (checkIfPlayerInTheRound(targetPlayerName, playersLeftInRound)&& !checkIfPlayerInTheRound(targetPlayerName, protectedPlayer)) {
                targetPlayerIndex = getPlayerIndex(targetPlayerName, playersLeftInRound);
                targetPlayer = playersLeftInRound.get(targetPlayerIndex);
            } else {
                sendToSinglePlayer(currentPlayer, "Name not recognized! " +
                        "\nAre you sure you spelled the name correctly?");
                continue;
            }
            if (targetPlayer.getPlayedHandmaid()) {
                sendToSinglePlayer(currentPlayer, "This Player is protected by the handmaid until his/her next turn." +
                        "\nYou have to choose another player.");
                if(!checkIfPlayerInTheRound(targetPlayerName, protectedPlayer)){
                    protectedPlayer.add(targetPlayer);
                }
                numberTargetPlayers = playersLeftInRound.size() - protectedPlayer.size() - 1;
            }
            if (targetPlayerIndex == currentPlayerIndex) {
                sendToSinglePlayer(currentPlayer, "You can not play against yourself" +
                        "\nYou have to choose another player.");
            } else break;
        }
        if(numberTargetPlayers == 0)
        {
            sendToSinglePlayer(currentPlayer, "Ups, seems like all players are protected or out of the round"
                    + "\nUnfortunately, your turn is over without any effect.");
            return playersLeftInRound;
        }
        /**
         * Find out which card was the Baron-Card, so choose the other Card.
         */
        Card playerCard = null;
        if (!currentPlayer.getCard1().getName().equals("baron")) {
            playerCard = currentPlayer.getCard1();
        } else {
            playerCard = currentPlayer.getCard2();
        }

        /**
         *  Compare Block:
         *  If there is an enemy and a card
         *  Compare the player card with the enemy card
         *  Throw the loser out of the list of players
         */
        if (targetPlayer != null && playerCard != null) {
            Card enemyCard = targetPlayer.getCard1();

            String cardCurrentPlayer = currentPlayer.getPlayerName() + "'s card is a " + playerCard.getName() + " with value: " + playerCard.getValue();
            String cardEnemyPlayer = targetPlayer.getPlayerName() + "'s card is a " + enemyCard.getName() + " with value: " + enemyCard.getValue();

            sendToSinglePlayer(currentPlayer, cardCurrentPlayer);
            sendToSinglePlayer(currentPlayer, cardEnemyPlayer);
            sendToSinglePlayer(targetPlayer, cardCurrentPlayer);
            sendToSinglePlayer(targetPlayer, cardEnemyPlayer);

            int playerValue = playerCard.getValue();
            int enemyValue = enemyCard.getValue();
            int compCards = playerValue - enemyValue;
            int loserindex = -1000;

            if (compCards == 0) {
                sendToAll(allPlayers, "Tie! Nobody has to go!");
                return playersLeftInRound;

            } else if (compCards < 0) {
                loserindex = currentPlayerIndex;
                sendToAll(allPlayers, currentPlayer.getPlayerName() + " lost against " + targetPlayer.getPlayerName());
                sendToAll(allPlayers, "\n" + currentPlayer.getPlayerName() + " is out of the round!");

            } else if (compCards > 0) {
                loserindex = targetPlayerIndex;
                sendToAll(allPlayers, currentPlayer.getPlayerName() + " won against " + targetPlayer.getPlayerName()
                        + "\n" + targetPlayer.getPlayerName() + " is out of the Game!");
            }
            if (-1 < loserindex) {
                removePlayer(playersLeftInRound,loserindex);
            }

        } else {
            sendToAll(allPlayers, "If you see this message, something went wrong :-)");
        }

        return playersLeftInRound;
    }

    /**
     * @param playerName
     * @param playersLeftInRound
     * @return true if player is left in the round, else return false
     */
    private boolean checkIfPlayerInTheRound(String playerName, List<Player> playersLeftInRound) {
        for (Player player : playersLeftInRound) {
            if (player.getPlayerName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }
}
