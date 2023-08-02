package gradle.test;

import examplemodel.activities.GNCStateTurning;
import examplemodel.generated.activities.GNCStateTurningMapper;
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
