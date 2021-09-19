package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Functions {
    static void initialize(DcMotor lift, Servo trigger, DcMotor flywheel, TouchSensor bottom) {
        flywheel.setPower(0);
        trigger.setPosition(0.8);
        lift.setPower(1);
        while (!bottom.isPressed())
        {
            ;
        }
        lift.setPower(0);
    }
    static void launch_ring(Servo trigger, DcMotor lift, DcMotor flywheel, double power, TouchSensor top)
    {
        trigger.setPosition(1);
        flywheel.setPower(power);
        lift.setPower(-0.6);
        while (!top.isPressed())
        {
            ;
        }
        lift.setPower(0);
    }
    static void encoderDrive(double speed, double leftInches, double rightInches, DcMotor frontRight, DcMotor frontLeft, DcMotor backLeft, DcMotor backRight) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;
        double COUNTS_PER_MOTOR_REV = 100;    // eg: TETRIX Motor Encoder
        final double DRIVE_GEAR_REDUCTION = 3.0;     // This is < 1.0 if geared UP
        final double WHEEL_DIAMETER_INCHES = 2.0;     // For figuring circumference
        final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);
        final double DRIVE_SPEED = 0.6;
        final double TURN_SPEED = 0.5;
        // Determine new target position, and pass to motor controller
        newFrontLeftTarget = frontLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newFrontRightTarget = frontRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
        newBackLeftTarget = backLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newBackRightTarget = backRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
        frontLeft.setTargetPosition(newFrontLeftTarget);
        frontRight.setTargetPosition(newFrontRightTarget);
        backLeft.setTargetPosition(newBackLeftTarget);
        backRight.setTargetPosition(newBackRightTarget);

        // Turn On RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setPower(Math.abs(speed));
        frontRight.setPower(Math.abs(speed));
        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));
        // Stop all motion;
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  sleep(250);   // optional pause after each move
    }
}