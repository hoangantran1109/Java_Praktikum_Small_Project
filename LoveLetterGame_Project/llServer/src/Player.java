/**
 * @author Julian Wiemer
 * Class Player
 * This class implements Player-specific attributes and methods
 */

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * The type Player.
 */
public class Player {

    private int tokenScore = 0;

    private final String playerName;

    private boolean playedHandmaid = false;

    private List<Card> discardPile;

    private int discardPileTotalValue = 0;

    private Card card1;
    private Card card2;

    private final PrintWriter out;

    private BlockingQueue<String> queue;

    /**
     * Constructor Player()
     * @param playerName player's username
     * @param out        PrintWriter for messages to Client
     * @param queue      the queue
     */
    public Player(String playerName, PrintWriter out, BlockingQueue<String> queue) {
        this.playerName = playerName;
        this.out = out;
        this.queue = queue;
    }

    /**
     * Increase score.
     */
    public void increaseScore() {
        this.tokenScore++;
    }

    /**
     * Gets score.
     * @return the score
     */
    public int getScore() {
        return tokenScore;
    }

    /**
     * Sets played handmaid.
     * @param playedHandmaid the played handmaid
     */
    public void setPlayedHandmaid(boolean playedHandmaid) {
        this.playedHandmaid = playedHandmaid;
    }

    /**
     * Gets played handmaid.
     * @return the played handmaid
     */
    public boolean getPlayedHandmaid() {
        return playedHandmaid;
    }

    /**
     * Sets card 1.
     * @param card1 the card 1
     */
    public void setCard1(Card card1) {
        this.card1 = card1;
    }

    /**
     * Gets card 1.
     * @return the card 1
     */
    public Card getCard1() {
        return card1;
    }

    /**
     * Sets card 2.
     * @param card2 the card 2
     */
    public void setCard2(Card card2) {
        this.card2 = card2;
    }

    /**
     * Gets card 2.
     * @return the card 2
     */
    public Card getCard2() {
        return card2;
    }

    /**
     * Get discard pile list.
     * @return the list
     */
    public List<Card> getDiscardPile(){
        return discardPile;
    }

    /**
     * Get discard pile total value int.
     * @return the int
     */
    public int getDiscardPileTotalValue(){
        return discardPileTotalValue;
    }

    /**
     * Add to discard pile.
     * @param card the card
     */
    public void addToDiscardPile(Card card){
        discardPile.add(card);
        discardPileTotalValue += card.getValue();
    }

    /**
     * Gets player name.
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets out.
     * @return the out
     */
    public PrintWriter getOut() {
        return out;
    }

    /**
     * Gets queue.
     * @return the queue
     */
    public BlockingQueue<String> getQueue() {
        return queue;
    }
}
