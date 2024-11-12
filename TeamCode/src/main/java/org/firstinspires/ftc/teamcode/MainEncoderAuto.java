package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous //(name="Robot: Auto Drive By Encoder", group="Robot")
public class MainEncoderAuto extends OpMode {
    ///////////////////////////////pseudocode///////////////////////////////
    //(robot is 17 inches long)
    //move right 24 inches
    //extend linear slide up to second ladder
    //move forward 7 inches(24-17)
    //move slide down slightly (clip specimen on second ladder)
    //release claw
    //move left a few inches (for other team's auto

    ///////////////////////////////code///////////////////////////////
    private ElapsedTime     runtime = new ElapsedTime();
    static final double countsPerRev = 1440;
    static final double wheelDiameter = 3.5;     // For figuring circumference (in inches)
    static final double countsPerInch  = countsPerRev / (wheelDiameter * Math.PI);
    static final double slideCountsPerInch = 1440;
    private DcMotor leftBack, rightBack, leftFront, rightFront; //Initializes direct current main wheel motors for the driving function of our robot, gary.
    private DcMotor linearSlide;
    private Servo clawServo;

    @Override
    public void runOpMode() {
        leftBack  = hardwareMap.get(DcMotor.class, "bl");
        rightBack  = hardwareMap.get(DcMotor.class, "br");
        leftFront  = hardwareMap.get(DcMotor.class, "fl");
        rightFront  = hardwareMap.get(DcMotor.class, "fr");
        linearSlide = hardwareMap.get(DcMotor.class, "linearSlide");

        clawServo = hardwareMap.get(Servo.class, "clawServo");
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
        int lbDir = 1;
        int rbDir = 1;
        int lfDir = 1;
        int rfDir = 1;
        int targetPos;

        //sets some motors to negative power depending on direction
        switch(direction) {
            case LEFT:
                rbDir = -1;
                lfDir = -1;
                break;
            case RIGHT:
                lbDir = -1;
                rfDir = -1;
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
            targetPos = (int)(inches * countsPerInch);
            leftBack.setTargetPosition(targetPos + leftBack.getCurrentPosition());
            rightBack.setTargetPosition(targetPos + rightBack.getCurrentPosition());
            leftFront.setTargetPosition(targetPos + leftFront.getCurrentPosition());
            rightFront.setTargetPosition(targetPos + rightFront.getCurrentPosition());

            leftBack.setPower(lbDir * speed);
            rightBack.setPower(rbDir * speed);
            leftFront.setPower(lfDir * speed);
            rightFront.setPower(rfDir * speed);
            while(opModeIsActive() && timeoutS < runtime.seconds() && (leftBack.isBusy() && rightBack.isBusy() && leftFront.isBusy() && rightFront.isBusy())) {
                telemetry.addData("currently going", String.valueOf(direction), " to ", targetPos);
                telemetry.update();
            }
            leftBack.setPower(0);
            rightBack.setPower(0);
            leftFront.setPower(0);
            rightFront.setPower(0);
            sleep(100);
        }
    }

    protected void moveClaw(boolean open) {
        if(open) {
            clawServo.setPosition(1);
        } else {
            clawServo.setPosition(0);
        }
    }

    protected void moveSlide(float inches, float speed, boolean up, float timeoutS) {
        int dir = 1;
        if (!up) {
            dir = -1;
        }
        if (opModeIsActive()) {
            runtime.reset();
            int targetPos = linearSlide.getCurrentPosition() + (int) (inches * slideCountsPerInch);
            linearSlide.setPower(dir * speed);
            while (opModeIsActive() && timeoutS < runtime.seconds() && linearSlide.isBusy()) {
                telemetry.addData("linear slide currently going", up ? "up" : "down", " to ", targetPos);
                telemetry.update();
            }
        }
    }

}
