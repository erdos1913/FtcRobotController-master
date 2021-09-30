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
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
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
                Functions.park(relicTemplate, frontRight, frontLeft, backLeft, backRight, error_range);
                break;
        }
    }
}
