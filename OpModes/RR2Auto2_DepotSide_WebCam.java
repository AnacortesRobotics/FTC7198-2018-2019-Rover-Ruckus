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

@Autonomous(name = "RR2Auto2_DepotSide_WebCam", group = "RR2")
public class RR2Auto2_DepotSide_WebCam extends LinearOpMode {
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
  
  @Override
  public void runOpMode() {
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
    sleep(4000);
    sample = vision.sample();
    vision.deactivate();
    
    lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    
    waitForStart();
    
    //if (opModeIsActive()) {
      // Put run blocks here.
      telemetry.addData("Position", sample);
      telemetry.update();
      mD.setPosition(1);
      dropLift();
      undoLatch();
      samplePath();
      //path1();
      depositMarker();
      path2Dif();
      sleep(5000);
    //}
  }
  
  private void samplePath() {
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

  /**
   * Describe this function...
   */
  private void dropLift() {
    telemetry.addData("dropLift", "Start");
    telemetry.update();
    lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    lift.setTargetPosition(-15000);
    lift.setPower(-0.9);
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
  private void undoLatch() {
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
  private void path1() {
    chassis.controlMecanum("forward", 100, 0.7);
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
  private void sampleUnobtainium() {
    telemetry.addData("sample", "Start");
    telemetry.update();
    sleep(3000);
    telemetry.addData("sample", "End");
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  private void sample() {
    if (getColor()) {
      telemetry.addLine("Hit");
    } else {
      telemetry.addLine("Move on");
    }
    telemetry.update();
    sleep(3000);
  }

  /**
   * Describe this function...
   */
  private void moveNext() {
  }

  /**
   * Describe this function...
   */
  private void path2Same() {
    telemetry.addData("path2", "Start");
    telemetry.update();
    chassis.controlMecanum("clockwise", -16, -0.5);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    mD.setPosition(0.1);
    chassis.controlMecanum("forward", 10, 0.7);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    chassis.controlMecanum("forward", -300, -0.7);
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
  
  private void path2Dif() {
    telemetry.addData("path2", "Start");
    telemetry.update();
    chassis.controlMecanum("clockwise", 16, 0.5);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    mD.setPosition(0.1);
    chassis.controlMecanum("forward", 10, 0.7);
    while(leftF.isBusy()||rightF.isBusy()||leftB.isBusy()||rightB.isBusy()) {
      telemetry.addData("Front Left:", "%d", leftF.getCurrentPosition());
      telemetry.addData("Front Right:", "%d", rightF.getCurrentPosition());
      telemetry.addData("Rear Left:", "%d", leftB.getCurrentPosition());
      telemetry.addData("Rear Right:", "%d", rightB.getCurrentPosition());
      telemetry.update();
    }
    chassis.controlMecanum("forward", -300, -0.7);
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

  /**
   * Describe this function...
   */
  private void depositMarker() {
    telemetry.addData("depositMarker", "Start");
    telemetry.update();
    mD.setPosition(0.8);
    sleep(1000);
    telemetry.addData("depositMarker", "End");
    telemetry.update();
  }
  
  protected boolean getColor() {

    // values is a reference to the hsvValues array.
    float[] hsvValues = new float[3];
    final float values[] = hsvValues;

    // bPrevState and bCurrState keep track of the previous and current state of the button
    boolean bPrevState = false;
    boolean bCurrState = false;
    
    //Declare string "result." This is a shorthand way of determining what the color is
    String result;

    // Get a reference to our sensor object.
    colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
    distanceSensor = hardwareMap.get(DistanceSensor.class, "colorSensor");

    // If possible, turn the light on in the beginning (it might already be on anyway,
    // we just make sure it is if we can).
    if (colorSensor instanceof SwitchableLight) {
      ((SwitchableLight)colorSensor).enableLight(true);
    }

    // Read the sensor
    NormalizedRGBA colors = colorSensor.getNormalizedColors();
    int color = colors.toColor();

    //The RGB color sensor values cap out ~0.23, but we use 0.2 for the sake generalization
    // Balance the colors. The values returned by getColors() are normalized relative to the
    // maximum possible values that the sensor can measure. For example, a sensor might in a
    // particular configuration be able to internally measure color intensity in a range of
    // [0, 10240]. In such a case, the values returned by getColors() will be divided by 10240
    // so as to return a value it the range [0,1]. However, and this is the point, even so, the
    // values we see here may not get close to 1.0 in, e.g., low light conditions where the
    // sensor measurements don't approach their maximum limit. In such situations, the *relative*
    // intensities of the colors are likely what is most interesting. Here, for example, we boost
    // the signal on the colors while maintaining their relative balance so as to give more
    // vibrant visual feedback on the robot controller visual display.
    float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
    colors.red   /= max;
    colors.green /= max;
    colors.blue  /= max;
    color = colors.toColor();
    
    if(colors.red >= 0.2 && colors.blue >= 0.2 && colors.green >= 0.2){
      result = "You have the color white!";
    } else if (colors.red >= 0.2 && colors.green >= 0.2){
      result = "You have the color yellow!";
    } else if (colors.blue >= 0.2){
      result = "You have the color blue!";
    } else if (colors.red >= 0.2){
      result = "You have the color red!";
    } else {
      result = "Negatory, commander!";
    } 
    
    if (result=="You have the color yellow!") {
      return true;
    } else {
      return false;
    }
  }
}
