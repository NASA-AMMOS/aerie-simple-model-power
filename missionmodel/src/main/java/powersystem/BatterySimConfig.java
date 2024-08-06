package powersystem;

import gov.nasa.jpl.aerie.merlin.framework.annotations.AutoValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;

/**
 * Battery configuration exposed to the planner as simulation configuration
 */
@AutoValueMapper.Record
public record BatterySimConfig( Double batteryCapacity,
                                Double busVoltage,
                                Double initialSOC) {

    // Battery capacity of on-board battery in (Ah)
    public static final Double DEFAULT_BATTERY_CAPACITY = 100.0;

    // Voltage the spacecraft uses to distribute power to its
    // various components (V). Also known as battery bus voltage.
    public static final Double DEFAULT_BUS_VOLTAGE = 28.0;

    // Initial value for the state of charge of the battery
    public static final Double DEFAULT_INITIAL_SOC = 100.0;


    public static @Export.Template BatterySimConfig defaultConfiguration() {
        return new BatterySimConfig(DEFAULT_BATTERY_CAPACITY,
                                    DEFAULT_BUS_VOLTAGE,
                                    DEFAULT_INITIAL_SOC);
    }

}

