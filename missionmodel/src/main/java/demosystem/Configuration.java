package demosystem;

import powersystem.PowerModelSimConfig;

import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Template;

public record Configuration(PowerModelSimConfig powerConfig) {

  public static final PowerModelSimConfig POWER_CONFIG = PowerModelSimConfig.defaultConfiguration();
  public static @Template Configuration defaultConfiguration() {
    return new Configuration(POWER_CONFIG);
  }

}
