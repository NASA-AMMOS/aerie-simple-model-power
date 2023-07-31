package genericpowersystem.activities.power;

import genericpowersystem.models.power.Camera_State;
import genericpowersystem.models.power.GNC_State;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import genericpowersystem.Mission;

@ActivityType("ChangeGNCState")
public class ChangeGNCState {
    @Parameter public long duration = 10;

    @EffectModel
    public void run(Mission model) {
        model.battery.pel.gncState.set(GNC_State.TURNING);
        delay(Duration.of(duration, Duration.HOURS));
        model.battery.pel.gncState.set(GNC_State.NOMINAL);
    }
}
