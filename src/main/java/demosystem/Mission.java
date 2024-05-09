package demosystem;


import demosystem.models.pel.PELModel;
import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.Registrar;

import powersystem.BatteryModel;
import powersystem.GenericSolarArray;
import powersystem.PowerSource;
import powersystem.RtgPowerProduction;

import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.spawn;

public class Mission {

    public final PELModel pel;
    public DistAndAngleCalculator calculator;
    public PowerSource powerSource;
    public final BatteryModel cbebattery;
    public final BatteryModel mevbattery;

    public final Registrar errorRegistrar;

    public Mission(final gov.nasa.jpl.aerie.merlin.framework.Registrar registrar, final Instant planStart, final Configuration config) {
        this.calculator = new DistAndAngleCalculator();
        spawn(calculator::run);

        // Initialize Power States and Loads
        this.pel = new PELModel();
        // Initialize Power Source
        //this.powerSource = new GenericSolarArray(config.powerConfig().powerSourceConfig(), calculator.distance, calculator.angle, calculator.eclipseFactor);
        this.powerSource = new RtgPowerProduction(config.powerConfig().powerSourceConfig(), planStart);
        this.cbebattery = new BatteryModel("cbe", config.powerConfig().batteryConfig(), pel.cbeTotalLoad, powerSource.getPowerProduction());
        this.mevbattery = new BatteryModel("mev", config.powerConfig().batteryConfig(), pel.mevTotalLoad, powerSource.getPowerProduction());

        this.errorRegistrar = new Registrar(registrar, Registrar.ErrorBehavior.Log);

        pel.registerStates(this.errorRegistrar);
        powerSource.registerStates(this.errorRegistrar);
        cbebattery.registerStates(this.errorRegistrar);
        mevbattery.registerStates(this.errorRegistrar);

        // Spawn daemon task to compute RtgPower at a fixed sample rate
        spawn(((RtgPowerProduction)this.powerSource)::sampleRtgPower);

    }
}

