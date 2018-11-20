package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Lift {
    DcMotor lift;
    public Lift(DcMotor liftInput) {
        lift = liftInput;
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    
    public void driveLift(double liftPower) {
        lift.setPower(liftPower);
    }
}
