package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="DebugMenu", group="Linear OpMode")
public class DebugMenu extends LinearOpMode {

        // ROBOT MOVEMENT //
    //Initializes all the direct current motors for the driving function of our robot, gary.
    private DcMotor[] Motors = {hardwareMap.get(DcMotor.class, "bl"), hardwareMap.get(DcMotor.class, "br"),
                hardwareMap.get(DcMotor.class, "fl"), hardwareMap.get(DcMotor.class, "fr"), hardwareMap.get(DcMotor.class, "ls")};
    private DcMotor currentMotor;
    private Servo[] Servos = {hardwareMap.get(Servo.class, "vas"), hardwareMap.get(Servo.class, "hao"),
            hardwareMap.get(Servo.class, "hcs"), hardwareMap.get(Servo.class, "hls"), hardwareMap.get(Servo.class, "sweeper")};
    private Servo currentServo;
    private String[] MotorNames = {"left back wheel", "right back wheel", "left front wheel", "right front wheel", "linear slide"};
    private String[] ServoNames = {"vertical arm", "horizontal arm", "horizontal claw", "horizontal slide", "sweeper"};
    private String[] DriveNames = {"forward", "backward", "left", "right", "left rotate", "right rotate"};
    private int currentMotorNum, currentServoNum, currentDriveNum;
    private int mode = 0;


        // JOYSTICK and MOVEMENT CONTROLS //
    private float /*LjoystickX, */LjoystickY, /*RjoystickX, RjoystickY, */Ltrigger;
    private boolean Lbumper, Rbumper, LDpad, RDpad;
    private boolean paused = false;




    @Override
    public void runOpMode() {
        // initializing the motors (pseudocode) (:skull:, :fire:, :splash:, :articulated-lorry:, :flushed:, :weary:, :sob:);
        Motors[0].setDirection(DcMotor.Direction.REVERSE);
        Motors[2].setDirection(DcMotor.Direction.REVERSE);

        waitForStart(); //waits for play on the driver hub :3

        while (opModeIsActive()) {
            telemetry.addData("CONTROLS: (Gamepad1) Use left stick y to increase/decrease position/speed,", "hold left trigger to slow down speed change, switch modes with dPad up/down, switch motors/servos with left/right bumper");
            //LjoystickX = gamepad1.left_stick_x;
            LjoystickY = gamepad1.left_stick_y;
            //RjoystickX = gamepad1.right_stick_x;
            //RjoystickY = gamepad1.right_stick_y;
            Lbumper = gamepad1.left_bumper;
            Rbumper = gamepad1.right_bumper;
            Ltrigger = gamepad1.left_trigger;
            LDpad = gamepad1.dpad_left;
            RDpad = gamepad1.dpad_right;

            if(Lbumper) {
                mode--;
            } else if(Rbumper) {
                mode++;
            }

            if(mode == 3) {
                mode = 0;
            } else if(mode == -1) {
                mode = 2;
            }

            if(mode == 0) {
                telemetry.addData("Current Mode:","Motor");
                MotorMode();
            } else if(mode == 1) {
                telemetry.addData("Current Mode:","Servo");
                ServoMode();
            } else if(mode == 2) {
                telemetry.addData("Current Mode:","Drive");
                DriveMode();
            }

            telemetry.update(); //update output screen
        }


    }

    private void MotorMode() {
        if(LDpad) {
            currentMotor.setPower(0);
            currentMotorNum--;
        } else if(RDpad) {
            currentMotor.setPower(0);
            currentMotorNum++;
        }

        if(currentMotorNum < 0) {
            currentMotorNum = Motors.length - 1;
        } else if(currentMotorNum >= Motors.length) {
            currentMotorNum = 0;
        }

        currentMotor = Motors[currentMotorNum];
        currentMotor.setPower(LjoystickY);
        telemetry.addData("Moving motor", MotorNames[currentMotorNum], "at speed", currentMotor.getPower());
    }
    private void ServoMode() {
        if(LDpad) {
            currentServoNum--;
        } else if(RDpad) {
            currentServoNum++;
        }

        if(currentServoNum < 0) {
            currentServoNum = Servos.length - 1;
        } else if(currentServoNum >= Servos.length) {
            currentServoNum = 0;
        }

        float newServoPos =  (float)currentServo.getPosition() + (LjoystickY/5)* (1-Ltrigger);
        currentServo.setPosition(newServoPos);
    }
    private void DriveMode() {

    }
}
