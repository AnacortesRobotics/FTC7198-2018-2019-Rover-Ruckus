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

/**
 * Abstract auto class version 3
 */
public abstract class RR2Auto3 extends LinearOpMode {

  // Define robot object
  private RR2Robot robot;
  
  // Define sample string
  String sample = "none";
  
  // Initialize robot object for Auto
  public void initRobot() {
    robot = new RR2Robot(telemetry, hardwareMap, this);
    telemetry.setAutoClear(false);
    robot.vision.init(telemetry, hardwareMap);
    robot.vision.activate();
  }
  
  // Move from lander to sample field, targeting the identified mineral
  public void samplePath() {
    robot.chassis.controlMecanum("forward", 30, 0.7);
    if(sample=="left") {
      robot.chassis.controlMecanum("right", -37, -0.5);
    } else if (sample=="right") {
      robot.chassis.controlMecanum("right", 50, 0.7);
    } else {
      robot.chassis.controlMecanum("right", 13, 0.5);
    }
    robot.chassis.controlMecanum("forward", 30, 0.7);
  }

  // Use the vision robot object to sample and display the location
  public void sampleAuto(boolean toSample) {
    if(toSample) {
      sample = robot.vision.sample();
    }
    robot.vision.deactivate();
    telemetry.addData("Sample side",sample);
    telemetry.update();
    sleep(2000);
  }
  
  // Lowers robot from lander, moves collector out to improve weight distribution for Mecanum accuracy
  public void dropLift() {
    telemetry.addData("dropLift", "Start");
    telemetry.update();
    robot.lift.dropLift();
    robot.poseTracker.resetAngle();
    robot.collector.controlCollector(25,0);
    telemetry.addData("dropLift", "End");
    telemetry.update();
  }

  // Slide the lift out of the lander mount
  public void undoLatch() {
    telemetry.addData("undoLatch", "Start");
    telemetry.update();
    robot.chassis.controlMecanum("right", -16, -0.5);
    telemetry.addData("undoLatch", "End");
    telemetry.update();
  }

  // Moves the robot forward a little bit
  public void path1() {
    robot.chassis.controlMecanum("forward", 50, 0.7);
  }
  
  // Deposits the team marker and moves to opposing lander without sampling
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
  
  // Deposits the team marker, samples, and moves to opposing lander
  public void path2Sample() {
    telemetry.addData("path2", "Start");
    telemetry.update();
    if (sample=="right") {
      robot.chassis.controlMecanum("forward", 60, 0.7);
      robot.chassis.rotate(45,0.8);
      robot.chassis.controlMecanum("forward", 30, 0.7);
      robot.markerArm.setPosition(0.9);
      sleep(1000);
      robot.markerArm.setPosition(0.5);
      robot.chassis.rotate(90,0.8);
      robot.chassis.controlMecanum("right", 60, 0.8);
      robot.chassis.controlMecanum("forward", 160, 0.7);
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
      robot.chassis.controlMecanum("forward", 80, 0.6);
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

  // Deposits Team Marker
  public void depositMarker() {
    telemetry.addData("depositMarker", "Start");
    telemetry.update();
    robot.markerArm.setPosition(0.8);
    sleep(1000);
    telemetry.addData("depositMarker", "End");
    telemetry.update();
  }
  
  // Moves to the crater and parks
  public void craterPath() {
    telemetry.addData("craterPath", "Start");
    telemetry.update();
    robot.chassis.controlMecanum("forward", 10, 0.5);
    robot.collector.controlCollector(100,0);
    telemetry.addData("craterPath", "End");
    telemetry.update();
  }
  
  // Used for testing commands without running the entire Auto, See TestAuto.java
  public void testFunction() {
    telemetry.setAutoClear(true);
    robot.chassis.rotate(90,0.7);
    robot.chassis.rotate(-90,0.7);
    sleep(500);
  }
}
