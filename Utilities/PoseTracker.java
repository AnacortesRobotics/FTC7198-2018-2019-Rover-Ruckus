package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

// This is a class that provides useful methods using the REV IMU
public class PoseTracker {
	
	// Define robot object
	RR2Robot r;
	
	// Define IMU object
    BNO055IMU imu;
	
	// Define necessary fields
    Orientation lastAngles;
    double globalAngle, correction;
    
	// Constructs a PoseTracker object
    public PoseTracker(RR2Robot _r) {
		
		// Set robot references
        r = _r;
		
		// Construct a Parameters object and modify from default
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        
		// Map imu to hardware
        imu = r.hardwareMap.get(BNO055IMU.class, "imu");
        
		// Initialize IMU
        imu.initialize(parameters);
        
		// Report when initialization is complete
        r.telemetry.addLine("Initialized");
        r.telemetry.update();
    }
    
	// Reset current tracked angle, and store the current true heading
    public void resetAngle() {
      lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
      globalAngle = 0;
    }
    
	// Calculate and return the current tracked angle using the previous true angle and the current true angle
    public double getAngle() {
      Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
      double deltaAngle = 0;
      if (angles != null) {
        deltaAngle = angles.firstAngle - lastAngles.firstAngle;
      } else {
        r.telemetry.addLine("Err0r");
        r.telemetry.update();
      }
      
      if (deltaAngle < -180) {
        deltaAngle += 360;
      } else if (deltaAngle > 180) {
        deltaAngle -= 360;
      }
      
      globalAngle += deltaAngle;
      
      lastAngles = angles;
      
      return globalAngle;
    }
    
	// Returns the amount to correct a straight movement to avoid slight power imbalance
    public double checkDirection() {
      double correction, angle, gain = .1;
      
      angle = getAngle();
      
      if (angle == 0) {
        correction = 0;
      } else {
        correction = -angle;
      }
      
      correction = correction * gain;
      
      return correction;
    }
}