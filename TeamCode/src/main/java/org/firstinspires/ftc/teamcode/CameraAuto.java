package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Autonomous
public class CameraAuto {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    private CameraAuto
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //motors
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight  = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //servos
        lift = hardwareMap.get(DcMotor.class, "lift");
        trigger = hardwareMap.get(Servo.class, "trigger");
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        //sensors
        bottomTouch = hardwareMap.get(TouchSensor.class, "sensorBottom");
        topTouch = hardwareMap.get(TouchSensor.class, "sensorTop");
        colorBottom = hardwareMap.get(RevColorSensorV3.class, "colorBottom");
        colorBottom.initialize();
        colorBottom.enableLed(false);
        //imu
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.calibrationDataFile = "IMUCalibration.json";
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 50);
        Functions.initialize(lift, trigger, flywheel, bottomTouch);

    }
    @Override
    public void loop() {
        runtime.reset();
        switch (stage)
        {
            case 1:
                backLeft.setPower(1);
                backRight.setPower(1);
                frontRight.setPower(1);
                frontLeft.setPower(1);
                while (colorBottom.blue() < 120)
                {
                    ;
                }
                backLeft.setPower(0);
                backRight.setPower(0);
                frontRight.setPower(0);
                frontLeft.setPower(0);
                stage++;
                break;
            case 2:
                Functions.encoderDrive(1, 24, 24, frontRight, frontLeft, backLeft, backRight);
                stage++;
            case 3:
                //Drivetrain.rotate(-90, imu);
                stage++;
                break;
            case 4:
                Functions.encoderDrive(1, 27, 27, frontRight, frontLeft, backLeft, backRight);
                stage++;
            case 5:
                flywheel.setPower(1);
                while (flywheel.getPower() != 1)
                {
                    ;
                }
                stage++;
            case 6:
                Functions.launch_ring(trigger, lift, flywheel, 1.0, topTouch);
                stage++;
            case 7:
                Functions.initialize(lift, trigger, flywheel, bottomTouch);
                stage++;
            default:
                break;
        }
        telemetry.addData("Stage", stage);
        telemetry.addData("Red", colorBottom.red());
        telemetry.addData("Blue", colorBottom.blue());
        telemetry.addData("Green", colorBottom.green());
        telemetry.addData("Alpha", colorBottom.alpha());
        telemetry.update();
    }
}
