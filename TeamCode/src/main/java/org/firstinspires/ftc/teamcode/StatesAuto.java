package org.firstinspires.ftc.teamcode;

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
    static final double ticksPerRev = 1440;
    static final double wheelDiameter = 3.5;     // For figuring circumference (in inches)
    static final double ticksPerInch  = ticksPerRev / (wheelDiameter * Math.PI);

    protected DcMotor leftBack, rightBack, leftFront, rightFront; //Initializes direct current main wheel motors for the driving function of our robot, gary.
    protected DcMotor linearSlide;
    //private Servo hLinearSlide;
    protected Servo vArmServo, hArmOpen, hLinearSlide, hClawServo;


    protected int transferStep;
    protected boolean enableTransfer;
    protected ElapsedTime transferTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        //setting motors and servos
        leftBack    = hardwareMap.get(DcMotor.class, "bl");
        rightBack   = hardwareMap.get(DcMotor.class, "br");
        leftFront   = hardwareMap.get(DcMotor.class, "fl");
        rightFront  = hardwareMap.get(DcMotor.class, "fr");
        linearSlide = hardwareMap.get(DcMotor.class, "ls");

        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        linearSlide.setDirection(DcMotor.Direction.FORWARD);

        vArmServo = hardwareMap.get(Servo.class, "vas");

        hArmOpen = hardwareMap.get(Servo.class, "hao");
        hLinearSlide = hardwareMap.get(Servo.class, "hls");
        hClawServo = hardwareMap.get(Servo.class, "hcs");

        telemetry.addData("Starting pos: ", leftBack.getCurrentPosition());
        telemetry.update();
        waitForStart();

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
        //TODO: fix left & right values + add rotate
        switch(direction) {
            case LEFT:
                rbDir = -1.5;
                lfDir = -1.5;
                lbDir = 1.5;
                rfDir = 1.5;
                break;
            case RIGHT:
                lbDir = -1.5;
                rfDir = -1.5;
                lfDir = 1.5;
                rbDir = 1.5;
                break;
            case FORWARD:
                break;
            case BACKWARD:
                lbDir = -1;
                rbDir = -1;
                lfDir = -1;
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

            leftBack.setPower(speed);
            rightBack.setPower(speed);
            leftFront.setPower(speed);
            rightFront.setPower(speed);

            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while(opModeIsActive() && timeoutS < runtime.seconds() && (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy())) {
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




