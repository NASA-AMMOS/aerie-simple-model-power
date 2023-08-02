package gradle.test;

import examplemodel.activities.GNCStateNominal;
import examplemodel.generated.activities.GNCStateNominalMapper;
import org.junit.jupiter.api.Test;

class GNCStateNominalTest {
    private final GNCStateNominalMapper mapper;

    public GNCStateNominalTest() {
        this.mapper = new GNCStateNominalMapper();
    }

    @Test
    public void testDefaultSerializationDoesNotThrow() {
        this.mapper.getInputType().getArguments(new GNCStateNominal());
    }
}
