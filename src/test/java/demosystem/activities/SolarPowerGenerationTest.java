package gradle.test;

import powersystem.activities.power.SolarPowerGeneration;
import demosystem.generatedpowersystem.activities.power.SolarPowerGenerationMapper;
import org.junit.jupiter.api.Test;

class SolarPowerGenerationTest {
  private final SolarPowerGenerationMapper mapper;

  public SolarPowerGenerationTest() {
    this.mapper = new SolarPowerGenerationMapper();
  }

  @Test
  public void testDefaultSerializationDoesNotThrow() {
    this.mapper.getInputType().getArguments(new SolarPowerGeneration());
  }
}
