package org.firstinspires.ftc.teamcode.actuators;

/**
 * Created by LBYPatrick on 10/20/2017.
 */

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * This Controller class is copied from GamepadB in LBYPatrick/2018Robot
 * (Which would be merged to LSRobotics/2018Robot when FRC 2018 ended)
 * Some small changes has been made due to different APIs in FTC
 */

final public class Controller {

    final private static int NUM_PRECISE_KEY = 6;
    final private static int NUM_NORMAL_KEY = 14;
    private boolean isDebug = false;

    //Indexes for the buttons
    final public static int jLeftX = 0;
    final public static int jLeftY = 1;
    final public static int jRightX = 2;
    public final static int jRightY = 3;
    final public static int LT = 4;
    final public static int RT = 5;
    public final static int jLeftDown = 6;
    public final static int jRightDown = 7;
    public final static int A = 8;
    final public static int B = 9;
    public final static int X = 10;
    public final static int Y = 11;
    final public static int LB = 12;
    final public static int RB = 13;
    public final static int back = 14;
    public final static int start = 15;
    final public static int dPadUp = 16;
    final public static int dPadDown = 17;
    final public static int dPadLeft = 18;
    final public static int dPadRight = 19;

    private ValueContainer previous = new ValueContainer();
    private Gamepad xGP;
    private boolean[] state = new boolean[NUM_NORMAL_KEY + NUM_PRECISE_KEY];


    public void setDebugMode(boolean value) {
        isDebug = value;
    }
    private class ValueContainer {

        double[] preciseKey = new double[NUM_PRECISE_KEY];
        boolean[] normalkey = new boolean[NUM_NORMAL_KEY];


        ValueContainer() {
            for (int i = 0; i < NUM_PRECISE_KEY; ++i) { preciseKey[i] = 0; }
            for (int i = 0; i < NUM_NORMAL_KEY; ++i) { normalkey[i] = false; }
        }
    }

    public Controller(Gamepad gamepad) {
        xGP = gamepad;
        for(int i = 0; i < state.length; ++i) { state[i] = false; }
    }

    public boolean isKeyChanged(int key) {
        return state[key];
    }

    public boolean isKeyHeld(int key) {
        return getValue(key) > 0;
    }

    public boolean isKeysChanged(int... keys) {
        for (int key : keys) {
            if(isKeyChanged(key)) return true;
        }

        return false;
    }

    public boolean isGamepadChanged() {
        for(boolean i : state) {
            if(i) return true;
        }
        return false;
    }

    public double getValue(int key) {
        if(key < NUM_PRECISE_KEY) return previous.preciseKey[key];
        else return previous.normalkey[key-NUM_PRECISE_KEY]? 1 : 0;
    }

    public boolean isKeyToggled(int key) {
        return isKeyChanged(key) && (getValue(key) > 0);
    }

    public void updateStatus() {

        try {
            ValueContainer current = new ValueContainer();

            //data Collection
            current.preciseKey[jLeftX] = xGP.left_stick_x;
            current.preciseKey[jLeftY] = xGP.left_stick_y;
            current.preciseKey[jRightX] = xGP.right_stick_x;
            current.preciseKey[jRightY] = xGP.right_stick_y;
            current.preciseKey[LT] = xGP.left_trigger;
            current.preciseKey[RT] = xGP.right_trigger;
            current.normalkey[A - NUM_PRECISE_KEY] = xGP.a;
            current.normalkey[B - NUM_PRECISE_KEY] = xGP.b;
            current.normalkey[X - NUM_PRECISE_KEY] = xGP.x;
            current.normalkey[Y - NUM_PRECISE_KEY] = xGP.y;
            current.normalkey[LB - NUM_PRECISE_KEY] = xGP.left_bumper;
            current.normalkey[RB - NUM_PRECISE_KEY] = xGP.right_bumper;
            current.normalkey[back - NUM_PRECISE_KEY] = xGP.back;
            current.normalkey[start - NUM_PRECISE_KEY] = xGP.start;
            current.normalkey[jLeftDown - NUM_PRECISE_KEY] = xGP.left_stick_button;
            current.normalkey[jRightDown - NUM_PRECISE_KEY] = xGP.right_stick_button;

            //Dpad -- No POV in FTC, makes life easier...
            current.normalkey[dPadUp - NUM_PRECISE_KEY] = xGP.dpad_up;
            current.normalkey[dPadDown - NUM_PRECISE_KEY] = xGP.dpad_down;
            current.normalkey[dPadLeft - NUM_PRECISE_KEY] = xGP.dpad_left;
            current.normalkey[dPadRight - NUM_PRECISE_KEY] = xGP.dpad_right;
            //State Comparison

            for (int i = 0; i < NUM_PRECISE_KEY; ++i) {
                state[i] = current.preciseKey[i] != previous.preciseKey[i];
            }

            for (int i = 0; i < NUM_NORMAL_KEY; ++i) {
                state[i + NUM_PRECISE_KEY] = current.normalkey[i] != previous.normalkey[i];
            }

            previous = current;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
