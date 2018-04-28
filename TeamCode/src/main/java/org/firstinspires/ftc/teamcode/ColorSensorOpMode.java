package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * This is an example LinearOpMode that shows how to use a color sensor in a generic
 * way, insensitive which particular make or model of color sensor is used. The opmode
 * assumes that the color sensor is configured with a name of "color sensor".
 *
 * If the color sensor has a light which is controllable, you can use the X button on
 * the gamepad to toggle the light on and off.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "Sensor: Blue", group = "Sensor")

public class ColorSensorOpMode extends LinearOpMode {


    final public class RGBSensorControl {

        class BallColor {
            final static int BLUE = 0;
            final static int RED = 1;
            final static int UNKNOWN = 2;
        }

        private final ColorSensor device;
        public int redVal = 0;
        public int blueVal = 0;
        public int greenVal = 0;
        private float [] hsvValue = new float[3];

        public RGBSensorControl (ColorSensor colorSensorObject) {this.device = colorSensorObject;}

        public int getBallColor() {

            //Update color sensor data
            updateColorData();

            Color.RGBToHSV((redVal * 255) / 800, (greenVal * 255) / 800, (blueVal * 255) / 800, hsvValue);

            if(hsvValue[1] > 0.6) {
                if (hsvValue[0] >= 210 && hsvValue[0] <= 275) return BallColor.BLUE;
                else if (hsvValue[0] >= 330 || hsvValue[0] <= 40) return BallColor.RED;
            }

            return BallColor.UNKNOWN;
        }

        public void updateColorData() {
            device.enableLed(true);
            redVal = device.red();
            greenVal = device.green();
            blueVal = device.blue();
            device.enableLed(false);
        }
    }


    /** The colorSensor field will contain a reference to our color sensor hardware object */
    ColorSensor colorSensor;
    DcMotor leftMoter;
    DcMotor rightMoter;
    DcMotor TopleftMoter;
    DcMotor ToprightMoter;
    Servo servo_1 = null;
    Servo servo_2 = null;
    double servoPosition = 0.0;

    /** The relativeLayout field is used to aid in providing interesting visual feedback
     * in this sample application; you probably *don't* need something analogous when you
     * use a color sensor on your robot */
    View relativeLayout;

    /**
     * The runOpMode() method is the root of this LinearOpMode, as it is in all linear opModes.
     * Our implementation here, though is a bit unusual: we've decided to put all the actual work
     * in the main() method rather than directly in runOpMode() itself. The reason we do that is that
     * in this sample we're changing the background color of the robot controller screen as the
     * opmode runs, and we want to be able to *guarantee* that we restore it to something reasonable
     * and palatable when the opMode ends. The simplest way to do that is to use a try...finally
     * block around the main, core logic, and an easy way to make that all clear was to separate
     * the former from the latter in separate methods.
     */
    @Override

    public void runOpMode() throws InterruptedException {

        colorSensor = hardwareMap.get(ColorSensor.class, "sensor_color");
        servo_1 = hardwareMap.get(Servo.class, "servo_1");
        servo_2 = hardwareMap.get(Servo.class, "servo_2");

        //Initialize Wheels
        leftMoter = hardwareMap.get(DcMotor.class, "backleft_drive");
        rightMoter = hardwareMap.get(DcMotor.class, "backright_drive");
        leftMoter.setDirection(DcMotor.Direction.FORWARD);
        rightMoter.setDirection(DcMotor.Direction.REVERSE);

        RGBSensorControl color_sensor = new RGBSensorControl(colorSensor);

        waitForStart();

        while(!opModeIsActive()); //Block Code Execution

        //Phase 1
        servo_2.setPosition(0.8);
        sleep(2000);
        servo_1.setPosition(0.7);
        sleep(2000);

        //Phase 2
        switch(color_sensor.getBallColor()) {

            case RGBSensorControl.BallColor.BLUE :

                servo_2.setPosition(0.1);
                break;

            case RGBSensorControl.BallColor.RED:

                servo_2.setPosition(1);

            default: break;
        }
        sleep(1000);

        //Final Phase
        leftMoter.setPower(0.9);
        rightMoter.setPower(0.75);
        sleep(3000);

        leftMoter.setPower(0);
        rightMoter.setPower(0);

    }
}