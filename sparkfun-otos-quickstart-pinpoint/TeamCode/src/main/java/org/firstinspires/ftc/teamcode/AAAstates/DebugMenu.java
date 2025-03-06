package org.firstinspires.ftc.teamcode.AAAstates;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.acmerobotics.roadrunner.ftc.Encoder;

@Disabled//(name="DebugMenu", group="Linear OpMode")
public class DebugMenu extends LinearOpMode {

        // ROBOT MOVEMENT //
    //Initializes all the direct current motors for the driving function of our robot, gary.
    private DcMotor[] Motors = {hardwareMap.get(DcMotor.class, "left_back"), hardwareMap.get(DcMotor.class, "right_back"),
                hardwareMap.get(DcMotor.class, "left_front"), hardwareMap.get(DcMotor.class, "right_front"), hardwareMap.get(DcMotor.class, "vertical_slide_left"),
                hardwareMap.get(DcMotor.class, "vertical_slide_right"), hardwareMap.get(DcMotor.class, "hang_motor_left"), hardwareMap.get(DcMotor.class, "hang_motor_right")};
    private Servo[] Servos = {hardwareMap.get(Servo.class, "bucket_arm_woohoo"), hardwareMap.get(Servo.class, "horizontal_arm"),
                hardwareMap.get(Servo.class, "horizontal_claw"), hardwareMap.get(Servo.class, "horizontal_slide"), hardwareMap.get(Servo.class, "sweeper")};
    private DcMotor[][] driveMotors = {/*f*/{Motors[0], Motors[1], Motors[2], Motors[3]}, /*b*/{Motors[0], Motors[1], Motors[2], Motors[3]}, /*l*/{Motors[0], Motors[1], Motors[2], Motors[3]},
            /*r*/{Motors[0], Motors[1], Motors[2], Motors[3]}, /*lr*/{Motors[0], Motors[1], Motors[2], Motors[3]}, /*rr*/{Motors[0], Motors[1], Motors[2], Motors[3]},
            /*vls*/{Motors[4], Motors[5], null, null}, /*hang*/{Motors[6], Motors[7], null, null}};
    private float[][] driveDirs = {/*f*/{1, 1, 1, 1}, /*b*/{-1, -1, -1, -1}, /*l*/{1, -1, -1, 1}, /*r*/{-1, 1, 1, -1}, /*rl*/{1, -1, 1, -1}, /*rr*/{-1, 1, -1, 1}, /*vls*/{1, 1}, /*hang*/{0, 0, 0, 0}};
    private DcMotor currentMotor;
    private Servo currentServo;
    private String[] MotorNames = {"left back wheel", "right back wheel", "left front wheel", "right front wheel", "linear slide"};
    private String[] ServoNames = {"vertical arm", "horizontal arm", "horizontal claw", "horizontal slide", "sweeper"};
    private String[] DriveNames = {"forward", "backward", "left", "right", "left rotate", "right rotate", "vertical slide", "hang"};
    private int currentMotorNum, currentServoNum, currentDriveNum;
    private int mode = 0;


        // JOYSTICK and MOVEMENT CONTROLS //
    private float /*LjoystickX, */LjoystickY, /*RjoystickX, RjoystickY, */Ltrigger;
    private boolean Lbumper, Rbumper, LDpad, RDpad;
    private boolean paused = true;




