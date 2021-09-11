package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class TestAuto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    private Servo launch = null;
    private Servo grabber = null;
    private Servo trigger = null;
    private DcMotor flywheel = null;
    private DcMotor arm = null;
    private DcMotor intake = null;
    private DcMotor lift = null;
    @Override
    public void runOpMode() {
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
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            if (gamepad1.x){
                functions.initialize(lift, trigger, flywheel);
            }
            else if (gamepad1.a) {
                //Ready the trigger
                trigger.setPosition(1);
            }
            else if (gamepad1.b) {
                functions.launch_ring(trigger, lift, flywheel, 1);
            }
            else if (gamepad1.y) {
                //Start the flywheel
                flywheel.setPower(1);
            }
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}

