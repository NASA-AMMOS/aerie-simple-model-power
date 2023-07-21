package gradle.test;

import genericpowersystem.activities.power.UpdatePowerLoad;
import genericpowersystem.generated.activities.power.UpdatePowerLoadMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BatteryDischargeTest {
  private final UpdatePowerLoadMapper mapper;

  public BatteryDischargeTest() {
    this.mapper = new UpdatePowerLoadMapper();
  }

  @Test
  public void testDefaultSerializationDoesNotThrow() {
    this.mapper.getInputType().getArguments(new UpdatePowerLoad());
  }
}

