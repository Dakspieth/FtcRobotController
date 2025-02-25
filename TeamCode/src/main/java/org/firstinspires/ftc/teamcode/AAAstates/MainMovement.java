package org.firstinspires.ftc.teamcode.AAAstates;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="MainMovement", group="Linear OpMode")
public class MainMovement extends LinearOpMode {

        // ROBOT MOVEMENT //
    private DcMotor leftBack, rightBack, leftFront, rightFront; //Initializes all the direct current motors for the driving function of our robot, gary.
    float speedSlow = 0.45f, speedFast = 1f; // slow and fast mode for movement
    float netS; // speed the motor actually uses
    double StrafeBL = 0f, StrafeBR = 0f, StrafeFL = 0f, StrafeFR = 0f;
    double RotateBL = 0f, RotateBR = 0f, RotateFL = 0f, RotateFR = 0f;
    boolean Strafing, Rotating;

    float rotationSpeed = 1f; // Robot rotation speed multiplier, 1.5 for fast mode, 0.5 for slow mode

        // JOYSTICK and MOVEMENT CONTROLS //
    public float LjoystickX, LjoystickY, RjoystickX, RjoystickY;
    final float joystickDeadzone = 0.1f; // Area where joystick will not detect input

    double hsMinExtensionR, hsMaxExtensionR;
    double hArmUpValue, hArmDownValue, hArmMidValue;

        // ROBOT OTHER STUFF //

    private boolean sweep = false;

    //private ElapsedTime hSlideTimer = new ElapsedTime();
    private ElapsedTime hClawTimer = new ElapsedTime();
    private ElapsedTime hArmTimer = new ElapsedTime();
    private ElapsedTime vArmTimer = new ElapsedTime();
    private ElapsedTime transferTimer = new ElapsedTime();
    private ElapsedTime sweeperTimer = new ElapsedTime();
    private  ElapsedTime hangTimer1 = new ElapsedTime();


    //private ElapsedTime transferCD = new ElapsedTime(); //cooldown 4 transfer

    boolean enableTransfer = false;

    // hang stuff
    boolean hangMotorsOn = false;
    private int hangDirection = -1;



    //private final float clawSpeed = 1.0f; unused idk whats up w/ this

        // vertical slide
    private DcMotor vLinearSlideRight, vLinearSlideLeft; // motor to control vertical linear slide

    private DcMotor hangMotorLeft, hangMotorRight;
    private Servo vArmServo;  // v is slang for vertical btw
    boolean vSlideArmOut = false; // mounted onto the linear slide
    private final float linearSlideSpeed = 0.75f; // if this becomes 1 it will make the speed faster O.-

        // horizontal slide
    boolean hArmUp = false;
    private Servo hClawServo, hLinearSlideLeft, hLinearSlideRight, hClawRotate; // h is short for horizontal btw
    private Servo hArmOpen, sweeper;
    boolean hClawOpen = false;

    int transferStep = 0;
    int chamberStep = 0;






