package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.oldScripts.ScrimmageAuto;

@Autonomous(name="StatesAutoLeft", group="Robot")
public class StatesAutoLeft extends StatesAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();

        //tests
        driveInches(140, 0.5f, dir.FORWARD, 30);
        //driveInches(140, 0.5f, dir.RIGHT, 30);
        //easeInches(140, 1, 0.5f, dir.FORWARD, 30);


        /*
        //basket specimen 1
        driveSeconds(0.35f, 0.25f, dir.LEFT);
        hClawServo.setPosition(0.75);
        linearSlide.setPower(0.75f);
        driveSeconds(3.25, 0.25f, dir.BACKWARD);
        driveSeconds(1, 0.25f, dir.LEFTROT);
        driveSeconds(0.25, 0.25f, dir.BACKWARD);
        vArmServo.setPosition(0);
        sleep(3000);

        //get spec 2
        vArmServo.setPosition(0.82);
        sleep(2000);
        driveSeconds(1, 0.25f, dir.FORWARD);
        driveSeconds(1.1, 0.25f, dir.LEFTROT);
        hLinearSlide.setPosition(0.625);
        linearSlide.setPower(-0.3f);
        hArmOpen.setPosition(0.18);
        sleep(1000);
        hClawServo.setPosition(0.377);
        sleep(1000);
        driveSeconds(1, 0.25f, dir.FORWARD);
        linearSlide.setPower(0);
        //hClawServo.setPosition(0.375);
        //sleep(1000);
        hClawServo.setPosition(0.75);
        sleep(1000);

        //transfer spec 2
        hArmOpen.setPosition(0.89);
        hLinearSlide.setPosition(0.65);
        sleep(1200);
        hClawServo.setPosition(0.65);
        sleep(500);
        hLinearSlide.setPosition(0.7);
        hArmOpen.setPosition(0.18);
        sleep(200);
        hClawServo.setPosition(0.75);
        sleep(100);
        //hArmOpen.setPosition(0.11);
        sleep(400);

        //basket spec 2
        hLinearSlide.setPosition(0.475);
        sleep(1000);
        driveSeconds(1, 0.25f, dir.BACKWARD);
        linearSlide.setPower(0.75f);
        driveSeconds(1.1, 0.25f, dir.RIGHTROT);
        driveSeconds(2, 0.25f, dir.BACKWARD);
        vArmServo.setPosition(0);
        sleep(3000);
        vArmServo.setPosition(0.875);
        driveSeconds(0.5, 0.25f, dir.FORWARD);
        sleep(1000);
        linearSlide.setPower(0);
         */
    }
}