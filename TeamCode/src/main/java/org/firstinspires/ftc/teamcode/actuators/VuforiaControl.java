package org.firstinspires.ftc.teamcode.actuators;

/**
 * Created by LBYPatrick on 12/7/2017.
 */

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import com.qualcomm.robotcore.hardware.HardwareMap;


final public class VuforiaControl {

    class Positions {
        final static int LEFT = 0;
        final static int CENTER = 1;
        final static int RIGHT = 2;
        final static int UNKNOWN = 3;
        final static int ERROR = 4;
    }

    private final VuforiaTrackable relicTemplate;
    private VuforiaTrackables relicTrackables;


    public VuforiaControl(HardwareMap hardwareMap, boolean isBackCamera, boolean isMonitorUsed,String licenseKey) {


        //Determine whether using the camera monitor mode
        VuforiaLocalizer.Parameters parameters;
        if(isMonitorUsed) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        }
        else parameters = new VuforiaLocalizer.Parameters();

        //Determine which camera to use
        parameters.cameraDirection = isBackCamera ? VuforiaLocalizer.CameraDirection.BACK : VuforiaLocalizer.CameraDirection.FRONT;

        //Put license key in
        parameters.vuforiaLicenseKey = licenseKey;
        VuforiaLocalizer device = ClassFactory.createVuforiaLocalizer(parameters);

        //Load Vuforia template for FTC relic recovery
        relicTrackables = device.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);

        relicTrackables.activate();
    }

    public int getPosition() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        switch(vuMark) {
            case LEFT: return Positions.LEFT;
            case CENTER: return Positions.CENTER;
            case RIGHT: return Positions.RIGHT;
            case UNKNOWN: return Positions.UNKNOWN;
            default: return Positions.ERROR;
        }
    }

    public void shutdown() {
        this.relicTrackables.deactivate();
    }

}
