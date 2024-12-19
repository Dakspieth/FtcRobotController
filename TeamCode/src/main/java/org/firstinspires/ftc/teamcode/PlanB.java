package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.text.Format;

@Autonomous(name="PlanB", group="Robot")
public class PlanB extends ScrimmageAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();


        //1.6s ≈ 2ft at 0.25speed
        //ROTATION: 6.5S ≈ 360 °
        //basket specimen 1
        hClawServo.setPosition(0.75);
        driveSeconds(0.05f, 0.25f, dir.LEFT);
        linearSlide.setPower(0.5f);
        driveSeconds(3.75f, 0.25f, dir.BACKWARD);
        driveSeconds(1, 0.25f, dir.LEFTROT);
        driveSeconds(0.25f, 0.25f, dir.BACKWARD);
        hLinearSlide.setPosition(0.475);
        hArmOpen.setPosition(0.82);
        sleep(2000);
        vArmServo.setPosition(0);
        sleep(2000);
        //get spec 2
        hClawServo.setPosition(0.377);
        vArmServo.setPosition(0.82);
        sleep(1000);
        driveSeconds(1, 0.25f, dir.FORWARD);
        driveSeconds(1, 0.25f, dir.LEFTROT);
        hLinearSlide.setPosition(0.475);
        linearSlide.setPower(-0.3f);
        sleep(1000);
        driveSeconds(1, 0.25f, dir.FORWARD);
        linearSlide.setPower(0);
        hClawServo.setPosition(0.75);
        sleep(1000);

        //transfer spec 2
            hArmOpen.setPosition(0.82);
            hLinearSlide.setPosition(0.6461);
            sleep(1200);
            hClawServo.setPosition(0.6);
            sleep(500);
            hLinearSlide.setPosition(0.7);
            sleep(200);
            //hClawServo.setPosition(0.75);
            sleep(100);
            hArmOpen.setPosition(0.15);
            sleep(400);

        //basket spec 2
        hLinearSlide.setPosition(0.475);
        sleep(1000);
        driveSeconds(3, 0.25f, dir.BACKWARD);
        linearSlide.setPower(0.5f);
        driveSeconds(0.5, 0.25f, dir.FORWARD);
        driveSeconds(1, 0.25f, dir.RIGHTROT);
        driveSeconds(1, 0.25f, dir.BACKWARD);
        vArmServo.setPosition(0);
        sleep(1000);
        vArmServo.setPosition(0.875);
        driveSeconds(0.5, 0.25f, dir.FORWARD);
        sleep(1000);
        linearSlide.setPower(0);

    }
}



/*
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
        vArmServo.setPosition(0);
        sleep(1000);

        //get spec 2
        vArmServo.setPosition(0.875);
        sleep(2000);
        driveSeconds(1, 0.25f, dir.FORWARD);
        driveSeconds(1, 0.25f, dir.LEFTROT);
        hLinearSlide.setPosition(0.475);
        linearSlide.setPower(-0.3f);
        hArmOpen.setPosition(0.13);
        sleep(1000);
        hClawServo.setPosition(0.375);
        sleep(1000);
        driveSeconds(0.25, 0.25f, dir.FORWARD);
        linearSlide.setPower(0);
        //hClawServo.setPosition(0.375);
        //sleep(1000);
        hClawServo.setPosition(0.75);
        sleep(1000);

        //transfer spec 2
        hArmOpen.setPosition(0.88);
        hLinearSlide.setPosition(0.6461);
        sleep(1200);
        hClawServo.setPosition(0.6);
        sleep(500);
        hLinearSlide.setPosition(0.7);
        sleep(200);
        //hClawServo.setPosition(0.75);
        sleep(100);
        hArmOpen.setPosition(0.15);
        sleep(400);

        //basket spec 2
        hLinearSlide.setPosition(0.475);
        sleep(1000);
        driveSeconds(0.5, 0.25f, dir.BACKWARD);
        linearSlide.setPower(0.5f);
        driveSeconds(1, 0.25f, dir.RIGHTROT);
        driveSeconds(1, 0.25f, dir.BACKWARD);
        vArmServo.setPosition(0);
        sleep(1000);
        vArmServo.setPosition(0.875);
        driveSeconds(0.5, 0.25f, dir.FORWARD);
        sleep(1000);
        linearSlide.setPower(0);

    }
}
*/
