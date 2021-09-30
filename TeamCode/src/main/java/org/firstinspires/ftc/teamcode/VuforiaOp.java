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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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


@TeleOp(name="WebCam ID", group ="Concept")
public class VuforiaOp extends LinearOpMode {

    public static final String TAG = "Vuforia VuMark Sample";

    OpenGLMatrix lastLocation = null;


    VuforiaLocalizer vuforia;


    WebcamName webcamName;
    float error_range = 10;
    @Override public void runOpMode() {
        DcMotor frontLeft  = hardwareMap.get(DcMotor .class, "frontLeft");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight  = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        webcamName = hardwareMap.get(WebcamName.class, "frontCamera");


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parameters.vuforiaLicenseKey = "ATsWqNz/////AAABmWYRlY7DGU8dtTwW/Yw4PNtPvPXxTdmekWVEYn733h49VAyjzQnsW1TZ+VVTaO7AekdQGZr/KSuDrOw9AI68/uRoZsF1ukki/sKQE/vKPKvK0mOz3l0KfdFiuSKRXZHVlvGdDok1elfQEkFndz/I3GgTFLD6JNBONF5M4khp36vjBP2a/IPvQsefLMDwrvNirNfPMYnRKHH6+d8z3sbWBwfQp7i1c5l0hgcljwPT1Qq1dzYufmsFmvqsioIcZH0G1TuWWHxnBvrpN9/l4dV8gxA6+XdaAiABeiU7d+tzzUMuoLHt9iUW98+/mG4RqpecRTyMnk+ne5LXpyWfXdXsaTInZ7yh2/QaWgUDDfktkWuK";

        parameters.cameraName = webcamName;
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);

        boolean rotated = false;
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        relicTrackables.activate();

        while (opModeIsActive()) {
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
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
