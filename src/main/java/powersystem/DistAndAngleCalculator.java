package powersystem;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

/**
 * This class updates the distance and angle automatically through daemon tasks.
 */
public class DistAndAngleCalculator {
    private GenericSolarArray arr;

    public DistAndAngleCalculator(GenericSolarArray arr) {
        this.arr = arr;
    }

    /**
     * Function to update distance
     */
    public void updateDistance() {
        double dCount = 0;
        while(true) {
            double value = dCount;
            arr.distance.set(value);
            dCount += 0.2;
            delay(Duration.of(2, Duration.HOURS));
        }
    }

    /**
     * Function to update angle
     */
    public void updateAngle() {
        int max = 90;
        int min = -90;
        int range = max - min + 1;
        while (true) {
            double value = (Math.random() * range) + min;
            arr.angle.set(value);
            delay(Duration.of(2, Duration.HOURS));
        }
    }

    /**
     * Function that constantly runs (called by the GenericSolarArray class) to update distance and angle
     */
    public void run() {
        //automatically updates the distance
        ModelActions.spawn(this::updateDistance);
        //automatically updates the angle
        ModelActions.spawn(this::updateAngle);
    }
}