package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.actuators.*;


@TeleOp(name="Test1OpMode", group="Linear Opmode")
public class TeleOp10 extends LinearOpMode {

    static DriveTrain wheels;
    static ServoControl leftServer, rightServer;
    static MotorControl intakeLeft, intakeRight, liftLeft, liftRight;
    static Controller gamepad;
    static ElapsedTime runtime;

    public void initialize() {

        //底盘--四个车轮
        wheels = new DriveTrain(hardwareMap.dcMotor.get("back_left_drive"),
                                hardwareMap.dcMotor.get("back_right_drive"));

        //普通轮
        wheels.setWheelMode(DriveTrain.WheelMode.TANK_WHEEL);

        /**
         * 或者你们也可以...
         * wheels.setWheelMode(DriveTrain.WheelMode.MECANUM_WHEEL); // 麦康纳姆轮（万向轮）
         * wheels.setWheelMode(DriveTrain.WheelMode.OMNI_WHEEL); //全向轮
         */

        //左右两个servo手臂(好像你写的就是这个东西...)
        leftServer = new ServoControl(hardwareMap.servo.get("server_left"),true,-1, 1);
        rightServer = new ServoControl(hardwareMap.servo.get("server_right"),false,-1,1);

        intakeLeft = new MotorControl(hardwareMap.dcMotor.get("intake_left"),false);
        intakeRight = new MotorControl(hardwareMap.dcMotor.get("intake_right"),true);
        liftLeft = new MotorControl(hardwareMap.dcMotor.get("lift_left"),false);
        liftRight = new MotorControl(hardwareMap.dcMotor.get("lift_right"),true);
        //计时器 -- FTC官方也用这个
        runtime = new ElapsedTime();

        //手柄控制框架 -- 把FTC的gamepad1
        gamepad = new Controller(gamepad1);
    }

    public void outputData() {

        //轮胎信息
        telemetry.addData("Wheel Power", wheels.getSpeed(DriveTrain.Wheels.REAR_LEFT) + ", " + wheels.getSpeed(DriveTrain.Wheels.REAR_RIGHT));
        telemetry.addData("Lift Power", liftLeft.getPower() + ", " + liftRight.getPower());
        telemetry.addData("Intake Power", intakeLeft.getPower() + ", " + intakeRight.getPower());
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


            //伺服 (A键收回，B键放出）
            if(gamepad.isKeysChanged(Controller.A, Controller.B)) {
                leftServer.moveWithButton(gamepad.isKeyHeld(Controller.A));
                rightServer.moveWithButton(gamepad.isKeyHeld(Controller.A));
            }

            //升降，方向键上下控制
                liftLeft.moveWithButton(gamepad.isKeyHeld(Controller.dPadUp),gamepad.isKeyHeld(Controller.dPadDown));
                liftLeft.moveWithButton(gamepad.isKeyHeld(Controller.dPadUp),gamepad.isKeyHeld(Controller.dPadDown));

            //吸入方块，X,Y控制
                intakeLeft.moveWithButton(gamepad.isKeyHeld(Controller.X),gamepad.isKeyHeld(Controller.Y));
                intakeRight.moveWithButton(gamepad.isKeyHeld(Controller.X),gamepad.isKeyHeld(Controller.Y));


            //输出信息
            outputData();
        }

        //到这个地方就是比赛结束了，此时isOpModeActive()返回的就是false了，循环就会跳出来

    }
}
