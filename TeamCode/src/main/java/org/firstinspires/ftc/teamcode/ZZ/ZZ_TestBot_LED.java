package org.firstinspires.ftc.teamcode.ZZ;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotParts.Common.ButtonMgr;
import org.firstinspires.ftc.teamcode.RobotParts.LegacyBots.Robot;

//import com.qualcomm.robotcore.util.Range;
//import java.util.Locale;

@TeleOp (name="ZZ_TestBot_LED", group="Test")
//@Disabled
public class ZZ_TestBot_LED extends LinearOpMode {

//    Robot robot   = new Robot(this);
//    ButtonMgr buttonMgr = new ButtonMgr(this);
//    Orientation angles;

    Robot robot;
    ButtonMgr buttonMgr;
    int[] ledcolors = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int ledmode = 1;

    @Override
    public void runOpMode() {
        robot = new Robot(this);
        buttonMgr = new ButtonMgr(this);
        robot.init();

        // Ensure the robot it stationary, then reset the encoders and calibrate the gyro.
        robot.motor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Send telemetry message to alert driver that we are calibrating;
        telemetry.addData(">", "Calibrating Gyro");    //
        telemetry.update();

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && !robot.sensorIMU.isGyroCalibrated())  {
            sleep(50);
            idle();
        }

        telemetry.addData(">", "Robot Ready.");    //
        telemetry.update();

        robot.motor0.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.motor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.motor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (Display Gyro value), and reset gyro before we move..
        while (!isStarted()) {
            //angles = robot.sensorIMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData(">", "Robot Heading = %.1f", robot.returnImuHeading(true));
            telemetry.update();
            sleep(100);
        }

        double tgtPower = 0;
        double tgtPosition = 0.5;

        //double counter = 0;

        ElapsedTime loopElapsedTime = new ElapsedTime();

        int tgtMotor = 0;
        int tgtServo = 0;

        boolean toggleLB = false;
        boolean toggleRB = false;
        boolean toggleA = false;
        boolean tgtServoLive = false;

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};
        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;
        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        robot.qled.changeLength(20);
        robot.qled.setBrightness(1);
        robot.qled.setDefaultBrightness(1);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            robot.loop();  // this will take care of clearing out the bulk reads
            buttonMgr.updateAll();

            //counter++;

            /* Color Sensor Section */
//            Color.RGBToHSV((int) (robot.sensorColor.red() * SCALE_FACTOR),
//                    (int) (robot.sensorColor.green() * SCALE_FACTOR),
//                    (int) (robot.sensorColor.blue() * SCALE_FACTOR),
//                    hsvValues);

            /* IMU */
            //angles = robot.sensorIMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            /* Check for button presses to switch test motor & servo */
            // Left bumper switches motors
            //if (gamepad1.left_bumper && !toggleLB) {
