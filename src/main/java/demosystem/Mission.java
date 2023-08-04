package demosystem;


import demosystem.models.pel.PELModel;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import powersystem.BatteryModel;
import powersystem.GenericSolarArray;



public class Mission {

    public final PELModel pel;
    public DistAndAngleCalculator calculator;
    public GenericSolarArray array;
    public final BatteryModel battery;



    public Mission(final Registrar registrar, final Configuration config) {
        this.calculator = new DistAndAngleCalculator();
        ModelActions.spawn(calculator::run);
        this.pel = new PELModel();
        this.array = new GenericSolarArray(8.0, calculator.distance, calculator.angle);
        this.battery = new BatteryModel(32.0, 220.0, pel.totalLoad, array);
        pel.registerStates(registrar);
        battery.registerStates(registrar);
    }
}

