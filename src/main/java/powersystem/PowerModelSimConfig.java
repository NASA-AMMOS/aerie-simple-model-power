package powersystem;

import demosystem.Configuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.AutoValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Template;

@AutoValueMapper.Record
public record PowerModelSimConfig(BatterySimConfig batteryConfig,
                                  SolarArraySimConfig solarArrayConfig) {

    public static final BatterySimConfig DEFAULT_BATTERY_CONFIG = BatterySimConfig.defaultConfiguration();

    public static final SolarArraySimConfig DEFAULT_SOLAR_ARRAY_CONFIG = SolarArraySimConfig.defaultConfiguration();

    public static @Template PowerModelSimConfig defaultConfiguration() {
        return new PowerModelSimConfig(DEFAULT_BATTERY_CONFIG,
                                       DEFAULT_SOLAR_ARRAY_CONFIG);
    }

}

