package demosystem.activities;

import demosystem.Configuration;
import demosystem.generated.GeneratedModelType;
import gov.nasa.jpl.aerie.merlin.driver.*;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class SimulationTest {
    @Test
    void testSimulation() {
        final var simulationStartTime = Instant.now();
        final var simulationDuration = Duration.of(24, Duration.HOURS);

        final Map<ActivityDirectiveId, ActivityDirective> schedule = new HashMap<>();

//        schedule.put(new ActivityDirectiveId(1L), new ActivityDirective(
//                Duration.of(1, Duration.HOURS),
//                "TurnOnCamera",
//                Map.of("duration", SerializedValue.of(1)),
//                null,
//                true
//        ));


        final var results = simulate(Configuration.defaultConfiguration(), simulationStartTime, simulationDuration, schedule);
//        for (final var segment : results.discreteProfiles.get("/cameraState").getRight()) {
//            System.out.println(segment.extent() + " " + segment.dynamics());
//        }
    }

    public SimulationResults simulate(
            Configuration configuration,
            Instant simulationStartTime,
            Duration simulationDuration,
            Map<ActivityDirectiveId, ActivityDirective> schedule
    ) {
        return SimulationDriver.simulate(
                makeMissionModel(new MissionModelBuilder(), simulationStartTime, configuration),
                schedule,
                simulationStartTime,
                simulationDuration,
                simulationStartTime,
                simulationDuration,
                () -> { return false; }
        );
    }

    private static MissionModel<?> makeMissionModel(final MissionModelBuilder builder, final Instant planStart, final Configuration config) {
        final var factory = new GeneratedModelType();
        final var registry = DirectiveTypeRegistry.extract(factory);
        final var model = factory.instantiate(planStart, config, builder);
        return builder.build(model, registry);
    }
}