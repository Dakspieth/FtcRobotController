package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PlanB", group="Robot")
public class PlanB extends ScrimmageAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();


        //1.6s ≈ 2ft at 0.25speed
        //ROTATION: 6.5S ≈ 360 °
        //basket specimen 1
        linearSlide.setPower(0.5f);
        driveSeconds(3, 0.25f, dir.BACKWARD);
        driveSeconds(1, 0.25f, dir.LEFTROT);
        moveArm(0);
        sleep(3000);

        //get spec 2
        moveArm(0.875);
        sleep(2000);
        driveSeconds(1, 0.25f, dir.FORWARD);
        driveSeconds(1, 0.25f, dir.LEFTROT);
        hLinearSlide.setPosition(0.475);
        linearSlide.setPower(-0.3f);
        hArmOpen.setPosition(0.13);
        sleep(1000);
        hClawServo.setPosition(0.375);
        sleep(1000);
        driveSeconds(0.5, 0.25f, dir.FORWARD);
        linearSlide.setPower(0);
        //hClawServo.setPosition(0.375);
        //sleep(1000);
        hClawServo.setPosition(0.75);
        sleep(1000);

        //transfer spec 2
            hArmOpen.setPosition(0.15);
            hLinearSlide.setPosition(0.605);
            sleep(1200);
            hClawServo.setPosition(0.375);
            sleep(500);
            hLinearSlide.setPosition(0.575);
            sleep(200);
            hClawServo.setPosition(0.75);
            sleep(100);

        //basket spec 2
        hLinearSlide.setPosition(0.475);
        sleep(100);
        driveSeconds(0.5, 0.25f, dir.BACKWARD);
        linearSlide.setPower(0.5f);
        driveSeconds(1, 0.25f, dir.RIGHTROT);
        driveSeconds(1, 0.25f, dir.BACKWARD);
        moveArm(0);
        sleep(3000);
        moveArm(0.875);
        driveSeconds(0.5, 0.25f, dir.FORWARD);
        linearSlide.setPower(0);
        
    }
}
