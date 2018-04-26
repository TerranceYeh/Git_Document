package org.firstinspires.ftc.teamcode.actuators;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by LBYPatrick on 2017/11/3.
 */

final public class DriveTrain {


    public enum Wheels{
        FRONT_LEFT,
        FRONT_RIGHT,
        REAR_LEFT,
        REAR_RIGHT,
    }

    private              MotorControl FL                 = null;
    private              MotorControl FR                 = null;
    private              MotorControl BL = null;
    private              MotorControl BR = null;
    private              double  maxSpeed           = 1.0;
    private              double  speedLevel         = 1.0;
    private              double  frontLeftPower     = 0;
    private              double  frontRightPower    = 0;
    private              double  rearLeftPower      = 0;
    private              double  rearRightPower     = 0;
    private              boolean is4WD              = false;
    private              boolean isMecanum          = false;
    private              int wheelMode = 1; //OMNI_WHEEL as default

    static public class WheelMode {
        final public static int TANK_WHEEL = 0;
        final public static int OMNI_WHEEL = 1;
        final public static int MECANUM_WHEEL = 2;
    }
    //Constructor for 4WD
    public DriveTrain(MotorControl frontLeftMotor, MotorControl frontRightMotor, MotorControl rearLeftMotor, MotorControl rearRightMotor) {

        FL = frontLeftMotor;
        FR = frontRightMotor;
        BL = rearLeftMotor;
        BR = rearRightMotor;

        FL.setReverse(true);
        BL.setReverse(true);

        is4WD = true;
    }

    public DriveTrain(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor rearLeftMotor, DcMotor rearRightMotor) {
        this(new MotorControl(frontLeftMotor), new MotorControl(frontRightMotor), new MotorControl(rearLeftMotor),new MotorControl(rearRightMotor));
    }

    public DriveTrain(DcMotor frontLeftMotor, DcMotor frontRightMotor) {
        this(new MotorControl(frontLeftMotor), new MotorControl(frontRightMotor));
    }


    //Constructor for 2WD
    public DriveTrain(MotorControl leftMotor, MotorControl rightMotor) {

        BL = leftMotor;
        BR = rightMotor;

        is4WD = false;
    }

    public void setWheelMode(int wheelMode) {
        if(wheelMode >= 0 && wheelMode <= 2) {this.wheelMode = wheelMode;}
    }

    public void drive(double sideMove, double forwardBack, double rotation) {
        switch(wheelMode) {
            case WheelMode.TANK_WHEEL:
                tankDrive(forwardBack,rotation);
                break;

            case WheelMode.OMNI_WHEEL:
                omniDrive(sideMove,forwardBack,rotation);
                break;
            case WheelMode.MECANUM_WHEEL:
                mecanumDrive(sideMove,forwardBack,rotation);
        }
    }

    public void tankDrive(double forwardBack, double rotation) {

        rotation = -rotation; // FTC 2018 tuning

        //Calculate Adequate Power Level for motors
        rearLeftPower = Range.clip(forwardBack + rotation, -1.0, 1.0);
        rearRightPower = Range.clip(forwardBack - rotation, -1.0, 1.0);

        if (is4WD) {
            frontLeftPower = rearLeftPower;
            frontRightPower = rearRightPower;
        }
        //Pass calculated power level to motors
        BL.move(rearLeftPower);
        BR.move(-rearRightPower);
        if(is4WD) {
            FL.move(frontLeftPower);
            FR.move(-frontRightPower);
        }

    }

    //From http://ftckey.com/programming/advanced-programming/
    public void omniDrive(double sideMove, double forwardBack, double rotation) {

        sideMove = -sideMove;

        FR.move(getLimited(forwardBack+sideMove-rotation));
        FL.move(getLimited(forwardBack-sideMove+rotation));
        BR.move(getLimited(forwardBack-sideMove-rotation));
        BL.move(getLimited(forwardBack+sideMove+rotation));
    }

    private double getLimited(double value) {
        if(value < -1) return -1;
        else if(value > 1) return 1;
        else return value;
    }

    public void mecanumDrive(double sideMove, double forwardBack, double rotation) {

        //A little Math from https://ftcforum.usfirst.org/forum/ftc-technology/android-studio/6361-mecanum-wheels-drive-code-example
        final double r = Math.hypot(sideMove, forwardBack);
        final double robotAngle = Math.atan2(forwardBack, sideMove) - Math.PI / 4;

        frontLeftPower = r * Math.cos(robotAngle) + rotation;
        frontRightPower = r * Math.sin(robotAngle) - rotation;
        rearLeftPower = r * Math.sin(robotAngle) + rotation;
        rearRightPower = r * Math.cos(robotAngle) - rotation;

        // Send calculated power to motors
        FL.move(frontLeftPower);
        FR.move(frontRightPower);
        BL.move(rearLeftPower);
        BR.move(rearRightPower);
    }

    public void updateSpeedLimit(double speed) {

        if(is4WD) {
            FL.updateSpeedLimit(speed);
            FR.updateSpeedLimit(speed);
        }
        BL.updateSpeedLimit(speed);
        BR.updateSpeedLimit(speed);
    }

    public double getEncoderInfo(Wheels position) {
        switch (position) {
            case FRONT_LEFT  : return FR.getCurrentPosition();
            case FRONT_RIGHT : return FL.getCurrentPosition();
            case REAR_LEFT   : return BL.getCurrentPosition();
            case REAR_RIGHT  : return BR.getCurrentPosition();
            default          : return 666; // Actually won't happen because the enum has already limited the actual parameter
        }
    }

    public double getSpeed(Wheels position) {
        switch (position) {
            case FRONT_LEFT  : return FR.getPower();
            case FRONT_RIGHT : return FL.getPower();
            case REAR_LEFT   : return BL.getPower();
            case REAR_RIGHT  : return BR.getPower();
            default          : return 666; // Won't happen because of the enum in parameter
        }
    }

    public boolean get4WDStat() {return is4WD;}
}