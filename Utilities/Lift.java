package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Lift {
    DcMotor liftMotor;
    Servo liftHook;
    RR2Robot r;
    public Lift(RR2Robot _r) {
        r = _r;
        liftMotor = r.hardwareMap.dcMotor.get("lift");
        liftHook = r.hardwareMap.servo.get("liftHook");
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    
    public String toString() {
        return "Position: " + liftMotor.getCurrentPosition() + 
        "\n Hooked: " + (liftHook.getPosition()==0?"yes":"no");
    }
    
    public void driveLift(double liftPower, boolean hooked) {
        liftMotor.setPower(liftPower);
        if(hooked) {
            liftHook.setPosition(0);
        } else {
            liftHook.setPosition(1);
        }
    }
    
    public void dropLift() {
        //3300
        int loops = 0;
        liftMotor.setTargetPosition(-150);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(1);
        liftHook.setPosition(1);
        while(liftMotor.isBusy()&&!r.linearOpMode.isStopRequested()) {
            loops++;
            r.telemetry.addData("Lift Pos", liftMotor.getCurrentPosition());
            r.telemetry.update();
        }
        r.linearOpMode.sleep(500);
        liftMotor.setTargetPosition(3150);
        while(liftMotor.isBusy()) {
            loops++;
        }
    }
    
    public void raiseLift() {
        //3300
        int loops = 0;
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setTargetPosition(-3200);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0.7);
        while(liftMotor.isBusy()) {
            loops++;
        }
        liftHook.setPosition(0);
    }
}
