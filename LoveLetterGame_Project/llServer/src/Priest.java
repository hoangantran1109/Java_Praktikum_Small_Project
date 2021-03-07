/**
 * @author Simon Wiethase
 * Class Priest
 */

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ServerConnection.
 */
public class Priest extends CardFunctions implements Card{
    int value = 2;
    String name = "priest";
    String detailDescription = "When you discard the priest, you can secretly look at another players hand.";

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
     * startEffect() implements Priest's card ability
     * Method lets the Player choose a player and shows his hand
     *
     * @param currentPlayer      the current player
     * @param playersLeftInRound list of all players who are in the round
     * @param playersInGame         list of all players
     * @param deck               card deck of the current game round
     * @return playersLeftInRound  list of all players who are in the round, excluding the loser of this round
     */
    public List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> playersInGame, Deck deck)
            throws InterruptedException {

        List<Player> playersUnprotected = new ArrayList<>();

        for(Player player : playersLeftInRound){
            if(!player.getPlayedHandmaid() && !player.getPlayerName().equals(currentPlayer.getPlayerName())) {
                playersUnprotected.add(player);
            }
        }

        if(playersUnprotected.size()==0){
            sendToSinglePlayer(currentPlayer, "Ups, seems like all players are protected or out of the round"
                    + "\nUnfortunately, your turn is over without any effect.");
        } else {
            sendToSinglePlayer(currentPlayer, "Name the player you want to target:" +
                    "\n/[player]" +
                    "\nSelectable players are:\n");
            for(Player player : playersUnprotected){
                sendToSinglePlayer(currentPlayer, player.getPlayerName());
            }
            sendToSinglePlayer(currentPlayer, "\nWhose cards do you want to see?\n");
            String selectedPlayer = "";

            /**
             * repeat question until a valid player is selected
             */
            boolean foundSelectablePlayer = false;
            while(!foundSelectablePlayer){

                selectedPlayer = currentPlayer.getQueue().take();

                for(Player player : playersUnprotected){
                    if(player.getPlayerName().equals(selectedPlayer)){
                        sendToSinglePlayer(currentPlayer, "You look at the hand and see a ");
                        sendToSinglePlayer(currentPlayer, player.getCard1().getName());
                        sendToSinglePlayer(currentPlayer, "\n");
                        foundSelectablePlayer = true;
                    }
                }
            }
        }
        return playersLeftInRound;
    }
}
