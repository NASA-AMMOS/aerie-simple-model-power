package genericpowersystem.models.pel;

/**
 * This class was created by the pel_java_generator.py script and represents the state(s) of the GNC as an enum and associates
 * a power load amount to each state.
 */

public enum GNC_State {
	NOMINAL(200.0),
	TURNING(100.0);
    private final double load;
    GNC_State(double load) {
        this.load = load;
    }

    /**
     * Function that returns the load of state of the instrument.
     * @return the power needed for that state
     */
    public double getLoad() {
        return load;
    }
}
    