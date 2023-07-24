package genericpowersystem.models.power;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.merlin.framework.resources.real.RealResource;

public class BatterySOCController {
    private BatteryModel battery;
    private double EPSILON = 0.000001;
    public BatterySOCController(BatteryModel battery) {
        this.battery = battery;
    }
    public void limitBatteryCharging() {
        while (true) {
            var batteryIsFull = battery.integratedNetPower.isBetween(this.battery.batteryCapacityWH,
                    Double.POSITIVE_INFINITY);
            var battNotYetRegisteredFull = battery.batteryFull.is(false);
            ModelActions.waitUntil(batteryIsFull.and(battNotYetRegisteredFull));
            battery.batteryFull.set(true);
            //battery.batterySOC = RealResource.scaleBy((1 / battery.integratedNetPower.get()), battery.integratedNetPower).scaledBy(100.0);
        }
    }

    public void limitBatteryDepletion() {
        while (true) {
            var batteryIsEmpty = battery.integratedNetPower.isBetween(Double.NEGATIVE_INFINITY, 0.0);
            var battNotYetRegisteredEmpty = battery.batteryEmpty.is(false);
            ModelActions.waitUntil(batteryIsEmpty.and(battNotYetRegisteredEmpty));
            battery.batteryEmpty.set(true);
            //battery.batterySOC = battery.batterySOC.scaledBy(0.0);
        }
    }

    public void enableNormalBatteryFullOps() {
        while (true) {
            var batteryIsNotFull = battery.integratedNetPower.isBetween(Double.NEGATIVE_INFINITY,
                    this.battery.batteryCapacityWH-EPSILON);
            var battStillRegisteredFull = battery.batteryFull.is(true);
            ModelActions.waitUntil(batteryIsNotFull.and(battStillRegisteredFull));
            battery.batteryFull.set(false);
            /**
            var val = RealResource.add(RealResource.scaleBy(1.0/battery.batteryCapacityWH, battery.integratedNetPower).scaledBy(100), battery.batterySOC);
            if (val.get() <= 100.0) {
                battery.batterySOC = RealResource.add(RealResource.scaleBy(1.0/battery.batteryCapacityWH, battery.integratedNetPower).scaledBy(100), battery.batterySOC);
            } else {
                battery.batterySOC = RealResource.scaleBy((1 / battery.integratedNetPower.get()), battery.integratedNetPower).scaledBy(100.0);
            }
             */
        }
    }

    public void enableNormalBatteryEmptyOps() {
        while (true) {
            var batteryIsNotEmpty = battery.integratedNetPower.isBetween(0.0 + EPSILON,
                    Double.POSITIVE_INFINITY);
            var battStillRegisteredEmpty = battery.batteryEmpty.is(true);
            ModelActions.waitUntil(batteryIsNotEmpty.and(battStillRegisteredEmpty));
            battery.batteryEmpty.set(false);
            /**
            var val = RealResource.add(RealResource.scaleBy(1.0/battery.batteryCapacityWH, battery.integratedNetPower).scaledBy(100), battery.batterySOC);
            if (val.get() >= 0.0) {
                battery.batterySOC = RealResource.add(RealResource.scaleBy(1.0/battery.batteryCapacityWH, battery.integratedNetPower).scaledBy(100), battery.batterySOC);
            } else {
                battery.batterySOC = battery.batterySOC.scaledBy(0.0);
            }
             */
        }
    }

    public void run() {
        // The four conditions we care about monitoring:
        // 1. integratedNetPower == batteryCapacityWH but batteryFull == false -> set batteryFull to true
        ModelActions.spawn(this::limitBatteryCharging);
        // 2. integratedNetPower < batteryCapacityWH but batteryFull == true -> set batteryFull to false
        ModelActions.spawn(this::enableNormalBatteryFullOps);
        // 3 integratedNetPower == 0.0 but batteryEmpty == false -> set batteryEmpty to true
        ModelActions.spawn(this::limitBatteryDepletion);
        // 4. integratedNetPower > 0.0 but batteryEmpty == true -> set batteryEmpty to false
        ModelActions.spawn(this::enableNormalBatteryEmptyOps);
    }
}

