package phases;

import errors.GameException;
import gui.GUI;
import player.Player;

/**
 * Created by Gril on 31/12/2015.
 */
public class EndPhase implements Phase {
    private GUI gui;

    public EndPhase(GUI gui){
        this.gui = gui;
    }

    @Override
    public void execute(Player player) {
        gui.displayMessage("You can move troops from one territory to another. 1 troop must stay.", player);
        player.canFinishPhase(true);
        gui.setMaxNumberCounter(100);
        try {
            player.doMoving();
        }catch (GameException ex){
            execute(player);
        }
        player.reset();
    }

}
