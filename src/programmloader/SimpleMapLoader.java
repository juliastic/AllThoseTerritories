package programmloader;

import data.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by manuelesberger on 19/12/15.
 * TODO: Create grafical fileselector
 */
public class SimpleMapLoader implements IMapLoader {
    private String pathToMapFile;

    public SimpleMapLoader(String pathToMapFile) {
        this.pathToMapFile = pathToMapFile;
    }

    @Override
    public String getPathToMapFile() {
        return pathToMapFile;
    }

    @Override
    public Map loadMap() {
        return loadMap(pathToMapFile);
    }

    @Override
    public Map loadDefault() {
        String path = DefaultMaps.AFRICA.getValue();
        return loadMap(path);
    }

    private Map loadMap(String path) {
        Map map = new Map();
        try {
            Scanner scanner = new Scanner(new File(path));
            LineParser parser = new LineParser(map);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                parser.parseLine(line);
            }
            return parser.getMap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
