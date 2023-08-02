package examplemodel.models.power;

import com.google.common.collect.ImmutableSet;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.merlin.framework.Condition;
import java.util.Set;

public abstract class ForwardingRegister<V> {

    protected abstract Register<V> delegate();

    public void set(final V value) {
        delegate().set(value);
    }

    public V get() {
        return delegate().get();
    }

    public Condition isOneOf(final Set<V> values) {
        return delegate().isOneOf(values);
    }

    @SafeVarargs
    public final Condition isOneOf(final V... values) {
        return delegate().isOneOf(ImmutableSet.copyOf(values));
    }

    public Condition is(final V value) {
        return delegate().is(value);
    }
}

