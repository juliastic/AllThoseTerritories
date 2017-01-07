package main;

import data.Map;
import programmloader.DefaultMaps;
import programmloader.IMapLoader;
import programmloader.SimpleMapLoader;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by manuelesberger on 19/12/15.
 */
public class Main {

    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.setLevel(Level.FINE);
        IMapLoader loader;
        loader = new SimpleMapLoader(DefaultMaps.WORLD.getValue());
        Map map = loader.loadMap();

        Game game = new Game(map);
        game.gui().view();
    }
}
