package demosystem.models.pel;
/**
* This class was created by the pel_java_generator.py script and represents the state(s) of the Camera as an enum and associates
* a power load amount to each state.
*/
public enum Camera_State {
	OFF(0.0, 0.0),
	ON(150.0, 165.0);
    private final double cbeload;
    private final double mevload;
    Camera_State(double cbeload, double mevload) {
        this.cbeload = cbeload;  //in Watts
        this.mevload = mevload; //in Watts
    }
    
    /**
    * Function that returns the cbe load of state of the instrument.
    * @return the power needed for that state
    */
    public double getCBELoad() {
        return cbeload;  
    }
    
    /**
    * Function that returns the mev load of state of the instrument.
    * @return the power needed for that state
    */
    public double getMEVLoad() {
        return mevload;  
    }
}
