package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

// This is a class that will control any mecanum chassis

public class MecanumChassis {
    DcMotor fL;
    DcMotor fR;
    DcMotor rL;
    DcMotor rR;
    double variance = 2.2;
    
    MecanumChassis(DcMotor frontLeft, DcMotor frontRight, DcMotor rearLeft, DcMotor rearRight) {
        //Sets all the motors to be in class variables
        fL = frontLeft;
        fR = frontRight;
        rL = rearLeft;
        rR = rearRight;
        //Reverses the two left motors
        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        rL.setDirection(DcMotorSimple.Direction.REVERSE);
        //Tells all motors to reset their encoders
        fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Tells all the motors to use encoders
        fL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Tells all motors that they should brake when not told to move
        fL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    
    public void driveMecanum(double forward, double clockwise, double right) {
        double frontLeft = forward + clockwise - right;
        double frontRight = forward - (clockwise - right);
        double rearLeft = forward + (clockwise + right);
        double rearRight = forward - (clockwise + right);
        double maxSpeed = Math.abs(frontLeft);
        
        if (Math.abs(frontRight) > maxSpeed) {
          maxSpeed = Math.abs(frontRight);
        } else if (Math.abs(rearLeft) > maxSpeed) {
          maxSpeed = Math.abs(rearLeft);
        } else if (Math.abs(rearRight) > maxSpeed) {
          maxSpeed = Math.abs(rearRight);
        }
        if (maxSpeed > 1) {
          frontLeft = frontLeft / maxSpeed;
          frontRight = frontRight / maxSpeed;
          rearLeft = rearLeft / maxSpeed;
          rearRight = rearRight / maxSpeed;
        }
        ((DcMotorEx) fL).setVelocity(300 * frontLeft, AngleUnit.DEGREES);
        ((DcMotorEx) fR).setVelocity(300 * frontRight, AngleUnit.DEGREES);
        ((DcMotorEx) rL).setVelocity(300 * rearLeft, AngleUnit.DEGREES);
        ((DcMotorEx) rR).setVelocity(300 * rearRight, AngleUnit.DEGREES);
    }
    
    public void controlMecanum(String type, int distance, double power) {
      fL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      fR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      rL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      rR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      fL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      fR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      rL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      rR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      switch(type) {
        case "forward":
          distance = (int) Math.floor(distance*35.09);
          fL.setTargetPosition(distance);
          fR.setTargetPosition(distance);
          rL.setTargetPosition(distance);
          rR.setTargetPosition(distance);
          fL.setPower(power);
          fR.setPower(power);
          rL.setPower(power);
          rR.setPower(power);
        break;
        case "right":
          distance = (int) Math.floor(distance*35.09);
          fL.setTargetPosition(-distance);
          fR.setTargetPosition(distance);
          rL.setTargetPosition(distance);
          rR.setTargetPosition(-distance);
          fL.setPower(-power);
          fR.setPower(power);
          rL.setPower(power);
          rR.setPower(-power);
        break;
        case "clockwise":
          distance = (int) Math.floor(((distance>0)?distance-variance:distance+variance)*79.152);
          fL.setTargetPosition(distance);
          fR.setTargetPosition(-distance);
          rL.setTargetPosition(distance);
          rR.setTargetPosition(-distance);
          fL.setPower(power);
          fR.setPower(-power);
          rL.setPower(power);
          rR.setPower(-power);
        break;
      }
    }
}
