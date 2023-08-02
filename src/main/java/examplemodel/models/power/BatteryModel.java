package examplemodel.models.power;

import gov.nasa.jpl.aerie.merlin.framework.ModelActions;
import gov.nasa.jpl.aerie.merlin.framework.resources.real.RealResource;
import examplemodel.models.pel.PELModel;


/**
 * This class represents the battery of a spacecraft. It is tied to a solar array (a separate class) and the power load
 * of the spacecraft. The battery's state of charge is dependent on the net power - how much power is being generated by
 * the spacecraft and how much power the spacecraft needs. The battery state of charge will not change if the power being
 * generated by the solar array is equal to the power load of the spacecraft (net power = 0). However, if net power is not
 * equal to 0 then the battery will be charged/discharged and the state of charge will reflect that.
 */

public class BatteryModel {
    public double busVoltage;  //the voltage of the battery in volts
    public double batteryCapacityAH; //the battery's capacity in amp-hours
    public double batteryCapacityWH; //the battery's capacity in watt-hours
    public DerivedState<Double> actualNetPowerW;  //represents the net power into/out of the battery
    public DerivedState<Double> netPowerW;   //net power for the computation of batterySOC
    //public SettableState<Double> powerLoadW;
    public DerivedState<Double> powerLoadW; //represents how much power is required by the spacecraft, connected to the PEL
    public RealResource batterySOC;  //the state of charge of the battery
    //public DerivedState<Double> batterySOC;
    //public double initialBatteryChargeWH = 0.0;
    public double initialBatterySOC;  //the initial battery state of charge before any charging/discharging
    public SettableState<Boolean> batteryFull;  //whether the battery is at 100% or not
    public SettableState<Boolean> batteryEmpty;  //whether the battery is at 0% or not
    public IntegratedState integratedNetPower;   //the integration of the net power, represents how much the battery was
                                                // charged/discharged and helps calculate the SOC
    public GenericSolarArray array;    //solar array that effects the charging of the battery
    public BatterySOCController controller; //helpful in determining what state the battery is in (full or empty) and
                                            // limits the integrated net power between 0 to battery capacity in watt-hours
    public PELModel pel;

    /**
     * The constructor for the battery model
     * @param busVoltage the voltage of the battery
     * @param batteryCapacityAH the capacity of the battery in amp-hours
     * @param initialBatterySOC the initial battery state of charge
     */

    public BatteryModel(double busVoltage, double batteryCapacityAH, double initialBatterySOC) {
        this.busVoltage = busVoltage;
        this.batteryCapacityAH = batteryCapacityAH;
        this.batteryCapacityWH = this.batteryCapacityAH * this.busVoltage;
        this.pel = new PELModel();
        this.initialBatterySOC = initialBatterySOC;
        //this.powerLoadW = SettableState.builder(Double.class).initialValue(0.0).build();
        this.powerLoadW = DerivedState.builder(Double.class)
                .sourceStates(this.pel.avionicsState, this.pel.telecommState, this.pel.cameraState, this.pel.gncState)
                .valueFunction(this::computeLoad)
                .build();
        this.array = new GenericSolarArray(5.0);
        this.batteryFull = SettableState.builder(Boolean.class).initialValue(false).build();
        this.batteryEmpty = SettableState.builder(Boolean.class).initialValue(true).build();
        this.actualNetPowerW = DerivedState.builder(Double.class)
                .sourceStates(this.array.solarInputPower, this.powerLoadW)
                .valueFunction(this::computeNetPowerW)
                .build();
        this.netPowerW = DerivedState.builder(Double.class)
                .sourceStates(this.array.solarInputPower, this.powerLoadW, this.batteryFull, this.batteryEmpty)
                .valueFunction(this::netPowerWBattery)
                .build();
        this.integratedNetPower = IntegratedState.builder()
                .integrandState(this.netPowerW)
                .initialValue(0.0)
                .build();
/**
        this.batterySOC = DerivedState.builder(Double.class)
                .sourceStates(this.netPowerW)
                .valueFunction(this::updateSOC)
                .build();
 */
        this.batterySOC = RealResource.scaleBy(1.0/batteryCapacityWH, this.integratedNetPower).scaledBy(100);
        this.controller = new BatterySOCController(this);
        ModelActions.spawn(controller::run);
    }


    /**
     * Function that computes the actual net power of the spacecraft based on how much power is being generated by the solar
     * array and how much power is required by the spacecraft (the power load)
     * @return the net power of the system
     */
    public double computeNetPowerW() {
        return array.solarInputPower.get() - powerLoadW.get();
    }


    /**
     * Function that is useful is determining the integrated net power and the battery SOC, helps limit the integrated
     * power from 0 to battery capacity and the battery SOC from 0 to 100, a modified net power function
     * @return the net power to be used for the integrated power and the battery SOC
     */
    public double netPowerWBattery() {
        double net = (array.solarInputPower.get() - powerLoadW.get()) / 3600;
        if (batteryFull.get()) {
            return Math.min(net, 0.0);
        } else if (batteryEmpty.get()) {
            return Math.max(net, 0.0);
        } else {
            return net;
        }
    }


    /**
     * Calculates the state of charge of the battery by integrating the net power and limiting the battery SOC based on
     * if it is full or empty
     * @return the battery SOC, a value between 0 and 100
     */
    public double updateSOC() {
        double incCharge = (integratedNetPower.get() / batteryCapacityWH) * 100;
        if (batterySOC == null) {
            return this.initialBatterySOC + incCharge;
        }
        if ((batterySOC.get() + incCharge) > 100.0) {   //if current battery SOC + the power flowing in is greater than 100%, set the battery SOC to 100%
            return 100.0;
        } else if ((batterySOC.get() + incCharge) < 0.0) { //if the current battery SOC + the power flowing out is less than 0%, set the battery SOC to 0%
            return 0.0;
        }
        return (batterySOC.get() + incCharge);
    }

    /**
     * Computes the power load of the spacecraft so the battery will be discharged accordingly, value changes whenever
     * the states of the instruments change
     * @return the power load of the spacecraft
     */

    public double computeLoad() {
        return this.pel.avionicsState.get().getLoad() + this.pel.cameraState.get().getLoad() + this.pel.telecommState.get().getLoad() + this.pel.gncState.get().getLoad();
    }


}