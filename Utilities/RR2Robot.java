package org.firstinspires.ftc.teamcode.Utilities;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class RR2Robot {
    Telemetry telemetry;
    HardwareMap hardwareMap;
    public MecanumChassis chassis;
    public Collector collector;
    public Lift lift;
    
    public Servo markerArm;
    
    public RR2Robot(Telemetry tIn, HardwareMap mapIn) {
        telemetry = tIn;
        hardwareMap = mapIn;
        chassis = new MecanumChassis(this);
        collector = new Collector(this);
        lift = new Lift(this);
        
        markerArm = hardwareMap.servo.get("WHY");
    }
}
