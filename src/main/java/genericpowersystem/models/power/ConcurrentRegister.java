package genericpowersystem.models.power;

import gov.nasa.jpl.aerie.merlin.framework.CellRef;
import gov.nasa.jpl.aerie.merlin.framework.resources.discrete.DiscreteResource;
import genericpowersystem.models.power.cells.ConcurrentRegisterCell;
import genericpowersystem.models.power.effects.ConcurrentRegisterEffect;

/**
 * {@code ConcurrentRegisters} are built to mirror standard {@link
 * gov.nasa.jpl.aerie.contrib.models.Register} objects, but with one key difference: concurrent
 * {@code set()} operations with the same value are accepted, the value change is registered, and
 * there is no conflict. By contrast, traditional {@code Registers} would consider concurrent {@code
 * set(newValue)} and {@code set(newValue)} operations conflicted, and the register's value would
 * remain at {@code priorValue}.
 *
 * <p>The primary use case for concurrent registers is to support {@link DerivedState}s and {@link
 * SemiLazilyDerivedState}s. These state types are backed by {@code Registers}, and depending upon
 * their dependencies and invocation patterns by clients, may experience simultaneous {@code set()}
 * operations on the underlying {@code Registers}.
 *
 * @param <Value> the value type of the resource; must be immutable
 */
public final class ConcurrentRegister<Value> implements DiscreteResource<Value> {

    private final CellRef<Value, ConcurrentRegisterCell<Value>> ref;

    private ConcurrentRegister(final Value initialValue) {
        this.ref = ConcurrentRegisterCell.allocate($ -> $, initialValue, ConcurrentRegisterEffect::set);
    }

    public static <Value> ConcurrentRegister<Value> create(final Value initialValue) {
        return new ConcurrentRegister<>(initialValue);
    }

    @Override
    public Value getDynamics() {
        return this.ref.get().getValue();
    }

    public boolean isConflicted() {
        return this.ref.get().isConflicted();
    }

    public void set(final Value value) {
        this.ref.emit(value);
    }
}

