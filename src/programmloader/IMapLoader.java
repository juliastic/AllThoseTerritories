package programmloader;

import data.Map;

/**
 * Created by manuelesberger on 19/12/15.
 */
public interface IMapLoader {
    String getPathToMapFile();
    Map loadMap();
    Map loadDefault();
}
