package powersystem.activities.power;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import demosystem.Mission;
import powersystem.ArrayDeploymentStates;

@ActivityType("SolarArrayDeployment")
public class SolarArrayDeployment {

   @Parameter
   public double deployDuration = 30; // minutes

    @EffectModel
    public void run(Mission model) {
        model.array.setSolarArrayDeploymentState(ArrayDeploymentStates.DEPLOYING);
        delay(Duration.roundNearest(deployDuration, Duration.MINUTES));
        model.array.setSolarArrayDeploymentState(ArrayDeploymentStates.DEPLOYED);
    }
}
