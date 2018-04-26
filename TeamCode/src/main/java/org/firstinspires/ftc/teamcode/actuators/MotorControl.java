package org.firstinspires.ftc.teamcode.actuators;


import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by LBYPatrick on 11/9/2017.
 */

public class MotorControl {

            private     double  speedLimit = 1.0;
            private     double  motorSpeed = 0;
            private     DcMotor motor;

    public MotorControl(DcMotor dcMotorObj, boolean isForward) {

        motor = dcMotorObj;
        motor.setDirection((isForward?DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE));
    }

    public void setReverse(boolean isReverse) {
        motor.setDirection(isReverse? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
    }

    public MotorControl(DcMotor dcMotorObj) {
        this(dcMotorObj, true);
    }

    private double getLimitedSpeed(double rawSpeed) {return rawSpeed * speedLimit;}

    public void moveWithButton(boolean up, boolean down) {

        if (up == down) motor.setPower(0);
        else { motor.setPower(getLimitedSpeed(up? 1 : -1)); }
    }

    public void move(double value) {
        motor.setPower(getLimitedSpeed(value));
    }

    public double getPower() { return motor.getPower(); }

    public double getSpeed() {
        return getPower();
    }

    private int getPosition() {
        return motor.getCurrentPosition();
    }

    public int getCurrentPosition() {
        return getPosition();
    }

    public void updateSpeedLimit(double speed) { speedLimit = speed; }
}
