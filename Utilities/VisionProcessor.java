package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


public class VisionProcessor {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private String sample = "failedInit";
    private Telemetry telemetry;
    private HardwareMap hardwareMap;

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = "AbfrKYL/////AAAAGfUho+x13kKJkH/SBEqJFnkvL102usBXiZRgdAjtKaiq1x1aXSGd+5abnOmK0BgdlMhCbc4d+gQv5Dl46y5HUqcpoLIXJvG6BDLMPaFX48pqSO9tQlx2FZjTWVbUePzGTnJll5xbVjY8zTNRd7iDUyk9imxm2GzTHyM758Z7oTY0k5m4DV++KgTo6cy2BE495DAUOc+CNVE8gDu003Ua5yF6iclNWeI3NZP1ob8VBOnS0ksriNEtP66HjSiRGyCCiz9HVdA8Z0883MW8sUZ0VAXH2qqsTL57lJ80jqSI7YLZpuShi6EUMr9sCF7UzlE1sD0/8OBiAcs8RQNy2Qc1GtxXb/VDm8q2Q3ZJetTXK/H/";

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
    
    public void VisionProcessor() {
    }
    // SKJ - Changed constructor name
    // from: VisionProcessing
    // to: VisionProcessor
    // added second parameter for hardwaremap
    // added private property for hardwaremap
    // assigned parameter to property in this constructor
    public void init(Telemetry tInput, HardwareMap hwMap) {
        telemetry = tInput;
        hardwareMap = hwMap;
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
            sample = "none";
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        sample = "TensorError";
    }
    
    public void activate() {
        if (tfod != null) {
                tfod.activate();
        }
    }
    
    public String sample () {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
              telemetry.addData("# Object Detected", updatedRecognitions.size());
              if (updatedRecognitions.size() == 3) {
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
                    sample = "left";
                  } else {
                    telemetry.addData("Gold Mineral Position", "Center");
                    sample = "left";
                  }
                }
              }
              telemetry.update();
            }
        }
        return sample;
    }
    
    public void deactivate() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }
    
    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine. 
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}