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

  private DcMotor leftF;
  private DcMotor rightF;
  private DcMotor leftB;
  private DcMotor rightB;
  private DcMotor lift;
  private MecanumChassis chassis;
  private AndroidTextToSpeech androidTextToSpeech;
  
  private double forward;
  private double clockwise;
  private double right;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void init() {
    leftF = hardwareMap.dcMotor.get("leftF");
    rightF = hardwareMap.dcMotor.get("rightF");
    leftB = hardwareMap.dcMotor.get("leftB");
    rightB = hardwareMap.dcMotor.get("rightB");
    chassis = new MecanumChassis(leftF, rightF, leftB, rightB);
    lift = hardwareMap.dcMotor.get("lift");
    androidTextToSpeech = new AndroidTextToSpeech();
    // Put initialization blocks here.
    androidTextToSpeech.initialize();
    androidTextToSpeech.setLanguageAndCountry("en", "US");
    lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
  }

  @Override
  public void loop() {
    // Put loop blocks here.
    getInput();
    chassis.driveMecanum(forward, clockwise, right);
    moveLift();
    telemetry.addData("Angle:", lift.getCurrentPosition());
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  private void getInput() {
    forward = -((gamepad1.left_stick_y + gamepad1.right_stick_y) / 2);
    right = gamepad1.right_stick_x;
    clockwise = -((gamepad1.left_stick_y - gamepad1.right_stick_y) / 2);
  }

  /**
   * Describe this function...
   */
  private void moveLift() {
    lift.setPower((-gamepad1.left_trigger)+(gamepad1.right_trigger));
  }

  /**
   * Describe this function...
   */
  private void moveLatch() {
  }

  /**
   * Describe this function...
   */
  private void intake() {
  }

  /**
   * Describe this function...
   */
  private void deposit() {
  }
}
