package genericpowersystem.models.power;

import gov.nasa.jpl.aerie.merlin.framework.resources.real.RealResource;

public class BatteryModel {
    public double busVoltage;
    public double batteryCapacityAH;
    public double batteryCapacityWH;
    public DerivedState<Double> netPowerW;   //represents the net power into/out of the battery
    public SettableState<Double> sinkPowerW;   //represents how much power is required by the spacecraft
    //public RealResource batterySOC;
    public DerivedState<Double> batterySOC;
    //public double initialBatteryChargeWH = 0.0;
    public double initialBatterySOC;
    public IntegratedState integratedNetPower;
    public GenericSolarArray array;    //solar array that effects the charging of the battery

    public BatteryModel(double busVoltage, double batteryCapacityAH, double initialBatterySOC) {
        this.busVoltage = busVoltage;
        this.batteryCapacityAH = batteryCapacityAH;
        this.batteryCapacityWH = this.batteryCapacityAH * this.busVoltage;
        this.initialBatterySOC = initialBatterySOC;
        //this.initialBatteryChargeWH = convertSOCtoWH(initialBatterySOC);
        this.sinkPowerW = SettableState.builder(Double.class).initialValue(5.0).build();
        this.array = new GenericSolarArray(5.0);
        this.netPowerW = DerivedState.builder(Double.class)
                .sourceStates(this.array.solarInputPower, this.sinkPowerW)
                .valueFunction(this::computeNetPowerW)
                .build();
        this.integratedNetPower = IntegratedState.builder()
                .integrandState(this.netPowerW)
                .initialValue(0.0)
                .build();

        this.batterySOC = DerivedState.builder(Double.class)
                .sourceStates(this.netPowerW)
                .valueFunction(this::updateSOC)
                .build();

        //this.batterySOC = RealResource.scaleBy(1.0/batteryCapacityWH, this.integratedNetPower).scaledBy(100);
        //this.batterySOC = updateSOC();
    }


    public double convertWToA(double watts) {
        double amps = watts / busVoltage;
        return amps;
    }

    public double convertAToW(double amps) {
        double watts = amps * busVoltage;
        return watts;
    }

    public double convertSOCtoAH(double soc) {
        double ah = (soc * batteryCapacityAH) / 100.0;
        return ah;
    }

    public double convertSOCtoWH(double soc) {
        double wh = (soc * batteryCapacityWH) / 100.0;
        return wh;
    }


    //computes the net power based on how much power is required by the spacecraft and how much power is being generated
    //the solar array
    //updates the SOC of the battery after computing the new net power
    public double computeNetPowerW() {
        return array.solarInputPower.get() - sinkPowerW.get();
    }

    //calculates the state of charge of the battery by integrating the net power and checking if the battery is full or not
    //already
    public double updateSOC() {
        /**
        RealResource incCharge = RealResource.scaleBy(1.0/batteryCapacityWH, this.integratedNetPower).scaledBy(100);
        if (batterySOC == null) {
            return RealResource.scaleBy(0.0, integratedNetPower);
        }
        if ((batterySOC.get() + incCharge.get()) > 100.0) {
            return RealResource.scaleBy(1.0/integratedNetPower.get(), integratedNetPower).scaledBy(100);
        } else if ((batterySOC.get() + incCharge.get()) < 0.0) {
            return RealResource.scaleBy(0.0, integratedNetPower);
        }
        return RealResource.add(batterySOC, incCharge);
        //another thing you could do is pass in the expression for incCharge into the if/else statements and see if that works
        */
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


    //adds to the power required by the spacecraft
    public void addSink(double rate) {
        var curr = this.sinkPowerW.get();
        this.sinkPowerW.set(rate + curr);
    }

    //removes from the power required by the spacecraft
    public void removeSink(double rate) {
        var curr = this.sinkPowerW.get();
        this.sinkPowerW.set(curr - rate);
    }

}
