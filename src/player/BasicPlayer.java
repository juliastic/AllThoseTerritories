package player;

import data.Continent;
import data.Dices;
import data.Territory;
import errors.GameException;
import errors.NotANeigbourException;
import errors.NotEnoughTroopsException;
import errors.NotYourTerritoryException;
import main.Game;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manuelesberger on 01/01/16.
 */

public abstract class BasicPlayer implements Player {

    private String name;
    private Color color;
    protected Game game;
    protected boolean canFinishPhase;

    protected List<Territory> ownedTerritories;
    protected List<Continent> continents;

    BasicPlayer(String name, Color color, Game game) {
        this.name = name;
        this.game = game;
        this.color = color;

        this.ownedTerritories = new LinkedList<>();
        this.continents = new LinkedList<>();
        continents.addAll(game.getMap().getContinents());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void placeTroopsOnTerritory(int count, Territory territory) throws NotYourTerritoryException {
        if (this.ownedTerritories.contains(territory) || territory.getOwner() == null) {
            if (territory.getOwner() == null) {
                territory.setOwner(this);
                this.addTerritory(territory);
            }
            territory.addTroops(count);
            game.gui().repaint();
        } else {
            throw new NotYourTerritoryException(territory, this);
        }
    }

    @Override
    public Territory[] getTerritories() {
        return ownedTerritories.toArray(new Territory[ownedTerritories.size()]);
    }

    @Override
    public int getTotalTroops() {
        int troops = ownedTerritories.size() / 3;

        for (Continent continent : continents) {
            if (continent.containsAllTerritories(ownedTerritories.toArray(new Territory[ownedTerritories.size()]))) {
                troops += continent.getBonus();
            }
        }

        if(troops < 3) troops = 3;
        return troops;
    }

    @Override
    public void attackTerritory(Territory from, Territory to, int troopCount) throws NotYourTerritoryException, NotANeigbourException, NotEnoughTroopsException {
        if (ownedTerritories.contains(from) && !ownedTerritories.contains(to) && from.neighborsTerritory(to) && from.troops() > 1) {
            if(from.troops() - troopCount < 1){
                throw new GameException("Can't attack with "+troopCount+" troops", this);
            }
            int toTroops = 0;
            if(to.troops() >= 2){
                toTroops = 2;
            } else {
                toTroops = 1;
            }

            Dices attacker = new Dices(this, troopCount);
            Dices defender = new Dices(to.getOwner(), toTroops);

            Dices[] dicesList = new Dices[2];
            dicesList[0] = attacker;
            dicesList[1] = defender;

            game.gui().displayDices(dicesList);

            int maxAttacker = 0;
            for(int i : attacker.getDiceValues()){
                maxAttacker = i>maxAttacker ? i : maxAttacker;
            }

            int maxDefender = 0;
            for(int i : defender.getDiceValues()){
                maxDefender = i>maxDefender ? i : maxDefender;
            }

            if(maxDefender >= maxAttacker){
                from.addTroops(-troopCount);
            }else{
                to.addTroops(-toTroops);
            }

            if(to.troops() <= 0){
                from.addTroops(-troopCount);
                to.setOwner(this);
                to.addTroops(troopCount);
            }
        } else if (!ownedTerritories.contains(from)) {
            throw new NotYourTerritoryException(from, this);
        } else if (!from.neighborsTerritory(to)) {
            throw new NotANeigbourException(from, to, this);
        } else {
            throw new NotEnoughTroopsException(from, this);
        }
    }

    @Override
    public int getTroopsOnTerritory(Territory territory) throws NotYourTerritoryException {
        if (ownedTerritories.contains(territory)) {
            return territory.troops();
        } else {
            throw new NotYourTerritoryException(territory, this);
        }
    }

    @Override
    public void moveTroops(int troopsCount, Territory from, Territory to) throws NotYourTerritoryException, NotANeigbourException, NotEnoughTroopsException {
        if (ownedTerritories.contains(from) && ownedTerritories.contains(to) && from.neighborsTerritory(to) && from.troops() > troopsCount) {
                from.addTroops(-troopsCount);
                to.addTroops(troopsCount);
        } else if (!ownedTerritories.contains(from)) {
            throw new NotYourTerritoryException(from, this);
        } else if (!from.neighborsTerritory(to)) {
            throw new NotANeigbourException(from, to, this);
        } else {
            throw new NotEnoughTroopsException(from, this);
        }
    }

    @Override
    public boolean belongsToPlayer(Territory territory) {
        if (ownedTerritories.contains(territory)) {
            return true;
        }
        return false;
    }

    @Override
    public void canFinishPhase(boolean canFinishPhase) {
        this.canFinishPhase = canFinishPhase;
    }

    @Override
    public boolean canAttackTerritory(Territory from, Territory to) {
        if (this.belongsToPlayer(from) && !this.belongsToPlayer(to) && from.neighborsTerritory(to)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addTerritory(Territory territory) {
        for(Territory terr : ownedTerritories){
           if(territory.getName().equals(terr.getName())) return;
        }
        this.ownedTerritories.add(territory);
    }

    @Override
    public void removeTerritory(Territory territory) throws NotYourTerritoryException {
        if(!this.ownedTerritories.remove(territory)){
            throw new NotYourTerritoryException(territory, this);
        }
        //cause code above doesn't work unfortunately
        ownedTerritories.forEach(a -> {
            if(a.getName().equals(territory.getName())){
                ownedTerritories.remove(a);
            }
        });
    }
}
