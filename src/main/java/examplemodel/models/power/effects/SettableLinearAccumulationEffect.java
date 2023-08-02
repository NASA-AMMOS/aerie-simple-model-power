package examplemodel.models.power.effects;

import gov.nasa.jpl.aerie.contrib.traits.CommutativeMonoid;
import gov.nasa.jpl.aerie.merlin.protocol.model.EffectTrait;
import java.util.OptionalDouble;
public class SettableLinearAccumulationEffect {

    public final OptionalDouble deltaIntegrand;
    public final OptionalDouble deltaArea;

    public SettableLinearAccumulationEffect(final OptionalDouble deltaIntegrand, final OptionalDouble deltaArea) {
        this.deltaIntegrand = deltaIntegrand;
        this.deltaArea = deltaArea;
    }

    public static SettableLinearAccumulationEffect addIntegrand(final double deltaIntegrand) {
        return new SettableLinearAccumulationEffect(OptionalDouble.of(deltaIntegrand), OptionalDouble.empty());
    }

    public static SettableLinearAccumulationEffect addArea(final double deltaArea) {
        return new SettableLinearAccumulationEffect(OptionalDouble.empty(), OptionalDouble.of(deltaArea));
    }

    public static SettableLinearAccumulationEffect empty() {
        return new SettableLinearAccumulationEffect(OptionalDouble.empty(), OptionalDouble.empty());
    }

    public static SettableLinearAccumulationEffect merge(
            SettableLinearAccumulationEffect left, SettableLinearAccumulationEffect right) {
        OptionalDouble combinedDeltaRate;
        if (left.deltaIntegrand.isEmpty() && right.deltaIntegrand.isEmpty()) {
            combinedDeltaRate = OptionalDouble.empty();
        } else {
            combinedDeltaRate = OptionalDouble.of(left.deltaIntegrand.orElse(0) + right.deltaIntegrand.orElse(0));
        }

        OptionalDouble combinedDeltaVolume;
        if (left.deltaArea.isPresent() && right.deltaArea.isPresent()) {
            throw new UnsupportedOperationException(
                    "GM: Don't currently know how to merge two concurrent deltaAreas...");
        } else if (left.deltaArea.isEmpty() && right.deltaArea.isEmpty()) {
            combinedDeltaVolume = OptionalDouble.empty();
        } else {
            combinedDeltaVolume = OptionalDouble.of(left.deltaArea.orElse(0) + right.deltaArea.orElse(0));
        }

        return new SettableLinearAccumulationEffect(combinedDeltaRate, combinedDeltaVolume);
    }

    public static final EffectTrait<SettableLinearAccumulationEffect> TRAIT =
            new CommutativeMonoid<>(SettableLinearAccumulationEffect.empty(), SettableLinearAccumulationEffect::merge);
}
