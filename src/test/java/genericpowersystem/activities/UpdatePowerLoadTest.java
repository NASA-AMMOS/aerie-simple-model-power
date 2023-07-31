package gradle.test;

import genericpowersystem.activities.power.UpdatePowerLoad;
import genericpowersystem.generated.activities.power.UpdatePowerLoadMapper;
import org.junit.jupiter.api.Test;

class UpdatePowerLoadTest {
    private final UpdatePowerLoadMapper mapper;

    public UpdatePowerLoadTest() {
      this.mapper = new UpdatePowerLoadMapper();
    }

    @Test
    public void testDefaultSerializationDoesNotThrow() {
      this.mapper.getInputType().getArguments(new UpdatePowerLoad());
    }
}

