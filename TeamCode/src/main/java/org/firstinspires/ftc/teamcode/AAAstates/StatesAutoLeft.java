package org.firstinspires.ftc.teamcode.AAAstates;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="StatesAutoLeft", group="Robot")
public class StatesAutoLeft extends StatesAuto {
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


  /*      while(true){
            moveClaw(false);
            SetVArmPos("down");
            SetHArmPos("up");
            sleep(1500);
            SetHArmPos("down");
            sleep(1500);
        }
*/

        SetVArmPos("down");
        SetHArmPos("up");
        //outtake preload sample
        SetVSlideSpeed(0.75);
        driveInches(7, 0.4f, dir.LEFT, 30);
        driveInches(17.5f, 0.4f, dir.BACKWARD, 30);
        driveInches(46, 0.4f, dir.LEFTROT, 30);
        driveInches(5, 0.4f, dir.BACKWARD, 30);
        SetVArmPos("out");
        sleep(800);
        SetVArmPos("down");

            //intake spike sample 1
            driveInches(12, 0.4f, dir.FORWARD, 30);
            driveInches(47f, 0.4f, dir.LEFTROT, 30);
            SetVSlideSpeed(-0.75);
            SetHArmPos("down");
            SethSlidePos(0.55);
            moveClaw(true);
            sleep(1000);
            SetVSlideSpeed(0);
            SethSlidePos(hsOut);
            sleep(450);//reduce this if low time(extra wait but dont feel like changing)
            driveInches(4, 0.2f, dir.FORWARD, 30);
            moveClaw(false);
            sleep(500);

            //transfer spike sample 1
            SetHArmPos("up");
            sleep(600);
            SethSlidePos(hsIn);
            sleep(600);
            hClawServo.setPosition(0.625);
            sleep(200);
            //hClawServo.setPosition(0.75);
            SethSlidePos(hsOut);
            sleep(100);

            //outtake spike sample 1
            SetVSlideSpeed(0.75);
            driveInches(3, 0.2f, dir.BACKWARD, 30);
            driveInches(43f, 0.4f, dir.RIGHTROT, 30);
            driveInches(16, 0.4f, dir.BACKWARD, 30);
            SetVArmPos("out");
            sleep(800);
            SetVArmPos("down");

        // intake spike sample 2
        SetHArmPos("down");
        SethSlidePos(hsIn);
        sleep(100);
        moveClaw(true);
        sleep(500);
        easeInches(62f, 0.5f, 0.5f, dir.BLROT, 30);
        SetVSlideSpeed(-0.75);
        SethSlidePos(0.5);
        sleep(250);
        driveInches(6, 0.5f, dir.FORWARD, 30);
        moveClaw(false);
        sleep(550);
        driveInches(6, 0.6f, dir.BACKWARD, 30);
//        sleep(1000);
        SetVSlideSpeed(0);

        //transfer spike sample 2
        SetHArmPos("up");
        sleep(900);
        SethSlidePos(0.585);
        sleep(600);
        hClawServo.setPosition(0.625);
        sleep(200);
        // mn  hClawServo.setPosition(0.75);
        SethSlidePos(hsOut);
        sleep(100);

        //outtake spike sample 2
        SetVSlideSpeed(0.8);
        easeInches(70.5f, 0.5f, 0.5f, dir.BLROTNEG, 30);
        //sleep(800);
        SetVArmPos("out");
        SetHArmPos("up");
        sleep(800);
        SetVArmPos("down");
        SetHArmPos("down");
        sleep(500);

            //intake spike sample 3
            driveInches(4, 0.4f, dir.FORWARD, 30);
            SetVSlideSpeed(-0.6);
            SetHArmPos("down");
            sleep(200);
            driveInches(78f, 0.5f, dir.LEFTROT, 30);
            //driveInches(75, 0.5f, dir.LEFTROT, 30);
            //driveInches(4, 0.5f, dir.LEFT, 30);
            hClawServo.setPosition(0.575);
            sleep(100);
            driveInches(11.2f, 0.3f, dir.FORWARD, 30);
            //sleep(250);
            moveClaw(false);
            sleep(200);

            driveInches(8, 0.3f, dir.BACKWARD, 30);

            //transfer spike sample 3
            SetHArmPos("up");
            sleep(900);
            SethSlidePos(hsIn);
            sleep(650);
            hClawServo.setPosition(0.625);
            sleep(200);
            //hClawServo.setPosition(0.75);
            SethSlidePos(hsOut);
            sleep(100);

            //outtake spike sample 3
            SetVSlideSpeed(0.8f);
            //driveInches(4, 0.5f, dir.Right, 30);
            driveInches(66.5f, 0.5f, dir.RIGHTROT, 30);
            driveInches(10, 0.5f, dir.BACKWARD, 30);
            SetVArmPos("out");
            sleep(800);
            SetVArmPos("down");
            moveClaw(false);
            sleep(500);

        //go to chamber to save a few seconds in teleop
        easeInches(24, 0.7f, 0.7f , dir.BLROT, 30);
        SetVSlideSpeed(-0.5 );
        //driveInches(38, 0.7f, dir.FORWARD, 30);
        //driveInches(50, 0.7f, dir.RIGHTROT, 30);


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////



        /*
        driveSeconds(0.35f, 0.25f, dir.LEFT);
        hClawServo.setPosition(0.75);
        SetVSlideSpeed(0.75f);
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
        SetVSlideSpeed(-0.3f);
        hArmOpen.setPosition(0.18);
        sleep(1000);
        hClawServo.setPosition(0.377);
        sleep(1000);
        driveSeconds(1, 0.25f, dir.FORWARD);
        SetVSlideSpeed(0);
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
        SetVSlideSpeed(0.75f);
        driveSeconds(1.1, 0.25f, dir.RIGHTROT);
        driveSeconds(2, 0.25f, dir.BACKWARD);
        vArmServo.setPosition(0);
        sleep(3000);
        vArmServo.setPosition(0.875);
        driveSeconds(0.5, 0.25f, dir.FORWARD);
        sleep(1000);
        SetVSlideSpeed(0);
         */
    }
}