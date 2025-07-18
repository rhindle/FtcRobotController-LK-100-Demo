package org.firstinspires.ftc.teamcode.RobotParts.SpintakeBot;

import org.firstinspires.ftc.teamcode.RobotParts.Common.Parts;
import org.firstinspires.ftc.teamcode.Tools.DataTypes.NavigationTarget;
import org.firstinspires.ftc.teamcode.Tools.DataTypes.Position;
import org.firstinspires.ftc.teamcode.Tools.PartsInterface;

public class SB_Auto implements PartsInterface {

   /* Public OpMode members. */
   public Parts parts;

   public static boolean isAuto = false;

   /* Constructor */
   public SB_Auto(Parts parts){
      construct(parts);
   }

   void construct(Parts parts){
      this.parts = parts;
   }

   public void initialize(){
   }

   public void preInit() {
   }

   public void initLoop() {
   }

   public void preRun() {
   }

   public void runLoop() {
   }

   public void stop() {
   }

   public void eStop() {
      isAuto = false;
   }

   public boolean driveToTargetBackground(NavigationTarget navTarget) {
      return driveToTargetsBackground(new NavigationTarget[] {navTarget});
   }

   public boolean driveToTargetsBackground(NavigationTarget... navTargets) {
      if (!isAutoRunning()) return false;    //exit right away if stopped
      parts.autoDrive.addNavTargets(navTargets);
      parts.autoRunLoop();                   // todo: do this or not?
      return true;
   }

   public boolean driveToTarget(NavigationTarget navTarget) {
      return driveToTargets(new NavigationTarget[] {navTarget});
   }

   public boolean driveToTargets(NavigationTarget... navTargets) {
      if (!isAutoRunning()) return false;    //exit right away if stopped
      parts.autoDrive.addNavTargets(navTargets);
      return waitForDriveComplete();
   }

   public boolean waitForDriveComplete() {
      while (isAutoRunning()) {
         parts.autoRunLoop();
         switch (parts.autoDrive.getStatus()) {
            case SUCCESS:         // we're done, successfully
               return true;
            case DRIVING:         // we're still in progress
               break;
            case HOLDING:
            case IDLE:
               return true;        // todo: figure this out for sure, combine with success??
            case LATE:
            case TIMEOUT:
            case CANCELED:
            case LOST:
            default:              // we're done, unsuccessfully
               parts.autoDrive.cancelNavigation();
               return false;
         }
      }
      return false;               // auto or opmode stopped
   }

   public void delay(long ms) {
      delay(ms,false);
   }
   public void delay(long ms, boolean blocking) {
      if (blocking) {
         parts.opMode.sleep(ms);
      } else {
         ms += System.currentTimeMillis();
         while (isAutoRunning() && ms > System.currentTimeMillis()) parts.autoRunLoop();
      }
   }

   public boolean isAutoRunning() {
      return (parts.opMode.opModeIsActive() && isAuto);
   }

   public void setIsAuto(boolean boo){
      isAuto = boo;
   }

   public void testAutoMethod0() {
      driveToTargetsBackground(new NavigationTarget[]{
              new NavigationTarget(new Position(-28,-1,0), parts.dsMisc.toleranceTransition, 1.0,5000,true),
              new NavigationTarget(new Position(-15, -1, 0), parts.dsMisc.toleranceTransition, 1.0,5000,true),
              new NavigationTarget(new Position(-12, -13, 0), parts.dsMisc.toleranceTransition, 1.0,5000,false),
              new NavigationTarget(new Position(-30, -13, 0), parts.dsMisc.toleranceTransition, 1.0,5000,true),
              new NavigationTarget(new Position(-12, 13, 0), parts.dsMisc.toleranceTransition, 1.0,5000,true),
              new NavigationTarget(new Position(-24, 13, 0), parts.dsMisc.toleranceTransition, 1.0,5000,true),
              new NavigationTarget(new Position(-33, -4, 0), parts.dsMisc.toleranceHigh, 1.0,5000,false) });
      waitForDriveComplete();
   }

   public void testAutoMethod() {
      isAuto = true;
      boolean result;
      int timeLimit = 5000;
      double speed = 1.0;
      // warning: don't use noSlow=true for tight tolerances or when the robot is simply rotating (the results are terrible jumpy movement)
      result = driveToTarget( new NavigationTarget(new Position(-28,-1,0), parts.dsMisc.toleranceMedium, speed, timeLimit));
      if (!result) return;  // could branch or something
      delay(500);
      driveToTarget( new NavigationTarget(new Position(-28,-1,135), parts.dsMisc.toleranceMedium, speed, timeLimit));
      delay(500);
      driveToTarget( new NavigationTarget(new Position(-28,-1,0), parts.dsMisc.toleranceTransition, speed, timeLimit));
      driveToTarget( new NavigationTarget(new Position(-28,-1,-90), parts.dsMisc.toleranceTransition, speed, timeLimit));
      driveToTarget( new NavigationTarget(new Position(-28,-1,-135), parts.dsMisc.toleranceMedium, speed, timeLimit));
      delay(500);
      driveToTarget( new NavigationTarget(new Position(-28,-1,0), parts.dsMisc.toleranceMedium, speed, timeLimit));
      delay(1000);
      speed = 0.7;
      driveToTargetsBackground(new NavigationTarget[]{
              new NavigationTarget(new Position(-28,-1,0), parts.dsMisc.toleranceTransition, speed, timeLimit,true),
              new NavigationTarget(new Position(-15, -1, 0), parts.dsMisc.toleranceTransition, speed, timeLimit,true),
              new NavigationTarget(new Position(-12, -13, 0), parts.dsMisc.toleranceTransition, speed, timeLimit,false),
              new NavigationTarget(new Position(-30, -13, 30), parts.dsMisc.toleranceTransition, speed, timeLimit,true),
              new NavigationTarget(new Position(-12, 13, -30), parts.dsMisc.toleranceTransition, speed, timeLimit,true),
              new NavigationTarget(new Position(-24, 13, 0), parts.dsMisc.toleranceTransition, speed, timeLimit,true),
              new NavigationTarget(new Position(-33, -4, 0), parts.dsMisc.toleranceHigh, speed, timeLimit,false) });
      waitForDriveComplete();
   }
}