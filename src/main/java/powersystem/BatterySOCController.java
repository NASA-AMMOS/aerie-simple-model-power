package powersystem;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;

/**
 * This class is used by the BatteryModel class, and it is helpful in making sure the battery state of charge stays
 * in the limits of 0 to 100 by working with the integrated net power.
 */
public class BatterySOCController {
//    private BatteryModel battery;
//    private double EPSILON = 0.000001;
//    public BatterySOCController(BatteryModel battery) {
//        this.battery = battery;
//    }
//
//    /**
//     * This function makes sure that the integrated net power does not exceed the battery capacity.
//     */
//    public void limitBatteryCharging() {
//        while (true) {
//            var batteryIsFull = battery.batteryCharge.isBetween(this.battery.batteryCapacityAH,
//                    Double.POSITIVE_INFINITY);
//            var battNotYetRegisteredFull = battery.batteryFull.is(false);
//            ModelActions.waitUntil(batteryIsFull.and(battNotYetRegisteredFull));
//            battery.batteryFull.set(true);
//        }
//    }
//
//    /**
//     * This function makes sure the integrated net power does not go below 0.
//     */
//    public void limitBatteryDepletion() {
//        while (true) {
//            var batteryIsEmpty = battery.batteryCharge.isBetween(Double.NEGATIVE_INFINITY, 0.0);
//            var battNotYetRegisteredEmpty = battery.batteryEmpty.is(false);
//            ModelActions.waitUntil(batteryIsEmpty.and(battNotYetRegisteredEmpty));
//            battery.batteryEmpty.set(true);
//        }
//    }
//
//    /**
//     * This function makes sure that the battery is charging correctly.
//     */
//    public void enableNormalBatteryFullOps() {
//        while (true) {
//            var batteryIsNotFull = battery.batteryCharge.isBetween(Double.NEGATIVE_INFINITY,
//                    this.battery.batteryCapacityAH-EPSILON);
//            var battStillRegisteredFull = battery.batteryFull.is(true);
//            ModelActions.waitUntil(batteryIsNotFull.and(battStillRegisteredFull));
//            battery.batteryFull.set(false);
//        }
//    }
//
//    /**
//     * This function makes sure that the battery is discharging correctly.
//     */
//    public void enableNormalBatteryEmptyOps() {
//        while (true) {
//            var batteryIsNotEmpty = battery.batteryCharge.isBetween(0.0 + EPSILON,
//                    Double.POSITIVE_INFINITY);
//            var battStillRegisteredEmpty = battery.batteryEmpty.is(true);
//            ModelActions.waitUntil(batteryIsNotEmpty.and(battStillRegisteredEmpty));
//            battery.batteryEmpty.set(false);
//        }
//    }
//
//    /**
//     * This function is what is called in the BatteryModel to make sure that the other methods in this class are
//     * constantly running and checking the integrated net power and battery state of charge.
//     */
//    public void run() {
//        // The four conditions we care about monitoring:
//        // 1. integratedNetPower == batteryCapacityWH but batteryFull == false -> set batteryFull to true
//        ModelActions.spawn(this::limitBatteryCharging);
//        // 2. integratedNetPower < batteryCapacityWH but batteryFull == true -> set batteryFull to false
//        ModelActions.spawn(this::enableNormalBatteryFullOps);
//        // 3 integratedNetPower == 0.0 but batteryEmpty == false -> set batteryEmpty to true
//        ModelActions.spawn(this::limitBatteryDepletion);
//        // 4. integratedNetPower > 0.0 but batteryEmpty == true -> set batteryEmpty to false
//        ModelActions.spawn(this::enableNormalBatteryEmptyOps);
//    }
}

