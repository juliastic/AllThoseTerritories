package gui;

import data.Territory;
import player.ActionDependent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by manuelesberger on 31/12/15.
 */
public class DefaultMouseListener extends MouseAdapter {

    private java.util.List<ActionDependent> actionDependents;
    private Territory[] territories;
    private Territory startTerritory;

    public DefaultMouseListener(Territory[] territories) {
        this.actionDependents = new java.util.LinkedList<ActionDependent>();
        this.territories = territories;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       for(Territory territory : territories){
            if(territory.contains(e.getPoint())){
                actionDependents.forEach(a -> a.pressedOn(territory));
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startTerritory = getTerritory(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Territory goalTerritory = getTerritory(e.getPoint());
        if (startTerritory != null && goalTerritory != null && !goalTerritory.equals(startTerritory)) {
            actionDependents.forEach(a -> a.draggedFromTo(startTerritory, goalTerritory));
        }
    }

    private Territory getTerritory(Point point){
        for (Territory territory : territories) {
            if (territory.contains(point)) {
                return territory;
            }
        }
        return null;
    }

    public void addActionDependent(ActionDependent dependent){
        actionDependents.add(dependent);
    }

    public void removeActionDependent(ActionDependent dependent){
        actionDependents.remove(dependent);
    }
}
