package phases;

import data.Territory;
import errors.GameException;
import gui.GUI;
import player.Player;

/**
 * Created by manuelesberger on 20/12/15.
 */
public class PreparePhase {
    private Territory[] territories;
    private GUI gui;

    public PreparePhase(GUI gui, Territory[] territories) {
        this.gui = gui;
        this.territories = territories;
    }

    public void execute(Player[] players) {
        gui.setMaxNumberCounter(1);
        gui.setNumberCounter(1);
        int counter = playersIndexer(players);
        while (!allTerritoriesOwnedByPlayers()) {
            if (counter == -1) counter = playersIndexer(players);
            players[counter].canFinishPhase(false);
            gui.displayMessage("One player after the other has to choose an unoccupied territory.", players[counter]);
            try {
                players[counter].doTerritoryPlacement(-1);
            }catch (GameException ex){
                execute(players);
            }
            gui.repaint();
            players[counter].reset();
            counter--;
        }
    }


    private int playersIndexer(Player[] players){
        return  players.length-1;
    }

    private boolean allTerritoriesOwnedByPlayers() {
        for(Territory territory : territories){
           if(territory.getOwner() == null) return false;
        }
        return true;
    }
}
