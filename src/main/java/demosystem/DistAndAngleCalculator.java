package demosystem;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import powersystem.SettableState;


/**
 * This class updates the distance and arrayToSunAngle automatically through daemon tasks.
 */
public class DistAndAngleCalculator {
    public SettableState<Double> distance;  //distance of spacecraft from the Sun in AU
    public SettableState<Double> angle;  //arrayToSunAngle between the suns rays and the normal vector of the surface of the solar array (because of the spacecraft's orientation)

    public DistAndAngleCalculator() {
        this.distance = SettableState.builder(Double.class).initialValue(1.0).build();
        this.angle = SettableState.builder(Double.class).initialValue(-90.0).build();
    }

    /**
     * Function to update distance
     */
    public void updateDistance() {
        double dCount = 1.6;
        while(true) {
            double value = dCount;
            distance.set(value);
            dCount += 0.01;
            delay(Duration.of(1, Duration.HOURS));
        }
    }

    /**
     * Function to update arrayToSunAngle
     */
    public void updateAngle() {
        int max = 90;
        int min = -90;
        double currAngle = 45;
        while (true) {
            currAngle = currAngle + 10;
            if (currAngle > max) {
                currAngle = min + (currAngle-max);
            }
            angle.set(currAngle);
            delay(Duration.of(2, Duration.HOURS));
        }
    }

    /**
     * Function that constantly runs (called by the GenericSolarArray class) to update distance and arrayToSunAngle
     */
    public void run() {
        //automatically updates the distance
        ModelActions.spawn(this::updateDistance);
        //automatically updates the arrayToSunAngle
        ModelActions.spawn(this::updateAngle);
    }
}
