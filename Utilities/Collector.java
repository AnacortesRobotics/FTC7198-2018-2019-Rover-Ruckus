package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

// This is a class that will control any collector using a rotation, extension, spinner scheme
public class Collector {

    // Define robot object
    RR2Robot r;

    // Define hardware objects
    DcMotor body, slide;
    Servo spinner;

    // Set constants
    static final double COUNTS_PER_MOTOR_BODY = 383.6; // Specific to goBILDA 5202-0002-0014
    static final double COUNTS_PER_MOTOR_SLIDE = 145.6; // Specific to goBILDA 5202-0002-0005

    // Set fields
    double countsPerDegree = COUNTS_PER_MOTOR_BODY * 24.0 / 360.0; // Multiplies by the external gear ratio, divides to get degrees from a full rotation 
    double countsPerInch = COUNTS_PER_MOTOR_SLIDE / 4; //Divides by spool diameter

    // Constructs a Collector object
    public Collector(RR2Robot _r) {

        // Set robot reference
        r = _r;

        // Map devices to hardware
        body = r.hardwareMap.dcMotor.get("body");
        slide = r.hardwareMap.dcMotor.get("slide");
        spinner = r.hardwareMap.servo.get("spinner");

        // Set motors to brake when not moving
        body.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Resets all motor encoders
        body.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set motors to use encoders
        body.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Returns the current positions of the three motors for telemetry
    public String toString() {
        return "Angle: " + body.getCurrentPosition() +
            "\nSlide: " + slide.getCurrentPosition() +
            "\nSpinner: " + spinner.getPosition();
    }

    // Sets the speed of each motor based on user inputs, applying a slowdown to the goBILDA motors because otherwise they move too fast
    public void driveCollector(double bodyPower, double slidePower, double collectPower) {
        body.setPower(bodyPower * 0.8);
        slide.setPower(slidePower * 0.2);
        spinner.setPosition(collectPower);

    }

    // Moves the motors to a specified position
    public void controlCollector(int targetBodyDegrees, int targetSlideDistance) {
        
        // Resets all motor encoders
        body.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set motors to use encoders
        body.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set target positions
        body.setTargetPosition((int) Math.floor(targetBodyDegrees * countsPerDegree));
        slide.setTargetPosition((int) Math.floor(-targetSlideDistance * countsPerInch));

        // Set motors to run to the specified position
        body.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set the power to move at
        body.setPower(0.4);
        slide.setPower(0.4);

        // Wait until complete and output telemetry
        while ((body.isBusy() || slide.isBusy()) && !r.linearOpMode.isStopRequested()) {
            r.telemetry.addData("Target Pos:", body.getTargetPosition());
            r.telemetry.addData("Current Pos:", body.getCurrentPosition());
            r.telemetry.update();
        }
    }
}