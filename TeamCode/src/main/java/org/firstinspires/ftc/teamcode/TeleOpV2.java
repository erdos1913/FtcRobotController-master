package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name="TeleOpV2", group ="Test")
public class TeleOpV2 extends LinearOpMode {
    @Override
    public void runOpMode()
    {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        Servo trigger = hardwareMap.get(Servo.class, "trigger");
        DcMotor lift = hardwareMap.get(DcMotor.class, "lift");
        TouchSensor bottomTouch = hardwareMap.get(TouchSensor.class, "sensorBottom");
        TouchSensor topTouch = hardwareMap.get(TouchSensor.class, "sensorTop");
        double flywheel_power = 0;
        double left_targetPower = 0;
        double right_targetPower = 0;
        double intake_power = 0;
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();

        while (opModeIsActive())
        {
            left_targetPower = gamepad1.right_stick_y;
            right_targetPower = gamepad1.left_stick_y;
            frontLeft.setPower(left_targetPower);
            backLeft.setPower(left_targetPower);
            backRight.setPower(right_targetPower);
            frontRight.setPower(right_targetPower);
            flywheel.setPower(flywheel_power);
            intake.setPower(intake_power);
            if (gamepad1.y) {
                flywheel_power = 0.2;
            }
            else {
                flywheel_power = 0;
            }
            if (gamepad1.left_bumper)
            {
                intake_power = 1;
            }
            else
            {
                intake_power = 0;
            }
            if (gamepad1.x)
            {
                Functions.initialize(lift, trigger, flywheel, bottomTouch, intake);
            }
            if (gamepad1.b)
            {
                Functions.launch_ring(trigger, lift, flywheel, 0.2, topTouch);
            }

        }
    }
}
