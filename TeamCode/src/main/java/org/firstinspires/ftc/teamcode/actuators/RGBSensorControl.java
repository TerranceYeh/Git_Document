package org.firstinspires.ftc.teamcode.actuators;


import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by LBYPatrick on 12/6/2017.
 */

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
