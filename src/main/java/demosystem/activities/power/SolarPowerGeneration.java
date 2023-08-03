package demosystem.activities.power;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import demosystem.Mission;

@ActivityType("SolarPowerGeneration")
public class SolarPowerGeneration {

    //@Parameter public double angleDeg = 90.0;  //angle between normal vector of array and sun radiation angle
    //@Parameter public double dist = 1;  //distance between arrays and Sun in AU
    //@Parameter public double area = 5; //area of array in m^2
    @Parameter public long duration = 50;

/**

    @Validation("angleDeg should be >= -90 and <= 90")
    public boolean checkAngle() {
        return angleDeg >= -90.0 && angleDeg <= 90.0;
    }
 */




    @EffectModel
    public void run(Mission model) {
        //model.battery.array.distance.set(dist);
        //model.battery.array.angle.set(angleDeg);
        //model.battery.array.area.set(area);
        model.battery.array.startSolarArrayDeployment();
        delay(Duration.of(1, Duration.MINUTES));
        model.battery.array.endSolarArrayDeployment();
        delay(Duration.of(duration, Duration.HOURS));
        model.battery.array.closeSolarArrays();
    }
}
