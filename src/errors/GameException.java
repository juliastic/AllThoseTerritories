package errors;

import data.Territory;
import player.Player;

/**
 * Created by manuelesberger on 30/12/15.
 */
public class GameException extends IllegalArgumentException {
    public GameException(String msg, Player player) {
        super(msg);
        player.reset();
    }
}
