package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="MainMovement", group="Linear OpMode")
public class MainMovement extends LinearOpMode {

        // ROBOT MOVEMENT //
    private DcMotor leftBack, rightBack, leftFront, rightFront; //Initializes all the direct current motors for the driving function of our robot, gary.
    float speedSlow = 0.45f, speedFast = 1f; // slow and fast mode for movement
    float netS; // speed the motor actually uses
    float StrafeBL = 0f, StrafeBR = 0f, StrafeFL = 0f, StrafeFR = 0f;
    float RotateBL = 0f, RotateBR = 0f, RotateFL = 0f, RotateFR = 0f;
    boolean Strafing, Rotating;

    float rotationSpeed = 1f; // Robot rotation speed multiplier, 1.5 for fast mode, 0.5 for slow mode

        // JOYSTICK and MOVEMENT CONTROLS //
    public float LjoystickX, LjoystickY, RjoystickX, RjoystickY;
    final float joystickDeadzone = 0.1f; // Area where joystick will not detect input

        // ROBOT OTHER STUFF //

    //private final float clawSpeed = 1.0f; unused idk whats up w/ this

        // vertical slide
    private DcMotor linearSlide; // motor to control vertical linear slide
    private Servo vClawServo, vArmServo;  // v is slang for vertical btw
    boolean vClawOpen = false; // is the claw open? False = closed, true = open
    boolean vSlideArmOut = false; // mounted onto the linear slide
    private final float linearSlideSpeed = 0.75f;

        // horizontal slide
    boolean hArmUp = false;
    private Servo hClawServo, hLinearSlide, hClawRotate; // h is short for horizontal btw
    private Servo hArmOpen;
    boolean hClawOpen = false;








    @Override
    public void runOpMode() {
        // initializing the motors (pseudocode) (:skull:, :fire:, :splash:, :articulated-lorry:, :flushed:, :weary:, :sob:);
        leftBack  = hardwareMap.get(DcMotor.class, "bl"); //    CH0
        rightBack  = hardwareMap.get(DcMotor.class, "br"); //   EH0
        leftFront  = hardwareMap.get(DcMotor.class, "fl"); //   CH1
        rightFront  = hardwareMap.get(DcMotor.class, "fr"); //  EH1
        linearSlide = hardwareMap.get(DcMotor.class, "ls"); //  EH2

        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);

        //vClawServo = hardwareMap.get(Servo.class, "vcs"); //    CH3
        vArmServo = hardwareMap.get(Servo.class, "vas"); //     CH2
        hClawRotate = hardwareMap.get(Servo.class, "hcr"); //   EH4
        hClawServo = hardwareMap.get(Servo.class, "hcs"); //    EH5
        hArmOpen = hardwareMap.get(Servo.class, "hao"); //      EH3
        hLinearSlide = hardwareMap.get(Servo.class, "hls"); //  EH1

        hArmOpen.setDirection(Servo.Direction.REVERSE);

        hArmOpen.setPosition(0.8525);



        linearSlide.setPower(0); // zero the linear slide's power so it doesn't move while not active

        telemetry.addData("Status", "Initialized OwO");
        telemetry.update();

        waitForStart(); //waits for play on the driver hub :3

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


