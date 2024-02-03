// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import java.lang.Math;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
  public static class LimelightConstants {
    // i'm pretty sure i don't actually need these, but they're here anyways
    
    // the tags should be centered relative to the openings
    // the first number is height from ground to bottom of tag
    // the second number gets the middle of the tag
    // all distances are measured in inches
    public static final double SPEAKERHEIGHT = (53.88 + (1/2)*6.5);
    public static final double AMPHEIGHT = (50.13 + (1/2)*6.5);
    public static final double LIMELIGHTHEIGHT = 0; // put this later in inches, please
    public static final double LIMELIGHTANGLE = Math.toRadians(0); // put this later in degrees, please
}
}
