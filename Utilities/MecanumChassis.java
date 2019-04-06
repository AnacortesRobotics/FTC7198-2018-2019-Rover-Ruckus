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

// This is a class that will control any mecanum chassis

public class MecanumChassis {
    DcMotor frontLeft, frontRight, rearLeft, rearRight;
    Telemetry telemetry;
    double frontLeftP, frontRightP, rearLeftP, rearRightP = 0;
    double variance = 2.2;
    RR2Robot r;
    
    public MecanumChassis(RR2Robot _r) {
        r = _r;
        telemetry = r.telemetry;
        //Sets all the motors to be in class variables
        frontLeft = r.hardwareMap.dcMotor.get("leftF");
        frontRight = r.hardwareMap.dcMotor.get("rightF");
        rearLeft = r.hardwareMap.dcMotor.get("leftB");
        rearRight = r.hardwareMap.dcMotor.get("rightB");
        //Reverses the two left motors
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //Tells all motors to reset their encoders
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Tells all the motors to use encoders
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Tells all motors that they should brake when not told to move
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    
    public String toString() {
        return "FrontLeft: " + frontLeft.getPower() + 
        "\nFrontRight: " + frontRight.getPower() +
        "\nRearLeft: " + rearLeft.getPower() +
        "\nRearRight: " + rearRight.getPower();
    }
    
    public void driveMecanum(double forward, double clockwise, double right) {
        double frontLeftP = forward + clockwise - right;
        double frontRightP = forward - (clockwise - right);
        double rearLeftP = forward + (clockwise + right);
        double rearRightP = forward - (clockwise + right);
        double maxSpeed = Math.abs(frontLeftP);
        
        if (Math.abs(frontRightP) > maxSpeed) {
          maxSpeed = Math.abs(frontRightP);
        } else if (Math.abs(rearLeftP) > maxSpeed) {
          maxSpeed = Math.abs(rearLeftP);
        } else if (Math.abs(rearRightP) > maxSpeed) {
          maxSpeed = Math.abs(rearRightP);
        }
        if (maxSpeed > 1) {
          frontLeftP = frontLeftP / maxSpeed;
          frontRightP = frontRightP / maxSpeed;
          rearLeftP = rearLeftP / maxSpeed;
          rearRightP = rearRightP / maxSpeed;
        }
        ((DcMotorEx) frontLeft).setVelocity(300 * frontLeftP, AngleUnit.DEGREES);
        ((DcMotorEx) frontRight).setVelocity(300 * frontRightP, AngleUnit.DEGREES);
        ((DcMotorEx) rearLeft).setVelocity(300 * rearLeftP, AngleUnit.DEGREES);
        ((DcMotorEx) rearRight).setVelocity(300 * rearRightP, AngleUnit.DEGREES);
    }
    
    public void controlMecanum(String type, int distance, double power) {
      frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
      frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
      switch(type) {
        case "forward":
          distance = (int) Math.floor(distance*35.09);
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
          distance = (int) Math.floor(distance*35.09);
          frontLeft.setTargetPosition(-distance);
          frontRight.setTargetPosition(distance);
          rearLeft.setTargetPosition(distance);
          rearRight.setTargetPosition(-distance);
          frontLeft.setPower(-power);
          frontRight.setPower(power);
          rearLeft.setPower(power);
          rearRight.setPower(-power);
        break;
        case "clockwise":
          distance = (int) Math.floor(((distance>0)?distance-variance:distance+variance)*79.152);
          frontLeft.setTargetPosition(distance);
          frontRight.setTargetPosition(-distance);
          rearLeft.setTargetPosition(distance);
          rearRight.setTargetPosition(-distance);
          frontLeft.setPower(power);
          frontRight.setPower(-power);
          rearLeft.setPower(power);
          rearRight.setPower(-power);
        break;
      }
      while((frontLeft.isBusy()||frontRight.isBusy()||rearLeft.isBusy()||rearRight.isBusy())&&!r.linearOpMode.isStopRequested()) {
        telemetry.addData("Type: ", type);
        telemetry.addData("Amount: ", distance);
        telemetry.addData("Distance", r.getDist());
        telemetry.addData("Front Left:", "%d", frontLeft.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", frontRight.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", rearLeft.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rearRight.getCurrentPosition());
        telemetry.update();
      }
    }
    
    public void electricSlide(double dist) {
      while (r.getDist()>dist&&!r.linearOpMode.isStopRequested()) {
        driveMecanum(0,0,-0.7);
      }
      driveMecanum(0,0,0);
    }
    
    public void rotate(double degrees, double power) {
      double leftPower, rightPower;
      frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      
      r.poseTracker.resetAngle();
      
      if (degrees < 0) {
        setPower(power,-power);
        while(r.poseTracker.getAngle() == degrees) {
          telemetry.addData("Angle:", r.poseTracker.getAngle());
          telemetry.update();
        }
        while(r.poseTracker.getAngle() > degrees&&!r.linearOpMode.isStopRequested()) {
          double correction = 1-r.poseTracker.getAngle()/degrees;
          if (correction<0.1) correction = 0.1;
          setPower(power*correction,-power*correction);
          telemetry.addData("Angle:", r.poseTracker.getAngle());
          telemetry.addData("Correction:", correction);
          telemetry.update();
        }
      } else if (degrees > 0){
        setPower(-power,power);
        while(r.poseTracker.getAngle() < degrees&&!r.linearOpMode.isStopRequested()) {
          double correction = 1-r.poseTracker.getAngle()/degrees;
          if (correction<0.1) correction = 0.1;
          setPower(-power*correction,power*correction);
          telemetry.addData("Angle:", r.poseTracker.getAngle());
          telemetry.addData("Correction:", correction);
          telemetry.update();
        }
      }
      
      setPower(0,0);
      
      r.poseTracker.resetAngle();
    }
    
    public double getPower(String side) {
      if (side == "left") {
        return frontLeft.getPower();
      } else {
        return frontRight.getPower();
      }
    }
    
    public String setPower(double left, double right) {
      frontLeft.setPower(left);
      frontRight.setPower(right);
      rearLeft.setPower(left);
      rearRight.setPower(right);
      return "Left: " + left + " Right: " + right;
    }
}
