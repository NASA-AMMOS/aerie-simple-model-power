package gradle.test;

import genericpowersystem.activities.power.TurnOnTelecomm;
import genericpowersystem.generated.activities.power.TurnOnTelecommMapper;
import org.junit.jupiter.api.Test;

class TurnOnTelecommTest {
    private final TurnOnTelecommMapper mapper;

    public TurnOnTelecommTest() {
        this.mapper = new TurnOnTelecommMapper();
    }

    @Test
    public void testDefaultSerializationDoesNotThrow() {
        this.mapper.getInputType().getArguments(new TurnOnTelecomm());
    }
}
