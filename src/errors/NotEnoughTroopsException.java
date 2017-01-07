package errors;

import player.Player;
import data.Territory;

/**
 * Created by manuelesberger on 30/12/15.
 */
public class NotEnoughTroopsException extends GameException {
    public NotEnoughTroopsException(Territory territory, Player player){
        super("You have not enough troops on territory " + territory.getName() + ". Your maximum troop count on this territory is: "+ player.getTroopsOnTerritory(territory), player);
    }
}
