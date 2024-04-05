package powersystem;

import gov.nasa.jpl.aerie.contrib.streamline.core.Resource;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.Registrar;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.polynomial.Polynomial;

/**
 * Abstract class representing a spacecraft power source. This class contains the methods and fields required
 * to connect the power source to the BatteryModel
 */
public abstract class PowerSource {
    protected Resource<Polynomial> powerProduction;   //total power produced by power source (W)

    public Resource<Polynomial> getPowerProduction() {
        return this.powerProduction;
    }

    /**
     * Method for Aerie to register the resources in this model
     * @param registrar how Aerie knows what the resources are
     */
    public abstract void registerStates( Registrar registrar);

}
