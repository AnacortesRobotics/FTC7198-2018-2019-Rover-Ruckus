package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

// This is a class that will control any Mecanum chassis
public class MecanumChassis {

    // Define robot reference
    RR2Robot r;
    
    // Define the motor devices
    DcMotor frontLeft, frontRight, rearLeft, rearRight;

    // Define necessary values
    double frontLeftP, frontRightP, rearLeftP, rearRightP = 0;
    static double MAX_ROTATIONAL_VELOCITY = 300;
    static double MECANUM_CIRCUMFERENCE = 35.09;

    public MecanumChassis(RR2Robot _r) {

        // Set robot reference
        r = _r;
        
        // Maps motors to hardware
        frontLeft = r.hardwareMap.dcMotor.get("leftF");
        frontRight = r.hardwareMap.dcMotor.get("rightF");
        rearLeft = r.hardwareMap.dcMotor.get("leftB");
        rearRight = r.hardwareMap.dcMotor.get("rightB");

        // Reverses the two left motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Resets all motor encoders
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set all motors to use encoders
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set all motors to brake at power 0
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // Returns a string with all of the motor powers for telemetry
    public String toString() {
        return "FrontLeft: " + frontLeft.getPower() +
               "\nFrontRight: " + frontRight.getPower() +
               "\nRearLeft: " + rearLeft.getPower() +
               "\nRearRight: " + rearRight.getPower();
    }

    // Powers the motors based on human inputs
    public void driveMecanum(double forward, double clockwise, double right) {

        //Use basic vector math to get each motors speed
        double frontLeftP = forward + clockwise - right;
        double frontRightP = forward - (clockwise - right);
        double rearLeftP = forward + (clockwise + right);
        double rearRightP = forward - (clockwise + right);

        // Finds the highest speed among the 4(four)
        double maxSpeed = Math.abs(frontLeftP);
        if (Math.abs(frontRightP) > maxSpeed) {
            maxSpeed = Math.abs(frontRightP);
        } else if (Math.abs(rearLeftP) > maxSpeed) {
            maxSpeed = Math.abs(rearLeftP);
        } else if (Math.abs(rearRightP) > maxSpeed) {
            maxSpeed = Math.abs(rearRightP);
        }

        // If the max speed exceeds 1, use a ratio to bring all the speeds down evenly
        if (maxSpeed > 1) {
            frontLeftP = frontLeftP / maxSpeed;
            frontRightP = frontRightP / maxSpeed;
            rearLeftP = rearLeftP / maxSpeed;
            rearRightP = rearRightP / maxSpeed;
        }

        // Power the motors using a percent of the maximum velocity
        ((DcMotorEx) frontLeft).setVelocity(MAX_ROTATIONAL_VELOCITY * frontLeftP, AngleUnit.DEGREES);
        ((DcMotorEx) frontRight).setVelocity(MAX_ROTATIONAL_VELOCITY * frontRightP, AngleUnit.DEGREES);
        ((DcMotorEx) rearLeft).setVelocity(MAX_ROTATIONAL_VELOCITY * rearLeftP, AngleUnit.DEGREES);
        ((DcMotorEx) rearRight).setVelocity(MAX_ROTATIONAL_VELOCITY * rearRightP, AngleUnit.DEGREES);
    }

    // Takes a type of move and a value for that move, and drives the robot to meet that value
    public void controlMecanum(String type, int distance, double power) {

        // Reset all motor encoders
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Sets all motors to accept a position and power, then move to that
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set all necessary motor values
        switch (type) {
            case "forward":
                distance = (int) Math.floor(distance * MECANUM_CIRCUMFERENCE);
                frontLeft.setTargetPosition(distance);
                frontRight.setTargetPosition(distance);
                rearLeft.setTargetPosition(distance);
                rearRight.setTargetPosition(distance);
                frontLeft.setPower(power);
                frontRight.setPower(power);
                rearLeft.setPower(power);
                rearRight.setPower(power);
                break;
            case "right":
                distance = (int) Math.floor(distance * MECANUM_CIRCUMFERENCE);
                frontLeft.setTargetPosition(-distance);
                frontRight.setTargetPosition(distance);
                rearLeft.setTargetPosition(distance);
                rearRight.setTargetPosition(-distance);
                frontLeft.setPower(-power);
                frontRight.setPower(power);
                rearLeft.setPower(power);
                rearRight.setPower(-power);
                break;
        }

        // Add telemetry specific to the current movement
        while ((frontLeft.isBusy() || frontRight.isBusy() || rearLeft.isBusy() || rearRight.isBusy()) && !r.linearOpMode.isStopRequested()) {
            r.telemetry.addData("Type: ", type);
            r.telemetry.addData("Amount: ", distance);
            r.telemetry.addData("Distance", r.getDist());
            r.telemetry.addData("Front Left:", "%d", frontLeft.getCurrentPosition());
            r.telemetry.addData("Front Right:", "%d", frontRight.getCurrentPosition());
            r.telemetry.addData("Rear Left:", "%d", rearLeft.getCurrentPosition());
            r.telemetry.addData("Rear Right:", "%d", rearRight.getCurrentPosition());
            r.telemetry.update();
        }
    }

    // Moves left until it gets a specified distance away from a wall
    public void electricSlide(double dist) {
        while (r.getDist() > dist && !r.linearOpMode.isStopRequested()) {
            driveMecanum(0, 0, -0.7);
        }
        driveMecanum(0, 0, 0);
    }

    // Rotates the robot to a certain position using the poseTracker's IMU
    public void rotate(double degrees, double power) {

        double leftPower, rightPower;

        //Tell motors to run with encoders
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Resets the pose angle
        r.poseTracker.resetAngle();

        // Rotates to the specified position, reduces speed as it gets close
        if (degrees < 0) {
            setSimplePower(power, -power);
            while (r.poseTracker.getAngle() == degrees) {
                r.telemetry.addData("Angle:", r.poseTracker.getAngle());
                r.telemetry.update();
            }
            while (r.poseTracker.getAngle() > degrees && !r.linearOpMode.isStopRequested()) {
                double correction = 1 - r.poseTracker.getAngle() / degrees;
                if (correction < 0.1) correction = 0.1;
                setSimplePower(power * correction, -power * correction);
                r.telemetry.addData("Angle:", r.poseTracker.getAngle());
                r.telemetry.addData("Correction:", correction);
                r.telemetry.update();
            }
        } else if (degrees > 0) {
            setSimplePower(-power, power);
            while (r.poseTracker.getAngle() < degrees && !r.linearOpMode.isStopRequested()) {
                double correction = 1 - r.poseTracker.getAngle() / degrees;
                if (correction < 0.1) correction = 0.1;
                setSimplePower(-power * correction, power * correction);
                r.telemetry.addData("Angle:", r.poseTracker.getAngle());
                r.telemetry.addData("Correction:", correction);
                r.telemetry.update();
            }
        }

        // Stop the robot
        setSimplePower(0, 0);

        // Reset the angle
        r.poseTracker.resetAngle();
    }

    // Sets the power on the motors directly based on a left and right value
    public String setSimplePower(double left, double right) {
        frontLeft.setPower(left);
        frontRight.setPower(right);
        rearLeft.setPower(left);
        rearRight.setPower(right);
        return "Left: " + left + " Right: " + right;
    }
}