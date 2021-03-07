/**
 * @author Alina Klessinger
 * Class Princess
 * This class creates the Princess Card using the Card Interface
 */

import java.util.List;

public class Princess extends CardFunctions implements Card {

    private int value = 8;
    private String name = "princess";
    private String detailDescription = "If you discard the princess, you are out of the round";

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
     * startEffect()
     * This method implements the Princess' card ability
     * If a player discards the Princess, he/she is out of the round
     * @param currentPlayer         the current player
     * @param playersLeftInRound    list of all players who are in the round
     * @param playersInGame         list of all players
     * @param deck                  card deck of the current game round
     * @return players left in round
     */
    public List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> playersInGame, Deck deck) {

        sendToSinglePlayer(currentPlayer, "You've discarded a Princess \nSorry, you are out of the round!");

        int currentPlayerIndex;
        currentPlayerIndex= getPlayerIndex(currentPlayer.getPlayerName(),playersLeftInRound);
        removePlayer(playersLeftInRound, currentPlayerIndex);
        return playersLeftInRound;
    }
}