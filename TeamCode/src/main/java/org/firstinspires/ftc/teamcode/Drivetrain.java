package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.lang.reflect.InvocationTargetException;

public class Drivetrain {

    static boolean isBuffering = false;

    public static void rotate(Class c, double a, BNO055IMU imu) throws Exception {
        if (isBuffering){
            throw new Exception("Cannot buffer more than one Drivetrain action at once");
        }
        double startAngle;
        double angle;
        startAngle = startAngle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
        angle = startAngle;
        while (true) {
            angle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - startAngle;
            Double leftPower = Double.valueOf(0);
            double rightPower = Double.valueOf(0);
            if (a - angle <= 5) {
                leftPower = Double.valueOf(Range.clip(0, -1, 1));
                rightPower = Double.valueOf(Range.clip(0, -1, 1));
                return;
            } else {
                leftPower = Double.valueOf(Range.clip(1 - Math.pow(angle / a, 2), 0.3, 1.0));
                rightPower = Double.valueOf(Range.clip(Math.pow(angle / a, 2) - 1, -1.0, -0.3));
            }
            String className = c.getName();
            Class.forName(className).getMethods();
            Class opMode = Class.forName(className);
            try {
                opMode.getMethod("setDrivetrainMotors",Double.class,Double.class).invoke(null,leftPower,rightPower);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static void rotateBuffer(double a, BNO055IMU imu) throws Exception {
        try {
            return;
        } finally {
            if (isBuffering){
                throw new Exception("Cannot buffer more than one Drivetrain action at once");
            }
            isBuffering = true;
            double startAngle;
            double angle;
            startAngle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            angle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - startAngle;
            while (true) {
                angle = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - startAngle;
                Double leftPower = Double.valueOf(0);
                Double rightPower = Double.valueOf(0);
                if (a - angle <= 5) {
                    leftPower = Double.valueOf(Range.clip(0, -1, 1));
                    rightPower = Double.valueOf(Range.clip(0, -1, 1));
                } else {
                    leftPower = Double.valueOf(Range.clip(1 - Math.pow(angle / a,2), 0.3, 1.0));
                    rightPower = Double.valueOf(Range.clip(Math.pow(angle / a, 2) - 1, -1.0, -0.3));
                }
                String className = Thread.currentThread().getStackTrace()[2].getClassName();
                Class.forName(className).getMethods();
                Class opMode = Class.forName(className);
                try {
                    opMode.getMethod("setDrivetrainMotors",Double.class,Double.class).invoke(null,leftPower,rightPower);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                if (leftPower==0&&rightPower==0){
                    isBuffering = false;
                    return;
                }
            }
        }
    }

}
