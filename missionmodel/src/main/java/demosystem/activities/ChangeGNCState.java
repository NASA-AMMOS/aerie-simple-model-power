package demosystem.activities;

import demosystem.models.pel.GNC_State;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.discrete.DiscreteEffects;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import demosystem.Mission;

@ActivityType("ChangeGNCState")
public class ChangeGNCState {
    @Parameter public GNC_State newState = GNC_State.TURNING;

    @EffectModel
    public void run(Mission model) {
        DiscreteEffects.set(model.pel.gncState, newState);
    }
}