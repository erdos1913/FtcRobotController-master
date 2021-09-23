package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Image;
import com.vuforia.Matrix34F;
import com.vuforia.PIXEL_FORMAT;
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
        params.vuforiaLicenseKey = "ATLBIAn/////AAABmR9jXL4mVkCVlS3czuUEqVEZgVqD7w3Z4uaIJkGio0URiF8lIAErs7kiyVZUlYixochfzcUoJNf2SFphNNO9Src7sGGrCI7eM0LMMQkzBp2boM0ZYqfMOxIh1boee3VnPEuj3nNBTOxRfbc3BozDP/NpiIfhJoBML/YcvL+AEWdiHlxWD/ShveDB8lo2N2VQok00Y/9l8owMFi8er3EyOKNvJkKgS5yrDiys+ciIybloaJEHNAiyMa7xw3KGNoduvdVNQlZeRXAS+71658xe2ZxuSgvOfnklQRaRQCh+EbL2Ds5g9fvnBs0Kb+f3GeAtFQnmx2ERhDqBmsPuUZNRgMxrpyYm+J/1PgYlrGMFdxe1\n";
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
            params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
            vuforia.setFrameQueueCapacity(1);

            VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take();
            Image rgb = null;

            long numImages = frame.getNumImages();


            for (int i = 0; i < numImages; i++) {
                if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                    rgb = frame.getImage(i);
                    break;
                }
            }
            telemetry.update();
        }
    }
}
