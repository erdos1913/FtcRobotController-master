package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Behaviors.Drivetrain;

@Autonomous
public class FreightFrenzy_Auto extends OpMode {
    OpenGLMatrix lastLocation = null;


    VuforiaLocalizer vuforia;


    WebcamName webcamName;
    float error_range = 10;
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
    BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    boolean rotated = false;
    boolean arrived = false;

    int stage = 1;
    @Override
    public void init()
    {
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        webcamName = hardwareMap.get(WebcamName.class, "frontCamera");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters2 = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parameters2.vuforiaLicenseKey = "ATsWqNz/////AAABmWYRlY7DGU8dtTwW/Yw4PNtPvPXxTdmekWVEYn733h49VAyjzQnsW1TZ+VVTaO7AekdQGZr/KSuDrOw9AI68/uRoZsF1ukki/sKQE/vKPKvK0mOz3l0KfdFiuSKRXZHVlvGdDok1elfQEkFndz/I3GgTFLD6JNBONF5M4khp36vjBP2a/IPvQsefLMDwrvNirNfPMYnRKHH6+d8z3sbWBwfQp7i1c5l0hgcljwPT1Qq1dzYufmsFmvqsioIcZH0G1TuWWHxnBvrpN9/l4dV8gxA6+XdaAiABeiU7d+tzzUMuoLHt9iUW98+/mG4RqpecRTyMnk+ne5LXpyWfXdXsaTInZ7yh2/QaWgUDDfktkWuK";

        parameters2.cameraName = webcamName;
        imu.initialize(parameters);
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters2);


        Functions.initialize(lift, trigger, flywheel, bottomTouch, intake);
    }
    public void loop()
    {
        switch (stage)
        {
            case 1:
                Functions.encoderDrive(1, 12, 12, frontRight, frontLeft, backLeft, backRight);
                break;
            case 2:
                //Encoder spin
                break;
            case 3:
                Functions.encoderDrive(0.6, -4, -4, frontRight, frontLeft, backLeft, backRight);
                break;
            case 4:
                Drivetrain.rotate(this.getClass(), -100, imu);
                break;
            case 5:
                Functions.encoderDrive(1, 20, 20, frontRight, frontLeft, backLeft, backRight);
                break;
            case 6:
                VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
                VuforiaTrackable relicTemplate = relicTrackables.get(0);
                relicTemplate.setName("relicVuMarkTemplate");
                relicTrackables.activate();
                while (!arrived)
                {
                    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                        telemetry.addData("VuMark", "%s visible", vuMark);


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
                            telemetry.addData("TX", tX);
                            telemetry.addData("TY", tY);
                            telemetry.addData("TZ", tZ);
                            telemetry.addData("RX", rX);
                            telemetry.addData("RY", rY);
                            telemetry.addData("RZ", rZ);
                            //Check if you're far away
                            if (tZ > 600) {
                                telemetry.addData("Status", "Moving towards VuMark");
                                if (backLeft.getPower() == 0) {
                                    backLeft.setPower(-0.7);
                                    backRight.setPower(-0.7);
                                    frontLeft.setPower(-0.7);
                                    frontRight.setPower(-0.7);
                                }
                            }
                            else if (tZ > 200)
                            {
                                if (rotated) {
                                    backLeft.setPower(-0.7);
                                    backRight.setPower(-0.7);
                                    frontLeft.setPower(-0.7);
                                    frontRight.setPower(-0.7);
                                }
                                else if (Math.abs(backLeft.getPower()) > 0) {
                                    backLeft.setPower(0);
                                    backRight.setPower(0);
                                    frontLeft.setPower(0);
                                    frontRight.setPower(0);
                                }
                            }
                            //If you've reached the final parked position
                            else {
                                telemetry.addData("Status", "Arrived");
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
                                            telemetry.addData("Status", "Rotating right");
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
                                            telemetry.addData("Status", "Rotating left");
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
                        telemetry.addData("VuMark", "not visible");
                        telemetry.addData("State", "Locating VuMark");
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

                    telemetry.update();
                }
                break;
        }
    }
}
