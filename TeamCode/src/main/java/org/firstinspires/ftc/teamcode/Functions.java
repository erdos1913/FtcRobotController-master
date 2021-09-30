package org.firstinspires.ftc.teamcode;

import android.telephony.mbms.MbmsErrors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

public class Functions {
    static void initialize(DcMotor lift, Servo trigger, DcMotor flywheel, TouchSensor bottom, DcMotor intake) {
        flywheel.setPower(0);
        trigger.setPosition(0.8);
        lift.setPower(1);
        intake.setPower(0);
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
    static void park(VuforiaTrackable relicTemplate, DcMotor frontRight, DcMotor frontLeft, DcMotor backLeft, DcMotor backRight, float error_range)
    {
        boolean rotated;
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {


            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getFtcCameraFromTarget();

            if (pose != null) {
                VectorF trans = pose.getTranslation();
                Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                //Get transform
                double tX = trans.get(0);
                double tY = trans.get(1);
                double tZ = trans.get(2);
                //Get rotation
                double rX = rot.firstAngle;
                double rY = rot.secondAngle;
                double rZ = rot.thirdAngle;
                //Check if you're far away
                if (tZ > 600) {
                    if (backLeft.getPower() == 0) {
                        backLeft.setPower(-0.7);
                        backRight.setPower(-0.7);
                        frontLeft.setPower(-0.7);
                        frontRight.setPower(-0.7);
                    }
                }
                else if (tZ > 200)
                {
                    try {
                        if (rotated) {
                            backLeft.setPower(-0.7);
                            backRight.setPower(-0.7);
                            frontLeft.setPower(-0.7);
                            frontRight.setPower(-0.7);
                        } else if (Math.abs(backLeft.getPower()) > 0) {
                            backLeft.setPower(0);
                            backRight.setPower(0);
                            frontLeft.setPower(0);
                            frontRight.setPower(0);
                        }
                    }
                    catch (Exception MbmsErrors)
                    {
                        backLeft.setPower(-0.7);
                        backRight.setPower(-0.7);
                        frontLeft.setPower(-0.7);
                        frontRight.setPower(-0.7);
                    }
                }
                //If you've reached the final parked position
                else {
                    if (Math.abs(backLeft.getPower()) > 0) {
                        backLeft.setPower(0);
                        backRight.setPower(0);
                        frontLeft.setPower(0);
                        frontRight.setPower(0);
                    }
                }
                //If you aren't perfectly straight
                if (rY != 0) {
                    //Determine which direction
                    if (rY > 0) {
                        if ((180 - rY) > 0) {
                            if ((180 - rY) > error_range) {
                                backLeft.setPower(-0.7);
                                backRight.setPower(0.7);
                                frontLeft.setPower(-0.7);
                                frontRight.setPower(0.7);
                            } else {
                                if (Math.abs(backLeft.getPower()) > 0) {
                                    backLeft.setPower(0);
                                    backRight.setPower(0);
                                    frontLeft.setPower(0);
                                    frontRight.setPower(0);
                                    rotated = true;
                                }
                            }
                        } else {
                            if (Math.abs(backLeft.getPower()) > 0) {
                                rotated = true;
                                backLeft.setPower(0);
                                backRight.setPower(0);
                                frontLeft.setPower(0);
                                frontRight.setPower(0);
                            }
                        }
                    }
                    else
                    {
                        if ((180 - Math.abs(rY)) > 0)
                        {
                            if ((180 - Math.abs(rY)) > error_range)
                            {
                                backRight.setPower(-0.7);
                                backLeft.setPower(0.7);
                                frontRight.setPower(-0.7);
                                frontLeft.setPower(0.7);
                                rotated = true;
                            }
                            else
                            {
                                if (Math.abs(backLeft.getPower()) > 0) {
                                    rotated = true;
                                    backLeft.setPower(0);
                                    backRight.setPower(0);
                                    frontLeft.setPower(0);
                                    frontRight.setPower(0);
                                }
                            }
                        }
                        else
                        {
                            if (Math.abs(backLeft.getPower()) > 0) {
                                rotated = true;
                                backLeft.setPower(0);
                                backRight.setPower(0);
                                frontLeft.setPower(0);
                                frontRight.setPower(0);
                            }
                        }
                    }
                }
            }
            else {
                if (Math.abs(backLeft.getPower()) > 0) {
                    backLeft.setPower(0);
                    backRight.setPower(0);
                    frontLeft.setPower(0);
                    frontRight.setPower(0);
                }
            }
        }
        else {
            if (backLeft.getPower() > 0)
            {
                backLeft.setPower(0.3);
                backRight.setPower(-0.3);
                frontLeft.setPower(0.3);
                frontRight.setPower(-0.3);
            }
            else if (backLeft.getPower() < 0 || backLeft.getPower() == 0)
            {
                backLeft.setPower(-0.3);
                backRight.setPower(0.3);
                frontLeft.setPower(-0.3);
                frontRight.setPower(0.3);
            }
        }
    }
}