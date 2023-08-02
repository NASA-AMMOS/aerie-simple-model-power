package examplemodel.models.power.cells;

import gov.nasa.jpl.aerie.merlin.framework.CellRef;
import gov.nasa.jpl.aerie.merlin.protocol.model.CellType;
import gov.nasa.jpl.aerie.merlin.protocol.model.EffectTrait;
import examplemodel.models.power.effects.ConcurrentRegisterEffect;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ConcurrentRegisterCell<T> {
    private final UnaryOperator<T> duplicator;

    private T value;
    private boolean conflicted;

    public ConcurrentRegisterCell(
            final UnaryOperator<T> duplicator, final T initialValue, final boolean conflicted) {
        this.duplicator = Objects.requireNonNull(duplicator);
        this.value = Objects.requireNonNull(initialValue);
        this.conflicted = conflicted;
    }

    public static <Event, T> CellRef<Event, ConcurrentRegisterCell<T>> allocate(
            final UnaryOperator<T> duplicator,
            final T initialValue,
            final Function<Event, ConcurrentRegisterEffect<T>> interpreter) {
        return CellRef.allocate(
                new ConcurrentRegisterCell<>(duplicator, initialValue, false),
                new ConcurrentRegisterApplicator<>(),
                interpreter);
    }

    public T getValue() {
        // Perform a defensive copy to prevent callers from accidentally mutating this Register.
        return this.duplicator.apply(this.value);
    }

    public boolean isConflicted() {
        return this.conflicted;
    }

    @Override
    public String toString() {
        return "{value=%s, conflicted=%s}".formatted(this.getValue(), this.isConflicted());
    }

    public static final class ConcurrentRegisterApplicator<T>
            implements CellType<ConcurrentRegisterEffect<T>, ConcurrentRegisterCell<T>> {
        @Override
        public ConcurrentRegisterCell<T> duplicate(final ConcurrentRegisterCell<T> cell) {
            return new ConcurrentRegisterCell<>(cell.duplicator, cell.value, cell.conflicted);
        }

        @Override
        public void apply(
                final ConcurrentRegisterCell<T> cell, final ConcurrentRegisterEffect<T> effect) {
            if (effect.newValue != null) {
                cell.value = effect.newValue;
                cell.conflicted = false;
            } else if (effect.conflicted) {
                cell.conflicted = true;
            }
        }

        @Override
        public EffectTrait<ConcurrentRegisterEffect<T>> getEffectType() {
            return new ConcurrentRegisterEffect.Trait<>();
        }
    }
}

