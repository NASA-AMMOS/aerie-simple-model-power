package examplemodel.models.power;

import java.util.function.Function;
import gov.nasa.jpl.aerie.merlin.framework.CellRef;
import gov.nasa.jpl.aerie.merlin.framework.resources.real.RealResource;
import gov.nasa.jpl.aerie.merlin.protocol.types.RealDynamics;
import examplemodel.models.power.cells.SettableLinearIntegrationCell;
import examplemodel.models.power.effects.SettableLinearAccumulationEffect;

public class SettableAccumulator {

    public final Area area;
    public final Integrand integrand;

    public SettableAccumulator() {
        this(0.0, 0.0);
    }

    public SettableAccumulator(final double initialAreaValue, final double initialIntegrandValue) {
        final SettableLinearIntegrationCell cell =
                new SettableLinearIntegrationCell(initialAreaValue, initialIntegrandValue);
        final CellRef<SettableLinearAccumulationEffect, SettableLinearIntegrationCell> ref =
                SettableLinearIntegrationCell.allocate(initialAreaValue, initialIntegrandValue, Function.identity());

        this.area = new Area(ref);
        this.integrand = new Integrand(ref);
    }

    public static final class Area implements RealResource {
        private final CellRef<SettableLinearAccumulationEffect, SettableLinearIntegrationCell> ref;

        private Area(final CellRef<SettableLinearAccumulationEffect, SettableLinearIntegrationCell> ref) {
            this.ref = ref;
        }

        public void add(final double delta) {
            this.ref.emit(SettableLinearAccumulationEffect.addArea(delta));
        }

        @Override
        public RealDynamics getDynamics() {
            return ref.get().getArea();
        }
    }

    public static final class Integrand implements RealResource {
        private final CellRef<SettableLinearAccumulationEffect, SettableLinearIntegrationCell> ref;

        private Integrand(final CellRef<SettableLinearAccumulationEffect, SettableLinearIntegrationCell> ref) {
            this.ref = ref;
        }

        public void add(final double delta) {
            this.ref.emit(SettableLinearAccumulationEffect.addIntegrand(delta));
        }

        @Override
        public RealDynamics getDynamics() {
            return ref.get().getIntegrand();
        }
    }
}
