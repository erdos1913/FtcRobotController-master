package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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
        int flywheel_power = 0;
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();

        while (opModeIsActive())
        {
            double left_targetPower = gamepad1.right_stick_y;
            double right_targetPower = gamepad1.left_stick_y;
            frontLeft.setPower(left_targetPower);
            backLeft.setPower(left_targetPower);
            backRight.setPower(right_targetPower);
            frontRight.setPower(right_targetPower);
            flywheel.setPower(flywheel_power);
            if (gamepad1.y) {
                flywheel_power = 1;
            }
            else
            {
                flywheel_power = 0;
            }
        }
    }
}
