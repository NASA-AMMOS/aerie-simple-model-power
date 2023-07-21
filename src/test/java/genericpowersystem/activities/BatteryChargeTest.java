package gradle.test;

import genericpowersystem.activities.power.SolarPowerGeneration;
import genericpowersystem.generated.activities.power.SolarPowerGenerationMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BatteryChargeTest {
  private final SolarPowerGenerationMapper mapper;

  public BatteryChargeTest() {
    this.mapper = new SolarPowerGenerationMapper();
  }

  @Test
  public void testDefaultSerializationDoesNotThrow() {
    this.mapper.getInputType().getArguments(new SolarPowerGeneration());
  }
}
