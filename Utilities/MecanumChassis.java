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
    BNO055IMU imu;
    Orientation lastAngles;
    Telemetry telemetry;
    double frontLeftP, frontRightP, rearLeftP, rearRightP = 0;
    double variance = 2.2;
    double globalAngle, correction;
    
    public MecanumChassis(RR2Robot r) {
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
        
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        
        imu = r.hardwareMap.get(BNO055IMU.class, "imu");
        
        imu.initialize(parameters);
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
      while(frontLeft.isBusy()||frontRight.isBusy()||rearLeft.isBusy()||rearRight.isBusy()) {
        telemetry.addData("Type: ", type);
        telemetry.addData("Amount: ", distance);
        telemetry.addData("Front Left:", "%d", frontLeft.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", frontRight.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", rearLeft.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rearRight.getCurrentPosition());
        telemetry.update();
      }
    }
    
    public void resetAngle() {
      lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
      globalAngle = 0;
    }
    
    public double getAngle() {
      Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
      double deltaAngle = angles.firstAngle - lastAngles.firstAngle;
      
      if (deltaAngle < -180) {
        deltaAngle += 360;
      } else if (deltaAngle > 180) {
        deltaAngle -= 360;
      }
      
      globalAngle += deltaAngle;
      
      lastAngles = angles;
      
      return globalAngle;
    }
    
    private double checkDirection() {
      double correction, angle, gain = .10;
      
      angle = getAngle();
      
      if (angle == 0) {
        correction = 0;
      } else {
        correction = -angle;
      }
      
      correction = correction * gain;
      
      return correction;
    }
    
    public void rotate(double degrees, double power) {
      double leftPower, rightPower;
      frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      
      resetAngle();
      if (degrees < 0) {
        leftPower = power;
        rightPower = -power;
      } else if (degrees > 0) {
        leftPower = -power;
        rightPower = power;
      } else return;
      
      frontLeft.setPower(leftPower);
      frontRight.setPower(rightPower);
      rearLeft.setPower(leftPower);
      rearRight.setPower(rightPower);
    }
    
    public void endRotate() {
      frontLeft.setPower(0);
      frontRight.setPower(0);
      rearLeft.setPower(0);
      rearRight.setPower(0);
      
      resetAngle();
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
