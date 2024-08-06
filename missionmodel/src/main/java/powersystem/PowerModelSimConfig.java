package powersystem;

import demosystem.Configuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.AutoValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Template;

@AutoValueMapper.Record
public record PowerModelSimConfig(BatterySimConfig batteryConfig,
                                  SolarArraySimConfig powerSourceConfig) {

    public static final BatterySimConfig DEFAULT_BATTERY_CONFIG = BatterySimConfig.defaultConfiguration();

    // Power configuration to use for solar array model
    public static final SolarArraySimConfig DEFAULT_SOLAR_ARRAY_CONFIG = SolarArraySimConfig.defaultConfiguration();

    // Power Configuration to Use for RTG Model
    // public static final RtgSimConfig DEFAULT_RTG_SIM_CONFIG = RtgSimConfig.defaultConfiguration();

    public static @Template PowerModelSimConfig defaultConfiguration() {
        return new PowerModelSimConfig(DEFAULT_BATTERY_CONFIG,
                DEFAULT_SOLAR_ARRAY_CONFIG);
    }

}

