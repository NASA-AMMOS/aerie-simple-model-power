package examplemodel.activities;

import examplemodel.models.pel.Camera_State;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import examplemodel.Mission;

@ActivityType("TurnOnCamera")
public class TurnOnCamera {
    @Parameter public long duration = 10;

    @EffectModel
    public void run(Mission model) {
        model.battery.pel.cameraState.set(Camera_State.ON);
        delay(Duration.of(duration, Duration.HOURS));
        model.battery.pel.cameraState.set(Camera_State.OFF);
    }
}