    @Override
    public void runOpMode() {
        // initializing the motors (pseudocode) (:skull:, :fire:, :splash:, :articulated-lorry:, :flushed:, :weary:, :sob:);
        leftBack = hardwareMap.get(DcMotor.class, "left_back"); //    CH0
        rightBack = hardwareMap.get(DcMotor.class, "right_back"); //   EH0
        leftFront = hardwareMap.get(DcMotor.class, "left_front"); //   CH1
        rightFront = hardwareMap.get(DcMotor.class, "right_front"); //  EH1
        vLinearSlideLeft = hardwareMap.get(DcMotor.class, "vertical_slide_left"); // CH2
        vLinearSlideRight = hardwareMap.get(DcMotor.class, "vertical_slide_right"); //  EH2
        hangMotorLeft = hardwareMap.get(DcMotor.class, "hang_motor_left"); // CH3
        hangMotorRight = hardwareMap.get(DcMotor.class, "hang_motor_right"); // EH3

        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        vLinearSlideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        vLinearSlideLeft.setDirection(DcMotor.Direction.REVERSE);
        hangMotorRight.setDirection(DcMotor.Direction.REVERSE);


        sweeper = hardwareMap.get(Servo.class, "sweeper"); //  CH0
        vArmServo = hardwareMap.get(Servo.class, "bucket_arm_woohoo"); //     CH2
        hLinearSlideLeft = hardwareMap.get(Servo.class, "horizontal_slide_left"); //  CH1
        hLinearSlideRight = hardwareMap.get(Servo.class, "horizontal_slide_right"); //  EH1
        hArmOpen = hardwareMap.get(Servo.class, "horizontal_arm"); //      EH4
        hClawServo = hardwareMap.get(Servo.class, "horizontal_claw"); //    EH5

        hArmOpen.setDirection(Servo.Direction.REVERSE);

        //hArmOpen.setPosition(0.84);

        //sweeper.setPosition(0);




       // SetVSlideSpeed(0); // zero the linear slide's power so it doesn't move while not active

        /*leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vLinearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);*/

        /*leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vLinearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);*/

        telemetry.addData("Status", "Initialized OwO");
        telemetry.update();

        waitForStart(); //waits for play on the driver hub :3
        vArmServo.setPosition(0.775);
        while (opModeIsActive()) {
            // detecting the x and y position of both joysticks
            LjoystickX = gamepad1.left_stick_x;
            LjoystickY = gamepad1.left_stick_y;
            RjoystickX = gamepad1.right_stick_x;
            RjoystickY = gamepad1.right_stick_y;

            epicRotationMovement(); // rotation on gary, the robot
            legendaryStrafeMovement(); // movement on gary

            HorizontalSlideMovement();
            HorizontalClawAndArm();
            VerticalSlideMovement();
            VerticalArmAndOuttake();
            TransferFunction();
            Sweeper();
            //LevelTwoHang();


            setMotorPowers();
            setHLSL();
            //TODO: values in gc ^^^^

            telemetry.addData("transfer milli:", transferTimer.milliseconds());
            telemetry.addData("transfer step:", transferStep);
            telemetry.addData("sweeper:", sweeper.getPosition());

            telemetry.update(); //update output screen
        }


    }

    //////////////////////// START OF MOVEMENT CODE ////////////////////////

    //////////////////////// START OF MOVEMENT CODE ////////////////////////

    //////////////////////// START OF MOVEMENT CODE ////////////////////////

    private void epicRotationMovement() {
        // rotates the robot if left stick is not being used (movement takes priorities)
        if (Math.abs(RjoystickX) > joystickDeadzone / 2) {
            Rotating = true;
            if(RjoystickX > 0) {
               // clockwise rotation
                wheelRotate(Math.abs(RjoystickX), -Math.abs(RjoystickX), Math.abs(RjoystickX), -Math.abs(RjoystickX));
                telemetry.addData("Right Stick rotating LEFT: ", RjoystickX);

            } else if (RjoystickX < 0) {
                // counter-clockwise rotation
                wheelRotate(-Math.abs(RjoystickX), Math.abs(RjoystickX), -Math.abs(RjoystickX), Math.abs(RjoystickX));
                telemetry.addData("Right Stick rotating RIGHT: ", RjoystickX);
                
            }
        } else {
            Rotating = false;
            wheelRotate(0, 0, 0, 0);
        }
    }

    //    _                 _        _    _ _             _         _     _                                 __ _
    //   (_)               | |      ( )  (_|_)           | |       ( )   | |                               / _| |
    //    _  __ _  ___ ___ | |__    |/    _ _  __ _  __ _| |_   _  |/    | |__   __ _ _ __   ___ _ __ ___ | |_| |_
    //   | |/ _` |/ __/ _ \| '_ \        | | |/ _` |/ _` | | | | |       | '_ \ / _` | '_ \ / __| '__/ _ \|  _| __|
    //   | | (_| | (_| (_) | |_) |       | | | (_| | (_| | | |_| |       | |_) | (_| | | | | (__| | | (_) | | | |_
    //   | |\__,_|\___\___/|_.__/        | |_|\__, |\__, |_|\__, |       |_.__/ \__,_|_| |_|\___|_|  \___/|_|  \__|
    //  _/ |                            _/ |   __/ | __/ |   __/ |
    // |__/                            |__/   |___/ |___/   |___/

