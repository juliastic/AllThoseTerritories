package data;

import player.Player;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by manuelesberger on 19/12/15.
 */
public class Territory {
    private List<Territory> neighbors;
    private List<Territory> subterritories;
    private String name;
    private Point[] coordinates;
    private Capital capital;
    private Continent continent;
    private Player owner;
    private int troops;

    public Territory(String name, Point[] coordinates) {
        this.neighbors = new LinkedList<>();
        this.name = name;

        this.coordinates = coordinates;
        subterritories = new LinkedList<>();
        subterritories.add(this);
        troops = 0;
    }

    public void addTroops(int count) {
        this.troops += count;
    }

    public void addNeighbor(Territory neighbor) {
        if (neighbor != null && !neighbors.contains(neighbor)) {
            neighbors.add(neighbor);
            neighbor.addNeighbor(this);
        }
    }

    public boolean neighborsTerritory(Territory neighbor) {
        return this.neighbors.contains(neighbor);
    }

    public Player getOwner() {
        return this.owner;
    }

    public void setOwner(Player player) {
        if(this.owner != null) this.owner.removeTerritory(this);
        this.owner = player;
        if(this.owner != null) this.owner.addTerritory(this);
    }

    public Capital getCapital() {
        return capital;
    }

    public void setCapital(Capital capital) {
        this.capital = capital;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public String getName() {
        return name;
    }

    public Point[] getCoordinates() {
        return coordinates;
    }

    public int troops() {
        return troops;
    }

    public Territory[] getNeighbors() {
        return neighbors.toArray(new Territory[neighbors.size()]);
    }

    public void addCoordiantes(Point[] coordinates) {
        subterritories.add(new Territory(this.name, coordinates));

        /*Point[] newCoordiantes = new Point[this.coordinates.length+coordinates.length];
        System.arraycopy(this.coordinates, 0, newCoordiantes, 0, this.coordinates.length);
        System.arraycopy(coordinates, 0, newCoordiantes, this.coordinates.length, coordinates.length);
        this.coordinates = newCoordiantes;*/
    }

    public List<Territory> getSubterritories() {
        return subterritories;
    }

    /**
     * Return true if the given point is contained inside the boundary.
     * See: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
     *
     * @param test The point to check
     * @return true if the point is inside the boundary, false otherwise
     */
    public boolean contains(Point test) {
        int i;
        int j;
        boolean result = false;
        for (Territory t : subterritories) {
            for (i = 0, j = t.coordinates.length - 1; i < t.coordinates.length; j = i++) {
                if ((t.coordinates[i].y > test.y) != (t.coordinates[j].y > test.y) && (test.x < (t.coordinates[j].x - t.coordinates[i].x) * (test.y - t.coordinates[i].y) / (t.coordinates[j].y - t.coordinates[i].y) + t.coordinates[i].x)) {
                    result = !result;
                }
            }
        }
        return result;
    }


    public Point centroid() {
        double centroidX = 0, centroidY = 0;

        int size = 0;
        for (Territory sub : getSubterritories()) {
            for (Point knot : sub.getCoordinates()) {
                centroidX += knot.getX();
                centroidY += knot.getY();
            }
            size += sub.getCoordinates().length;
        }
        return new Point((int) centroidX / size, (int) centroidY / size);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Territory){
            return ((Territory) obj).name.equals(this.name);
        }
        return super.equals(obj);
    }
}
