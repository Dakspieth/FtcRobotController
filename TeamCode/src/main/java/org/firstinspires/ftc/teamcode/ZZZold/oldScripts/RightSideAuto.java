package org.firstinspires.ftc.teamcode.ZZZold.oldScripts;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
public class RightSideAuto extends ScrimmageAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();

        driveSeconds(3, 0.25f, dir.RIGHT);

    }
}
