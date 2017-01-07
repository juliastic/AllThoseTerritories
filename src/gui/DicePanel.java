package gui;

import data.Dice;

import java.awt.*;
import javax.swing.*;

/**
 * http://wiki.scn.sap.com/wiki/display/Snippets/Implementing+dice+roller+using+java+Swing
 */
public class DicePanel extends JPanel {
    private int _diam = 6; // Diameter of spots
    private Dice dice;

    //====================================================== constructor
    /** Initialize to white background and 60x60 pixels. Initial roll.*/
    public DicePanel(Dice dice, Color color) {
        this.dice = dice;
        setBackground(color);
        //-- Preferred size is set, but layout may change it.
        setPreferredSize(new Dimension(40,40));
    }//end constructor
    //============================================ method paintComponent
    /** Draws spots of die face. */
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // required
        int w = getWidth();
        int h = getHeight(); // should use to resize spots too.
        switch (dice.getValue()) {
            case 1: drawSpot(g, w/2, h/2);
                break;
            case 3: drawSpot(g, w/2, h/2);
                // Fall thru to next case
            case 2: drawSpot(g, w/4, h/4);
                drawSpot(g, 3*w/4, 3*h/4);
                break;
            case 5: drawSpot(g, w/2, h/2);
                // Fall thru to next case
            case 4: drawSpot(g, w/4, h/4);
                drawSpot(g, 3*w/4, 3*h/4);
                drawSpot(g, 3*w/4, h/4);
                drawSpot(g, w/4, 3*h/4);
                break;
            case 6: drawSpot(g, w/4, h/4);
                drawSpot(g, 3*w/4, 3*h/4);
                drawSpot(g, 3*w/4, h/4);
                drawSpot(g, w/4, 3*h/4);
                drawSpot(g, w/4, h/2);
                drawSpot(g, 3*w/4, h/2);
                break;
        }
    }//end paintComponent
    /** Utility method used by paintComponent(). */
    //================================================== method drawSpot
    private void drawSpot(Graphics g, int x, int y) {
        g.fillOval(x-_diam/2, y-_diam/2, _diam, _diam);
    }//end drawSpot
}//