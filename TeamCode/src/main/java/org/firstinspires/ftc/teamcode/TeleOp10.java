package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.actuators.*;


@TeleOp(name="Test1OpMode", group="Linear Opmode")
public class TeleOp10 extends LinearOpMode {

    static DriveTrain wheels;
    static ServoControl leftArm, rightArm;
    static Controller gamepad;
    static ElapsedTime runtime;

    public void initialize() {

        //底盘--四个车轮
        wheels = new DriveTrain(hardwareMap.dcMotor.get("front_left_drive"),
                                hardwareMap.dcMotor.get("front_right_drive"),
                                hardwareMap.dcMotor.get("back_left_drive"),
                                hardwareMap.dcMotor.get("back_right_drive"));

        //普通轮
        wheels.setWheelMode(DriveTrain.WheelMode.TANK_WHEEL);

        /**
         * 或者你们也可以...
         * wheels.setWheelMode(DriveTrain.WheelMode.MECANUM_WHEEL); // 麦康纳姆轮（万向轮）
         * wheels.setWheelMode(DriveTrain.WheelMode.OMNI_WHEEL); //全向轮
         */

        //左右两个servo手臂(好像你写的就是这个东西...)
        leftArm = new ServoControl(hardwareMap.servo.get("arm_left"),true,-1, 1);
        rightArm = new ServoControl(hardwareMap.servo.get("arm_left"),false,-1,1);

        //计时器 -- FTC官方也用这个
        runtime = new ElapsedTime();

        //手柄控制框架 -- 把FTC的gamepad1
        Controller gamepad = new Controller(gamepad1);
    }

    public void outputData() {

        //轮胎信息
        telemetry.addData("LF Wheel Power", wheels.getSpeed(DriveTrain.Wheels.FRONT_LEFT));
        telemetry.addData("RF Wheel Power", wheels.getSpeed(DriveTrain.Wheels.FRONT_RIGHT));
        telemetry.addData("LB Wheel Power", wheels.getSpeed(DriveTrain.Wheels.REAR_LEFT));
        telemetry.addData("RB Wheel Power", wheels.getSpeed(DriveTrain.Wheels.REAR_RIGHT));

        //输出到Driver Station
        telemetry.update();
    }

    @Override
    public void runOpMode() {

        //初始化硬件
        initialize();

        //等用户在手机上按开始键 + 手柄上按PLAY + A键(手柄的两个键要同时按)
        waitForStart();

        //循环监测按钮+输出信息
        while(opModeIsActive()) {

            //获取手柄信息
            gamepad.updateStatus();

            //RT和LT控制油门和刹车，左摇杆控制旋转
            if(gamepad.isKeysChanged(Controller.RT, Controller.LT, Controller.jLeftX)) {

                wheels.drive(0,
                                gamepad.getValue(Controller.RT) - gamepad.getValue(Controller.LT),
                                gamepad.getValue(Controller.jLeftX));
            }


            //手臂
            if(gamepad.isKeysChanged(Controller.dPadLeft, Controller.dPadRight)) {
                leftArm.moveWithButton(gamepad.isKeyHeld(Controller.dPadLeft));
                rightArm.moveWithButton(gamepad.isKeyHeld(Controller.dPadLeft));
            }

            //输出信息
            outputData();
        }

        //到这个地方就是比赛结束了，此时isOpModeActive()返回的就是false了，循环就会跳出来

    }
}
