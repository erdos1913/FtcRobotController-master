package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class functions {
    static void initialize(DcMotor lift, Servo trigger, DcMotor flywheel) {
        lift.setPower(1);
        lift.setTargetPosition(1000);
        trigger.setPosition(0);
        flywheel.setPower(0);
    }
    static void launch_ring(Servo trigger, DcMotor lift, DcMotor flywheel, double power)
    {
        trigger.setPosition(1);
        lift.setPower(-0.6);
        flywheel.setPower(power);
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