/**
 * @author Julian Wiemer
 * Class King
 * This class implements the card "King" from the game "Love Letter"
 */
import java.util.List;

public class King extends CardFunctions implements Card {

    private int value = 6;
    private String name = "king";
    private String detailDescription = "When you discard the king, trade your remaining card with " +
            "the card held by another player of your choice.";
    private Player targetPlayer;



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


    private void print(Player p, String s) { sendToSinglePlayer(p, s); }

    /**
     * startEffect() implements Kings's card ability
     * Method lets the Player choose an player out of a list
     * The two players exchange their cards*
     * @param currentPlayer      the current player
     * @param playersLeftInRound list of all players who are in the round
     * @param playersInGame         list of all players
     * @param deck               card deck of the current game round
     * @return playersLeftInRound  list of all players who are in the round, excluding the loser of this round
     */
    public List<Player> startEffect(Player currentPlayer, List<Player> playersLeftInRound, List<Player> playersInGame, Deck deck)
            throws InterruptedException {
        print(currentPlayer, "You've played the king. Choose another player you want to swap your remaining card with.");
        print(currentPlayer, "You can only target a player who is not protected by the handmaid's effect.");
        print(currentPlayer, "Name the player you want to target:" +
                "\n/[player]");
        int numberPossibleTargetPlayers = 0;
        for (Player player : playersLeftInRound) {
            if(!player.getPlayedHandmaid() && !player.getPlayerName().equals(currentPlayer.getPlayerName())) {
                print(currentPlayer, player.getPlayerName());
                numberPossibleTargetPlayers++;
            }
        }
        if(numberPossibleTargetPlayers == 0) {
            print(currentPlayer, "Ups, seems like all players are protected or out of the round"
                    + "\nUnfortunately, your turn is over without any effect.");
            return playersLeftInRound;
        }

        /**
         * Asking the player which other player he/she wants to target
         * Looping until valid choice is made
         */
        String chosenPlayerName;
        int indexOfChosenPlayer = -1;
        while (true) {
            chosenPlayerName = currentPlayer.getQueue().take();
            for (int i = 0; i < playersLeftInRound.size(); i++) {
                if (playersLeftInRound.get(i).getPlayerName().equals(chosenPlayerName)) {
                    print(currentPlayer, "You chose: " + chosenPlayerName);
                    indexOfChosenPlayer = i;
                    break;
                }
            }
            if(indexOfChosenPlayer != -1) {
                if (playersLeftInRound.get(indexOfChosenPlayer).getPlayedHandmaid()) {
                    print(currentPlayer, "This Player is protected by the handmaid's effect until his/her next turn. " +
                           "\nYou have to choose another player.");
                    print(currentPlayer,"Name the player you want to target: " +
                            "\n/[player]");
                } else if(currentPlayer.getPlayerName().equals(chosenPlayerName)) {
                    print(currentPlayer, "You cannot choose yourself! " +
                           "\nYou have to choose another player.");
                    print(currentPlayer,"Name the player you want to target: " +
                            "\n/[player]");
                } else break;
            } else {
                print(currentPlayer, "Name not recognized!\nAre you sure you spelled the name correctly?");
            }
        }

        /**
         * The actual king-specific function: swaps currentPlayer's (not king-) card with targetPlayer's card
         */
        targetPlayer = playersLeftInRound.get(indexOfChosenPlayer);
        Card swappedCard = targetPlayer.getCard1();
        if (currentPlayer.getCard1().getName().equals("king")) {
            targetPlayer.setCard1(currentPlayer.getCard2());
            currentPlayer.setCard2(swappedCard);
        } else {
            targetPlayer.setCard1(currentPlayer.getCard1());
           currentPlayer.setCard1(swappedCard);
        }
        print(currentPlayer, "You traded your card with " + targetPlayer.getPlayerName() + "'s card.");
        print(targetPlayer,currentPlayer.getPlayerName() + " traded his card with your card.");
        return playersLeftInRound;
    }
}
