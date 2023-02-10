package org.firstinspires.ftc.teamcode.autos;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.autos.SleeveDetection;
import org.firstinspires.ftc.teamcode.autos.SleeveDetection.ParkingPosition;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.teamcode.opencv.ShippingElementRecognizer;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "Auto", group = "Autonomous")
public class auto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor BackLeft;
    private DcMotor BackRight;
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor Slide;
    private Servo Claw;
    OpenCvWebcam webcam;
    public void runOpMode() throws InterruptedException {

        telemetry.addData("status", "1");
        telemetry.update();
        FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        BackLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        FrontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        BackRight = hardwareMap.get(DcMotor.class, "BackRight");
        Claw = hardwareMap.get(Servo.class, "Claw");
        Slide = hardwareMap.get(DcMotor.class, "Slide");


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips

        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        Slide.setDirection(DcMotor.Direction.FORWARD);

        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Camera things
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);
        SleeveDetection pipeline = new SleeveDetection();
        webcam.setPipeline(pipeline);
        webcam.setMillisecondsPermissionTimeout(2500);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                // This will be called if the camera could not be opened
            }
        });
        telemetry.addData("status", pipeline.getPosition());
        telemetry.addData("status", "2");
        telemetry.update();
        waitForStart();

        runtime.reset();

        //Code cone delivery

        while (runtime.seconds() < .85 && opModeIsActive()) {
            FrontLeft.setPower(-0.5);
            FrontRight.setPower(0.5);
            BackLeft.setPower(0.5);
            BackRight.setPower(-0.5);
        }

        telemetry.addData("status", "3");
        telemetry.update();

        runtime.reset();

        if(pipeline.getPosition() == ParkingPosition.LEFT  && opModeIsActive()) {
            telemetry.addData("status", "left");
            telemetry.update();
            //Code left
            while (runtime.seconds() < .65 && opModeIsActive()) {
                FrontLeft.setPower(0.5);
                FrontRight.setPower(0.5);
                BackLeft.setPower(0.5);
                BackRight.setPower(0.5);
            }

            telemetry.addData("status", "left");
            telemetry.update();

        }
        else if(pipeline.getPosition() == ParkingPosition.CENTER && opModeIsActive()) {
            //Code center
            telemetry.addData("status", "center");
            telemetry.update();
        }

        else if(pipeline.getPosition() == ParkingPosition.RIGHT && opModeIsActive()) {
            //Code right
            telemetry.addData("status", "right");
            telemetry.update();
            while (runtime.seconds() < .15 && opModeIsActive()) {
                FrontLeft.setPower(-0.5);
                FrontRight.setPower(-0.5);
                BackLeft.setPower(-0.5);
                BackRight.setPower(-0.5);
            }
            while (runtime.seconds() < .75 && opModeIsActive()){
                FrontLeft.setPower(-0.5);
                FrontRight.setPower(-0.5);
                BackLeft.setPower(-0.5);
                BackRight.setPower(-0.5);
            }
        }
        else {
            telemetry.addData("status", "No cone detected");
            telemetry.update();
        }
    }
}