//            if (buttonMgr.wasTapped(1, ButtonMgr.Buttons.left_bumper)) {
//                stopAllMotors();
//                tgtMotor++;
//                if (tgtMotor>3) tgtMotor=0;
//                //toggleLB=true;
//            }
//            // Right bumper switches servos
//            //if (gamepad1.right_bumper && !toggleRB) {
//            if (buttonMgr.wasTapped(1, ButtonMgr.Buttons.right_bumper)) {
//                    tgtServo++;
//                if (tgtServo>5) tgtServo=0;
//                //toggleRB=true;
//                /* Get the position of the target servo.
//                 *  (This would be a lot nicer in an array) */
//                switch (tgtServo) {
//                    case 0: tgtPosition=robot.servo0.getPosition(); break;
//                    case 1: tgtPosition=robot.servo1.getPosition(); break;
//                    case 2: tgtPosition=robot.servo2.getPosition(); break;
//                    case 3: tgtPosition=robot.servo3.getPosition(); break;
//                    case 4: tgtPosition=robot.servo4.getPosition(); break;
//                    case 5: tgtPosition=robot.servo5.getPosition(); break;
//                    default: break;
//                }
//            }
//            // Y button resets the encoder for the current target motor
//            if (gamepad1.y) {
//                switch (tgtMotor) {
//                    case 0: robot.motor0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); robot.motor0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); break;
//                    case 1: robot.motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); robot.motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); break;
//                    case 2: robot.motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); robot.motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); break;
//                    case 3: robot.motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); robot.motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); break;
//                    default: break;
//                }
//            }
//            // A button toggles whether the servo position will be applied "Live"
//            //if (gamepad1.a & !toggleA) {
//            if (buttonMgr.wasTapped(1, ButtonMgr.Buttons.a)) {
//                tgtServoLive=!tgtServoLive;
//            //    toggleA = true;
//            }
//            // Toggles for certain buttons to avoid repeating actions
//            //if (!gamepad1.left_bumper) toggleLB=false;
//            //if (!gamepad1.right_bumper) toggleRB=false;
//            //if (!gamepad1.a) toggleA=false;
//
//            /* Check controls for changes to target motor power and target servo position */
//            // Left Stick controls motor power
//            // X button will make the motor full speed, otherwise only 25%
//            // Right Stick adjusts target servo position
//            // B button panic stops all motors (should no longer be necessary)
//            // Start button sets servo to center position (to stop if in continuous mode)
//            // !!! Can't seem to back the "back" button work ???
//            tgtPower = -gamepad1.left_stick_y * (gamepad1.x ? 1 : 0.25);
//            if (!gamepad1.start) {
//                tgtPosition = Math.max(0, Math.min(1, tgtPosition - gamepad1.right_stick_y * .025));
//            } else {
//                // added "stop" function in case the servo is in continuous mode
//                tgtPosition = 0.5;
//            }
//            if (gamepad1.b) {
//                stopAllMotors();
//                tgtPower=0;
//            }
//
//            /* Update the servo position */
//            if (tgtServoLive) {
//                switch (tgtServo) {
//                    case 0: robot.servo0.setPosition(tgtPosition); break;
//                    case 1: robot.servo1.setPosition(tgtPosition); break;
//                    case 2: robot.servo2.setPosition(tgtPosition); break;
//                    case 3: robot.servo3.setPosition(tgtPosition); break;
//                    case 4: robot.servo4.setPosition(tgtPosition); break;
//                    case 5: robot.servo5.setPosition(tgtPosition); break;
//                    default: break;
//                }
//            }
//
//            /* Set the motor power */
//            switch (tgtMotor) {
//                case 0: robot.motor0.setPower(tgtPower); break;
//                case 1: robot.motor1.setPower(tgtPower); break;
//                case 2: robot.motor2.setPower(tgtPower); break;
//                case 3: robot.motor3.setPower(tgtPower); break;
//                default: break;
//            }

            // If needed, could disable the servo signal with Servo.getController().pwmDisable()

            if (buttonMgr.wasTapped(1, ButtonMgr.Buttons.x)) {
                robot.qled.setColor(Color.BLUE);
            } else if (buttonMgr.wasTapped(1, ButtonMgr.Buttons.y)) {
                robot.qled.setColor(Color.YELLOW);
            } else if (buttonMgr.wasTapped(1, ButtonMgr.Buttons.b)) {
                robot.qled.setColor(Color.RED);
            } else if (buttonMgr.wasTapped(1, ButtonMgr.Buttons.a)) {
                robot.qled.setColor(Color.GREEN);
            } else if (buttonMgr.wasTapped(1, ButtonMgr.Buttons.back)) {
                robot.qled.turnAllOff();
            }

