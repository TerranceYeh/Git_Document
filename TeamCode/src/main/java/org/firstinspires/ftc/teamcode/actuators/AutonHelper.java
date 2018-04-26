package org.firstinspires.ftc.teamcode.actuators;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by LBYPatrick on 12/4/2017.
 */

@SuppressWarnings("StatementWithEmptyBody")
final public class AutonHelper {

    private final ElapsedTime stageTime = new ElapsedTime();
    private final LinearOpMode opModeObj;
    private final DriveTrain driveObj;
    private boolean isMecanum;


    public AutonHelper(LinearOpMode opMode, DriveTrain driveTrainObject, boolean isMecanum) {
        opModeObj = opMode;
        driveObj = driveTrainObject;
        this.isMecanum = isMecanum;
    }

    public boolean drive(double forwardBack, double leftRight, double time) {
        stageTime.reset();

        if(isMecanum) {driveObj.mecanumDrive(0,forwardBack,leftRight);}
        else driveObj.tankDrive(forwardBack,leftRight);

        while(this.opModeObj.opModeIsActive() && stageTime.milliseconds() <= time);
        driveObj.tankDrive(0,0);

        return this.opModeObj.opModeIsActive();

    }

    public boolean runMotor(MotorControl motor,double speed,double time) {

        this.stageTime.reset();

        motor.move(speed);
        while(this.opModeObj.opModeIsActive() && this.stageTime.seconds() <= time);
        motor.move(0);

        return this.opModeObj.opModeIsActive();

    }

}
