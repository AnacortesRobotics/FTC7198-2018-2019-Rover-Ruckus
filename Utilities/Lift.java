package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

// This is a class that will control the goBilda linear lift
public class Lift {

    // Define robot object
    RR2Robot r;

    // Define hardware objects
    DcMotor liftMotor;
    Servo liftHook;

    // Constructs a Lift object
    public Lift(RR2Robot _r) {

        // Set robot reference
        r = _r;

        // Map devices to hardware
        liftMotor = r.hardwareMap.dcMotor.get("lift");
        liftHook = r.hardwareMap.servo.get("liftHook");

        // Reset the lift encoder and tell it to use it
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Return the position and hook value for telemetry
    public String toString() {
        return "Position: " + liftMotor.getCurrentPosition() +
            "\n Hooked: " + (liftHook.getPosition() == 0 ? "yes" : "no");
    }

    // Sets the speed of the lift and whether the hook should be attached based on user input
    public void driveLift(double liftPower, boolean hooked) {
        liftMotor.setPower(liftPower);
        if (hooked) {
            liftHook.setPosition(0);
        } else {
            liftHook.setPosition(1);
        }
    }

    // Land the robot
    public void dropLift() {
        int loops = 0;

        // Lift robot to remove hook
        liftMotor.setTargetPosition(-75);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(1);

        // Undo hook
        liftHook.setPosition(1);

        // Wait until hook is undone
        while (liftMotor.isBusy() && !r.linearOpMode.isStopRequested()) {
            loops++;
            r.telemetry.addData("Lift Pos", liftMotor.getCurrentPosition());
            r.telemetry.update();
        }

        // Wait 500 milliseconds
        r.linearOpMode.sleep(500);

        // Lower robot to ground
        liftMotor.setTargetPosition(3150); // Note: 3300 MAX, otherwise will hit lift end
        while (liftMotor.isBusy() && !r.linearOpMode.isStopRequested()) {
            loops++;
        }
    }

    // Lift the robot
    public void raiseLift() {
        int loops = 0;

        // Reset lift motor encoder
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Raise robot off floor
        liftMotor.setTargetPosition(-3200); // Note: 3300 MAX, otherwise will hit lift end
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(1);
        while (liftMotor.isBusy()) {
            loops++;
        }

        // Use hook to prevent slipping
        liftHook.setPosition(0);
    }
}
