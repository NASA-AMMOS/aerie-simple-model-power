package gradle.test;

import examplemodel.activities.ChangeGNCState;
import examplemodel.generated.activities.ChangeGNCStateMapper;
import org.junit.jupiter.api.Test;

class ChangeGNCStateTest {
    private final ChangeGNCStateMapper mapper;

    public ChangeGNCStateTest() {
        this.mapper = new ChangeGNCStateMapper();
    }

    @Test
    public void testDefaultSerializationDoesNotThrow() {
        this.mapper.getInputType().getArguments(new ChangeGNCState());
    }
}
