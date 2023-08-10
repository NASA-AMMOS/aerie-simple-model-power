package demosystem;


import demosystem.models.pel.PELModel;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import powersystem.SettableState;

import powersystem.BatteryModel;
import powersystem.GenericSolarArray;

public class Mission {

    public final PELModel pel;
    public DistAndAngleCalculator calculator;
    public GenericSolarArray array;
    public final BatteryModel cbebattery;
    public final BatteryModel mevbattery;



    public Mission(final Registrar registrar, final Configuration config) {
        SettableState<Double> area = SettableState.builder(Double.class).initialValue(5.0).build();
        this.calculator = new DistAndAngleCalculator();
        ModelActions.spawn(calculator::run);

        this.pel = new PELModel();
        this.array = new GenericSolarArray(area, calculator.distance, calculator.angle);
        this.cbebattery = new BatteryModel(32.0, 220.0, pel.cbeTotalLoad, array.solarInputPower, "cbe");
        this.mevbattery = new BatteryModel(32.0, 220.0, pel.mevTotalLoad, array.solarInputPower, "mev");

        pel.registerStates(registrar);
        array.registerStates(registrar);
        cbebattery.registerStates(registrar);
        mevbattery.registerStates(registrar);
    }
}

