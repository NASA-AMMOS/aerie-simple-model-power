package powersystem;

import java.time.Instant;

import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.streamline.core.MutableResource;
import gov.nasa.jpl.aerie.contrib.streamline.core.monads.ResourceMonad;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.Registrar;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.black_box.Unstructured;

import gov.nasa.jpl.aerie.contrib.streamline.modeling.clocks.Clock;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.discrete.Discrete;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.discrete.DiscreteEffects;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.linear.Linear;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.polynomial.PolynomialEffects;
import gov.nasa.jpl.aerie.contrib.streamline.modeling.polynomial.PolynomialResources;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import static gov.nasa.jpl.aerie.contrib.streamline.core.MutableResource.resource;
import static gov.nasa.jpl.aerie.contrib.streamline.core.Resources.currentValue;
import static gov.nasa.jpl.aerie.contrib.streamline.modeling.black_box.UnstructuredResources.approximateAsLinear;
import static gov.nasa.jpl.aerie.contrib.streamline.modeling.discrete.Discrete.discrete;
import static gov.nasa.jpl.aerie.contrib.streamline.modeling.polynomial.PolynomialResources.asPolynomial;
import static gov.nasa.jpl.aerie.contrib.streamline.modeling.polynomial.PolynomialResources.asPolynomial$;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

public class RtgPowerProduction extends PowerSource {

    private MutableResource<Discrete<Double>> sampledRtgPower;

    private RtgSimConfig config;

    private MutableResource<Clock> durSincePlanStart;

    private Instant planStart;

    private final double YearToMillis = 365.25 * 24 * 3600 * 1000;

    public RtgPowerProduction(RtgSimConfig rtgSimConfig, Instant planStart) {
        this.config = rtgSimConfig;
        this.durSincePlanStart = resource(Clock.clock(Duration.ZERO));
        this.planStart = planStart;
        this.sampledRtgPower = resource(discrete(getRtgPower()));

        if (rtgSimConfig.sampleRtgPower()) {
            this.powerProduction = asPolynomial(this.sampledRtgPower);
        } else {
            MutableResource<Unstructured<Double>> unstructuredPower = resource(Unstructured.timeBased(t -> {
                // P(t1) = P(t0)*Exp(-k * (t1 - t0))
                long deltaMillis = Duration.addToInstant(planStart, t).toEpochMilli() - rtgSimConfig.decayStart().toEpochMilli();
                double newPower = rtgSimConfig.numRTGs() * rtgSimConfig.bolPowerPerRTG() *
                        Math.exp(-rtgSimConfig.decayRate()/100.0 * deltaMillis / YearToMillis); // decay rate is number of years
                return newPower;
            }));

            this.powerProduction = asPolynomial$( approximateAsLinear(unstructuredPower, 1e-4));
        }

    }

    private double getRtgPower() {
        Duration t = currentValue(durSincePlanStart);
        long deltaMillis = Duration.addToInstant(planStart, t).toEpochMilli() - config.decayStart().toEpochMilli();
        double newPower = config.numRTGs() * config.bolPowerPerRTG() *
                Math.exp(-config.decayRate()/100.0 * deltaMillis / YearToMillis); // decay rate is number of years
        return newPower;
    }

    public void sampleRtgPower() {
        DiscreteEffects.set(sampledRtgPower, getRtgPower());
        while(true) {
            delay(config.sampleRate());
            DiscreteEffects.set(sampledRtgPower, getRtgPower());
        }
    }


     // Method for Aerie to register the resources in this model
     // @param registrar how Aerie knows what the resources are
    @Override
    public void registerStates(Registrar registrar) {
        registrar.real("rtg.powerProduction",
                ResourceMonad.map(this.powerProduction, p -> Linear.linear( p.extract(), p.getCoefficient(1) )));
    }
}




