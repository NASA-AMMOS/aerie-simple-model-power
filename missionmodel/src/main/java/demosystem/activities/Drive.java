package demosystem.activities;

import demosystem.Mission;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.discrete.DiscreteEffects;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

@ActivityType("Drive")
public class Drive {
    @Export.Parameter
    public double duration = 10.0; // hours

    @Export.Parameter
    public double initialPower = 10.0; // W

    @Export.Parameter
    public int numSteps = 10;

    @Export.Parameter
    public double powerIncrement = 5.0; // W

    @ActivityType.EffectModel
    public void run(Mission model) {
        double durPerStep = duration/numSteps;
        DiscreteEffects.set(model.pel.locomotionPower_CBE, initialPower);
        for (int i = 0; i < numSteps; i++) {
            delay(Duration.roundNearest(durPerStep, Duration.HOURS));
            DiscreteEffects.increase(model.pel.locomotionPower_CBE, powerIncrement);
        }
    }

}
