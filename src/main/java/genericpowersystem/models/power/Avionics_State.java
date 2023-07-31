package genericpowersystem.models.power;
public enum Avionics_State {
	ON(500.0);
    private final double load;
    Avionics_State(double load) {
        this.load = load;
    }
    public double getLoad() {
        return load;
    }
}
    