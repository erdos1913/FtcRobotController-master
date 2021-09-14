package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class functions {
    static void initialize(DcMotor lift, Servo trigger, DcMotor flywheel, TouchSensor bottom) {
        flywheel.setPower(0);
        trigger.setPosition(0.7);
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
    static void move(int position, DcMotor backLeft, DcMotor backRight, DcMotor frontLeft, DcMotor frontRight){
        frontLeft.setTargetPosition(position);
        frontRight.setTargetPosition(position);
        backLeft.setTargetPosition(position - 3);
        backRight.setTargetPosition(position - 3);
        frontLeft.setPower(1);
        frontRight.setPower(1);
        backLeft.setPower(1);
        backRight.setPower(1);
    }
}