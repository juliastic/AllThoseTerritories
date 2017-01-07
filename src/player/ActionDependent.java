package player;

import data.Territory;

/**
 * Created by manuelesberger on 31/12/15.
 */
public interface ActionDependent {
    public void pressedOn(Territory territory);
    public void draggedFromTo(Territory from, Territory to);
    public void buttonPressed();
    public void numberCounter(int number);
}
