/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.broadcom.BroadcomColorSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;


@Autonomous()
public class KareemAuto extends OpMode {
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
    private boolean ready = false;
    private int stage = 0;
    private BNO055IMU imu = null;
    private TouchSensor bottomTouch = null;
    private TouchSensor topTouch = null;
    private RevColorSensorV3 colorBottom = null;
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
                Drivetrain.rotate(-90, imu);
                stage++;
                break;
            case 3:
                Functions.encoderDrive(1, 27, 27, frontRight, frontLeft, backLeft, backRight);
                stage++;
            case 4:
                flywheel.setPower(1);
                while (flywheel.getPower() != 1)
                {
                    ;
                }
                stage++;
            case 5:
                Functions.launch_ring(trigger, lift, flywheel, 1.0, topTouch);
                stage++;
            case 6:
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
