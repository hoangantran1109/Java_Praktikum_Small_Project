/**
 * @author the whole team
 * Class CardFunctions
 * this class provides functions that can be inherited by various cards
 */

import java.util.Iterator;
import java.util.List;

public class CardFunctions {

    /**
     * sendToAll()
     * prints a message on the console of every Player
     * @param playersInGame     all players in game
     * @param message           message to all player
     */
    public void sendToAll(List<Player> playersInGame, String message) {
        for (Player p : playersInGame) {
            sendToSinglePlayer(p, message);
        }
    }

    /**
     * sendToSinglePlayer()
     * outputs [Game] before each message in the game
     * @param messageReceiver   player to whom the message is addressed
     * @param message           message to be output
     */
    public void sendToSinglePlayer(Player messageReceiver, String message) {
        messageReceiver.getOut().println("[Game] " + message);
    }

    /**
     * askForTargetPlayer()
     * lists the possible players the current player can choose from
     * @param messageReceiver   current player
     * @param players           all players left in round
     */
    public void askForTargetPlayer(Player messageReceiver, List<Player> players) {
        sendToSinglePlayer(messageReceiver,"Name the player you want to target:"
                + "\n/[name]" +
                "\nYou can choose between: ");
        for (Player player : players) {
            if (!player.getPlayedHandmaid());
            sendToSinglePlayer(messageReceiver, player.getPlayerName());
        }
    }

    /**
     * getPlayerIndex()
     * @param playerName
     * @param playersLeftInRound
     * @return the index of player with a certain name in a certain list
     */
    public int getPlayerIndex(String playerName, List<Player> playersLeftInRound) {
        int playerIndex = 999;
        for (Player player : playersLeftInRound) {
            if (player.getPlayerName().equals(playerName)) {
                playerIndex = playersLeftInRound.indexOf(player);
            }
        }
        return playerIndex;
    }

    /**
     * removePlayer()
     * @param playersLeftInRound
     * @param index
     * @return the index of a certain player
     */
    public List<Player> removePlayer(List<Player> playersLeftInRound, int index){
        Player enemy = playersLeftInRound.get(index);
        for(Iterator<Player> player = playersLeftInRound.iterator(); player.hasNext();){
            Player checkPlayer= player.next();
            if(checkPlayer.equals(enemy)){
                player.remove();
            }
        }
        return playersLeftInRound;
    }
}