    private void legendaryStrafeMovement() {
        float maxSpeed = 1.0f; // Cap for speed robot can travel
        double addSpeed = Math.sqrt(LjoystickX * LjoystickX + LjoystickY * LjoystickY); // Added speed by calculating the distance the joystick is from the center

        // Alternate between SLOW && FAST mode depending on which bumper is held :P
        if (gamepad1.left_bumper) {    // slow mode !
            netS = speedSlow;    // speed is set to a slow constant speed for more precise movements
            rotationSpeed = speedSlow;

        } else if (gamepad1.right_bumper) {     // fast mode !
            speedFast = (Math.max(1.5f, speedFast + 0.05f)); // for making the fast mode accelerate gradually instead of instantly going faster
            netS = (Math.min(maxSpeed, (float) (addSpeed - joystickDeadzone) / (1.0f - joystickDeadzone))) * speedFast; // Speed is multiplied by the speedFast variable
            rotationSpeed = speedFast;

        } else { // default- no bumpers are held !
            netS = Math.min(maxSpeed, (float) (addSpeed - joystickDeadzone) / (1.0f - joystickDeadzone)); // Speed is set to default speed
            rotationSpeed = 1f;
            speedFast = 1f;
        }

        // calculates the angle of the joystick in radians --> degrees..
        double LangleInRadians = Math.atan2(-LjoystickY, LjoystickX);
        double LangleInDegrees = LangleInRadians * (180 / Math.PI);

        // strafe based on joystick angle :D
        if (Math.abs(LjoystickX) > joystickDeadzone || Math.abs(LjoystickY) > joystickDeadzone) {
            Strafing = true;
            //if stick is past the dead zone ->
            if (LangleInDegrees >= -22.5 && LangleInDegrees <= 22.5) {
                // right quadrant
                wheelStrafe(-netS, netS, netS, -netS);
                telemetry.addData("Left Stick quadrant: ", "RIGHT");

            } else if (LangleInDegrees > 22.5 && LangleInDegrees < 67.5) {
                // top-right quadrant
                wheelStrafe(0, netS, netS, 0);
                telemetry.addData("Left Stick quadrant: ", "TOP RIGHT");

            } else if (LangleInDegrees > -67.5 && LangleInDegrees < -22.5) {
                // bottom-right quadrant
                wheelStrafe(-netS, 0, 0, -netS);
                telemetry.addData("Left Stick quadrant: ", "BOTTOM RIGHT");

            } else if (LangleInDegrees >= 67.5 && LangleInDegrees <= 112.5) {
                // top quadrant
                wheelStrafe(netS, netS, netS, netS);
                telemetry.addData("Left Stick quadrant: ", "TOP");

            } else if (LangleInDegrees > -112.5 && LangleInDegrees < -67.5) {
                // bottom quadrant
                wheelStrafe(-netS, -netS, -netS, -netS);
                telemetry.addData("Left Stick quadrant: ", "BOTTOM");

            } else if (LangleInDegrees > 112.5 && LangleInDegrees < 157.5) {
                // top-left quadrant
                wheelStrafe(netS, 0, 0, netS);
                telemetry.addData("Left Stick quadrant: ", "TOP LEFT");

            } else if (LangleInDegrees > -157.5 && LangleInDegrees < -112.5) {
                // bottom-left quadrant
                wheelStrafe(0, -netS, -netS, 0);
                telemetry.addData("Left Stick quadrant: ", "BOTTOM LEFT");

            } else if (LangleInDegrees >= 157.5 || LangleInDegrees <= -157.5) {
                // left quadrant
                wheelStrafe(netS, -netS, -netS, netS);
                telemetry.addData("Left Stick quadrant: ", "LEFT");

            }

        } else {
            Strafing = false;

            wheelStrafe(0, 0, 0, 0);
            telemetry.addData("nut driving", null);
        }
    }
    private void wheelStrafe(double bl, double br, double fl, double fr) { // For setting the wheel strafe values
        StrafeBL = bl;
        StrafeBR = br;
        StrafeFL = fl;
        StrafeFR = fr;
    }
    private void wheelRotate(double bl, double br, double fl, double fr) { // For setting the wheel rotation values
        RotateBL = bl;
        RotateBR = br;
        RotateFL = fl;
        RotateFR = fr;
    }

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    private void HorizontalSlideMovement() {
        hsMinExtensionR = 0.57;
        hsMaxExtensionR = 0.377;
        //hsMaxExtensionR = 0;
        //hsMinExtensionR = 1;
        // controls - horizontal slide
        boolean hsExtendBtn = gamepad2.left_bumper, hsRetractBtn = gamepad2.right_bumper;
        double hsStickY = gamepad2.right_stick_y;

        telemetry.addData("HLS Right Pos: ", hLinearSlideRight.getPosition());
        telemetry.addData("HLS Left Pos: ", hLinearSlideLeft.getPosition());
        // Gradual horizontal slide Movement
        if(Math.abs(hsStickY) > joystickDeadzone) {
            // moves the horizontal linear slide with joystick
            hLinearSlideRight.setPosition(Math.min(hsMinExtensionR, Math.max(hsMaxExtensionR, hLinearSlideRight.getPosition() + (hsStickY / 400)))); //used to be division by 800
            //hArmOpen.setPosition(Math.min(hsMinExtensionR, Math.max(hsMaxExtensionR, hArmOpen.getPosition() + (hsStickY / 400)))); //used to be division by 800

        } else {
            // make slide stay in place so it doesn't slide back and forth while driving
            hLinearSlideRight.setPosition(hLinearSlideRight.getPosition());
            //hArmOpen.setPosition(hArmOpen.getPosition());
        }

        // Snap horizontal slide to FULLY EXTENDED
        if (hsExtendBtn) {
            //hLinearSlideLeft.setPosition(hsMaxExtensionL);
            hLinearSlideRight.setPosition(hsMaxExtensionR);
        }

        // Snaps horizontal slide to FULLY RETRACTED
        if (hsRetractBtn) {
            //hLinearSlideLeft.setPosition(hsMinExtensionL);
            hLinearSlideRight.setPosition(hsMinExtensionR);
        }
        telemetry.addData("hArmPos", hArmOpen.getPosition());

    }

