package org.firstinspires.ftc.teamcode.Utilities;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.teamcode.OpModes.*;


public class RR2Robot {
    Telemetry telemetry;
    HardwareMap hardwareMap;
    public MecanumChassis chassis;
    public Collector collector;
    public Lift lift;
    public VisionProcessor vision;
    public Servo markerArm;
    public PoseTracker poseTracker;
    public LinearOpMode linearOpMode;
    public DistanceSensor dist;
    
    public RR2Robot(Telemetry tIn, HardwareMap mapIn, OpMode opModeIn) {
        telemetry = tIn;
        hardwareMap = mapIn;
        chassis = new MecanumChassis(this);
        collector = new Collector(this);
        lift = new Lift(this);
        vision = new VisionProcessor();
        poseTracker = new PoseTracker(this);
        markerArm = hardwareMap.servo.get("WHY");
        dist = (DistanceSensor)hardwareMap.get("distSensor");
        if (opModeIn instanceof LinearOpMode) {
            linearOpMode = (LinearOpMode) opModeIn;
        }
    }
    
    public double getDist() {
        return dist.getDistance(DistanceUnit.CM);
    }
}
