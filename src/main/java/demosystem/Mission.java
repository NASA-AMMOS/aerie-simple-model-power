package demosystem;


import demosystem.models.pel.PELModel;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import powersystem.BatteryModel;



public class Mission {

    public final PELModel pel = new PELModel();
    public final BatteryModel battery = new BatteryModel(32.0, 220.0, 10.0, pel.totalLoad);


    public Mission(final Registrar registrar, final Configuration config) {
        pel.registerStates(registrar);
        battery.registerStates(registrar);
    }
}

/**
 registrar.discrete("battery.powerLoadW", battery.powerLoadW, new DoubleValueMapper());
 registrar.discrete("battery.array.distance", battery.array.distance, new DoubleValueMapper());
 registrar.discrete("battery.array.angle", battery.array.angle, new DoubleValueMapper());
 registrar.discrete("battery.array.solarInputPower", battery.array.solarInputPower, new DoubleValueMapper());
 registrar.discrete("battery.netPowerW", battery.netPowerW, new DoubleValueMapper());
 registrar.discrete("battery.actualNetPowerW", battery.actualNetPowerW, new DoubleValueMapper());
 //registrar.discrete("battery.batterySOC", battery.batterySOC, new DoubleValueMapper());
 registrar.real("battery.batterySOC", battery.batterySOC);
 registrar.real("battery.integratedNetPower", battery.integratedNetPower);
 */