//            if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.x)) {
//                setLedColorArray(Color.BLUE, ledmode);
//                ledmode++;
//            } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.y)) {
//                setLedColorArray(Color.YELLOW, ledmode);
//                ledmode++;
//            } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.b)) {
//                setLedColorArray(Color.RED, ledmode);
//                ledmode++;
//            } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.a)) {
//                setLedColorArray(Color.GREEN, ledmode);
//                ledmode++;
//            } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.back)) {
//                setLedColorArray(0, 0);
//                ledmode=1;
//            }

            if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.dpad_down)) {
                robot.qled.setMirrorMode(0);
            } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.dpad_left)) {
                robot.qled.setMirrorMode(1);
            } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.dpad_up)) {
                robot.qled.setMirrorMode(2);
            } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.dpad_right)) {
                robot.qled.setAnimationOnOff(1);
            }

            if (buttonMgr.isPressed(2, ButtonMgr.Buttons.left_bumper)) {
                if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.x)) {
                    robot.qled.setColorGroup(0,2,Color.rgb(120, 0, 90));  //purple
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.y)) {
                    robot.qled.setColorGroup(0,2,Color.rgb(127, 50, 0));  //yellow
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.b)) {
                    robot.qled.setColorGroup(0,2,Color.rgb(48, 45, 36));  //white
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.a)) {
                    robot.qled.setColorGroup(0, 2, Color.rgb(0, 127, 0));  //green
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.back)) {
                    robot.qled.forceReboot();
                }
            }
            else if (buttonMgr.isPressed(2, ButtonMgr.Buttons.right_bumper)) {
                if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.x)) {
                    robot.qled.setColorGroup(2,2,Color.rgb(120, 0, 90));  //purple
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.y)) {
                    robot.qled.setColorGroup(2,2,Color.rgb(127, 50, 0));  //yellow
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.b)) {
                    robot.qled.setColorGroup(2,2,Color.rgb(48, 45, 36));  //white
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.a)) {
                    robot.qled.setColorGroup(2,2,Color.rgb(0, 127, 0));  //green
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.back)) {
                    robot.qled.forceReboot();
                }
            }
/*            if (buttonMgr.isPressed(2, ButtonMgr.Buttons.left_bumper)) {
                if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.x)) {
                    robot.qled.setColorGroupX2(0,2,Color.rgb(80, 0, 110),5,2,Color.rgb(80, 0, 110));  //purple
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.y)) {
                    robot.qled.setColorGroupX2(0,2,Color.rgb(127, 50, 0),5,2,Color.rgb(127, 50, 0));  //yellow
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.b)) {
                    robot.qled.setColorGroupX2(0,2,Color.rgb(64, 60, 48),5,2,Color.rgb(64, 60, 48));  //white
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.a)) {
                    robot.qled.setColorGroupX2(0,2,Color.rgb(0, 127, 0),5,2,Color.rgb(0, 127, 0));  //green
                }
            }
            else if (buttonMgr.isPressed(2, ButtonMgr.Buttons.right_bumper)) {
                if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.x)) {
                    robot.qled.setColorGroupX2(2,2,Color.rgb(80, 0, 110),7,2,Color.rgb(80, 0, 110));  //purple
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.y)) {
                    robot.qled.setColorGroupX2(2,2,Color.rgb(127, 50, 0),7,2,Color.rgb(127, 50, 0));  //yellow
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.b)) {
                    robot.qled.setColorGroupX2(2,2,Color.rgb(64, 60, 48),7,2,Color.rgb(64, 60, 48));  //white
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.a)) {
                    robot.qled.setColorGroupX2(2,2,Color.rgb(0, 127, 0),7,2,Color.rgb(0, 127, 0));  //green
                }
            }*/
            /*else {
                if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.x)) {
                    setLedColorArray(Color.rgb(120, 0, 90), ledmode);  //purple
                    ledmode++;
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.y)) {
                    setLedColorArray(Color.rgb(127, 50, 0), ledmode);  //yellow
                    ledmode++;
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.b)) {
                    setLedColorArray(Color.rgb(48, 45, 36), ledmode);  //white
                    ledmode++;
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.a)) {
                    setLedColorArray(Color.rgb(0, 127, 0), ledmode);  //green
                    ledmode++;
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.back)) {
                    setLedColorArray(0, 0);
                    ledmode = 1;
                }
            } */
            else {
                if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.x)) {
                    pushLedColorArray(Color.rgb(120, 0, 90));  //purple
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.y)) {
                    pushLedColorArray(Color.rgb(127, 50, 0));  //yellow
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.b)) {
                    pushLedColorArray(Color.rgb(48, 45, 36));  //white
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.a)) {
                    pushLedColorArray(Color.rgb(0, 127, 0));  //green
                } else if (buttonMgr.wasTapped(2, ButtonMgr.Buttons.back)) {
                    pushLedColorArray(0, false);
                    pushLedColorArray(0, false);
                    robot.qled.turnAllOff();
                }
            }

            if(ledmode>2) ledmode=1;

                /* Add extensive telemetry for debugging */
            telemetry.addLine()
                .addData("Mtr",tgtMotor)
                .addData("Pwr", "%.2f", tgtPower)
                .addData("Srv", tgtServo)
                .addData("Pos","%.3f", tgtPosition)
                .addData("Live?", tgtServoLive);
            telemetry.addLine()
                .addData("Encoder 0", robot.motor0.getCurrentPosition())
                .addData("1", robot.motor1.getCurrentPosition())
                .addData("2", robot.motor2.getCurrentPosition())
                .addData("3", robot.motor3.getCurrentPosition());
            telemetry.addLine()
                .addData("Motor 0", "%.2f", robot.motor0.getPower())
                .addData("1", "%.2f", robot.motor1.getPower())
                .addData("2", "%.2f", robot.motor2.getPower())
                .addData("3", "%.2f", robot.motor3.getPower());
            telemetry.addLine()
                .addData("S 0", "%.2f", robot.servo0.getPosition())
                .addData("1","%.2f", robot.servo1.getPosition())
                .addData("2","%.2f", robot.servo2.getPosition())
                .addData("3","%.2f",  robot.servo3.getPosition())
                .addData("4","%.2f", robot.servo4.getPosition())
                .addData("5","%.2f", robot.servo5.getPosition());
