package player;

import data.Territory;
import errors.NotANeigbourException;
import errors.NotEnoughTroopsException;
import errors.NotYourTerritoryException;

import java.awt.Color;

/**
 * Created by manuelesberger on 20/12/15.
 */

public interface Player {

    String getName();

    Color getColor();

    void removeTerritory(Territory territory) throws NotYourTerritoryException;

    void addTerritory(Territory territory);

    /**
     *  Places Troops on a Territory.
     *  Cases:
     *   1. Territory has no owner => owner is this player, troops will be placed
     *   2. Territory owner is this player => troops will be added
     *   3. Territory owner is another player => error will be thrown. To occupy this territory call the attackTerritory method
     *
     * @param count
     * @param territory
     * @throws NotYourTerritoryException see Case 3.
     */
    void placeTroopsOnTerritory(int count, Territory territory) throws NotYourTerritoryException;


    /**
     *
     * @return null if player owns no territories
     */
    Territory[] getTerritories();

    /**
     * Based on territories and continents this method returns the available troops for the next round.
     * This method does not work roundbased. It can be called anytime and the outcome can be different, even in the same round!
     * @return available Troops for this Round
     */
    int getTotalTroops();

    /**
     * Attacks opponent territory with up to 3 troops
     * Cases:
     *  1. to is a neighbour territory of from:
     *      a. Territory to has no owner then the same behaviour as placeTroopsOnTerritory happens
     *      b. Territory is own territory then nothing happens (Logging lvl info will inform about that call)
     *      c. Territory is opponents territory. Then dices (up to 3 dices for the attacker and up to 2 dices for de defender) will decide if attack worked.
     *  2. to is not a neighbor territory of from => Exception will be thrown
     *  3. from is not your territory => Exception will be thrown
     *  4. troopCount is to big. =>  Exception will be thrown
     * @param from
     * @param to
     * @param troopCount
     * @throws NotYourTerritoryException
     * @throws NotANeigbourException
     * @throws NotEnoughTroopsException
     */
    void attackTerritory(Territory from, Territory to, int troopCount) throws NotYourTerritoryException, NotANeigbourException, NotEnoughTroopsException;

    int getTroopsOnTerritory(Territory territory) throws NotYourTerritoryException;

    /**
     * Move troops from one own territory to a neighbour territory that is owned by this player.
     * @param troopsCount
     * @param from
     * @param to
     * @throws NotYourTerritoryException
     * @throws NotANeigbourException
     * @throws NotEnoughTroopsException
     */
    void moveTroops(int troopsCount, Territory from, Territory to) throws NotYourTerritoryException, NotANeigbourException, NotEnoughTroopsException;

    boolean belongsToPlayer(Territory territory);

    //get territory by click or by computer choice
    int doTerritoryPlacement(int maxTroops);

    boolean finishedPhase();

    void canFinishPhase(boolean canFinishPhase);

    void doAttack();

    void doMoving();

    void reset();

    boolean canAttackTerritory(Territory from, Territory to);
}
