package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Functions {
    static void initialize(DcMotor lift, Servo trigger, DcMotor flywheel, TouchSensor bottom) {
        flywheel.setPower(0);
        trigger.setPosition(0.8);
        while (!bottom.isPressed())
        {
            lift.setPower(1);
        }
        lift.setPower(0);
    }
    static void launch_ring(Servo trigger, DcMotor lift, DcMotor flywheel, double power, TouchSensor top)
    {
        trigger.setPosition(1);
        flywheel.setPower(power);
        while (!top.isPressed())
        {
            lift.setPower(-0.6);
        }
        lift.setPower(0);
    }

    static void drive(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, double length) {
        double distance = -length;

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        double COUNTS_PER_INCH = 47.746;

        newFrontLeftTarget = frontLeft.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        newFrontRightTarget = frontRight.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        newBackLeftTarget = backLeft.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        newBackRightTarget = backRight.getCurrentPosition() + (int)(distance * COUNTS_PER_INCH);
        frontLeft.setTargetPosition(newFrontLeftTarget);
        frontRight.setTargetPosition(newFrontRightTarget);
        backLeft.setTargetPosition(newBackLeftTarget);
        backRight.setTargetPosition(newBackRightTarget);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        frontLeft.setPower(Math.abs(.6));
        frontRight.setPower(Math.abs(.6));
        backLeft.setPower(Math.abs(.6));
        backRight.setPower(Math.abs(.6));

        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}