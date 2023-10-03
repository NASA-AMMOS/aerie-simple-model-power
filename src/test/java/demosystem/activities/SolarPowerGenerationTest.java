package demosystem.activities;

import powersystem.activities.power.SolarArrayDeployment;
import demosystem.generatedpowersystem.activities.power.SolarArrayDeploymentMapper;
import org.junit.jupiter.api.Test;

class SolarPowerGenerationTest {
  private final SolarArrayDeploymentMapper mapper;

  public SolarPowerGenerationTest() {
    this.mapper = new SolarArrayDeploymentMapper();
  }

  @Test
  public void testDefaultSerializationDoesNotThrow() {
    this.mapper.getInputType().getArguments(new SolarArrayDeployment());
  }
}
