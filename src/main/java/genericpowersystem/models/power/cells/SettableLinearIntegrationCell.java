package genericpowersystem.models.power.cells;

import java.util.function.Function;
import gov.nasa.jpl.aerie.merlin.framework.CellRef;
import gov.nasa.jpl.aerie.merlin.protocol.model.CellType;
import gov.nasa.jpl.aerie.merlin.protocol.model.EffectTrait;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.merlin.protocol.types.RealDynamics;
import genericpowersystem.models.power.effects.SettableLinearAccumulationEffect;

/**
 * This class exists to service Clipper's needs for an IntegratedState, but these specific mechanics
 * are intended to be replaced with an Aerie-provided SettableAccumulator.
 *
 * <p>This class was directly inspired by
 * gov.nasa.jpl.aerie.contrib.cells.linear.LinearIntegrationCell}, and modifications have been made
 * to support discrete area mutations.
 */
public final class SettableLinearIntegrationCell {
    private double initialArea;
    private double accumulatedArea;
    private double integrand;

    public SettableLinearIntegrationCell(
            final double initialArea, final double integrand, final double accumulatedArea) {
        this.initialArea = initialArea;
        this.accumulatedArea = accumulatedArea;
        this.integrand = integrand;
    }

    public SettableLinearIntegrationCell(final double initialArea, final double integrand) {
        this(initialArea, integrand, 0.0);
    }

    public static <Event> CellRef<Event, SettableLinearIntegrationCell> allocate(
            final double initialArea,
            final double integrand,
            final Function<Event, SettableLinearAccumulationEffect> interpreter) {
        return CellRef.allocate(
                new SettableLinearIntegrationCell(initialArea, integrand),
                new SettableLinearIntegrationApplicator(),
                interpreter);
    }

    public RealDynamics getArea() {
        return RealDynamics.linear(this.initialArea + this.accumulatedArea, this.integrand);
    }

    public RealDynamics getIntegrand() {
        return RealDynamics.constant(this.integrand);
    }

    public static final class SettableLinearIntegrationApplicator
            implements CellType<SettableLinearAccumulationEffect, SettableLinearIntegrationCell> {
        @Override
        public SettableLinearIntegrationCell duplicate(final SettableLinearIntegrationCell cell) {
            return new SettableLinearIntegrationCell(
                    cell.initialArea, cell.integrand, cell.accumulatedArea);
        }

        @Override
        public void apply(
                final SettableLinearIntegrationCell cell, final SettableLinearAccumulationEffect effect) {
            effect.deltaIntegrand.ifPresent((deltaIntegrand) -> cell.integrand += deltaIntegrand);

            effect.deltaArea.ifPresent(
                    (deltaArea) -> {
                        final double currentTotalArea = cell.initialArea + cell.accumulatedArea;
                        final double newTotalArea = currentTotalArea + deltaArea;
                        // treat the total area as the new initial area and reset the accumulated area
                        cell.initialArea = newTotalArea;
                        cell.accumulatedArea = 0;
                    });
        }

        @Override
        public void step(final SettableLinearIntegrationCell cell, final Duration elapsedTime) {
            // Law: The passage of time shall not alter a valid dynamics.
            cell.accumulatedArea += cell.integrand * elapsedTime.ratioOver(Duration.SECOND);
        }

        @Override
        public EffectTrait<SettableLinearAccumulationEffect> getEffectType() {
            return SettableLinearAccumulationEffect.TRAIT;
        }
    }
}
