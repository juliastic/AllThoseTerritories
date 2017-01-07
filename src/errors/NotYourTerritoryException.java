package errors;

import data.Territory;
import player.Player;

/**
 * Created by manuelesberger on 30/12/15.
 */
public class NotYourTerritoryException extends GameException {
    public NotYourTerritoryException(Territory territory, Player player) {
        super("Territory " + territory.getName() + " does not belong to you. This territory belongs to player: "+territory.getOwner().getName(), player);
    }
}
