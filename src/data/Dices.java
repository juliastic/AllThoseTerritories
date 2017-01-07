package data;

import player.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by manuelesberger on 19/01/16.
 */
public class Dices {
    private List<Dice> dices;
    private Player player;

    public Dices(Player player, int numberOfDices) {
        this.player = player;
        dices = new LinkedList<>();
        for(int i = numberOfDices; i>0 ; i--){
            dices.add(new Dice());
        }
    }

    public Player player(){
        return player;
    }

    public List<Dice> dices(){
        return dices;
    }

    public int[] getDiceValues(){
        int[] values = new int[dices.size()];
        for(int i = 0; i<values.length; i++){
            values[i] = dices.get(i).getValue();
        }
        return values;
    }
}
