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
    final float speedSlow = 0.5f; // Slow mode for robot chassis movement
    final float speedFast = 1.5f; // Speedy mode for robot chassis movement
    float rotationSpeed = 1f; // Robot rotation speed multiplier, 1.5 for fast mode, 0.5 for slow mode

        // JOYSTICK and MOVEMENT CONTROLS //
    public float LjoystickX, LjoystickY, RjoystickX, RjoystickY;
    final float joystickDeadzone = 0.1f; // Area where joystick will not detect input
    boolean usingLStick; // Detect whether left stick is being used or not- for prioritizing rotation over directional movement

        // ROBOT OTHER STUFF //

    private final float clawSpeed = 1.0f;

        // vertical slide
    private DcMotor linearSlide; // motor to control vertical linear slide
    private Servo vClawServo, vArmServo;  // v is slang for vertical btw
    boolean vClawOpen = false; // is the claw open? False = closed, true = open
    boolean vSlideArmOut = false; // mounted onto the linear slide
    private final float linearSlideSpeed = 0.75f;

        // horizontal slide
    boolean hArmUp = false;
    private Servo hClawServo, hLinearSlide, hClawRotate; // h is slang for horizontal btw
    private  Servo hArmOpen;
    boolean hClawOpen = false;

    float bl = 0f, br = 0f, fl = 0f, fr = 0f;






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

        vClawServo = hardwareMap.get(Servo.class, "vcs"); //    CH3
        vArmServo = hardwareMap.get(Servo.class, "vas"); //     CH2
        hClawRotate = hardwareMap.get(Servo.class, "hcr"); //   EH4
        hClawServo = hardwareMap.get(Servo.class, "hcs"); //    EH5
        hArmOpen = hardwareMap.get(Servo.class, "hao"); //      EH3
        hLinearSlide = hardwareMap.get(Servo.class, "hls"); //  EH1

        hArmOpen.setDirection(Servo.Direction.REVERSE);



        linearSlide.setPower(0); // zero the linear slide's power so it doesn't move while not active

        telemetry.addData("Status", "Initialized");
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
            LimbMovement(); // controlling linear slide and claw on gary, OUR robot

            telemetry.addData("Status", "Run Time: " + Runtime.getRuntime()); // tracks how long program has been running
            telemetry.update(); //update output screen

            bl = 0f;
            br = 0f;
            fl = 0f;
            fr = 0f;
        }


    }

    //////////////////////// START OF MOVEMENT CODE ////////////////////////

    //////////////////////// START OF MOVEMENT CODE ////////////////////////
    
    //////////////////////// START OF MOVEMENT CODE ////////////////////////
    


    private void epicRotationMovement() {
        // rotates the robot if left stick is not being used (movement takes priorities)
        if ((Math.abs(RjoystickX) >= joystickDeadzone / 2) && !usingLStick) {
            if(RjoystickX < 0) {
                setMotorPowers(1, -1, 1, -1, -Math.abs(RjoystickX) * rotationSpeed); // clockwise rotation
                telemetry.addData("Right Stick rotating LEFT: ", RjoystickX);

            } else if (RjoystickX > 0) {
                setMotorPowers(-1, 1, -1, 1, -Math.abs(RjoystickX) * rotationSpeed); // counter-clockwise rotation
                telemetry.addData("Right Stick rotating RIGHT: ", RjoystickX);
                
            }
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
        float netS; // speed the motor actually uses

        // Alternate between SLOW && FAST mode depending on which bumper is held :P
        if (gamepad1.left_bumper) {    // slow mode !
            netS = speedSlow;    // Speed is set to a slow constant speed for more precise movements 
            rotationSpeed = speedSlow;
        } else if (gamepad1.right_bumper) {     // fast mode !
            netS = (Math.min(maxSpeed, (float) (addSpeed - joystickDeadzone) / (1.0f - joystickDeadzone))) * speedFast; // Speed is multiplied by the speedFast variable
            rotationSpeed = speedFast;

        } else {    // default- no bumpers are held !
            netS = Math.min(maxSpeed, (float) (addSpeed - joystickDeadzone) / (1.0f - joystickDeadzone)); // Speed is set to default speed
            rotationSpeed = 1f;
        }

        // calculates the angle of the joystick in radians --> degrees..
        double LangleInRadians = Math.atan2(-LjoystickY, LjoystickX);
        double LangleInDegrees = LangleInRadians * (180 / Math.PI);

        // strafe based on joystick angle :D
        if (Math.abs(LjoystickX) > joystickDeadzone || Math.abs(LjoystickY) > joystickDeadzone) {
            usingLStick = true;
            
            //if stick is past the deadzone ->
            if (LangleInDegrees >= -22.5 && LangleInDegrees <= 22.5) {
                // right quadrant
                setMotorPowers(-1, 1, 1, -1, netS);
                telemetry.addData("Left Stick quadrant: ", "RIGHT");

            } else if (LangleInDegrees > 22.5 && LangleInDegrees < 67.5) {
                // top-right quadrant
                setMotorPowers(0, 1, 1, 0, netS);
                telemetry.addData("Left Stick quadrant: ", "TOP RIGHT");

            } else if (LangleInDegrees > -67.5 && LangleInDegrees < -22.5) {
                // bottom-right quadrant
                setMotorPowers(-1, 0, 0, -1, netS);
                telemetry.addData("Left Stick quadrant: ", "BOTTOM RIGHT");

            } else if (LangleInDegrees >= 67.5 && LangleInDegrees <= 112.5) {
                // top quadrant
                setMotorPowers(1, 1, 1, 1, netS);
                telemetry.addData("Left Stick quadrant: ", "TOP");

            } else if (LangleInDegrees > -112.5 && LangleInDegrees < -67.5) {
                // bottom quadrant
                setMotorPowers(-1, -1, -1, -1, netS);
                telemetry.addData("Left Stick quadrant: ", "BOTTOM");

            } else if (LangleInDegrees > 112.5 && LangleInDegrees < 157.5) {
                // top-left quadrant
                setMotorPowers(1, 0, 0, 1, netS);
                telemetry.addData("Left Stick quadrant: ", "TOP LEFT");

            } else if (LangleInDegrees > -157.5 && LangleInDegrees < -112.5) {
                // bottom-left quadrant
                setMotorPowers(0, -1, -1, 0, netS);
                telemetry.addData("Left Stick quadrant: ", "BOTTOM LEFT");

            } else if (LangleInDegrees >= 157.5 || LangleInDegrees <= -157.5) {
                // left quadrant
                setMotorPowers(1, -1, -1, 1, netS);
                telemetry.addData("Left Stick quadrant: ", "LEFT");

            }

        } else {
            usingLStick = false;
            setMotorPowers(0, 0, 0, 0, 0); // zero all motor powers
        }





    }

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    //////////////////////// END OF MOVEMENT CODE ////////////////////////

    private void LimbMovement() {


        if(Math.abs(gamepad2.right_stick_y) > joystickDeadzone) {
            // moves the horizontal linear slide 
            hLinearSlide.setPosition(Math.min(1, Math.max(0, hLinearSlide.getPosition() + gamepad2.right_stick_y / 20)));
        } else {
            // keep the movement of the slide as is when not changing its position (to prevent it from swinging back and forth while robot drives)
            hLinearSlide.setPosition(hLinearSlide.getPosition());
        }

        //snaps horizontal linear slide to fully extended
        if(gamepad2.dpad_up){
            hLinearSlide.setPosition(0.8);
            sleep(100); // wait for teh motion to finish
        }
        //snaps horizontal linear slide to fully retracted
        if(gamepad2.dpad_down){
            hLinearSlide.setPosition(0);
            sleep(100); // wait for the motion to finish
        }

        //open/closes horizontal linear slide claw
        if(gamepad2.b){
            hClawOpen = !hClawOpen; // toggle claw state to open it
            if(hClawOpen){
                hClawServo.setPosition(1); //arm with claw swings out 
                telemetry.addData("1", hClawServo.getPosition());
            } else if(!hClawOpen) {
                hClawServo.setPosition(0); //arm with claw swings in 
                telemetry.addData("0", hClawServo.getPosition());
            }
            sleep(200);
        }




        if(gamepad2.x){
            vSlideArmOut = !vSlideArmOut; // toggle between arm positions
            if(vSlideArmOut){
                vArmServo.setPosition(0); //Arm swings out
                telemetry.addData("1", null);
            } else {
                vArmServo.setPosition(0.75); //Arm swings in
                telemetry.addData("0", null);
            }
            sleep(100);
        }

        //open or close chamber claw
        if (gamepad2.a) {
            if (vClawOpen) {
                vClawServo.setPosition(0); //open vertical claw
                telemetry.addData("Opening ", vClawServo.getPosition());
                vClawOpen = false; // switch claws open state
            } else {
                vClawServo.setPosition(1); //close vertical claw
                telemetry.addData("Closing  ", vClawServo.getPosition());
                vClawOpen = true; // switch claws open state
            }
            sleep(100); //creates cooldown for switching claw positions

        }

        //rotate chamber claw left
        if (gamepad2.y) {
            hArmUp = !hArmUp; // toggle arm rotation 

            if(hArmUp) { 
                hClawRotate.setPosition(0.3);
                sleep(500);
                hArmOpen.setPosition(0.75);
                sleep(1000);
                telemetry.addData(null,hClawRotate.getPosition());

            } else if(!hArmUp){
                hArmOpen.setPosition(0.25);
                sleep(500);
                hClawRotate.setPosition(0.9);
                sleep(1000);
                telemetry.addData(null,hClawRotate.getPosition());

            }
        }

        // moves linear slide
        if (Math.abs(gamepad2.left_stick_y) > joystickDeadzone) {
            // control vertical linear slide movement og direction 
            linearSlide.setPower(linearSlideSpeed * gamepad2.left_stick_y / -2);
            telemetry.addData("linear slide speed:", linearSlideSpeed * -gamepad2.left_stick_y /2);
        } else {
            linearSlide.setPower(0); // stop the linear slide from moving when joystick is centered 
        }

    }
    private void setMotorPowers(float BL, float BR, float FL, float FR, float speed) {

        bl += BL;
        br += BR;
        fl += FL;
        fr += FR;


        // set all the motor powers to the floats defined
        leftBack.setPower(bl * speed * 0.5);

        rightFront.setPower(fr * speed * 0.5);

        leftFront.setPower(fl * speed * 0.5);

        rightBack.setPower(br * speed * 0.5);

    }
}
