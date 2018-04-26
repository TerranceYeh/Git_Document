package org.firstinspires.ftc.teamcode.actuators;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by LBYPatrick on 10/27/2017.
 */

final public class ServoControl {


    //private static Servo servoObj = null;
    private double  minPos;
    private double  maxPos;
    private double  speedLimit  = 0.3;
    private boolean isClockWise;
    private Servo servo;


    public double getMaxPosition() {return maxPos;}

    public void updateSpeedLimit(double speed){this.speedLimit = speed;}
    
    double getLimitedSpeed(double speed) {return speed*speedLimit;}
    double getLimitedPosition(double position) {return (position > maxPos ? maxPos : (position < minPos? minPos : position));}
    
    public ServoControl(Servo servoObject, boolean isForward, double min, double max) {
        minPos = min;
        maxPos = max;
        servo = servoObject;
        servo.setDirection(Servo.Direction.FORWARD);
        servo.setDirection((isForward?Servo.Direction.FORWARD : Servo.Direction.REVERSE));
        isClockWise = isForward;
    }
    public void moveWithButton(boolean inward) {

        final double currentPosBuffer = servo.getPosition();
        final double expectedPosition = inward ? (currentPosBuffer - (speedLimit * speedLimit))
                : (currentPosBuffer + (speedLimit * speedLimit));

        move(expectedPosition);

    }

    public void moveFree(boolean inward) {
        final double currentPosBuffer = servo.getPosition();
        final double expectedPosition = inward ? (currentPosBuffer - (speedLimit * speedLimit))
                : (currentPosBuffer + (speedLimit * speedLimit));

        if(!(expectedPosition > 1 || expectedPosition < -1))
        servo.setPosition(expectedPosition);

    }

    public double getPosition() {
        return servo.getPosition();
    }

    public void move(double position) {

        /*
        double currentPosBuffer = servo.getPosition();


        if(position < currentPosBuffer) {
            servo.setDirection(isClockWise ? Servo.Direction.REVERSE : Servo.Direction.FORWARD);
            servo.setPosition(-position);
            servo.setDirection(isClockWise ? Servo.Direction.FORWARD : Servo.Direction.REVERSE);
        }
        */
        servo.setPosition(getLimitedPosition(position));
    }
}