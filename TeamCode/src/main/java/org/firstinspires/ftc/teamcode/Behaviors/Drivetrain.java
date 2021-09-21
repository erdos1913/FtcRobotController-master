package org.firstinspires.ftc.teamcode.Behaviors;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import java.lang.reflect.InvocationTargetException;

/*
The Drivetrain class contains sequences using the 4 drivetrain motors.  Each
method within this class will have two variants, a normal variant and a buffer variant.
When the normal variant is called, the program will not continue until the event
has finished.  The buffer variant will immediately return yet still run, allowing two or
more events to occur simultaneously.

For example, if one wants to turn while spinning up a flywheel, one could use the following sudo-code:

Drivetrain.rotateBuffer(this.getClass,180,imu);
flywheel.setPower(1);

For the methods to work correctly, each class they are called from must contain
the following field declarations (public or private does not matter):
    DcMotor frontLeft = null;
    DcMotor backLeft = null;
    DcMotor frontRight = null;
    DcMotor backRight = null;

In addition, each OpMode class must contain the following setter methods:
    public void setDrivetrainMotors(Double leftPower, Double rightPower){
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
    }

    public void setTelemetry(String[][] messages){
        for (String[] message: messages) {
            telemetry.addData(message[0],message[1]);
        }
    }

Finally, each OpMode that calls a method within this class must extend LinearOpMode
An example of a Linear Opmode can be found at:
 */
import org.firstinspires.ftc.robotcontroller.external.samples.BasicOpMode_Linear;

public class Drivetrain{

    static boolean isBuffering = false;

    public static void rotate(Class c, double a, BNO055IMU imu) {
        try {
            if (isBuffering) {
                throw new Exception("Cannot buffer more than one Drivetrain action at once");
            }
            double startAngle;
            double angle;
            startAngle = startAngle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            angle = startAngle;
            while ((boolean)c.getMethod("opModeIsActive").invoke(c)) {
                angle = Math.abs(imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - startAngle);
                Double leftPower;
                Double rightPower;
                if (180 - angle <= 5) {
                    leftPower = (double) Range.clip(0, 0, 0);
                    rightPower = (double) Range.clip(0, 0, 0);
                } else {
                    leftPower = Range.clip(1 - Math.pow(angle / 180.0, 2), 0.3, 1.0);
                    rightPower = Range.clip(Math.pow(angle / 180.0, 2) - 1, -1.0, -0.3);
                }
                c.getMethod("setDrivetrainMotors",Double.class,Double.class).invoke(c,leftPower,rightPower);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void rotateBuffer(Class c, double a, BNO055IMU imu) {
        try {
            return;
        } finally {
            try {
                if (isBuffering) {
                    throw new Exception("Cannot buffer more than one Drivetrain action at once");
                }
                isBuffering = true;
                double startAngle;
                double angle;
                startAngle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
                angle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - startAngle;
                while ((boolean)c.getMethod("opModeIsActive").invoke(c)) {
                    angle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - startAngle;
                    Double leftPower = Double.valueOf(0);
                    Double rightPower = Double.valueOf(0);
                    if (a - angle <= 5) {
                        leftPower = Double.valueOf(Range.clip(0, -1, 1));
                        rightPower = Double.valueOf(Range.clip(0, -1, 1));
                    } else {
                        leftPower = Double.valueOf(Range.clip(1 - Math.pow(angle / a, 2), 0.3, 1.0));
                        rightPower = Double.valueOf(Range.clip(Math.pow(angle / a, 2) - 1, -1.0, -0.3));
                    }
                    try {
                        c.getMethod("setDrivetrainMotors", Double.class, Double.class).invoke(c, leftPower, rightPower);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    if (leftPower == 0 && rightPower == 0) {
                        isBuffering = false;
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
