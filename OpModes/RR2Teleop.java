package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
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
  RR2Robot robot;
  //Add necessary variables
  private double forward;
  private double clockwise;
  private double right;
  private double bodyPower;
  private double slidePower;
  private double spinnerPower;
  private double liftPower;
  private boolean modeFall;
  private boolean activeHook;
  private boolean hookFall;
  private boolean liftFall;
  private int mode = 1;
  private int modeMax = 3;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void init() {
    robot = new RR2Robot(telemetry, hardwareMap);
  }
  
  /**
   * This function is executed when the driver presses the play button.
   */
  @Override
  public void loop() {
    // Gets input, then runs the correct functions.
    getInput();
    robot.chassis.driveMecanum(forward, clockwise, right);
    robot.collector.driveCollector(bodyPower, slidePower, spinnerPower);
    robot.lift.driveLift(liftPower, activeHook);
    telemetry.addLine(robot.lift.toString());
    telemetry.addData("Mode:", mode);
    telemetry.update();
  }

  /**
   * This function takes the inputs from the Gamepads and converts them to variables.
   */
  private void getInput() {
    if(gamepad1.a && !modeFall) {
      if(mode<modeMax) {
        mode++;
      } else {
        mode=0;
      }
      modeFall = true;
    } else if(!gamepad1.a) {
      modeFall = false;
    }
    switch (mode) {
      case 0: getInputReg(); break;
      case 1: getInputForza(); break;
      case 2: getInputP2Rot(); break;
    }
  }
  
  private void getInputReg() {
    forward = -((gamepad1.left_stick_y + gamepad1.right_stick_y) / 2);
    right = gamepad1.right_stick_x;
    clockwise = -((gamepad1.left_stick_y - gamepad1.right_stick_y) / 2);
    bodyPower = -gamepad2.right_stick_y;
    slidePower = gamepad2.left_stick_y;
    //spinnerPower = gamepad2.right_stick_y;
    liftPower = (-gamepad2.left_trigger)+(gamepad2.right_trigger);
    if(gamepad2.a && !hookFall) {
      activeHook = !activeHook;
      hookFall = true;
    } else if(!gamepad2.a) {
      hookFall = false;
    }
  }
  
  private void getInputForza() {
    forward = gamepad1.right_trigger-gamepad1.left_trigger;
    right = gamepad1.right_stick_x;
    clockwise = gamepad1.left_stick_x;
    bodyPower = -gamepad2.right_stick_y;
    slidePower = gamepad2.left_stick_y;
    //spinnerPower = gamepad2.right_stick_y;
    liftPower = (-gamepad2.left_trigger)+(gamepad2.right_trigger);
    if(gamepad2.a && !hookFall) {
      activeHook = !activeHook;
      hookFall = true;
    } else if(!gamepad2.a) {
      hookFall = false;
    }
  }
  
  private void getInputP2Rot() {
    forward = gamepad1.right_trigger-gamepad1.left_trigger;
    right = gamepad1.right_stick_x;
    clockwise = gamepad2.left_stick_x;
    bodyPower = -gamepad2.right_stick_y;
    slidePower = gamepad2.left_stick_y;
    //spinnerPower = gamepad2.right_stick_y;
    liftPower = (-gamepad2.left_trigger)+(gamepad2.right_trigger);
    if(gamepad2.a && !hookFall) {
      activeHook = !activeHook;
      hookFall = true;
    } else if(!gamepad2.a) {
      hookFall = false;
    }
  }
}
