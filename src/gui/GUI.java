package gui;

import data.Dices;
import player.ActionDependent;
import player.Player;


/**
 * Created by manuelesberger on 31/12/15.
 */

public interface GUI {
    void view();

    void addActionDependent(ActionDependent actionDependent);

    void removeActionDependent(ActionDependent actionDependent);

    void displayDices(Dices[] dicesList);

    void setMaxNumberCounter(int numberCounter);

    void setNumberCounter(int numberCounter);

    void disableInput(boolean disable);

    void displayMessage(String message, Player player);

    void displayWinner(Player player, int roundCount);

    void repaint();
}
