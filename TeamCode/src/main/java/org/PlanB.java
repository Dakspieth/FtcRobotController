package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="ScrimmageAuto_Left", group="Robot")
public class ScrimmageAuto_Left extends ScrimmageAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();

        //1.6s ≈ 2ft at 0.25speed
        //hang specimen
        linearSlide.setPower(0.25f);
        driveSeconds(24 * secondsPerInch, 0.25f, dir.FORWARD);
        vArmServo.setPosition(0);
        driveSeconds(6 * secondsPerInch, 0.25, dir.BACKWARD);
        linearSlide.setPower(0);
    }
}
