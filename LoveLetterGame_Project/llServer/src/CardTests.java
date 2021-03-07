/**
 * @author simon wiethase
 * Class CardTests
 * this class provides tests for cardvalues and correct deckvalues
 */

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTests {

    /**
     * whenCardValueIsAsked_returnsCorrectValues()
     * compares the card value getters with their expected values
     */
    @Test
    public void whenCardValueIsAsked_returnsCorrectValues(){

        //arrange
        //act
        //assert
        assertEquals(new Guard().getValue(), 1);
        assertEquals(new Priest().getValue(), 2);
        assertEquals(new Baron().getValue(), 3);
        assertEquals(new Handmaid().getValue(), 4);
        assertEquals(new Prince().getValue(), 5);
        assertEquals(new King().getValue(), 6);
        assertEquals(new Countess().getValue(), 7);
        assertEquals(new Princess().getValue(), 8);
    }

    /**
     * whenDeckIsCreated_HasCorrectSize()
     * compares the deck size with the expected deck size based on the number of players
     */
    @Test
    public void whenDeckIsCreated_HasCorrectSize(){

        //arrange
        Deck deckForTwoPlayers = new Deck(2);
        Deck deckForThreePlayers = new Deck(3);
        Deck deckForFourPlayers = new Deck(4);

        //act
        int deckSizeForTwoPlayers = deckForTwoPlayers.cards.size();
        int deckSizeForThreePlayers = deckForThreePlayers.cards.size();
        int deckSizeForFourPlayers = deckForFourPlayers.cards.size();

        //assert
        assertEquals(deckSizeForTwoPlayers, 12);
        assertEquals(deckSizeForThreePlayers, 15);
        assertEquals(deckSizeForFourPlayers, 15);
    }
}
