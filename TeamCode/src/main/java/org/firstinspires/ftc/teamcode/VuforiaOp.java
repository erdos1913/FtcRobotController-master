package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Matrix34F;
import com.vuforia.Tool;
import com.vuforia.Vec2F;
import com.vuforia.Vec3F;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaTrackableContainer;

import java.util.Arrays;

@Autonomous
public class VuforiaOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = "FTC10237";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        VuforiaLocalizerImplSubclass vuforia = new VuforiaLocalizerImplSubclass(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        waitForStart();

        beacons.activate();

        while(opModeIsActive())
        {
            if (vuforia.rgb != null)
            {
                Bitmap bm = Bitmap.createBitmap(vuforia.rgb.getWidth(), vuforia.rgb.getHeight(), Bitmap.Config.RGB_565);
                bm.copyPixelsFromBuffer(vuforia.rgb.getPixels());
                vuforia.getFrameQueue().take().getImage(1);

            }

            for (VuforiaTrackable beac : beacons)
            {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getRawPose();

                if (pose != null)
                {
                    Matrix34F rawPose = new Matrix34F();
                    float[] poseData = Arrays.copyOfRange(pose.transposed().getData(), 0, 12);
                    rawPose.setData(poseData);

                    Vec2F upperLeft = Tool.projectPoint(com.vuforia.CameraDevice.getInstance().getCameraCalibration(), rawPose, new Vec3F(-127, 92, 0));
                    Vec2F upperRight = Tool.projectPoint(com.vuforia.CameraDevice.getInstance().getCameraCalibration(), rawPose, new Vec3F(127, 92, 0));
                    Vec2F lowerRight = Tool.projectPoint(com.vuforia.CameraDevice.getInstance().getCameraCalibration(), rawPose, new Vec3F(127, -92, 0));
                    Vec2F lowerLeft = Tool.projectPoint(com.vuforia.CameraDevice.getInstance().getCameraCalibration(), rawPose, new Vec3F(-127, -92, 0));
                }
            }
            telemetry.update();
        }
    }
}
