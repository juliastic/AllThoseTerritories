package errors;

import data.Territory;
import player.Player;

/**
 * Created by manuelesberger on 30/12/15.
 */
public class NotANeigbourException extends GameException {
    public NotANeigbourException(Territory from, Territory to, Player player) {
        super("This territory " + from.getName() + " is not a neighbour of territory "+ to.getName(), player);
    }
}
