/**
 * @author Ludwig Kraus
 * Class Countess
 * This class implements the card "Countess" from the game "Love Letter"
 */
import java.util.List;

public class Countess extends CardFunctions implements Card {

    private int value = 7;
    private String name = "countess";
    private String  detailDescription = "If you hold this card and either the king or prince " +
            "\nin your hand, you must discard the countess";

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
     * This function simply informs all players that a countess has been discarded.
     * Nothing else happens.
     */
    public List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> playersInGame, Deck deck) {
        sendToAll(playersInGame, currentPlayer.getPlayerName() + " has played a Countess. Nothing else happens. " +
                "What an unexciting card...");
        return playersLeftInRound;
    }
}
