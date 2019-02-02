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

public abstract class RR2Auto2 extends LinearOpMode {
  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
   //AndyMark: 1680
  static final int COUNTS_PER_MOTOR_REV = 1680; 
  private DcMotor leftF;
  private DcMotor rightF;
  private DcMotor leftB;
  private DcMotor rightB;
  private DcMotor lift;
  private MecanumChassis chassis;
  private Servo mD;
  
  NormalizedColorSensor colorSensor;
  DistanceSensor distanceSensor;
  
  VisionProcessor vision;
  String sample = "none";
  
  public void initRobot() {
    leftF = hardwareMap.dcMotor.get("leftF");
    rightF = hardwareMap.dcMotor.get("rightF");
    leftB = hardwareMap.dcMotor.get("leftB");
    rightB = hardwareMap.dcMotor.get("rightB");
    chassis = new MecanumChassis(leftF, rightF, leftB, rightB);
    lift = hardwareMap.dcMotor.get("lift");
    mD = hardwareMap.servo.get("WHY");
    
    vision = new VisionProcessor();
    vision.init(telemetry, hardwareMap);
    vision.activate();
    
    lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    
  }
  
  public void samplePath() {
    chassis.controlMecanum("forward", 30, 0.7);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    if(sample=="left") {
      chassis.controlMecanum("right", -45, -0.5);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    } else if (sample=="right") {
      chassis.controlMecanum("right", 50, 0.5);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    }
    chassis.controlMecanum("forward", 30, 0.7);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
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
    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    lift.setTargetPosition(28500);
    lift.setPower(1);
    while(lift.isBusy()) {
      telemetry.addData("Lift:", lift.getCurrentPosition());
      telemetry.update();
    }
    
    //lift.setPower(0);
    telemetry.addData("dropLift", "End");
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  public void undoLatch() {
    telemetry.addData("undoLatch", "Start");
    telemetry.update();
    chassis.controlMecanum("right", -16, -0.5);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    telemetry.addData("undoLatch", "End");
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  public void path1() {
    chassis.controlMecanum("forward", 50, 0.7);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
  }

  /**
   * Describe this function...
   */
  public void sampleUnobtainium() {
    telemetry.addData("sample", "Start");
    telemetry.update();
    sleep(3000);
    telemetry.addData("sample", "End");
    telemetry.update();
  }
  
  /**
   * Describe this function...
   */
  public void path2NoSample() {
    telemetry.addData("path2", "Start");
    telemetry.update();
    chassis.controlMecanum("forward", 60, 0.7);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    mD.setPosition(0.7);
    sleep(1000);
    chassis.controlMecanum("clockwise", -37, -0.5);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    chassis.controlMecanum("forward", 300, 0.8);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    telemetry.addData("path2", "End");
    telemetry.update();
  }
  
  public void path2Sample() {
    telemetry.addData("path2", "Start");
    telemetry.update();
    if(sample=="left"){
      chassis.controlMecanum("clockwise", 16, 0.5);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    }else if(sample=="right"){
      chassis.controlMecanum("forward", 60, 0.7);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
      chassis.controlMecanum("clockwise", -16, -0.5);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    }
    if (sample=="right") {
      chassis.controlMecanum("forward", 30, 0.7);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    } else {
      chassis.controlMecanum("forward", 85, 0.7);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    }
    mD.setPosition(0.8);
    sleep(1000);
    if (sample=="right") {
      chassis.controlMecanum("clockwise", -25, -0.5);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    } else if(sample=="center"||sample=="TensorError") {
      chassis.controlMecanum("clockwise", 12, 0.5);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    }
    if (sample=="right") {
      chassis.controlMecanum("right", 30, 0.8);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    } else {
      chassis.controlMecanum("right", -30, -0.8);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    }
    if (sample=="right") {
      chassis.controlMecanum("forward", 280, 0.7);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    } else {
      chassis.controlMecanum("forward", -280, -0.7);
      while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
        telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
        telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
        telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
        telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
        telemetry.update();
      }
    }
    telemetry.addData("path2", "End");
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  public void depositMarker() {
    telemetry.addData("depositMarker", "Start");
    telemetry.update();
    mD.setPosition(0.8);
    sleep(1000);
    telemetry.addData("depositMarker", "End");
    telemetry.update();
  }
}
