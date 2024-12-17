package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PlanB", group="Robot")
public class PlanB extends ScrimmageAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();


        //1.6s ≈ 2ft at 0.25speed
        //ROTATION: 6.5S ≈ 360 °
        //basket specimen
        linearSlide.setPower(0.5f);
        driveSeconds(3, 0.25f, dir.BACKWARD);
        driveSeconds(1, 0.25f, dir.LEFTROT);
        moveArm(0);
        sleep(3000);
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
        hClawServo.setPosition(0.375);
        sleep(1000);
        hClawServo.setPosition(0.75);
        sleep(1000);
        transferSample();
        while(true) {
            telemetry.addData("step:", transferStep);
            telemetry.addData("timer:", transferTimer);
            telemetry.update();
        }

    }
}
