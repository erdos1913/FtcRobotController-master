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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;


@Autonomous()
public class KareemAuto extends OpMode {

    // Declare OpMode members.
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
    private BNO055IMU imu = null;
    private TouchSensor bottomTouch = null;
    private TouchSensor topTouch = null;
    private ColorSensor colorBottom = null;
    private int count = 0;
    private int count2 = 0;
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight  = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        lift = hardwareMap.get(DcMotor.class, "lift");
        trigger = hardwareMap.get(Servo.class, "trigger");
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        bottomTouch = hardwareMap.get(TouchSensor.class, "sensorBottom");
        topTouch = hardwareMap.get(TouchSensor.class, "sensorTop");
        colorBottom = hardwareMap.get(ColorSensor.class, "colorBottom");
        colorBottom.enableLed(true);
        imu = hardwareMap.get(BNO055IMU.class, "imu 1");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu.initialize(parameters);
//        frontLeft.setDirection(DcMotor.Direction.REVERSE);
//        backLeft.setDirection(DcMotor.Direction.FORWARD);
//        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
//        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
//        backLeft.setTargetPosition(3000);
//        backRight.setTargetPosition(3000);
//        frontLeft.setTargetPosition(3000);
//        frontRight.setTargetPosition(3000);
//        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        backLeft.setPower(1);
//        backRight.setPower(1);
//        frontLeft.setPower(1);
//        frontRight.setPower(1);
//        if (imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle == 0)
//        {
//            while(backRight.isBusy())
//            {
//                count2 ++;
//            }
//            frontLeft.setTargetPosition(-3500);
//            frontRight.setTargetPosition(-3500);
//            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            frontLeft.setPower(1);
//            frontRight.setPower(1);
//            while(backRight.isBusy())
//            {
//                count2 ++;
//            }
//            Functions.initialize(lift, trigger, flywheel, bottomTouch);
//        }
//        else {
//            Range.clip(0, -1.0, 1.0);
//            rightPower = Range.clip(0, -1.0, 1.0);
//            frontLeft.setPower(leftPower);
//            backLeft.setPower(leftPower);
//            frontRight.setPower(rightPower);
//            backRight.setPower(rightPower);
//        }
    }

    @Override
    public void loop() {
        count = count+1;
        runtime.reset();
        if (count == 1)
        {
            Functions.launch_ring(trigger, lift, flywheel, 0.8, topTouch);
        }
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Color", colorBottom.blue());
        telemetry.update();
    }
}
