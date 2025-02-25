package org.firstinspires.ftc.teamcode.AAAstates;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous //(name="Robot: Auto Drive By Encoder", group="Robot")
public class StatesAuto extends LinearOpMode {

    ///////////////////////////////code///////////////////////////////
    final private ElapsedTime runtime = new ElapsedTime();
    //constants for inch functions
    //static final double ticksPerRev = 384.5;
    //static final double wheelDiameter = 3.5;     // For figuring circumference (in inches)
    //static final double ticksPerInch  = ticksPerRev / (wheelDiameter * Math.PI);
    static final double ticksPerInch = 29;
    static final double rotConst = 0.2797;
    static final double blrotConst = 0.5625;

    protected final double hsOut = 0.377;
    protected final double hsIn = 0.5775;

    static final int maxSlideTicks = 2000;
    static final int minSlideTicks = 0;
    protected float hArmPos = 0.9f;

    protected DcMotorEx leftBack, rightBack, leftFront, rightFront; //Initializes direct current main wheel motors for the driving function of our robot, gary.
    protected DcMotor vLinearSlideLeft, vLinearSlideRight, hangMotorLeft, hangMotorRight;
    //private Servo hLinearSlideRight;
    protected Servo vArmServo, hArmOpen, hLinearSlideLeft, hLinearSlideRight, hClawServo;


    protected int transferStep;
    protected boolean enableTransfer;
    protected ElapsedTime transferTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        //setting motors and servos
        leftBack    = hardwareMap.get(DcMotorEx.class, "left_back");
        rightBack   = hardwareMap.get(DcMotorEx.class, "right_back");
        leftFront   = hardwareMap.get(DcMotorEx.class, "left_front");
        rightFront  = hardwareMap.get(DcMotorEx.class, "right_front");
        vLinearSlideLeft = hardwareMap.get(DcMotor.class, "vertical_slide_left"); //
        vLinearSlideRight = hardwareMap.get(DcMotor.class, "vertical_slide_right"); //  EH2
        hangMotorLeft = hardwareMap.get(DcMotor.class, "hang_motor_left"); // CH3
        hangMotorRight = hardwareMap.get(DcMotor.class, "hang_motor_right"); // EH3

        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        vLinearSlideLeft.setDirection(DcMotor.Direction.REVERSE);


        vArmServo = hardwareMap.get(Servo.class, "bucket_arm_woohoo");

