/**
 * Class Handmaid using the Card Interface.
 * @author Hoang An Tran
 * This Class creates the Handmaid Card which contains functionality and attributes.
 */
import java.util.List;

public class Handmaid extends CardFunctions implements Card {
    private int value = 4;
    private String name = "handmaid";
    private String detailDescription = "When you played the handmaid, you are immune to the effects of " +
                                    "other player's card until your next turn ";

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
     * Special card function
     *
     * @param currentPlayer      the current player
     * @param playersLeftInRound the players left in round
     * @param deck               the deck
     * @return List<Player>  returns the players left in round
     */
    public List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> playersInGame, Deck deck) {
        currentPlayer.setPlayedHandmaid(true);
        sendToSinglePlayer(currentPlayer, getDetailDescription());
        return playersLeftInRound;
    }
}
