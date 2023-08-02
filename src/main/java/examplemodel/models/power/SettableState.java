package examplemodel.models.power;

import com.google.common.reflect.TypeToken;
import gov.nasa.jpl.aerie.contrib.models.Register;
import examplemodel.models.power.ArrayedStates.BaseBuilder1DWithInitValues;
import examplemodel.models.power.ArrayedStates.BaseBuilder2DWithInitValues;
import java.util.LinkedHashSet;
import java.util.Set;

/** @param <V> the value type of the state; must be immutable */
public class SettableState<V> extends ForwardingRegister<V> implements PublishingResource<V> {

    private final Register<V> delegate;
    private final LinkedHashSet<Subscriber> subscribers;
    private final TypeToken<V> valueType;

    public SettableState(final V initialValue, TypeToken<V> valueType) {
        this.delegate = Register.forImmutable(initialValue);
        this.subscribers = new LinkedHashSet<>();
        this.valueType = valueType;
    }

    public TypeToken<V> getClassToken() {
        return valueType;
    }

    @Override
    protected Register<V> delegate() {
        return delegate;
    }

    @Override
    public V getDynamics() {
        return delegate().getDynamics();
    }

    @Override
    public void set(V value) {
        super.set(value);
        this.updateSubscribers();
    }

    /**
     * Registers a subscriber on this publisher
     *
     * @param subscriber the object to register as a subscriber
     */
    @Override
    public void registerSubscriber(Subscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    /**
     * Returns the (unique) set of objects that are registered as subscriber on the current publisher
     *
     * @return the unique set of subscribers
     */
    @Override
    public Set<Subscriber> getSubscribers() {
        return subscribers;
    }

    public static <V> Builder<V> builder(Class<V> valueTypeClass) {
        return SettableState.builder(TypeToken.of(valueTypeClass));
    }

    public static <V> Builder<V> builder(TypeToken<V> valueTypeToken) {
        return new SettableState.Builder<V>(valueTypeToken);
    }

    public static <K, V> ArrayedBuilder1D<K, V> arrayedBuilder1D(Class<V> valueTypeClass) {
        return SettableState.arrayedBuilder1D(TypeToken.of(valueTypeClass));
    }

    public static <K, V> ArrayedBuilder1D<K, V> arrayedBuilder1D(TypeToken<V> valueTypeToken) {
        return new SettableState.ArrayedBuilder1D<K, V>(valueTypeToken);
    }

    public static <R, C, V> ArrayedBuilder2D<R, C, V> arrayedBuilder2D(Class<V> valueTypeClass) {
        return SettableState.arrayedBuilder2D(TypeToken.of(valueTypeClass));
    }

    public static <R, C, V> ArrayedBuilder2D<R, C, V> arrayedBuilder2D(TypeToken<V> valueTypeToken) {
        return new SettableState.ArrayedBuilder2D<R, C, V>(valueTypeToken);
    }

    public static final class Builder<V>
            extends StateBuilders.BaseBuilderWithInitValue<Builder<V>, SettableState<V>, V> {

        protected Builder(TypeToken<V> valueType) {
            super(valueType);
        }

        @Override
        protected Builder<V> getThis() {
            return this;
        }

        @Override
        public SettableState<V> build() {
            checkBuilderState();
            return new SettableState<>(getInitialValue(), getValueType());
        }
    }

    public static final class ArrayedBuilder1D<K, V>
            extends BaseBuilder1DWithInitValues<ArrayedBuilder1D<K, V>, K, SettableState<V>, V> {

        private ArrayedBuilder1D(TypeToken<V> valueType) {
            super(valueType);
        }

        @Override
        protected ArrayedBuilder1D<K, V> getThis() {
            return this;
        }

        @Override
        protected SettableState<V> createState(K key, V initialValue) {
            return SettableState.<V>builder(getValueType())
                    .initialValue(initialValue)
                    .build();
        }
    }

    public static final class ArrayedBuilder2D<R, C, V>
            extends BaseBuilder2DWithInitValues<ArrayedBuilder2D<R, C, V>, R, C, SettableState<V>, V> {

        public ArrayedBuilder2D(TypeToken<V> valueType) {
            super(valueType);
        }

        @Override
        protected ArrayedBuilder2D<R, C, V> getThis() {
            return this;
        }

        @Override
        protected SettableState<V> createState(R rowKey, C columnKey, V initialValue) {
            return SettableState.<V>builder(getValueType())
                    .initialValue(initialValue)
                    .build();
        }
    }
}

