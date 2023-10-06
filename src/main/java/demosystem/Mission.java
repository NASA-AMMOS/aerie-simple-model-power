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
        this.calculator = new DistAndAngleCalculator();
        ModelActions.spawn(calculator::run);

        this.pel = new PELModel();
        this.array = new GenericSolarArray(config.powerConfig().solarArrayConfig(), calculator.distance, calculator.angle);
        this.cbebattery = new BatteryModel("cbe", config.powerConfig().batteryConfig(), pel.cbeTotalLoad, array.powerProduction);
        this.mevbattery = new BatteryModel("mev", config.powerConfig().batteryConfig(), pel.mevTotalLoad, array.powerProduction);

        pel.registerStates(registrar);
        array.registerStates(registrar);
        cbebattery.registerStates(registrar);
        mevbattery.registerStates(registrar);
    }
}

