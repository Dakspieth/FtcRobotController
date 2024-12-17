package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PlanC", group="Robot")
public class PlanC extends ScrimmageAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();

        driveSeconds(3, 0.25f, dir.RIGHT);

    }
}
