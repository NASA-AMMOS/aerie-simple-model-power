package gradle.test;

import demosystem.activities.TurnOnCamera;
import demosystem.generated.activities.TurnOnCameraMapper;
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
