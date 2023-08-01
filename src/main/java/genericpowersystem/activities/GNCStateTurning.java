package genericpowersystem.activities;


import genericpowersystem.models.pel.GNC_State;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import genericpowersystem.Mission;

@ActivityType("GNCStateTurning")
public class GNCStateTurning {
    //@Parameter public long duration = 10;

    @EffectModel
    public void run(Mission model) {
        model.battery.pel.gncState.set(GNC_State.TURNING);
        //delay(Duration.of(duration, Duration.HOURS));
        //model.battery.pel.gncState.set(GNC_State.NOMINAL);
    }
}
