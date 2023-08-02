package examplemodel.models.pel;

/**
 * This class was created by the pel_java_generator.py script and represents the state(s) of the Camera as an enum and associates
 * a power load amount to each state.
 */

public enum Camera_State {
	OFF(0.0),
	ON(200.0);
    private final double load;
    Camera_State(double load) {
        this.load = load;  //in Watts
    }

    /**
     * Function that returns the load of state of the instrument.
     * @return the power needed for that state
     */
    public double getLoad() {
        return load;  
    }
}
    