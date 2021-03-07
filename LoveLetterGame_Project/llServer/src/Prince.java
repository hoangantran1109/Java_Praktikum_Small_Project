/**
 * This class implements the card "Prince" from the game "Love Letter"
 * @author Luwig Kraus
 */

import java.util.List;

public class Prince extends CardFunctions implements  Card  {

    private int value = 5;
    private String name = "prince";
    private String detailDescription = "When you discard the prince, choose any player (you can choose yourself)." +
            "\nThe chosen player discards his or her card and draws a new one.";

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
     * startEffect() implements the Prince's card ability
     * The special function of the Prince class lets the Player choose any player (including himself).
     * The chosen player then discards their card (without effect unless it is the Princess) and draws
     *  a new card.
     *
     * @param currentPlayer      the current player
     * @param playersLeftInRound list of all players who are in the round
     * @param playersInGame         list of all players
     * @param deck               card deck of the current game round
     * @return playersLeftInRound  list of all players who are in the round, excluding the loser of this round
     */
    public List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> playersInGame, Deck deck)
            throws InterruptedException {

        sendToSinglePlayer(currentPlayer,"You've played the prince. Name the player you want to target:\n..you can also choose yourself" +
                "\nThe chosen Player has to discard his hand and draw a new card.");
        sendToSinglePlayer(currentPlayer,"You can only target a player who is not protected by the handmaid's effect.");

        /**
         * Asking the player which other player he or she wants to target
         * Looping until valid choice is made
         */
        String targetPlayerName;
        int targetPlayerIndex;
        Player targetPlayer;
        while (true) {
            askForTargetPlayer(currentPlayer, playersLeftInRound);
            targetPlayerName = currentPlayer.getQueue().take();

            /**
             * checks if the the input equals the name of one of the players left in the round
             */
            if (checkIfPlayerInTheRound(targetPlayerName, playersLeftInRound)) {
                targetPlayerIndex = getPlayerIndex(targetPlayerName, playersLeftInRound);
                targetPlayer = playersLeftInRound.get(targetPlayerIndex);
            } else {
                sendToSinglePlayer(currentPlayer,"Name not recognized! " +
                        "\nAre you sure you spelled the name correctly?");
                continue;
            }
            if (targetPlayer.getPlayedHandmaid()) {
                sendToSinglePlayer(currentPlayer,"This Player is protected by the handmaid's effect until his/her next turn." +
                        "\nYou have to choose another player.");
            } else break;
        }

        /**
         * Reaction to the players choice
         * If the player has to discard a princess he or she is knocked out of the round
         * Otherwise he or she discards their hand without any effects and draws a new card from the deck
         * If the deck is empty, the player draws the burned card
         */

        String newCardPrivateMessage = "The prince forced you to discard your hand and to draw a new card." +
                "\nYour new card is a " + targetPlayer.getCard1().getName();
        String newCardPublicMessage = "The prince card forced " + targetPlayer.getPlayerName() +
                " to discard his/her hand and to draw a new card.";

        /**
         * If the player chose himself (card2 is not null) make sure his card2 is the prince with swapCards()
         */
        boolean swappedCards = false;
        if (targetPlayer.getCard2() != null && !targetPlayer.getCard2().getName().equals("prince")) {
            swapCards(targetPlayer);
            swappedCards = true;
        }

        /**
         * If the target player has to discard a princess, he or she is kicked out of the round
         */
        if (targetPlayer.getCard1().getName().equals("princess")) {
            sendToSinglePlayer(targetPlayer,"The prince forced you to discard the princess." +
                    "\nYou are out of the round now.");
            sendToAll(playersInGame, targetPlayer.getPlayerName() + " was forced by the prince to discard a princess." +
                    "\nTherefore he was kicked out of the round.");
            removePlayer(playersLeftInRound,targetPlayerIndex);
        } else if (deck.isEmpty()) {
            targetPlayer.setCard1(deck.getBurnedCard());
            sendToSinglePlayer(targetPlayer,newCardPrivateMessage);
            sendToAll(playersInGame, newCardPublicMessage);
        } else {
            targetPlayer.setCard1(deck.drawCard());
            sendToSinglePlayer(targetPlayer, newCardPrivateMessage);
            sendToAll(playersInGame, newCardPublicMessage);
        }
        if (swappedCards) {
            swapCards(targetPlayer);
        }
        return playersLeftInRound;
    }

    /**
     * checkIfPlayerInTheRound()
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

    /**
     * swapCards()
     * swap card1 and card2 of a player
     * @param player
     */
    private void swapCards(Player player) {
        Card tempCard = player.getCard1();
        player.setCard1(player.getCard2());
        player.setCard2(tempCard);
    }
}


