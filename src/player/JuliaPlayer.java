package player;

import data.Territory;
import errors.GameException;
import main.Game;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manuelesberger on 03/01/16.
 */
public class JuliaPlayer extends BasicPlayer {
    private boolean finishedPhase;
    private static final int SLEEP_TIME = 100;
    /**
     * this is a state variable, if nothing could be attacked the computer has to move troops.
     */
    private boolean doMoving;

    public JuliaPlayer(String name, Color color, Game game) {
        super(name, color, game);
        doMoving = false;
    }

    @Override
    public int doTerritoryPlacement(int maxTroops) {
        sleep();
        Territory territory;
        int troops;
        if (maxTroops < 0) {
            List<Territory> extendedTerritories = new LinkedList<>();
            for (Territory ter : game.getMap().getTerritoryArray()) {
                if (ter.getOwner() == null) {
                    extendedTerritories.add(ter);
                }
            }
            territory = chooseTerritory(extendedTerritories);
            troops = 1;
        } else {
            territory = chooseTerritory(ownedTerritories);
            troops = (int) (maxTroops * Math.random()) + 1;
        }

        this.placeTroopsOnTerritory(troops, territory);
        return troops;
    }

    @Override
    public void canFinishPhase(boolean canFinishPhase) {
        super.canFinishPhase(canFinishPhase);
    }

    @Override
    public boolean finishedPhase() {
        sleep();
        return finishedPhase;
    }

    @Override
    public void doAttack() {
        prepareAttack();

        int attackCounter = 3;
        while (!finishedPhase && attackCounter <= 0) {
            prepareAttack();
            attackCounter--;
            sleep();
        }
    }

    @Override
    public void doMoving() {
        if (doMoving) {
            //            prepareMoving();
            prepareBetterMoving();
        }
        sleep();
    }

    @Override
    public void reset() {
        finishedPhase = false;
        canFinishPhase = false;
    }

    private Territory chooseTerritory(List<Territory> territories) {
        if (territories.size() > 0) {
            int randomIndex = (int) (Math.random() * (territories.size() + 1) - 1);
            return territories.get(randomIndex);
        }
        return null;
    }


    private void prepareAttack() throws GameException {
        List<Territory> tempTerritories = new LinkedList<>(ownedTerritories);
        boolean attacked = false;

        while (tempTerritories.size() > 0 && !attacked) {
            Territory territory = chooseTerritory(tempTerritories);
            tempTerritories.remove(territory);
            if (territory != null && territory.troops() > 1) {
                int troops = territory.troops();
                for (Territory neighbor : territory.getNeighbors()) {
                    if (!neighbor.getOwner().equals(this) && troops > neighbor.troops()) {
                        troops = choooseTroopAmountForAttack(troops);
                        this.attackTerritory(territory, neighbor, troops);
                        attacked = true;
                        break;
                    }
                }
            }
        }
        if (!attacked) {
            doMoving = true;
            finishedPhase = true;
        }
    }


    private int choooseTroopAmountForAttack(int troops) {
        if (troops > 5) {
            return 3;
        } else if (troops > 2) {
            return 2;
        } else {
            return 1;
        }
    }

    private void prepareMoving() {
        List<Territory> tempTerritories = new LinkedList<>(ownedTerritories);
        boolean moveInitiated = false;

        do {
            Territory territory = chooseTerritory(tempTerritories);
            if (territory != null) {
                int troops = territory.troops();
                for (Territory neighbor : territory.getNeighbors()) {
                    if ((neighbor.getOwner().equals(this) || neighbor.getOwner().equals(null)) && troops > 1) {
                        moveInitiated = true;
                        this.moveTroops(territory.troops() - 1, territory, neighbor);
                    } else if (tempTerritories.size() != 0) {
                        tempTerritories.remove(territory);
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        } while (!moveInitiated);
    }

    private void prepareBetterMoving() {
        Territory from = findTerritoryWithMostTroops();
        Territory to = enemyTerritory();
        Territory neighbour = PathFinding.getNeighbourWithSameOwner(from, to);
        if (from != null && neighbour != null) {
            this.moveTroops(from.troops() - 1, from, neighbour);
        }
    }

    private Territory enemyTerritory() {
        for (Territory territory : game.getMap().getTerritoryArray()) {
            if (territory.getOwner() != this) {
                return territory;
            }
        }
        return null;
    }

    private Territory findTerritoryWithMostTroops() {
        Territory bestTerritory = ownedTerritories.get(0);
        for (Territory territory : ownedTerritories) {
            if (territory.troops() > bestTerritory.troops()) {
                bestTerritory = territory;
            }
        }
        return bestTerritory;
    }

    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e){}
    }
}
