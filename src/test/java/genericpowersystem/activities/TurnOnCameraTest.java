package gradle.test;

import genericpowersystem.activities.power.TurnOnCamera;
import genericpowersystem.generated.activities.power.TurnOnCameraMapper;
import org.junit.jupiter.api.Test;

class TurnOnCameraTest {
    private final TurnOnCameraMapper mapper;

    public TurnOnCameraTest() {
        this.mapper = new TurnOnCameraMapper();
    }

    @Test
    public void testDefaultSerializationDoesNotThrow() {
        this.mapper.getInputType().getArguments(new TurnOnCamera());
    }
}
