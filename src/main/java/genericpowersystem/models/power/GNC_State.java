package genericpowersystem.models.power;
public enum GNC_State {
	NOMINAL(200.0),
	TURNING(100.0);
    private final double load;
    GNC_State(double load) {
        this.load = load;
    }
    public double getLoad() {
        return load;
    }
}
    