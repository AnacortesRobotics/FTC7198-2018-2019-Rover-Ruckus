package org.firstinspires.ftc.teamcode.Utilities;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.*;
import org.firstinspires.ftc.teamcode.OpModes.*;

// A class that manages all hardware for the robot in the Rover Ruckus Season
public class RR2Robot {

    // Define the Telemetry and Hardware objects
    Telemetry telemetry;
    HardwareMap hardwareMap;

    // Define the objects for the other Utilities
    public MecanumChassis chassis;
    public Collector collector;
    public Lift lift;
    public VisionProcessor vision;
    public PoseTracker poseTracker;

    // Define other hardware devices that don't have separate Utility classes
    public Servo markerArm;
    public LinearOpMode linearOpMode;
    public DistanceSensor dist;

    // Constructs an RR2Robot object
    public RR2Robot(Telemetry tIn, HardwareMap mapIn, OpMode opModeIn) {

        // Create the object specific variables from the OpMode
        telemetry = tIn;
        hardwareMap = mapIn;
        if (opModeIn instanceof LinearOpMode) {
            linearOpMode = (LinearOpMode) opModeIn;
        }

        // Construct the field objects
        chassis = new MecanumChassis(this);
        collector = new Collector(this);
        lift = new Lift(this);
        vision = new VisionProcessor(this);
        poseTracker = new PoseTracker(this);

        // Map the separate hardware devices
        markerArm = hardwareMap.servo.get("WHY");
        dist = (DistanceSensor) hardwareMap.get("distSensor");
    }

    // Returns the distance from the distance sensor to the nearest obstruction in centimeters
    public double getDist() {
        return dist.getDistance(DistanceUnit.CM);
    }
}