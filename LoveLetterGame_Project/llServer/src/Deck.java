/**
 * @author Simon Wiethase
 * Class Deck
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    List<Card> cards;
    List <Card> discardedCardsForTwoPlayers;
    private Card burnedCard;

    public Card drawCard(){
        Card topCard = cards.get(cards.size()-1);
        cards.remove(cards.size()-1);
        return topCard;
    }

    /**
     * isEmpty()
     * @return whether the deck is empty
     */
    public boolean isEmpty(){
        return cards.isEmpty();
    }

    /**
     * Deck()
     * @param totalPlayers
     * constructor for the deck
     * fills and shuffles the deck and burns 1 to 4 cards according to the number of players
     */
    public Deck(int totalPlayers){
        populateDeck();
        shuffleDeck();

        if(totalPlayers < 3){
            discardedCardsForTwoPlayers = new ArrayList<>();
            discardedCardsForTwoPlayers.add(drawCard());
            discardedCardsForTwoPlayers.add(drawCard());
            discardedCardsForTwoPlayers.add(drawCard());
        }
        burnedCard = drawCard();
    }

    private void populateDeck(){
        cards = new ArrayList<Card>();
        addCards(new Countess(), 1);
        addCards(new King(), 1);
        addCards(new Princess(), 1);
        addCards(new Prince(), 2);
        addCards(new Handmaid(), 2);
        addCards(new Baron(), 2);
        addCards(new Priest(), 2);
        addCards(new Guard(), 5);
        shuffleDeck();
    }

    /**
     * getBurnedCard()
     * @return gets the card that was burned at the beginning of the round (prince uses this card)
     */
    public Card getBurnedCard(){
        return burnedCard;
    }

    /**
     * getDiscardedCardsForTwoPlayers
     * @return the additional burned cards if only two players play
     */
    public List<Card> getDiscardedCardsForTwoPlayers(){
        return discardedCardsForTwoPlayers;
    }

    private void shuffleDeck() {
        Collections.shuffle(cards);
    }

    private void addCards(Card card, int repeatAdding){
        for(int i=0; i<repeatAdding; i++){
            cards.add(card);
        }
    }
}
