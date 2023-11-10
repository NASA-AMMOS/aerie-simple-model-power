package demosystem.activities;

import demosystem.models.pel.Telecom_State;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import demosystem.Mission;

@ActivityType("TurnOnTelecom")
public class TurnOnTelecom {
    @Parameter public long duration = 10;

    @EffectModel
    public void run(Mission model) {
        model.pel.telecomState.set(Telecom_State.ON);
        delay(Duration.of(duration, Duration.HOURS));
        model.pel.telecomState.set(Telecom_State.OFF);
    }
}
