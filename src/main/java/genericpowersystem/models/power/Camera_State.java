package genericpowersystem.models.power;
public enum Camera_State {
	OFF(0.0),
	ON(600.0);
    private final double load;
    Camera_State(double load) {
        this.load = load;
    }
    public double getLoad() {
        return load;
    }
}
    