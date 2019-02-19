package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Collector {
    DcMotor body, slide;
    static final double COUNTS_PER_MOTOR_BODY = 383.6;
    //Plus 25:1 worm drive
    static final double COUNTS_PER_MOTOR_SLIDE = 145.6;
    double countsPerDegree = COUNTS_PER_MOTOR_BODY*24.0/360.0;
    
    Telemetry telemetry;
    
    public Collector(RR2Robot r) {
        body = r.hardwareMap.dcMotor.get("body");
        slide = r.hardwareMap.dcMotor.get("slide");
        //Tell motors to brake when not moving
        body.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //Tell motors to use encoders
        body.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Tell motors to use encoders
        body.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        telemetry = r.telemetry;
    }
    
    public String toString() {
        return "Angle: " + body.getCurrentPosition() + 
        "\nSlide: " + slide.getCurrentPosition();
    }
    
    public void driveCollector(double bodyPower, double slidePower, double collectPower) {
        body.setPower(bodyPower*0.8);
        slide.setPower(slidePower*0.3);
    }
    
    public void controlCollector(int bodyDegrees, int slideDistance) {
        body.setTargetPosition((int)Math.floor(bodyDegrees*countsPerDegree));
        telemetry.addData("Target Pos:", body.getTargetPosition());
        telemetry.update();
        body.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        body.setPower(0.5);
        while(body.isBusy()) {
            telemetry.addData("Target Pos:", body.getTargetPosition());
            telemetry.addData("Current Pos:", body.getCurrentPosition());
            telemetry.update();
        }
    }
}
