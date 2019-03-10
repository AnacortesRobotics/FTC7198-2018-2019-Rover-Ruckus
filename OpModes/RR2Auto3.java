package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Utilities.*;

public abstract class RR2Auto3 extends LinearOpMode {
  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
   //AndyMark: 1680
  static final int COUNTS_PER_MOTOR_REV = 1680; 
  private RR2Robot robot;
  
  VisionProcessor vision;
  String sample = "none";
  
  public void initRobot() {
    robot = new RR2Robot(telemetry, hardwareMap);
    telemetry.setAutoClear(false);
    vision = new VisionProcessor();
    vision.init(telemetry, hardwareMap);
    vision.activate();
  }
  
  public void samplePath() {
    robot.chassis.controlMecanum("forward", 30, 0.7);
    if(sample=="left") {
      robot.chassis.controlMecanum("right", -37, -0.5);
    } else if (sample=="right") {
      robot.chassis.controlMecanum("right", 50, 0.5);
    }
    robot.chassis.controlMecanum("forward", 30, 0.7);
  }

  public void sampleAuto(boolean toSample) {
    if(toSample) {
      sample = vision.sample();
    }
    vision.deactivate();
    telemetry.addData("Sample side",sample);
    telemetry.update();
    sleep(2000);
  }
  /**
   * Describe this function...
   */
  public void dropLift() {
    telemetry.addData("dropLift", "Start");
    telemetry.update();
    robot.chassis.resetAngle();
    robot.lift.dropLift();
    telemetry.addData("dropLift", "End");
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  public void undoLatch() {
    telemetry.addData("undoLatch", "Start");
    telemetry.update();
    robot.chassis.controlMecanum("right", -16, -0.5);
    double angle = robot.chassis.getAngle();
    rotate(-angle,0.8);
    telemetry.addData("undoLatch", "End");
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  public void path1() {
    robot.chassis.controlMecanum("forward", 50, 0.7);
  }
  
  /**
   * Describe this function...
   */
  public void path2NoSample() {
    telemetry.addData("path2", "Start");
    telemetry.update();
    robot.chassis.controlMecanum("forward", 60, 0.7);
    robot.markerArm.setPosition(0.7);
    sleep(1000);
    rotate(135,0.8);
    robot.chassis.controlMecanum("forward", 180, 0.8);
    robot.collector.controlCollector(60,0);
    robot.collector.controlCollector(120,15);
    telemetry.addData("path2", "End");
    telemetry.update();
  }
  
  public void path2Sample() {
    telemetry.addData("path2", "Start");
    telemetry.update();
    if (sample=="right") {
      robot.chassis.controlMecanum("forward", 60, 0.7);
      rotate(45,0.8);
      robot.chassis.controlMecanum("forward", 30, 0.7);
    } else if (sample == "left"){
      robot.chassis.controlMecanum("forward", 60, 0.7);
    } else {
      robot.chassis.controlMecanum("forward", 85, 0.7);
    }
    robot.markerArm.setPosition(0.9);
    sleep(1000);
    robot.markerArm.setPosition(0.5);
    if (sample=="right") {
      rotate(90,0.8);
      robot.chassis.controlMecanum("right", 38, 0.8);
      robot.chassis.controlMecanum("forward", 160, 0.7);
    } else if(sample=="center"||sample=="TensorError") {
      rotate(-45,0.8);
      robot.chassis.controlMecanum("right", -5, -0.8);
      robot.chassis.controlMecanum("forward", -140, -0.7);
      rotate(-180,0.8);
    } else {
      rotate(-45,0.8);
      robot.chassis.controlMecanum("forward", -130, -0.7);
      robot.chassis.controlMecanum("clockwise", 60, 0.5);
    }
    robot.collector.controlCollector(60,0);
    robot.collector.controlCollector(120,15);
    telemetry.addData("path2", "End");
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  public void depositMarker() {
    telemetry.addData("depositMarker", "Start");
    telemetry.update();
    robot.markerArm.setPosition(0.8);
    sleep(1000);
    telemetry.addData("depositMarker", "End");
    telemetry.update();
  }
  
  /**
   * Describe this function...
   */
  public void craterPath() {
    telemetry.addData("craterPath", "Start");
    telemetry.update();
    robot.collector.controlCollector(60,0);
    robot.collector.controlCollector(120,15);
    telemetry.addData("craterPath", "End");
    telemetry.update();
  }
  
  public void testFunction() {
    robot.collector.controlCollector(-2,0);
  }
  
  //Fails at low power levels
  public void rotate(double target, double power) {
    robot.chassis.rotate(target, power);
    if (target < 0) {
      while(opModeIsActive() && robot.chassis.getAngle() == target) {
        telemetry.addData("Angle:", robot.chassis.getAngle());
        telemetry.update();
      }
      while(opModeIsActive() && robot.chassis.getAngle() > target) {
        double correction = 1-robot.chassis.getAngle()/target;
        if (correction<0.1) correction = 0.1;
        robot.chassis.setPower(power*correction,-power*correction);
        telemetry.addData("Angle:", robot.chassis.getAngle());
        telemetry.addData("Correction:", correction);
        telemetry.update();
      }
    } else {
      while(opModeIsActive() && robot.chassis.getAngle() < target) {
        double correction = 1-robot.chassis.getAngle()/target;
        if (correction<0.1) correction = 0.1;
        robot.chassis.setPower(-power*correction,power*correction);
        telemetry.addData("Angle:", robot.chassis.getAngle());
        telemetry.addData("Correction:", correction);
        telemetry.update();
      }
    }
    robot.chassis.endRotate();
  }
}
