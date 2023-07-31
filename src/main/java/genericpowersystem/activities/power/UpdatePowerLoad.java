package genericpowersystem.activities.power;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Validation;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import genericpowersystem.Mission;

@ActivityType("UpdatePowerLoad")

public class UpdatePowerLoad {
    @Parameter public double powerReq = 100.0; //in watts which is a unit of power (a rate)
    @Parameter public long duration = 100;
    //@Parameter public double area = 5;
    @Validation("powerReq has to be positive")
    public boolean checkPowerReq() {
        return powerReq > 0;
    }

    @EffectModel
    public void run(Mission model) {
        //model.battery.array.area.set(area);
        //model.battery.addLoad(powerReq);
        delay(Duration.of(duration, Duration.HOURS));
        //model.battery.removeLoad(powerReq);
     }
}
