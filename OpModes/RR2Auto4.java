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

public abstract class RR2Auto4 extends LinearOpMode {
  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
   //AndyMark: 1680
  static final int COUNTS_PER_MOTOR_REV = 1680; 
  private RR2Robot robot;
  
  String sample = "none";
  
  public void initRobot() {
    robot = new RR2Robot(telemetry, hardwareMap, this);
    telemetry.setAutoClear(false);
    robot.vision.init(telemetry, hardwareMap);
    robot.vision.activate();
  }
  
  public void samplePath() {
    robot.chassis.controlMecanum("forward", 30, 0.7);
    if(sample=="left") {
      robot.chassis.rotate(30,0.7);
    } else if (sample=="right") {
      robot.chassis.rotate(-45,0.7);
    } else {
      robot.chassis.controlMecanum("right", 13, 0.5);
    }
    robot.chassis.controlMecanum("forward", 30, 0.7);
  }

  public void sampleAuto(boolean toSample) {
    if(toSample) {
      sample = robot.vision.sample();
    }
    robot.vision.deactivate();
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
    robot.lift.dropLift();
    robot.poseTracker.resetAngle();
    robot.collector.controlCollector(25,0);
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
    robot.chassis.rotate(135,0.8);
    robot.chassis.controlMecanum("forward", 180, 0.8);
    robot.collector.controlCollector(100,15);
    telemetry.addData("path2", "End");
    telemetry.update();
  }
  
  public void path2Sample() {
    telemetry.addData("path2", "Start");
    telemetry.update();
    if (sample=="right") {
      robot.chassis.controlMecanum("forward", 20, 0.7);
      robot.chassis.electricSlide(10);
      robot.markerArm.setPosition(0.9);
      sleep(1000);
      robot.markerArm.setPosition(0.5);
      robot.chassis.controlMecanum("forward", -140, -0.7);
      robot.chassis.rotate(-180,0.8);
    } else if(sample=="center"||sample=="TensorError") {
      robot.chassis.controlMecanum("forward", 60, 0.7);
      robot.chassis.rotate(-45,0.8);
      robot.chassis.electricSlide(10);
      robot.markerArm.setPosition(0.9);
      sleep(1000);
      robot.markerArm.setPosition(0.5);
      robot.chassis.controlMecanum("forward", -140, -0.7);
      robot.chassis.rotate(-180,0.8);
    } else {
      robot.chassis.rotate(-45,0.8);
      robot.chassis.controlMecanum("forward", 55, 0.6);
      robot.markerArm.setPosition(0.9);
      sleep(1000);
      robot.markerArm.setPosition(0.5);
      robot.chassis.rotate(-45,0.8);
      robot.chassis.controlMecanum("forward", -130, -0.7);
      robot.chassis.rotate(180,0.8);
    }
    robot.collector.controlCollector(100,10);
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
    robot.chassis.controlMecanum("forward", 10, 0.5);
    robot.collector.controlCollector(100,0);
    telemetry.addData("craterPath", "End");
    telemetry.update();
  }
  
  public void testFunction() {
    telemetry.setAutoClear(true);
    robot.chassis.rotate(90,0.7);
    robot.chassis.rotate(-90,0.7);
    sleep(500);
  }
}
