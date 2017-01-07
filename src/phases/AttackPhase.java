package phases;

import data.Dices;
import errors.GameException;
import gui.GUI;
import main.Game;
import player.ManuelPlayer;
import player.Player;

import java.awt.*;

/**
 * Created by Gril on 31/12/2015.
 */
public class AttackPhase implements Phase {
    private Game game;

    public AttackPhase(Game game) {
        this.game = game;
    }


    @Override
    public void execute(Player player) {
        game.gui().setMaxNumberCounter(3);
        player.canFinishPhase(true);
        game.gui().displayMessage("You can attack with up to 3 troops per attack. Press done when you want to finish attack.", player);
        while (!player.finishedPhase()) {
            player.reset();
            try {
                player.doAttack();
                if(player.getTerritories().length == game.getMap().getTerritories().size()) game.won(player);
            } catch (GameException ex) {
                System.out.println(ex.getMessage());
            }
        }
        game.gui().displayDices(null);
        player.reset();
    }

}