    @Override
    public void runOpMode() {
        // initializing the motors (pseudocode) (:skull:, :fire:, :splash:, :articulated-lorry:, :flushed:, :weary:, :sob:);
        Motors[0].setDirection(DcMotor.Direction.REVERSE);
        Motors[2].setDirection(DcMotor.Direction.REVERSE);

        waitForStart(); //waits for play on the driver hub :3

        while (opModeIsActive()) {
            telemetry.addData("CONTROLS: (Gamepad1) Use left stick y to increase/decrease position/speed,", "hold left trigger to slow down speed change, switch modes with dPad up/down, switch motors/servos with left/right bumper, click A to pause/play");
            //LjoystickX = gamepad1.left_stick_x;
            LjoystickY = gamepad1.left_stick_y;
            //RjoystickX = gamepad1.right_stick_x;
            //RjoystickY = gamepad1.right_stick_y;
            Lbumper = gamepad1.left_bumper;
            Rbumper = gamepad1.right_bumper;
            Ltrigger = gamepad1.left_trigger;
            LDpad = gamepad1.dpad_left;
            RDpad = gamepad1.dpad_right;

            if(gamepad1.a) {
                paused = !paused;
            }

            if(!paused) {
                if (Lbumper) {
                    mode--;
                } else if (Rbumper) {
                    mode++;
                }

                if (mode == 3) {
                    mode = 0;
                } else if (mode == -1) {
                    mode = 2;
                }

                if (mode == 0) {
                    telemetry.addData("Current Mode:", "Motor");
                    MotorMode();
                } else if (mode == 1) {
                    telemetry.addData("Current Mode:", "Servo");
                    ServoMode();
                } else if (mode == 2) {
                    telemetry.addData("Current Mode:", "Drive (2+ motors)");
                    DriveMode();
                }
            }
            telemetry.addData("paused =", paused);
            telemetry.addData("Motor positions:", "");
            telemetry.addData("left back motor:", Motors[0].getCurrentPosition());
            telemetry.addData("right back motor:", Motors[1].getCurrentPosition());
            telemetry.addData("left front motor:", Motors[2].getCurrentPosition());
            telemetry.addData("right front motor:", Motors[3].getCurrentPosition());
            telemetry.addData("right vls motor:", Motors[5].getCurrentPosition());


            telemetry.update(); //update output screen
        }


    }

    private void MotorMode() {
        //switches motors if dpad pressed
        if(LDpad) {
            currentMotor.setPower(0);
            currentMotorNum--;
        } else if(RDpad) {
            currentMotor.setPower(0);
            currentMotorNum++;
        }

        //loops back if selected motor is out of index
        if(currentMotorNum < 0) {
            currentMotorNum = Motors.length - 1;
        } else if(currentMotorNum >= Motors.length) {
            currentMotorNum = 0;
        }

        //sets motor power
        currentMotor = Motors[currentMotorNum];
        currentMotor.setPower(LjoystickY);
        telemetry.addData("Moving motor:", MotorNames[currentMotorNum], ", at speed:", currentMotor.getPower());
    }
    private void ServoMode() {
        //changes servo if dpad pressed
        if(LDpad) {
            currentServoNum--;
        } else if(RDpad) {
            currentServoNum++;
        }

        //loops back if chosen servo out of index
        if(currentServoNum < 0) {
            currentServoNum = Servos.length - 1;
        } else if(currentServoNum >= Servos.length) {
            currentServoNum = 0;
        }
        //sets to current servo
        currentServo = Servos[currentServoNum];

        //sets servo pos
        float newServoPos =  (float)currentServo.getPosition() + (LjoystickY/5)* (1-Ltrigger);
        currentServo.setPosition(newServoPos);
        telemetry.addData("Moving Servo:", ServoNames[currentServoNum], ", at speed:", currentServo.getPosition());
    }
    private void DriveMode() {
        //reset motors from last update
        for(DcMotor motor : driveMotors[currentDriveNum]) {
            motor.setPower(0);
        }

        //switch between drives if dpad buttons pressed
        if(LDpad) {
            currentDriveNum--;
        } else if(RDpad) {
            currentDriveNum++;
        }

        //loop back if chosen drive is outside of index
        if(currentDriveNum < 0) {
            currentDriveNum = driveMotors.length - 1;
        } else if(currentDriveNum >= driveMotors.length) {
            currentDriveNum = 0;
        }

        //sets motor powers
        int motorIndex = 0;
        for(DcMotor motor : driveMotors[currentDriveNum]) {
            motor.setPower(driveDirs[currentDriveNum][motorIndex] * LjoystickY);
            motorIndex++;
        }

        telemetry.addData("Doing/Going:", DriveNames[currentDriveNum], ", at speed:", LjoystickY);
    }
}