    private void setHLSL() {
        hLinearSlideLeft.setPosition((-0.95846 * hLinearSlideRight.getPosition()) + 0.68634);
    }
    private void HorizontalClawAndArm() {
        double hClawOpenValue = 0.377, hClawClosedValue = 0.75;
        hArmUpValue = 0.29;
        hArmDownValue = 0.985 ; // .835 and 0.135 before
        hArmMidValue = 0.8;
        // controls - horizontal claw and arm
        boolean hClawToggleBtn = gamepad2.b; // open/close claw
        boolean hArmToggleBtn = gamepad2.y; // swing horizontal arm out/in

        // HORIZONTAL CLAW OPEN ? CLOSE

        if(gamepad2.left_trigger > 0.5) {
            hClawServo.setPosition(0.625); // OPENS claw slighty
            hClawOpen = false;
        } else if(gamepad2.right_trigger > 0.5) {
            hClawServo.setPosition(hClawClosedValue); // OPENS claw
            hClawOpen = false;
        }
        else if (hClawToggleBtn && hClawTimer.milliseconds() >= 200) {
            hClawOpen = !hClawOpen; // toggle state of claw
            if (hClawOpen) {
                hClawServo.setPosition(hClawOpenValue); // OPENS claw
            } else if (!hClawOpen) {
                hClawServo.setPosition(hClawClosedValue); // CLOSES claw
            }
            hClawTimer.reset();
        }

        // HORIZONTAL ARM IN ? OUT
        telemetry.addData("H Arm Value", hArmUp);

        if (hArmToggleBtn && hArmTimer.milliseconds() >= 250) {
            hArmUp = !hArmUp; // toggle arm rotation

            if (hArmUp) {
                hArmOpen.setPosition(hArmDownValue);

            } else if (!hArmUp) {
                hArmOpen.setPosition(hArmUpValue);
            }
            hArmTimer.reset();
        }

        if(gamepad2.dpad_right){
            hArmOpen.setPosition(hArmMidValue);
            hArmUp = false;
        }
    }


    private void Sweeper() {
        boolean sweepBtn = gamepad2.dpad_left;

        //if left d-pad clicked
        if(sweepBtn && sweeperTimer.milliseconds() >= 150){
            sweep = true;
            sweeperTimer.reset();
            chamberStep = 0;
        }

        if(sweep){
            //sweeps out
            telemetry.addData("sweep timer", sweeperTimer.milliseconds());
            telemetry.addData("SWEEPPOS", sweeper.getPosition());
            if(chamberStep == 0) {
                sweeper.setPosition(0.875);
                hLinearSlideRight.setPosition(hsMinExtensionR - 0.1f);

                chamberStep = 1;
            } else if(chamberStep == 1 && sweeperTimer.milliseconds() >= 1000) {
                //sweeps in
                sweeper.setPosition(0);
                hArmOpen.setPosition(hArmDownValue);
                hClawServo.setPosition(0.377);
                chamberStep = 2;
            } else if(chamberStep == 2 && sweeperTimer.milliseconds() >= 1250) {
                sweep = false;
                sweeperTimer.reset();
                telemetry.addData("back", true);
                chamberStep = 0;
            }
        }
    }




