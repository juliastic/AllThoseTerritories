package programmloader;

import data.Map;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by manuelesberger on 19/12/15.
 */
public class LineParser {
    private Map map;
    private final static Logger LOGGER = Logger.getLogger(LineParser.class.getName());

    public LineParser(Map map) {
        this.map = map;
    }

    public void parseLine(String line) {
        if (line != null && !line.isEmpty()) {
            String command = line.substring(0, line.indexOf(' ') + 1);
            line = line.replaceFirst(command, "");
            command = command.trim();
            if (command.equalsIgnoreCase("patch-of")) {
                patch(line);
            } else if (command.equalsIgnoreCase("capital-of")) {
                capital(line);
            } else if (command.equalsIgnoreCase("neighbors-of")) {
                neighbors(line);
            } else if (command.equalsIgnoreCase("continent")) {
                continent(line);
            }
        }
    }

    private void patch(String line) {
        Matcher m = Pattern.compile("\\d").matcher(line);
        if (m.find()) {
            String name = line.substring(0, m.start());
            line = line.replaceFirst(name, "");
            name = name.trim();

            String[] params = line.split(" ");
            if (params.length > 5) {       //mind 6 parameter
                List<Point> points = new ArrayList<Point>();
                for (int i = 0; i + 1 < params.length; i += 2) {  //next 2 points
                    try {
                        int x = Integer.parseInt(params[i]);
                        int y = Integer.parseInt(params[i + 1]);
                        points.add(new Point(x, y));
                    } catch (NumberFormatException ex) {
                        LOGGER.log(Level.INFO, ex.getMessage());
                    }
                }
                map.patchOf(name, points.toArray(new Point[points.size()]));
            }
        }
    }

    private void capital(String line) {
        Matcher m = Pattern.compile("\\d").matcher(line);
        if (m.find()) {
            String name = line.substring(0, m.start());
            line = line.replaceFirst(name, "");
            name = name.trim();
            String[] params = line.split(" ");
            if (params.length == 2) {
                try {
                    int x = Integer.parseInt(params[0]);
                    int y = Integer.parseInt(params[1]);
                    Point coordinate = new Point(x, y);
                    map.capitalOf(name, coordinate);
                } catch (NumberFormatException ex) {
                    LOGGER.log(Level.INFO, ex.getMessage());
                }
            }else{
                LOGGER.log(Level.INFO, "can't parse capital "+ name);
            }
        }else{
            LOGGER.log(Level.INFO, "can't parse capital ");
        }
    }

    private void neighbors(String line) {
        Matcher m = Pattern.compile(":").matcher(line);
        if (m.find()) {
            String name = line.substring(0, m.start());
            line = line.replaceFirst(name, "");
            line = line.replace(":", "");
            name = name.trim();
            m = Pattern.compile("(([A-z]|\\s+)*\\-)").matcher(line);
            List<String> neighbors = new ArrayList<>();
            while(m.find()){
                String neighbour = line.substring(0, m.end());
                line = line.replaceFirst(neighbour, "");
                m = Pattern.compile("(([A-z]|\\s+)*\\-)").matcher(line);
                neighbour = neighbour.replace("-", "");
                neighbour = neighbour.trim();
                neighbors.add(neighbour);
            }
            line = line.trim();
            neighbors.add(line);
            map.neighborOf(name, neighbors.toArray(new String[neighbors.size()]));
        }
    }

    private void continent(String line) {
        Matcher m = Pattern.compile(":").matcher(line);
        if (m.find()) {
            String name = line.substring(0, m.start());
            line = line.replaceFirst(name, "");
            name = name.trim();
            try {
                m = Pattern.compile("\\d").matcher(name);
                if (m.find()) {
                    int bonus = Integer.parseInt((name.substring(m.start())));
                    name = name.replace("" + bonus, "");
                    name = name.trim();

                    String[] params = line.split(" ");
                    if (params.length >= 1) {
                        List<String> territories = new ArrayList<String>();
                        for (int i = 0; i < params.length; i++) {
                            territories.add(params[i]);
                        }
                        map.continent(name, bonus, territories.toArray(new String[territories.size()]));
                    }
                }
            } catch (NumberFormatException ex) {
                LOGGER.log(Level.INFO, ex.getMessage());
            }
        }
    }

    public Map getMap() {
        return map;
    }
}
