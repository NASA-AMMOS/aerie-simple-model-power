package gradle.test;

import genericpowersystem.activities.GNCStateTurning;
import genericpowersystem.generated.activities.GNCStateTurningMapper;
import org.junit.jupiter.api.Test;

class GNCStateTurningTest {
    private final GNCStateTurningMapper mapper;

    public GNCStateTurningTest() {
        this.mapper = new GNCStateTurningMapper();
    }

    @Test
    public void testDefaultSerializationDoesNotThrow() {
        this.mapper.getInputType().getArguments(new GNCStateTurning());
    }
}