    private void VerticalSlideMovement() {
        // controls - vertical slide
        double vsStickY = gamepad2.left_stick_y;

        if (Math.abs(vsStickY) > joystickDeadzone) { // controls the vertical slide
            SetVSlideSpeed(linearSlideSpeed * vsStickY / -1);
            telemetry.addData("linear slide speed:", linearSlideSpeed * -vsStickY / 1);
        } else {
            SetVSlideSpeed(0); // stop the linear slide from moving when joystick is centered
        }
    }

    private void SetVSlideSpeed(double speed) {
        vLinearSlideRight.setPower(speed);
        vLinearSlideLeft.setPower(speed);
    }


    private void VerticalArmAndOuttake() {
        double vArmOutValue = 0, vArmInValue = 0.775; // 0 , .875
        // controls - vertical arm
        boolean vArmToggleBtn = gamepad2.x;

        if (vArmToggleBtn && vArmTimer.milliseconds() >= 250) {
            vSlideArmOut = !vSlideArmOut;
            if (vSlideArmOut) {
                vArmServo.setPosition(vArmOutValue); //Arm swings out
                telemetry.addData("1", null);
            } else {
                vArmServo.setPosition(vArmInValue); //Arm swings in
                telemetry.addData("0", null);
            }
            vArmTimer.reset();
        }

    }

   /* private void LevelTwoHang() {

        //allows the player to move the hang motors
        if(gamepad2.dpad_right && hangTimer1.milliseconds() >= 200){
            hangMotorsOn = !hangMotorsOn;
            hangTimer1.reset();
        }

        if (hangMotorsOn){

            hangMotorLeft.setPower(1);
            hangMotorRight.setPower(1);

        } else {
            // sets hang motors to be off when hang mode is off
            hangMotorLeft.setPower(0);
            hangMotorRight.setPower(0);
        }

    }*/


    private void TransferFunction() {
        boolean transferBtn = gamepad2.a;


        if (transferBtn && transferTimer.milliseconds() >= 200) {
            transferStep = 0;
            enableTransfer = true;
            transferTimer.reset();
        }

        if (enableTransfer) {
            if(transferStep == 0) {
                hArmOpen.setPosition(hArmUpValue);
                hArmUp = true;
                transferTimer.reset();
                transferStep = 1;
            }else if (transferStep == 1 && transferTimer.milliseconds() >= 1000) {
                hLinearSlideRight.setPosition(hsMinExtensionR);
                transferTimer.reset();
                transferStep = 2;
            } else if(transferStep == 2 && transferTimer.milliseconds() >= 700) {
                hClawServo.setPosition(0.625);
                hClawOpen = true;
                transferTimer.reset();
                transferStep = 3;
            } else if(transferStep == 3 && transferTimer.milliseconds() >= 200) {
                if(hArmOpen.getPosition() <= 0.5) {
                    hLinearSlideRight.setPosition(hsMaxExtensionR);
                }
                transferTimer.reset();
                transferStep = 4;
            } else if(transferStep == 4 && transferTimer.milliseconds() >= 100) {
                transferStep = 0;
                transferTimer.reset();
                enableTransfer = false;

            }
        }
    }




//I know very well how this whole motor power setting is very long and inefficient but it used to be different and I don't feel like fixing it
    private void setMotorPowers() {
            
        if (Strafing || Rotating) {
            leftBack.setPower(((RotateBL + StrafeBL)) * rotationSpeed * 0.5);

            rightFront.setPower(((RotateFR + StrafeFR)) * rotationSpeed * 0.5);

            leftFront.setPower(((RotateFL + StrafeFL)) * rotationSpeed * 0.5);

            rightBack.setPower(((RotateBR + StrafeBR)) * rotationSpeed * 0.5);
            // hello :3
        } else {
            leftBack.setPower(0);

            leftFront.setPower(0);

            rightBack.setPower(0);

            rightFront.setPower(0);
        }
    }

}
