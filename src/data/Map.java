package data;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by manuelesberger on 19/12/15.
 */
public class Map {
    private java.util.Map<String, Territory> territories;
    private List<Capital> capitals;
    private List<Continent> continents;
    private final static Logger LOGGER = Logger.getLogger(Map.class.getName());

    public Map() {
        this.territories = new TreeMap<>();
        this.capitals = new LinkedList<>();
        this.continents = new LinkedList<>();
    }

    public void patchOf(String territory, Point[] coordinates){
        if (territories.containsKey(territory)) {
            territories.get(territory).addCoordiantes(coordinates);
        } else {
            territories.put(territory, new Territory(territory, coordinates));
        }
    }

    public Territory[] getTerritoryArray(){
        return this.getTerritories().values().toArray(new Territory[this.getTerritories().size()]);
    }

    public void capitalOf(String territory, Point cooridnate){
        new Capital(territories.get(territory), cooridnate);
    }

    public void neighborOf(String territory, String[] territories) {
        Territory terr = this.territories.get(territory);
        if (terr != null) {
            for (String current : territories) {
                terr.addNeighbor(this.territories.get(current));
            }
        } else {
            LOGGER.log(Level.INFO, "Can't find territory "+ territory + ".");
        }
    }

    public void continent(String continent, int bonus, String[] territories) {
        Continent cont = new Continent(continent, bonus);
        continents.add(cont);

        String temp = "";
        for (String territory : territories) {
            if (territory.equals("-")) {
                temp = temp.substring(0, temp.length() - 1);
                Territory terr = this.territories.get(temp);
                if (terr != null) {
                    cont.addTerritory(terr);
                } else {
                    LOGGER.log(Level.INFO, "Can't find territory " + temp + ".");
                }
                temp = "";
            } else if (!territory.equals(":")) {
                temp += territory + " ";
            }
        }
    }

    public java.util.Map<String, Territory> getTerritories() {
        return territories;
    }

    public List<Continent> getContinents() {
        return continents;
    }

    @Override
    public String toString() {
        String terr = "\n";
        for (String s: territories.keySet()) {
            terr += s+"\n";
        }
        return super.toString() + terr;
    }
}
