package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

// This is a class that uses a camera to find target elements based on the Rover Ruckus game
public class VisionProcessor {
	
	// Define class constants
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AYtXqHz/////AAABmU3In0fAY0/fn8QX5iqlqwNJT9qVS7+g0Jh8f32jZWG0vH9a0eAOw0L1KQosfbN20wfjaVq/1eALYOkGFmXFGFxKPVcuh5sT0VkVjhdxO8wld1Z+kBMrNNIEAF2h4bYL9y6WIeTOPM1vrhgX65NwMVSsDgjQEA2YhSSnCxjEcIYOmxJWwiErP26uPxRaWP2OtHtPaxsq0Ru2a4yLJZ3nEYf7btJdIzT1QUIwDlUW+KCNfTOp4kZJQv4KSE4oKi0UDsrUqzr4iDcph9MS3Y5EYTnZP3pI6vKw1EgpNF0P3rwO3fyY4HY1630ge6gB+vwEbB8pTt0uLt9kvdckpxJR705KFL1aso8GwgCtDER53SSY";
	
	// Define robot objects
	private Telemetry telemetry;
    private HardwareMap hardwareMap;
	
	// Define necessary fields
    private String sample = "failedInit";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;
    
	// Constructs a VisionProcessor object
    public void VisionProcessor() {};
	
	// Initialize Vuforia and TensorFlow
    public void init(Telemetry tInput, HardwareMap hwMap) {
        telemetry = tInput;
        hardwareMap = hwMap;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
		
		// Check to make sure a TensorFlow analysis is possible, then initialize it if it is
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
            sample = "none";
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        // Wait for the game to begin
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        sample = "TensorError";
    }
    
	// Activate the TensorFlow analysis
    public void activate() {
        if (tfod != null) {
                tfod.activate();
        }
    }
    
	// Sample the current camera frames with TensorFlow and return which side the gold cube is on
    public String sample () {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
              telemetry.addData("# Object Detected", updatedRecognitions.size());
              if (updatedRecognitions.size() == 2) {
                int goldMineralX = -1;
                int silverMineral1X = -1;
                int silverMineral2X = -1;
                for (Recognition recognition : updatedRecognitions) {
                  if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    goldMineralX = (int) recognition.getLeft();
                  } else if (silverMineral1X == -1) {
                    silverMineral1X = (int) recognition.getLeft();
                  } else {
                    silverMineral2X = (int) recognition.getLeft();
                  }
                }
                if (goldMineralX != -1 && silverMineral1X != -1) {
                  if (goldMineralX < silverMineral1X) {
                    telemetry.addData("Gold Mineral Position", "Left");
                    sample = "left";
                  } else {
                    telemetry.addData("Gold Mineral Position", "Center");
                    sample = "center";
                  }
                } else if (silverMineral1X != -1 && silverMineral2X != -1) {
                  telemetry.addData("Gold Mineral Position", "Right");
                  sample = "right";
                }
              } else if (updatedRecognitions.size() == 3) {
                int goldMineralX = -1;
                int silverMineral1X = -1;
                int silverMineral2X = -1;
                for (Recognition recognition : updatedRecognitions) {
                  if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    goldMineralX = (int) recognition.getLeft();
                  } else if (silverMineral1X == -1) {
                    silverMineral1X = (int) recognition.getLeft();
                  } else {
                    silverMineral2X = (int) recognition.getLeft();
                  }
                }
                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                  if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                    telemetry.addData("Gold Mineral Position", "Left");
                    sample = "left";
                  } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                    telemetry.addData("Gold Mineral Position", "Right");
                    sample = "right";
                  } else {
                    telemetry.addData("Gold Mineral Position", "Center");
                    sample = "center";
                  }
                }
              }
              telemetry.update();
            }
        }
        return sample;
    }
    
	// Deactivate the TensorFlow analysis
    public void deactivate() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }
    
    // Initialize the Vuforia localization engine
    private void initVuforia() {
		
        //Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    // Initialize the Tensor Flow Object Detection engine
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}