        hArmOpen = hardwareMap.get(Servo.class, "horizontal_arm");
        hLinearSlideLeft = hardwareMap.get(Servo.class, "horizontal_slide_left");
        hLinearSlideRight = hardwareMap.get(Servo.class, "horizontal_slide_right");
        hClawServo = hardwareMap.get(Servo.class, "horizontal_claw");

        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //vLinearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //vLinearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //vLinearSlideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);





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
            telemetry.addData("current tick:", vLinearSlideRight.getCurrentPosition());
            telemetry.update();
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
        RIGHTROT,
        BLROT,
        BLROTNEG,
        BRROT

    }
    protected void driveInches(float inches, float speed, dir direction, float timeoutS) {
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
                lbDir = -rotConst;
                rbDir = rotConst;
                lfDir = -rotConst;
                rfDir = rotConst;
                break;
            case RIGHTROT:
                lbDir = rotConst;
                rbDir = -rotConst;
                lfDir = rotConst;
                rfDir = -rotConst;
                break;
            case BLROT:
                lbDir = 0;
                rbDir = blrotConst;
                lfDir = 0;
                rfDir = blrotConst;
                break;
            case BLROTNEG:
                lbDir = 0;
                rbDir = -blrotConst;
                lfDir = 0;
                rfDir = -blrotConst;
                break;
            case BRROT:
                lbDir = -blrotConst;
                rbDir = 0;
                lfDir = -blrotConst;
                rfDir = 0;
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
            if(direction == dir.BLROT || direction == dir.BLROTNEG || direction == dir.BRROT) {
                leftBack.setTargetPosition(100000000);
                leftFront.setTargetPosition(10000000);
            }

            //TODO: tweak tolerance

            /*leftBack.setTargetPositionTolerance(5);
            rightBack.setTargetPositionTolerance(5);
            leftFront.setTargetPositionTolerance(5);
            rightFront.setTargetPositionTolerance(5);*/


            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftBack.setPower(speed);
            rightBack.setPower(speed);
            leftFront.setPower(speed);
            rightFront.setPower(speed);


            while(opModeIsActive() && timeoutS > runtime.seconds() && (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy())) {

                hArmOpen.setPosition(hArmPos);
                telemetry.addData("hArmPos var:", hArmPos);
                telemetry.addData("harmPos:", hArmOpen.getPosition());
                /*telemetry.addData("lbPower:", leftBack.getPower());
                telemetry.addData("rbPower:", rightBack.getPower());
                telemetry.addData("lfPower:", leftFront.getPower());
                telemetry.addData("rfPower:", rightFront.getPower());
                telemetry.addData("lbTargetPos:", leftBack.getTargetPosition());
                telemetry.addData("rbTargetPos:", rightBack.getTargetPosition());
                telemetry.addData("lfTargetPos:", leftFront.getTargetPosition());
                telemetry.addData("rfTargetPos:", rightFront.getTargetPosition());
                telemetry.addData("lbPos:", leftBack.getCurrentPosition());
                telemetry.addData("rbPos:", rightBack.getCurrentPosition());
                telemetry.addData("lfPos:", leftFront.getCurrentPosition());
                telemetry.addData("rfPos:", rightFront.getCurrentPosition());*/
                telemetry.update();
            }
        }
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);

    }

    protected void easeInches(float inches, float startSpeed, float endSpeed, dir direction, float timeoutS) {
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double lbDir = 1;
        double rbDir = 1;
        double lfDir = 1;
        double rfDir = 1;

        final int lbStartPos = leftBack.getCurrentPosition();
        final int rbStartPos = rightBack.getCurrentPosition();
        final int lfStartPos = leftFront.getCurrentPosition();
        final int rfStartPos = rightFront.getCurrentPosition();

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
                lbDir = -rotConst;
                rbDir = rotConst;
                lfDir = -rotConst;
                rfDir = rotConst;
                break;
            case RIGHTROT:
                lbDir = rotConst;
                rbDir = -rotConst;
                lfDir = rotConst;
                rfDir = -rotConst;
                break;
            case BLROT:
                lbDir = 0;
                rbDir = blrotConst;
                lfDir = 0;
                rfDir = blrotConst;
                break;
            case BLROTNEG:
                lbDir = 0;
                rbDir = -blrotConst;
                lfDir = 0;
                rfDir = -blrotConst;
                break;
            case BRROT:
                lbDir = -blrotConst;
                rbDir = 0;
                lfDir = -blrotConst;
                rfDir = 0;
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

            if(direction == dir.BLROT || direction == dir.BLROTNEG || direction == dir.BRROT ) {
                leftBack.setTargetPosition(100);
                leftFront.setTargetPosition(100);
            }

            //TODO: tweek tolerance
            /*leftBack.setTargetPositionTolerance(0);
            rightBack.setTargetPositionTolerance(0);
            leftFront.setTargetPositionTolerance(0);
            rightFront.setTargetPositionTolerance(0);*/


            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //leftBack.setPower(lbCurrentSpeed);
            //rightBack.setPower(rbCurrentSpeed);
            //leftFront.setPower(lfCurrentSpeed);
            //rightFront.setPower(rfCurrentSpeed);

            while(opModeIsActive() && timeoutS > runtime.seconds() && (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy())) {

                lbPercent = (float)leftBack.getCurrentPosition()/ (lbTargetPos - lbStartPos);
                rbPercent = (float)rightBack.getCurrentPosition()/ (rbTargetPos - rbStartPos);
                lfPercent = (float)leftFront.getCurrentPosition()/ (lfTargetPos - lfStartPos);
                rfPercent = (float)rightFront.getCurrentPosition()/ (rfTargetPos - rfStartPos);
                // y is currentSpeed
                // x is percent 
                // m is start speed
                // M is end speed
                //linear speed: y=(M-m)x + m
                    /*lbCurrentSpeed = (endSpeed - startSpeed) * lbPercent + startSpeed;
                    rbCurrentSpeed = (endSpeed - startSpeed) * rbPercent + startSpeed;
                    lfCurrentSpeed = (endSpeed - startSpeed) * lfPercent + startSpeed;
                    rfCurrentSpeed = (endSpeed - startSpeed) * rfPercent + startSpeed;*/
                //parabola speed: y= -4(M-m)x^2 + 4(M-m)x + m


                        lbCurrentSpeed = (float) ((-4*(endSpeed - startSpeed) * Math.pow((lbPercent), 2)) + (4*(endSpeed - startSpeed) * (lbPercent)) + startSpeed);
                        rbCurrentSpeed = (float) ((-4*(endSpeed - startSpeed) * Math.pow((rbPercent), 2)) + (4*(endSpeed - startSpeed) * (rbPercent)) + startSpeed);
                        lfCurrentSpeed = (float) ((-4*(endSpeed - startSpeed) * Math.pow((lfPercent), 2)) + (4*(endSpeed - startSpeed) * (lfPercent)) + startSpeed);
                        rfCurrentSpeed = (float) ((-4*(endSpeed - startSpeed) * Math.pow((rfPercent), 2)) + (4*(endSpeed - startSpeed) * (rfPercent)) + startSpeed);

                if(direction == dir.BLROT || direction == dir.BLROTNEG || direction == dir.BRROT) {
                    lbCurrentSpeed = 0;
                    lfCurrentSpeed = 0;
                }


                    leftBack.setPower(lbCurrentSpeed);
                    rightBack.setPower(rbCurrentSpeed);
                    leftFront.setPower(lfCurrentSpeed);
                    rightFront.setPower(rfCurrentSpeed);

                hArmOpen.setPosition(hArmPos);
                telemetry.addData("hArmPos var:", hArmPos);
                telemetry.addData("harmPos:", hArmOpen.getPosition());
                /*
                telemetry.addData("lbPower:", leftBack.getPower());
                telemetry.addData("rbPower:", rightBack.getPower());
                telemetry.addData("lfPower:", leftFront.getPower());
                telemetry.addData("rfPower:", rightFront.getPower());
                telemetry.addData("lbTargetPos:", leftBack.getTargetPosition());
                telemetry.addData("rbTargetPos:", rightBack.getTargetPosition());
                telemetry.addData("lfTargetPos:", leftFront.getTargetPosition());
                telemetry.addData("rfTargetPos:", rightFront.getTargetPosition());
                telemetry.addData("lbPos:", leftBack.getCurrentPosition());
                telemetry.addData("rbPos:", rightBack.getCurrentPosition());
                telemetry.addData("lfPos:", leftFront.getCurrentPosition());
                telemetry.addData("rfPos:", rightFront.getCurrentPosition());
                */

                telemetry.update();
            }
        }

        if(direction == dir.BLROT || direction == dir.BLROTNEG) {
            leftBack.setPower(0);
            leftFront.setPower(0);
        }

        /*leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
*/
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
        hLinearSlideRight.setPosition(pos);
        hLinearSlideLeft.setPosition((-0.95846 * hLinearSlideRight.getPosition()) + 0.68634);

    }

    protected void SetVArmPos(String down){

        if(down == "down"){
            vArmServo.setPosition(0.82f);
        }

        if(down == "out"){
            vArmServo.setPosition(0);
        }
    }

    protected void SetHArmPos(String down){

            if(down == "down"){
                hArmPos = 0.015f;
                hArmOpen.setPosition(hArmPos);
            }

            if(down == "up"){
                hArmPos = 0.7175f;
                hArmOpen.setPosition(hArmPos);//0.175f

            }



    }

    protected void resetDriveEncoders() {
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

   /* protected void transferSample(Boolean withSwag) {

            //transfer spike sample 1
            SetHArmPos("up");
            SethSlidePos(hsIn);

            sleep(1100);
            hClawServo.setPosition(0.625);

            sleep(200);
            hClawServo.setPosition(0.75);
            SethSlidePos(hsOut);

            sleep(100);
    }*/

}




