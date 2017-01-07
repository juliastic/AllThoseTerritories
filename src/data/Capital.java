package data;

import java.awt.*;

/**
 * Created by manuelesberger on 19/12/15.
 */
public class Capital {
    private Territory territory;
    private Point coordinate;

    public Capital(Territory territory, Point coordinate) {
        this.territory = territory;
        this.coordinate = coordinate;
        territory.setCapital(this);
    }

    public int x(){
        return (int)coordinate.getX();
    }


    public int y(){
        return (int)coordinate.getY();
    }
}
