package powersystem.activities.power;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import demosystem.Mission;

@ActivityType("SolarPowerGeneration")
public class SolarPowerGeneration {

   // @Parameter public long duration = 50;




    @EffectModel
    public void run(Mission model) {
        model.battery.array.startSolarArrayDeployment();
        delay(Duration.of(30, Duration.MINUTES));
        model.battery.array.endSolarArrayDeployment();
        //delay(Duration.of(duration, Duration.HOURS));
        //model.battery.array.closeSolarArrays();
    }
}
