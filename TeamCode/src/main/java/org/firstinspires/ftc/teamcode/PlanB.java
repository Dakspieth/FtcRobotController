package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PlanB", group="Robot")
public class PlanB extends ScrimmageAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();

        //1.6s ≈ 2ft at 0.25speed
        //hang specimen
        linearSlide.setPower(0.5f);
        driveSeconds(3, 0.25f, dir.BACKWARD);
        moveArm(0);
        sleep(200);
        driveSeconds(6 * secondsPerInch, 0.25f, dir.FORWARD);
        linearSlide.setPower(0);
    }
}
