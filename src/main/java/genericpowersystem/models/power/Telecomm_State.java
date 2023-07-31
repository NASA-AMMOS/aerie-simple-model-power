package genericpowersystem.models.power;
public enum Telecomm_State {
	OFF(0.0),
	ON(800.0);
    private final double load;
    Telecomm_State(double load) {
        this.load = load;
    }
    public double getLoad() {
        return load;
    }
}
    