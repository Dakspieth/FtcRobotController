package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous //(name="Robot: Auto Drive By Encoder", group="Robot")
public class StatesAuto extends LinearOpMode {

    ///////////////////////////////code///////////////////////////////
    final private ElapsedTime runtime = new ElapsedTime();
    //constants for inch functions
    static final double ticksPerRev = 384.5;
    static final double wheelDiameter = 3.5;     // For figuring circumference (in inches)
    static final double ticksPerInch  = ticksPerRev / (wheelDiameter * Math.PI);

    static final int maxSlideTicks = 1538;
    static final int minSlideTicks = 0;

    protected DcMotor leftBack, rightBack, leftFront, rightFront; //Initializes direct current main wheel motors for the driving function of our robot, gary.
    protected DcMotor vLinearSlideLeft, vLinearSlideRight, hangMotorLeft, hangMotorRight;
    //private Servo hLinearSlide;
    protected Servo vArmServo, hArmOpen, hLinearSlide, hClawServo;


    protected int transferStep;
    protected boolean enableTransfer;
    protected ElapsedTime transferTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        //setting motors and servos
        leftBack    = hardwareMap.get(DcMotor.class, "left_back");
        rightBack   = hardwareMap.get(DcMotor.class, "right_back");
        leftFront   = hardwareMap.get(DcMotor.class, "left_front");
        rightFront  = hardwareMap.get(DcMotor.class, "right_front");
        vLinearSlideLeft = hardwareMap.get(DcMotor.class, "vertical_slide_left"); //
        vLinearSlideRight = hardwareMap.get(DcMotor.class, "vertical_slide_right"); //  EH2
        hangMotorLeft = hardwareMap.get(DcMotor.class, "hang_motor_left"); // CH3
        hangMotorRight = hardwareMap.get(DcMotor.class, "hang_motor_right"); // EH3

        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        vLinearSlideLeft.setDirection(DcMotor.Direction.REVERSE);


        vArmServo = hardwareMap.get(Servo.class, "bucket_arm_woohoo");

