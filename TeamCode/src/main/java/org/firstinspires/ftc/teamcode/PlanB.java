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
        linearSlide.setPower(-0.5f);
        driveSeconds(1, 0.25f, dir.FORWARD);
        linearSlide.setPower(0);

        transferSample();
    }
}
