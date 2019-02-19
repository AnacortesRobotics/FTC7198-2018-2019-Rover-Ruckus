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

@Autonomous(name = "DepotSide", group = "RR2")
public class DepotSide extends RR2Auto3 {
 
  @Override
  public void runOpMode() {
    initRobot();
    
    waitForStart();
    
    //if (opModeIsActive()) {
      // Put run blocks here.
      //mD.setPosition(1);
      sampleAuto(false);
      dropLift();
      undoLatch();
      path1();
      //sample();
      path2NoSample();
      //path2();
      //sleep(5000);
    //}
  }
}

