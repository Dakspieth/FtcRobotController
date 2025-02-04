package org.firstinspires.ftc.teamcode.AAAstates;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="StatesAutoRight", group="Robot")
public class StatesAutoRight extends StatesAuto {
    public void runOpMode() {
        // Call the parent class method to use its setup
        super.runOpMode();

        //tests/////////////////////////////////////////
        //vSlidePos(1f, 0.15f);
        //vSlidePos(1, 0.5f);
        //vSlidePos(0, 0.75f);
//
        //driveInches(24, 0.5f, dir.FORWARD, 30);



        //easeInches(96, 0.5f, 0.1f, dir.BACKWARD, 30);

        //driveInches(96, 0.5f, dir.RIGHT, 30);
        //driveInches(96, 0.5f, dir.LEFT, 30);
        //driveInches(96, 0.5f, dir.BACKWARD, 30);

        //driveInches(240, 0.5f, dir.RIGHTROT, 30);
        //driveInches(240, 0.5f, dir.LEFTROT, 30);




        ////////////////// main /////////////////////


        driveInches(3, 0.5f, dir.LEFT, 30);
        easeInches(25, 0.3f, 0.5f, dir.FORWARD, 30);

























        /*driveInches(2, 0.6f, dir.FORWARD, 30);
        driveInches(21, 0.6f, dir.RIGHT, 30);

        driveInches(54, 0.6f, dir.FORWARD, 30);
        driveInches(14, 0.6f, dir.RIGHT, 30);
        driveInches(50, 0.6f, dir.BACKWARD, 30);

        driveInches(50, 0.6f, dir.FORWARD, 30);
        driveInches(10, 0.6f, dir.RIGHT, 30);
        driveInches(50, 0.6f, dir.BACKWARD, 30);

        driveInches(50, 0.6f, dir.FORWARD, 30);
        driveInches(3, 0.6f, dir.RIGHT, 30);
        driveInches(50, 0.6f, dir.BACKWARD, 30);

        driveInches(2, 0.6f, dir.FORWARD, 30);*/




    }
}