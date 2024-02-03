package frc.robot.utils;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

import java.util.Optional;
import java.lang.Math;
import java.time.Duration;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

// public class Limelight {
//     public void updateDistanceToTarget() {
//         double height_diff = Constants.LimelightConstants.SPEAKERHEIGHT-Constants.LimelightConstants.LIMELIGHTHEIGHT;
        
//     }
//     // public void updateDistanceToTarget() {
//     //     double goal_theta = Constants.VisionConstants.kLimelightConstants.kHorizontalPlaneToLens.getRadians() + Math.toRadians(mPeriodicIO.yOffset);
//     //     double height_diff = Constants.VisionConstants.kGoalHeight - Constants.VisionConstants.kLimelightConstants.kHeight;

//     //     mDistanceToTarget = Optional.of(height_diff / Math.tan(goal_theta) + Constants.VisionConstants.kGoalRadius); // add goal radius for offset to center of target
//     // }
// }



public final class Limelight {
    // base variables straight from the limelight
    static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    static NetworkTableEntry tx = table.getEntry("tx");
    static NetworkTableEntry ty = table.getEntry("ty");
    static NetworkTableEntry ta = table.getEntry("ta");
    static NetworkTableEntry tv = table.getEntry("tv");
    static double x = tx.getDouble(0.0);
    static double y = ty.getDouble(0.0);
    static double area = ta.getDouble(0.0);
    static double v = tv.getDouble(0);
    static boolean targetSeen = (v == 0) ? false : true; // turns v into a boolean because i did all of this based on v being a boolean
    // apriltag pose variables
    // all 'ap' networktable entries are from the apriltag
    // there's no naming conflict between the base variables and the apriltag variables so they're still short
    static double[] aprilarray = table.getEntry("targetpose_robotspace").getDoubleArray(new double[6]);
    static double ax = aprilarray[0], ay = aprilarray[1], az = aprilarray[2], rx = aprilarray[3], ry = aprilarray[4], rz = aprilarray[5];
    // static NetworkTableEntry aptx = apriltable.getEntry("tx");
    // static NetworkTableEntry apty = apriltable.getEntry("ty");
    // static NetworkTableEntry aptz = apriltable.getEntry("tz");
    // static NetworkTableEntry aprx = apriltable.getEntry("rx");
    // static NetworkTableEntry apry = apriltable.getEntry("ry");
    // static NetworkTableEntry aprz = apriltable.getEntry("rz");
    //NetworkTableInstance.getDefault().getTable("limelight").getEntry("<variablename>").getDoubleArray(new double[6]);

    // static double ax = aptx.getDouble(0.0); // a variables are position variables
    // static double ay = apty.getDouble(0.0); // r variables are rotaiton variables
    // static double az = aptz.getDouble(0.0);
    // static double rx = aprx.getDouble(0.0);
    // static double ry = apry.getDouble(0.0);
    // static double rz = aprz.getDouble(0.0);
    // initializing the storage variables as class-wide
    static double storedX;
    static double storedY;
    static double storedArea;
    static boolean targetIsValid = false; // exists for the 'try not to detect the bad tags' section of stableGetter()
    // counter variables for the stableGetter() method
    static int targetCounter = 0; // exists for the detection stability loop
    static int counter = 0; // exists for the not-a-for-loop in the tracking valid tag loop
    public static void stableGetter(){
    // keep searching for the target and outputting as normal until the target is seen
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");
    tv = table.getEntry("tv");
    x = tx.getDouble(0.0);
    y = ty.getDouble(0.0);
    area = ta.getDouble(0.0);    
    v = tv.getDouble(0);
        targetSeen = (v == 0) ? false : true;
        // when you first see the target, wait 200 ms before you actually start storing new limelight data to avoid bad detections
        if(targetSeen && !targetIsValid){
            System.out.println("yep. i do in fact see");
            x = 0;
            y = 0;
            area = 0;
            v = 0;
            if(targetCounter >= 10){
                targetIsValid = true;
                System.out.println("I HAVE SET THE TARGET AS VALID WE ARE GOOD");
            } else {
                 targetCounter++;
                 System.out.println("i have increased the counter; new counter is:");
                 System.out.println(targetCounter);
            }
        } else if(!targetIsValid){ // if the target is not seen and not valid, reset the counter and limelight variables
            targetCounter = 0;
            x = 0;
            y = 0;
            area = 0;
            v = 0;
            System.out.println("nope. i see nothing. counter is reset. variables are default.");
        }
        // if the target is valid, begin storing variables and reset all other counters
        if(targetIsValid && targetSeen){
            storedX = x;
            storedY = y;
            storedArea = area;
            counter = 0; // make sure that the counters reset if there's a target seen
            targetCounter = 0;

            System.out.println("I HAVE SEENED THE TARGET SUCCESSFULLY");
        }
        // if there was a valid target before but now there isn't, hold the last good frame's data for up to 240 ms until we get new data or 240 ms pass
        if (!targetSeen && targetIsValid) { //should be an else statement but it's probably fine as-is
            System.out.println("OI WE GOT A BREAK IN VISION");
            if(x != storedX || y != storedY || area != storedArea) { // if there's a new detected frame and no new good data is shown, bump the counter up
                counter++;
                System.out.println("one new frame has passed. the new counter is:");
                System.out.println(counter);
            }
            x = storedX; // set all of our limelight data to the last good data
            y = storedY;
            area = storedArea;
            targetSeen = true; // clearly the target must have been seen if we're doing this
            if(counter >= 12) { // once we declare the target no longer visible, we set the target to not seen and we reset the counter
                targetIsValid = false;
                counter = 0;
                System.out.println("the system deems that it is no longer looking at an image for a substantial period of time");
            }
        }
    }
    public static void postToDashboard(){ // self-explanatory
    // these variables are in the post method to test how they work
    aprilarray = table.getEntry("targetpose_robotspace").getDoubleArray(new double[6]);
    ax = aprilarray[0];
    ay = aprilarray[1];
    az = aprilarray[2];
    rx = aprilarray[3];
    ry = aprilarray[4];
    rz = aprilarray[5];
    System.out.println(ax);
    // post to smart dashboard periodically
    SmartDashboard.putNumber("Limelight X Error", x);
    SmartDashboard.putNumber("Limelight Y Error", y);
    SmartDashboard.putNumber("Limelight Area", area);
    SmartDashboard.putBoolean("Target Detected?", targetIsValid);
    // apriltag test variables
    SmartDashboard.putNumber("Robot X Position", ax);
    SmartDashboard.putNumber("Robot Y Position", ay);
    SmartDashboard.putNumber("Robot Z Position?", az);
    SmartDashboard.putNumber("Limelight X Rotation", rx); // i don't know which variable is which axis of rotation
    SmartDashboard.putNumber("Limelight Y Rotation", ry);
    SmartDashboard.putNumber("Limelight Z Rotation", rz);
    }
    // takes the x and y values and compares them to the apriltags; if the crosshair is close enough to the center, show that we're on-target
    public static void checkForTargets(){
        boolean isTargetThere = false;
        double yDiff = Math.abs(y);
        double xDiff = Math.abs(x);
            if(yDiff <.5 && xDiff <.5 && targetIsValid){ // if the x and y diffs are less than .5 degrees off and the target is valid (aka it is not reading junk data), say it's ready
                System.out.println("HEY BOSS IT'S THERE");
                isTargetThere = true;
            }
        SmartDashboard.putBoolean("On-Target Signal", isTargetThere);
    }
    }