package phases;

import errors.GameException;
import gui.GUI;
import player.Player;

/**
 * Created by manuelesberger on 20/12/15.
 */
public class AcquisitionPhase implements Phase {
    private GUI gui;

    public AcquisitionPhase(GUI gui){
        this.gui = gui;
    }

    @Override
    public void execute(Player player) {
        int troops = player.getTotalTroops();
        player.canFinishPhase(true);
        while(troops > 0){
            gui.setMaxNumberCounter(troops);
            gui.displayMessage("Player " + player.getName() + " can place up to "+ troops + " troops.", player);
            try {
                troops -= player.doTerritoryPlacement(troops);  // parameter 0 means don't mind troopcount
            }catch(GameException ex){
                System.out.println(ex.getMessage());
            }
            if (player.finishedPhase()) {
                troops = 0;
            }
            player.reset();
        }
    }

}
