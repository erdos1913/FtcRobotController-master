package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class functions {
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
}