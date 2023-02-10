package org.firstinspires.ftc.teamcode.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ShippingElementRecognizer extends OpenCvPipeline {
    private int shippingHubLevel = 3;
    double leftValue;
    double rightValue;
    public int getShippingHubLevel() {
        return shippingHubLevel;
    }

    public double getLeftValue() {
        return leftValue;
    }

    public double getRightValue() {
        return rightValue;
    }

    // Recognizes the shipping hub level based on where the team shipping element is located
    // Create two possible boxes it can be in
    static final Rect LEFTBOX = new Rect(
            new Point(0, 240),
            new Point(120, 320)
    );
    static final Rect RIGHTBOX = new Rect(
            new Point(121, 240),
            new Point(240, 320)
    );

    @Override
    public Mat processFrame(Mat input) {
        Mat mat = new Mat();
        Scalar lowHSV1 = new Scalar(0, 100, 20); // red lower in hsv
        Scalar highHSV1 = new Scalar(15, 255, 255); // red upper in hsv

        Scalar lowHSV2 = new Scalar(0, 100, 20); // __ lower in hsv
        Scalar highHSV2 = new Scalar(10, 255, 255); // __ upper in hsv

        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV); // convert to hsv
        Core.inRange(input, lowHSV1, highHSV1, mat); // make purple white, everything else black
        Mat left = mat.submat(LEFTBOX);

        leftValue = Core.sumElems(left).val[0] / LEFTBOX.area(); // Get white pixel / total pixel count
        Imgproc.rectangle(mat, LEFTBOX, new Scalar(255, 0, 0), 2); // draw rectangles around the boxes
        Imgproc.rectangle(mat, RIGHTBOX, new Scalar(255, 0, 0), 2);
        Scalar scalar = new Scalar(0,0,0);


        // If neither value is high enough , then the shipping hub level is 3, otherwise we continue



        // I think this stops a memory leak
        left.release();
        return mat;
    }
}