/*package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous //(name="Robot: Auto Drive By Encoder", group="Robot")
public class MainEncoderAutoRedo extends LinearOpMode {
    ///////////////////////////////pseudocode///////////////////////////////
    //(robot is 17 inches long)
    //move right 24 inches
    //extend linear slide up to second ladder
    //move forward 7 inches(24-17)
    //move slide down slightly (clip specimen on second ladder)
    //release claw
    //move left a few inches (for other team's auto

    ///////////////////////////////code///////////////////////////////
    private ElapsedTime runtime = new ElapsedTime();
    //constants for inch functions
    static final double ticksPerRev = 1440;
    static final double wheelDiameter = 3.5;     // For figuring circumference (in inches)
    static final double ticksPerInch  = ticksPerRev / (wheelDiameter * Math.PI);
    static final double slideTicksPerInch = 1;
    //Motor and servo declaration
    private DcMotor leftBack, rightBack, leftFront, rightFront; //Initializes direct current main wheel motors for the driving function of our robot, gary.
    private DcMotor linearSlide;
    //private Servo hLinearSlide;
    private Servo clawServo;

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

        clawServo = hardwareMap.get(Servo.class, "cs");
        telemetry.addData("Starting pos: ", leftBack.getCurrentPosition());
        telemetry.update();
        waitForStart();

    }

    protected enum dir { // dir is short for direction btw
        LEFT,
        RIGHT,
        FORWARD,
        BACKWARD

    }

    protected void driveInches(float inches, float speed, dir direction, float timeoutS) {
        double lbDir = 1;
        double rbDir = 1;
        double lfDir = 1;
        double rfDir = 1;
        double targetPos = 0;

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
            lbTargetPos = (int)(inches * countsPerInch * lbDir);
            rbTargetPos = (int)(inches * countsPerInch * rbDir);
            lfTargetPos = (int)(inches * countsPerInch * lfDir);
            rfTargetPos = (int)(inches * countsPerInch * rfDir);

            //comment motors here depending on if they have encoders
            leftBack.setTargetPosition(lbTargetPos + leftBack.getCurrentPosition());
            rightBack.setTargetPosition(rbTargetPos + rightBack.getCurrentPosition());
            leftFront.setTargetPosition(lfTargetPos + leftFront.getCurrentPosition());
            rightFront.setTargetPosition(rfTargetPos + rightFront.getCurrentPosition());
            
            //TODO: tweek tolerance
            leftBack.setTargetPositionTolerance(3);
            rightBack.setTargetPositionTolerance(3);
            leftFront.setTargetPositionTolerance(3);
            rightFront.setTargetPositionTolerance(3);

            leftBack.setPower(speed);
            rightBack.setPower(speed);
            leftFront.setPower(speed);
            rightFront.setPower(speed);

            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            while(opModeIsActive() && timeoutS < runtime.seconds() && (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy())) {
                telemetry.addData("currently going", String.valueOf(direction), " to ", targetPos);
                telemetry.update();
            }
        }
            leftBack.setPower(0);
            rightBack.setPower(0);
            leftFront.setPower(0);
            rightFront.setPower(0);

        }

        sleep(500);

    }

}*/





/* delete?
 * 
    protected void driveSeconds(double seconds, float speed, dir direction) {
        double lbDir = 1;
        double rbDir = 1;
        double lfDir = 1;
        double rfDir = 1;
        double currentTime = runtime.seconds();
        double endTime = runtime.seconds() + seconds;


        //sets some motors to negative power depending on direction
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
            leftBack.setPower(lbDir * speed);
            rightBack.setPower(rbDir * speed);
            leftFront.setPower(lfDir * speed);
            rightFront.setPower(rfDir * speed);
            targetPos = inches * ticksPerInch;

            while(currentTime < endTime) {
                telemetry.addData("currently going", String.valueOf(direction), " for ", ticks);
                telemetry.update();
                endTime = runtime.seconds();
            }
            leftBack.setPower(0);
            rightBack.setPower(0);
            leftFront.setPower(0);
            rightFront.setPower(0);

        }

        sleep(500);

    }


    protected void moveClaw(boolean clawOpen) {
        if(clawOpen) {
            clawServo.setPosition(1);
        } else {
            clawServo.setPosition(0);
        }
    }

    //not complete
    /*protected void moveSlide(float inches, float speed, boolean up, float timeoutS) {
        int dir = 1;
        if (!up) {
            dir = -1;
        }
        if (opModeIsActive()) {
            runtime.reset();
            int targetPos = linearSlide.getCurrentPosition() + (int) (inches * slideTicksPerInch);
            linearSlide.setPower(dir * speed);
            while (opModeIsActive() && timeoutS < runtime.seconds() && linearSlide.isBusy()) {
                telemetry.addData("linear slide currently going", up ? "up" : "down", " to ", targetPos);
                telemetry.update();
            }
            linearSlide.setPower(0);
        }
    }

    protected void moveSlideSeconds(double seconds, float speed, boolean up) {
        int dir = 1;
        if (!up) {
            dir = -1;
        }
        double currentTime = runtime.seconds();
        double endTime = runtime.seconds() + seconds;
        if (opModeIsActive()) {
            linearSlide.setPower(dir * speed);
            while(int i = 0; i < ticks; i++) {
                telemetry.addData("linear slide currently going", up ? "up" : "down", " to ", targetPos);
                telemetry.update();
            }
            linearSlide.setPower(0);
        }
    }

 */
