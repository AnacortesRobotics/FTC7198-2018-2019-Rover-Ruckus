package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Collector {
    DcMotor body;
    DcMotor slide;
    DcMotor spinner;
    
    public Collector(DcMotor bodyInput, DcMotor slideInput, DcMotor spinnerInput) {
        body = bodyInput;
        slide = slideInput;
        spinner = spinnerInput;
    }
    
    public void driveCollector(double bodyPower, double slidePower, double collectPower) {
        body.setPower(bodyPower);
        slide.setPower(slidePower);
        spinner.setPower(collectPower);
        
    }
    
    public void controlCollector(int bodyAngle, int slideDistance, boolean collect) {
        
    }
}