            setMotorPowers();
            telemetry.update(); //update output screen
        }


    }

    //////////////////////// START OF MOVEMENT CODE ////////////////////////

    //////////////////////// START OF MOVEMENT CODE ////////////////////////
    
    //////////////////////// START OF MOVEMENT CODE ////////////////////////

    private void epicRotationMovement() {
        // rotates the robot if left stick is not being used (movement takes priorities)
        if (Math.abs(RjoystickX) >= joystickDeadzone / 2) {
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
            netS = speedSlow;    // Speed is set to a slow constant speed for more precise movements 
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
    private void wheelStrafe(float bl, float br, float fl, float fr) { // For setting the wheel strafe values
        StrafeBL = bl;
        StrafeBR = br;
        StrafeFL = fl;
        StrafeFR = fr;
    }
    private void wheelRotate(float bl, float br, float fl, float fr) { // For setting the wheel rotation values
        RotateBL = bl;
        RotateBR = br;
        RotateFL = fl;
        RotateFR = fr;
    }

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    private void HorizontalSlideMovement() {
        double hsMinExtension = 0.9, hsMaxExtension = 0.475;
        // controls - horizontal slide
        boolean hsExtendBtn = gamepad2.dpad_up, hsRetractBtn = gamepad2.dpad_down;
        double hsStickY = gamepad2.right_stick_y;

        print("HLS Pos: ", hLinearSlide.getPosition());

        // Gradual horizontal slide Movement
        if(Math.abs(hsStickY) > joystickDeadzone) {
            // moves the horizontal linear slide with joystick
            hLinearSlide.setPosition(Math.min(hsMinExtension, Math.max(hsMaxExtension, hLinearSlide.getPosition() + (hsStickY / 800))));
        } else {
            // make slide stay in place so it doesn't slide back and fourth while driving
            //hLinearSlide.setPosition(hLinearSlide.getPosition());
        }

        // Snap horizontal slide to FULLY EXTENDED
        if (hsExtendBtn) {
            hLinearSlide.setPosition(hsMaxExtension);
            sleep(200); // waits for motion to complete
        }

        // Snaps horizontal slide to FULLY RETRACTED
        if (hsRetractBtn) {
            hLinearSlide.setPosition(hsMinExtension);
            sleep(200); // waits for motion to complete
        }

    }


    private void HorizontalClawAndArm() {
        double hClawOpenValue = 0.375, hClawClosedValue = 0.75;
        double hArmDownValue = 0.8525, hArmUpValue = 0.15; // .95 and 0.25 before
        // controls - horizontal claw and arm
        boolean hClawToggleBtn = gamepad2.b; // open/close claw
        boolean hArmToggleBtn = gamepad2.y; // swing horizontal arm out/in

        // HORIZONTAL CLAW OPEN ? CLOSE

        if (hClawToggleBtn) {
            hClawOpen = !hClawOpen; // toggle state of claw
            sleep(100);
            if (hClawOpen) {
                hClawServo.setPosition(hClawOpenValue); // OPENS claw
            } else if (!hClawOpen) {
                hClawServo.setPosition(hClawClosedValue); // CLOSES claw
            }
            sleep(200);
        }

        // HORIZONTAL ARM IN ? OUT

        if (hArmToggleBtn) {
            hArmUp = !hArmUp; // toggle arm rotation
                sleep(200);

            if (hArmUp) {
                hArmOpen.setPosition(hArmUpValue);
                sleep(400);

            } else if (!hArmUp) {
                hArmOpen.setPosition(hArmDownValue);
                sleep(400);

            }
        }
    }


    private void VerticalSlideMovement() {
        // controls - vertical slide
        double vsStickY = gamepad2.left_stick_y;

        if (Math.abs(vsStickY) > joystickDeadzone) { // controls the vertical slide
            linearSlide.setPower(linearSlideSpeed * vsStickY / -1.25);
            telemetry.addData("linear slide speed:", linearSlideSpeed * -vsStickY / 1.25);
        } else {
            linearSlide.setPower(0); // stop the linear slide from moving when joystick is centered
        }
    }


    private void VerticalArmAndOuttake() {
        double vArmOutValue = 0, vArmInValue = 0.875; // 0 , .875
        // controls - vertical arm
        boolean vArmToggleBtn = gamepad2.x;

        if (vArmToggleBtn) {
            vSlideArmOut = !vSlideArmOut;
            sleep(200);
            if (vSlideArmOut) {
                vArmServo.setPosition(vArmOutValue); //Arm swings out
                telemetry.addData("1", null);
            } else {
                vArmServo.setPosition(vArmInValue); //Arm swings in
                telemetry.addData("0", null);
            }
            sleep(200);
        }

    }

    private void TransferFunction() {
        boolean transferBtn = gamepad2.a;
        boolean enableTransfer = false;

        if (transferBtn) {
            enableTransfer = !enableTransfer;
            sleep(300);
            if (enableTransfer) {
                hArmOpen.setPosition(0.15);
                hLinearSlide.setPosition(0.605);
                print("linear position", hLinearSlide.getPosition());
                sleep(1200);
                hClawServo.setPosition(0.375); // OPENS claw
                sleep(500);
                hLinearSlide.setPosition(0.575);
                sleep(200);
                hClawServo.setPosition(0.75);
                sleep(100);
            }
        }
    }




    private void setMotorPowers() {
            
        if (Strafing && Rotating) {
            leftBack.setPower(((RotateBL + StrafeBL) / 2) * rotationSpeed * 0.5);

            rightFront.setPower(((RotateFR + StrafeFR) / 2) * rotationSpeed * 0.5);

            leftFront.setPower(((RotateFL + StrafeFL) / 2) * rotationSpeed * 0.5);

            rightBack.setPower(((RotateBR + StrafeBR) / 2) * rotationSpeed * 0.5);

        } else if (Strafing && !Rotating) {
            leftBack.setPower(StrafeBL * 0.5);

            rightFront.setPower(StrafeFR * 0.5);

            leftFront.setPower(StrafeFL * 0.5);

            rightBack.setPower(StrafeBR * 0.5);
        } else if (!Strafing && Rotating) {
            leftBack.setPower(RotateBL * rotationSpeed * 0.5);

            rightFront.setPower(RotateFR * rotationSpeed * 0.5);

            leftFront.setPower(RotateFL * rotationSpeed * 0.5);

            rightBack.setPower(RotateBR * rotationSpeed * 0.5);
        } else {
            leftBack.setPower(0);

            leftFront.setPower(0);

            rightBack.setPower(0);

            rightFront.setPower(0);
        }
    }

    public void print(String str, double num){
        telemetry.addData(str, num);
    }
}
