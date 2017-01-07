package data;

import java.util.Random;

/**
 * Created by manuelesberger on 19/01/16.
 */
/**
 * http://wiki.scn.sap.com/wiki/display/Snippets/Implementing+dice+roller+using+java+Swing
 */
public class Dice {
    //=============================================== instance variables
    private int _value; // value that shows on face of die
    //end instance variables
    //=============================================== class variables
    private static Random random = new Random(); // random generator
    //end class variables

    public Dice(){
        roll();
    }

    /** Produce random roll in range 1-6. Causes repaint().
     @return Result of roll (1-6).
     */
    public int roll() {
        int val = random.nextInt(6) + 1; // Range 1-6
        setValue(val);
        return val;
    }//end roll
    //================================================== method getValue
    /** Returns result of last roll.*/
    public int getValue() {
        return _value;
    }//end setValue
    //================================================== method setValue
    /** Sets the value of the Die. Causes repaint().
     @param spots Number from 1-6.
     */
    public void setValue(int spots) {
        _value = spots;
    }//end setValue
}
