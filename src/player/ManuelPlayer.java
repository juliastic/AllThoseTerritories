package player;

import data.Dices;
import data.Territory;
import main.Game;

import java.awt.*;

/**
 * Created by manuelesberger on 03/01/16.
 */
public class ManuelPlayer extends BasicPlayer implements ActionDependent {
    private static final int SLEEP_TIME = 250;
    private boolean finishedPhase;
    private Territory territory;
    private Territory from;
    private Territory to;
    private int number;

    public ManuelPlayer(String name, Color color, Game game) {
        super(name, color, game);
        this.finishedPhase = false;
    }

    @Override
    public void pressedOn(Territory territory) {
        this.territory = territory;
    }

    @Override
    public void draggedFromTo(Territory from, Territory to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void buttonPressed() {
        this.finishedPhase = true;
    }

    @Override
    public void numberCounter(int number) {
        this.number = number;
    }

    @Override
    public boolean finishedPhase() {
        return finishedPhase;
    }

    @Override
    public int doTerritoryPlacement(int maxTroops) {
        if(maxTroops > 0 ){
            game.gui().setNumberCounter((int) (Math.ceil(maxTroops / 2.0)));
        } else {
            game.gui().setNumberCounter(1);
        }

        while(number == 0) {
            gatherData(false, true, true);

            if(finishedPhase){
                return 0;
            }
            //troopcount check
            int troops = territory.troops()+number;
            if(maxTroops < 0) {
                if(troops > 1) this.reset();
            } else if(number > maxTroops){
                this.reset();
            }
        }

        this.placeTroopsOnTerritory(number, territory);
        return number;
    }

    public void doAttack() {
        this.gatherData(true,false,true);
        if(!finishedPhase) {
            this.attackTerritory(from, to, number);
        }
    }

    @Override
    public void doMoving() {
        this.gatherData(true, false, true);
        if (!finishedPhase) {
            this.moveTroops(number, from, to);
        }
    }

    private void gatherData(boolean fromTo, boolean territory, boolean number){
        game.gui().addActionDependent(this);
        while((fromTo && this.from == null) || (fromTo && this.to == null) || (number && this.number == 0)|| (territory && this.territory == null)) {
            try {
                if (canFinishPhase) {
                    if (finishedPhase) {
                        break;
                    }
                }
                Thread.currentThread().sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
            }
        }
        game.gui().removeActionDependent(this);
    }

    @Override
    public void reset(){
        from = null;
        to = null;
        finishedPhase = false;
        territory = null;
        number = 0;
    }
}