//            telemetry.addLine()
//                .addData("Color A", robot.sensorColor.alpha())
//                .addData("R", robot.sensorColor.red())
//                .addData("G", robot.sensorColor.green())
//                .addData("B", robot.sensorColor.blue());
//            telemetry.addLine()
//                .addData("Hue", "%.1f", hsvValues[0])
//                .addData("Distance (cm)", "%.01f", robot.sensorDistance.getDistance(DistanceUnit.CM));
            telemetry.addLine()
                .addData("D 0", (robot.digital0.getState() ? "T" : "F") +
                        " | 1 : " + (robot.digital1.getState() ? "T" : "F") +
                        " | 2 : " + (robot.digital2.getState() ? "T" : "F")  +
                        " | 3 : " + (robot.digital3.getState() ? "T" : "F")+
                        " | 4 : " + (robot.digital4.getState() ? "T" : "F") +
                        " | 5 : " + (robot.digital5.getState() ? "T" : "F") +
                        " | 6 : " + (robot.digital6.getState() ? "T" : "F") +
                        " | 7 : " + (robot.digital7.getState() ? "T" : "F"));
            telemetry.addData("Heading", "%.1f", robot.returnImuHeading());
            //telemetry.addData("Counter", counter);
            //telemetry.addData("LoopSpeed","%.1f",calcLoopSpeed());
            telemetry.addData("LoopTime(ms)","%.1f",loopElapsedTime.milliseconds());
            telemetry.addData("LoopSpeed(lps)","%.1f",1/(loopElapsedTime.milliseconds()/1000));
            loopElapsedTime.reset();
            telemetry.update();
        }
    }

    public void pushLedColorArray (int c, boolean update) {
        //push the colors from the first half onto the last half, add new colors at start
        for (int i=0; i<4; i++) {
            ledcolors[10-1-i] = ledcolors[i];
            ledcolors[i] = c;
        }
        if (update) robot.qled.setColors(ledcolors);
    }
    public void pushLedColorArray (int c) {
        pushLedColorArray(c, true);
    }

    public void setLedColorArray (int c, int mode) {
        if (mode != 2) {  //bottom
            for (int i=0; i<4; i++) {
                ledcolors[i]=c;
            }
        }
        if (mode != 1) {  //top
            for (int i=6; i<10; i++) {
                ledcolors[i]=c;
            }
        }
        if (mode != 0) {
            robot.qled.setColors(ledcolors);
        } else {
            robot.qled.turnAllOff();
        }

    }

    public void stopAllMotors () {
        robot.motor0.setPower(0);
        robot.motor1.setPower(0);
        robot.motor2.setPower(0);
        robot.motor3.setPower(0);
    }

//    static int loopCounter = 0;
//    static double loopSpeed = 0;
//    static ElapsedTime loopElapsedTime = new ElapsedTime();
//
//    private double calcLoopSpeed() {
//
//        loopCounter += 1;
//        if (loopElapsedTime.milliseconds() > 1000) {
//            //loopSpeedText = JavaUtil.formatNumber(loopCounter / (elapsedTime.milliseconds() / 1000), 1);
//            loopSpeed = loopCounter / (loopElapsedTime.milliseconds() / 1000);
//            loopCounter = 0;
//            loopElapsedTime.reset();
//        }
//        return loopSpeed;
//    }

}