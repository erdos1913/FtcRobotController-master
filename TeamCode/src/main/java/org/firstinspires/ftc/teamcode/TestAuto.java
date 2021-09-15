package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class TestAuto extends LinearOpMode {
    @Override


    public void runOpMode() {
        ElapsedTime runtime = new ElapsedTime();
        DcMotor frontLeft = null;
        DcMotor backLeft = null;
        DcMotor frontRight = null;
        DcMotor backRight = null;
        Servo launch = null;
        Servo grabber = null;
        Servo trigger = null;
        DcMotor flywheel = null;
        DcMotor arm = null;
        DcMotor intake = null;
        DcMotor lift = null;
        TouchSensor topTouch = null;
        TouchSensor bottomTouch = null;
        BNO055IMU imu = null;
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight  = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        arm = hardwareMap.get(DcMotor.class, "arm");
        intake = hardwareMap.get(DcMotor.class, "intake");
        lift = hardwareMap.get(DcMotor.class, "lift");
        launch = hardwareMap.get(Servo.class, "launch");
        grabber = hardwareMap.get(Servo.class, "grabber");
        trigger = hardwareMap.get(Servo.class, "trigger");
        bottomTouch = hardwareMap.get(TouchSensor.class, "sensorBottom");
        topTouch = hardwareMap.get(TouchSensor.class, "sensorTop");
        imu = hardwareMap.get(BNO055IMU.class, "imu 1");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu.initialize(parameters);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if (!bottomTouch.isPressed())
            {
                telemetry.addData("Pressed", "False");
            }
            else {
                telemetry.addData("Pressed", "True");
            }
            if (gamepad1.x){
                functions.initialize(lift, trigger, flywheel, bottomTouch);
            }
            else if (gamepad1.a) {
                //Ready the trigger
                trigger.setPosition(1);
            }
            else if (gamepad1.b) {
                functions.launch_ring(trigger, lift, flywheel, 1, topTouch);
            }
            else if (gamepad1.y) {
                //Start the flywheel
                flywheel.setPower(1);
            }
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("X", imu.getAngularOrientation().toString());
            telemetry.update();
        }
    }
}

