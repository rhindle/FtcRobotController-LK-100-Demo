package org.firstinspires.ftc.teamcode.Tools;

import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class ServoSSR implements Servo {

    private final Servo servo;
    private double offset = 0.0;            // offset is useful for syncing a pair of servos or "calibrating" a replacement servo
    private boolean enabled = false;        // tracks whether or not the servo is (or should be) enabled
    private boolean eStopped = false;       // tracks whether the servo is stopped in such a way that the position is unpredictable
    private int sweepTime = 1500;           // the time in ms it takes the servo to move its entire range
    private int wakeTime = 200;             // a short interval for the servo to move from disabled/parked to last position
    private long timer = 0;                 // a clock time to track whether a move should be complete

    public ServoSSR(Servo servo) {
        this.servo = servo;
    }

    // Future improvement possibility: For servos with feedback (e.g., Axon Max+),
    // add the ability to associate and configure an analog channel to read actual position and verify movement.

    // setters

     /**
     * Sets an offset value for the servo that will be subtracted with setting a position with setPosition()
     * @param offset the offset to subtracted
     * @return this for method chaining
     */
    public ServoSSR setOffset(double offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Sets the servo sweep time (full time to traverse from minimum to maximum position; e.g., -150° to 150°).
     * The value supplied should account for how the servo is loaded.
     * @param sweepTime the time in ms
     * @return this for method chaining
     */
    public ServoSSR setSweepTime(int sweepTime) {
        this.sweepTime = sweepTime;
        return this;
    }

    /**
     * Sets the servo "wake" time. If the servo was disabled, this time is allowed for it to return to its previous position.
     * Assumes a small amount of movement has happened while it is disabled.
     * @param wakeTime the time in ms
     * @return this for method chaining
     */
    public ServoSSR setWakeTime(int wakeTime) {
        this.wakeTime = wakeTime;
        return this;
    }

    /**
     * Sets the servo Pwm range to the maximum; i.e., 500-2500 μs
     * @return this for method chaining
     */
    public ServoSSR setFullPwmRange() {
        ((ServoImplEx) servo).setPwmRange(new PwmControl.PwmRange(500, 2500));
        return this;
    }

    /**
     * Sets the servo Pwm range
     * @param low the low end of the Pwm range in μs
     * @param high the high end of the Pwn range in μs
     * @return this for method chaining
     */
    public ServoSSR setPwmRange(double low, double high) {
        // could use some checking
        ((ServoImplEx) servo).setPwmRange(new PwmControl.PwmRange(low, high));
        return this;
    }

    // enabling and disabling pwm

    /**
     * Emergency Stop: Disables the Pwm signal for the servo such that the position is assumed to be lost/unknown.
     * Servo behavior may vary; goBilda servos will power down. Will automatically re-enable when another position is set.
     */
    public void stop() {
        disable();
        eStopped = true;  // we no longer know where the servo is, so need to time accordingly next move
    }

    /**
     * Disables the Pwm signal for the servo. Servo behavior may vary; goBilda servos will power down.
     * This is different than stop() in that it's assumed the servos won't move much (e.g., parked or resting).
     * Will automatically re-enable when another position is set.
     */
    public void disable() {
        //((ServoControllerEx) getController()).setServoPwmDisable(getPortNumber());
        ((ServoImplEx) servo).setPwmDisable();
        enabled = false;
        timer = 0;
    }

    /**
     * Enables the Pwm signal for the servo. (Will automatically re-enable when another position is set.)
     */
    public void enable() {
        //((ServoControllerEx) getController()).setServoPwmEnable(getPortNumber());
        ((ServoImplEx) servo).setPwmEnable();
        enabled = true;
    }

    // status responders & getters

    /**
     * Gets the stored offset value
     * @return the offset that is subtracted when setting position
     */
    public double getOffset() {
        return offset;
    }

    /**
     * Gets the servo position last set, accounting for the offset
     * @return the servo position + offset
     */
    public double getPositionWithOffset() {
        return getPosition() + offset;
    }

    /**
     * Determine if the servo is expected to be finished moving
     * (i.e., the timer associated with the servo movement is complete and the servo is enabled)
     * @return TRUE if the movement should be complete
     */
    public boolean isDone() {
        return enabled && timer != 0 && System.currentTimeMillis() >= timer;
    }

    /**
     * Determine if the servo timer is complete and therefore it is expected to be finished moving
     * (does not account for the possibility that it was disabled)
     * @return TRUE if the timer is complete
     */
    public boolean isTimerDone() {
        return System.currentTimeMillis() >= timer;
    }

    /**
     * Gets the amount of time remaining before the servo move is expected to be complete
     * @return the time remaining in ms
     */
    public long timeRemaining() {
        return Math.max(timer - System.currentTimeMillis(), 0);
    }

    /**
     * Determine if the servo is at a position (i.e., set to a certain position and the associated timer is complete)
     * @param comparePosition the position to check against the actual set position
     * @return TRUE if both the servo is set to that position and the time is complete
     */
    public boolean isAtPosition(double comparePosition) {
        return isSetPosition(comparePosition) && isDone();
    }

    /**
     * Determine if the servo is set to a certain position (not accounting for the timer)
     * @param comparePosition the position to check against the actual set position
     * @return TRUE if the servo is set to that position
     */
    public boolean isSetPosition(double comparePosition) {
        return(Math.round(getPositionWithOffset()*100.0) == Math.round(comparePosition*100.0));  // deals with rounding error
    }

    /**
     * Determine if the Pwm signal is enabled for the servo (as tracked internally by the wrapper)
     * @return TRUE if the servo Pwm is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Determine if the Pwm signal is disabled for the servo (as tracked internally by the wrapper)
     * @return TRUE if the servo Pwm is disabled
     */
    public boolean isDisabled() {
        return !enabled;
    }

    /**
     * Determine if the servo is in an emergency stopped state
     * (Pwm signal disabled for the servo, as tracked internally by the wrapper, and position unknown)
     * @return TRUE if the servo is stopped
     */
    public boolean isStopped() {
        return eStopped;
    }

    /**
     * @return the servo object, for whatever reason it's needed
     */
    public Servo getServo() {
        return servo;
    }

    // Servo class overrides

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return servo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
    }

    @Override
    public void close() {
    }

    @Override
    public ServoController getController() {
        return servo.getController();
    }

    @Override
    public int getPortNumber() {
        return servo.getPortNumber();
    }

    @Override
    public void setDirection(Direction direction) {
        servo.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return servo.getDirection();
    }

    @Override
    public void setPosition(double position) {
        if (eStopped) {
            timer = System.currentTimeMillis() + sweepTime;  //allow full sweep time because position is unknown
            eStopped = false;
        }
        else {
            if (enabled && isSetPosition(position)) return;        // has already been set (but not necessarily done moving), no need to update timer or position
            timer = calcSweepTimerValue(position) + (enabled ? 0 : wakeTime);   // add waketime if disabled
        }
        servo.setPosition(position - offset);
        enabled = true;                                           // setting a position re-enables, so update the tracker
    }

    @Override
    public double getPosition() {
        return servo.getPosition();
    }

    @Override
    public void scaleRange(double min, double max) {
        servo.scaleRange(min, max);
    }

    // internal methods

    private double calcSweepChange(double newPosition) {
        return Math.abs(getPositionWithOffset()-newPosition);
    }

    private long calcSweepTimerValue(double newPosition) {
        if (isDone()) {
            return System.currentTimeMillis() + (long)(calcSweepChange(newPosition) * (long)sweepTime);
        }
        else {
            // if the previous move was not complete, assume the worst case scenario for the next move
            // (i.e., the rest of the time remaining from the previous move plus the new move time)
            // possible future to do: calculate the predicted position based on time and last position
            return Math.max(timer, System.currentTimeMillis()) + (long)(calcSweepChange(newPosition) * (long)sweepTime);
        }
    }
}