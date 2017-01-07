package data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

/**
 * Created by manuelesberger on 19/12/15.
 */

public class Continent {
    private int bonus;
    private List<Territory> territories;

    public Continent(String name, int bonus) {
        this.bonus = bonus;
        this.territories = new LinkedList<>();
    }

    public void addTerritory(Territory territory) {
        territories.add(territory);
        territory.setContinent(this);
    }

    public boolean containsAllTerritories(Territory[] territories) {
        List<Territory> list = Arrays.asList(territories);
        return list.containsAll(this.territories);
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public int getBonus() {
        return bonus;
    }
}