        hArmOpen = hardwareMap.get(Servo.class, "horizontal_arm");
        hLinearSlide = hardwareMap.get(Servo.class, "horizontal_slide");
        hClawServo = hardwareMap.get(Servo.class, "horizontal_claw");

        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        vLinearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        vLinearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);



        telemetry.addData("Starting pos: ", leftBack.getCurrentPosition());
        telemetry.update();
        waitForStart();

    }

    protected void vSlidePos(float percentage, float speed) {
        int targetPos = (int)(minSlideTicks + (percentage * (maxSlideTicks - minSlideTicks)));
        vLinearSlideRight.setTargetPosition(targetPos);
        vLinearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        vLinearSlideLeft.setPower(speed);
        vLinearSlideRight.setPower(speed);
        if(vLinearSlideRight.getCurrentPosition() > vLinearSlideRight.getTargetPosition()) {
            vLinearSlideLeft.setPower(vLinearSlideLeft.getPower() * -1);
        }
        while (vLinearSlideRight.isBusy()) {
            telemetry.addData("going to perecent", "" + percentage, "ad", speed);
        }

        vLinearSlideLeft.setPower(0);
        vLinearSlideRight.setPower(0);




    }
    protected enum dir { // dir is short for direction btw
        LEFT,
        RIGHT,
        FORWARD,
        BACKWARD,
        LEFTROT,
        RIGHTROT

    }
    protected void driveInches(float inches, float speed, dir direction, float timeoutS) {
        double lbDir = 1;
        double rbDir = 1;
        double lfDir = 1;
        double rfDir = 1;

        int lbTargetPos = 0;
        int rbTargetPos = 0;
        int lfTargetPos = 0;
        int rfTargetPos = 0;

        //sets some motors to negative power depending on direction
        //^^^ pretty sure we dont need negs for encoder
        //TODO: fix left, right, & rotate values
        switch(direction) {
            case LEFT:
                lbDir = 1;
                rbDir = -1;
                lfDir = -1;
                rfDir = 1;
                break;
            case RIGHT:
                lbDir = -1;
                rbDir = 1;
                lfDir = 1;
                rfDir = -1;
                break;
            case FORWARD:
                lbDir = 1;
                rbDir = 1;
                lfDir = 1;
                rfDir = 1;
                break;
            case BACKWARD:
                lbDir = -1;
                rbDir = -1;
                lfDir = -1;
                rfDir = -1;
                break;
            case LEFTROT:
                lbDir = -1;
                rbDir = 1;
                lfDir = -1;
                rfDir = 1;
                break;
            case RIGHTROT:
                lbDir = 1;
                rbDir = -1;
                lfDir = 1;
                rfDir = -1;
                break;
        }
        if(opModeIsActive()) {
            runtime.reset();
            lbTargetPos = (int)(inches * ticksPerInch * lbDir);
            rbTargetPos = (int)(inches * ticksPerInch * rbDir);
            lfTargetPos = (int)(inches * ticksPerInch * lfDir);
            rfTargetPos = (int)(inches * ticksPerInch * rfDir);

            //comment motors here depending on if they have encoders
            leftBack.setTargetPosition(lbTargetPos + leftBack.getCurrentPosition());
            rightBack.setTargetPosition(rbTargetPos + rightBack.getCurrentPosition());
            leftFront.setTargetPosition(lfTargetPos + leftFront.getCurrentPosition());
            rightFront.setTargetPosition(rfTargetPos + rightFront.getCurrentPosition());

            //TODO: tweak tolerance
            //leftBack.setTargetPositionTolerance(3);
            //rightBack.setTargetPositionTolerance(3);
            //leftFront.setTargetPositionTolerance(3);
            //rightFront.setTargetPositionTolerance(3);

            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            leftBack.setPower(speed);
            rightBack.setPower(speed);
            leftFront.setPower(speed);
            rightFront.setPower(speed);

            while(/*opModeIsActive() && timeoutS < runtime.seconds() && */ (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy())) {
                telemetry.addData("currently going", String.valueOf(direction));
                telemetry.update();
            }
        }
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);

    }

    protected void easeInches(float inches, float startSpeed, float endSpeed, dir direction, float timeoutS) {
        double lbDir = 1;
        double rbDir = 1;
        double lfDir = 1;
        double rfDir = 1;

        int lbStartPos = leftBack.getCurrentPosition();
        int rbStartPos = rightBack.getCurrentPosition();
        int lfStartPos = leftFront.getCurrentPosition();
        int rfStartPos = rightFront.getCurrentPosition();

        int lbTargetPos = 0;
        int rbTargetPos = 0;
        int lfTargetPos = 0;
        int rfTargetPos = 0;
        float lbCurrentSpeed = startSpeed;
        float rbCurrentSpeed = startSpeed;
        float lfCurrentSpeed = startSpeed;
        float rfCurrentSpeed = startSpeed;
        float lbPercent = 0;
        float rbPercent = 0;
        float lfPercent = 0;
        float rfPercent = 0;



        //sets some motors to negative power depending on direction
        //^^^ pretty sure we dont need negs for encoder
        //TODO: fix left, right, & rotate values
        switch(direction) {
            case LEFT:
                rbDir = -1;
                lfDir = -1;
                lbDir = 1;
                rfDir = 1;
                break;
            case RIGHT:
                lbDir = -1;
                rfDir = -1;
                lfDir = 1;
                rbDir = 1;
                break;
            case FORWARD:
                lbDir = 1;
                rbDir = 1;
                lfDir = 1;
                rfDir = 1;
                break;
            case BACKWARD:
                lbDir = -1;
                rbDir = -1;
                lfDir = -1;
                rfDir = -1;
                break;
            case LEFTROT:
                lbDir = -1;
                rbDir = 1;
                lfDir = -1;
                rfDir = 1;
                break;
            case RIGHTROT:
                lbDir = 1;
                rbDir = -1;
                lfDir = 1;
                rfDir = -1;
                break;
        }

        if(opModeIsActive()) {
            runtime.reset();

            lbTargetPos = (int)(inches * ticksPerInch * lbDir);
            rbTargetPos = (int)(inches * ticksPerInch * rbDir);
            lfTargetPos = (int)(inches * ticksPerInch * lfDir);
            rfTargetPos = (int)(inches * ticksPerInch * rfDir);

            //comment motors here depending on if they have encoders
            leftBack.setTargetPosition(lbTargetPos + leftBack.getCurrentPosition());
            rightBack.setTargetPosition(rbTargetPos + rightBack.getCurrentPosition());
            leftFront.setTargetPosition(lfTargetPos + leftFront.getCurrentPosition());
            rightFront.setTargetPosition(rfTargetPos + rightFront.getCurrentPosition());

            //TODO: tweek tolerance
            //leftBack.setTargetPositionTolerance(3);
            //rightBack.setTargetPositionTolerance(3);
            //leftFront.setTargetPositionTolerance(3);
            //rightFront.setTargetPositionTolerance(3);

            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftBack.setPower(lbCurrentSpeed);
            rightBack.setPower(rbCurrentSpeed);
            leftFront.setPower(lfCurrentSpeed);
            rightFront.setPower(rfCurrentSpeed);

            while(opModeIsActive() && timeoutS < runtime.seconds() && (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy())) {

                lbPercent = leftBack.getCurrentPosition()/ Math.abs(lbTargetPos - lbStartPos);
                rbPercent = rightBack.getCurrentPosition()/ Math.abs(rbTargetPos - rbStartPos);
                lfPercent = leftFront.getCurrentPosition()/ Math.abs(lfTargetPos - lfStartPos);
                rfPercent = rightFront.getCurrentPosition()/ Math.abs(rfTargetPos - rfStartPos);
                lbCurrentSpeed = startSpeed + (lbPercent * Math.abs(endSpeed - startSpeed));
                rbCurrentSpeed = startSpeed + (rbPercent * Math.abs(endSpeed - startSpeed));
                lfCurrentSpeed = startSpeed + (lfPercent * Math.abs(endSpeed - startSpeed));
                rfCurrentSpeed = startSpeed + (rfPercent * Math.abs(endSpeed - startSpeed));

                leftBack.setPower(lbCurrentSpeed);
                rightBack.setPower(rbCurrentSpeed);
                leftFront.setPower(lfCurrentSpeed);
                rightFront.setPower(rfCurrentSpeed);
                telemetry.addData("currently going", String.valueOf(direction));
                telemetry.update();
            }
        }
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);

    }


    protected void moveClaw(boolean openClaw) {
        if(openClaw) {
            hClawServo.setPosition(0.377);
        } else {
            hClawServo.setPosition(0.75);
        }
    }
    protected void SetVSlideSpeed(double speed) {
        vLinearSlideRight.setPower(speed);
        vLinearSlideLeft.setPower(speed);
    }

    protected void SethSlidePos(double pos) {
        vLinearSlideRight.setPower(pos);
        vLinearSlideLeft.setPower(pos);
    }

    protected void transferSample() {
        transferStep = 0;
        enableTransfer = true;
        transferTimer.reset();

        if(enableTransfer) {
            if(transferStep == 0) {
                hArmOpen.setPosition(0.15);
                hLinearSlide.setPosition(0.605);
                transferTimer.reset();
                transferStep = 1;
            } else if(transferStep == 1 && transferTimer.milliseconds() >= 1200) {
                hClawServo.setPosition(0.375);
                transferTimer.reset();
                transferStep = 2;
            } else if(transferStep == 2 && transferTimer.milliseconds() >= 500) {
                hLinearSlide.setPosition(0.575);
                transferTimer.reset();
                transferStep = 3;
            } else if(transferStep == 3 && transferTimer.milliseconds() >= 200) {
                hClawServo.setPosition(0.75);
                transferTimer.reset();
                transferStep = 4;
            } else if(transferStep == 4 && transferTimer.milliseconds() >= 100) {
                transferStep = 0;
                transferTimer.reset();
                enableTransfer = false;
            }
        }
    }

}




