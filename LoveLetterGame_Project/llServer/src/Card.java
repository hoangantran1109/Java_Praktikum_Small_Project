import java.util.List;

/**
 * Interface Card
 *
 * @author Tobias Celik
 * This Interface is the base for all Cards and ensures a compability of Cards during the game
 */
public interface Card {

    int getValue();

    String getName();

    String getDetailDescription();

    /**
     * starts the specific effect of the card which was played by the current player
     * @param currentPlayer
     * @param playersLeftInRound
     * @param playersInGame
     * @param deck
     * @return
     */
    List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> playersInGame, Deck deck)
            throws InterruptedException;
}

