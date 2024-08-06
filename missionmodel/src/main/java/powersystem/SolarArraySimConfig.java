package powersystem;

import demosystem.Configuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.AutoValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Template;

@AutoValueMapper.Record
public record SolarArraySimConfig(ArrayDeploymentStates deploymentState,
                                  Double arrayMechArea,
                                  Double packingFactor,
                                  Double cellEfficiency,
                                  Double conversionEfficiency,
                                  Double otherLosses) {


    // Mechanical area of the arrays (m^2)
    public static final Double DEFAULT_ARRAY_MECH_AREA = 5.0;

    // Ratio between active cell and mechanical area on the
    // arrays (how dense are the arrays packed)
    public static final Double DEFAULT_PACKING_FACTOR = 0.9;

    // The efficiency of each solar cell
    public static final Double DEFAULT_CELL_EFFICIENCY = 0.28;

    // Loss factor for converting raw power from the arrays to power
    // usable by the spacecraft (e.g. the use of peak power tracking)
    public static final Double DEFAULT_CONVERSION_EFFICIENCY = 0.9;

    // Loss factor for other phenomenon/inefficiencies (e.g. shadowing,
    // cell mismatch, cover glass)
    public static final Double DEFAULT_OTHER_LOSSES = 1.0;

    // Loss factor for other phenomenon/inefficiencies (e.g. shadowing,
    // cell mismatch, cover glass)
    public static final ArrayDeploymentStates DEFAULT_DEPLOYMENT_STATE = ArrayDeploymentStates.DEPLOYED;


    public static @Template SolarArraySimConfig defaultConfiguration() {
        return new SolarArraySimConfig(
                DEFAULT_DEPLOYMENT_STATE,
                DEFAULT_ARRAY_MECH_AREA,
                DEFAULT_PACKING_FACTOR,
                DEFAULT_CELL_EFFICIENCY,
                DEFAULT_CONVERSION_EFFICIENCY,
                DEFAULT_OTHER_LOSSES);
    }

}