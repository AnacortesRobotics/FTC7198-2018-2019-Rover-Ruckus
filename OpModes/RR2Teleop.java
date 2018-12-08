package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;
import org.firstinspires.ftc.teamcode.Utilities.*;

@TeleOp(name = "RR2Teleop(Java)", group = "")
public class RR2Teleop extends OpMode {
  //Create motor objects
  private DcMotor leftF;
  private DcMotor rightF;
  private DcMotor leftB;
  private DcMotor rightB;
  private DcMotor body;
  private DcMotor slide;
  private DcMotor spinner;
  private DcMotor liftMotor;
  //Create subsection objects
  private MecanumChassis chassis;
  private Collector collector;
  private Lift lift;
  //Add necessary variables
  private double forward;
  private double clockwise;
  private double right;
  private double bodyPower;
  private double slidePower;
  private double spinnerPower;
  private double liftPower;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void init() {
    //Map motor objects to config profile
    leftF = hardwareMap.dcMotor.get("leftF");
    rightF = hardwareMap.dcMotor.get("rightF");
    leftB = hardwareMap.dcMotor.get("leftB");
    rightB = hardwareMap.dcMotor.get("rightB");
    body = hardwareMap.dcMotor.get("body");
    slide = hardwareMap.dcMotor.get("slide");
    spinner = hardwareMap.dcMotor.get("spinner");
    liftMotor = hardwareMap.dcMotor.get("lift");
    //Set up the subsection objects
    chassis = new MecanumChassis(leftF, rightF, leftB, rightB);
    collector = new Collector(body, slide, spinner);
    lift = new Lift(liftMotor);
  }
  
  /**
   * This function is executed when the driver presses the play button.
   */
  @Override
  public void loop() {
    // Gets input, then runs the correct functions.
    getInput();
    chassis.driveMecanum(forward, clockwise, right);
    collector.driveCollector(bodyPower, slidePower, spinnerPower);
    lift.driveLift(liftPower);
    telemetry.addData("Angle:", liftMotor.getCurrentPosition());
    telemetry.update();
  }

  /**
   * This function takes the inputs from the Gamepads and converts them to variables.
   */
  private void getInput() {
    forward = -((gamepad1.left_stick_y + gamepad1.right_stick_y) / 2);
    right = gamepad1.right_stick_x;
    clockwise = -((gamepad1.left_stick_y - gamepad1.right_stick_y) / 2);
    bodyPower = (-gamepad2.left_trigger)+(gamepad2.right_trigger);
    slidePower = gamepad2.left_stick_y;
    spinnerPower = gamepad2.right_stick_y;
    liftPower = (-gamepad1.left_trigger)+(gamepad1.right_trigger);
  }
}
