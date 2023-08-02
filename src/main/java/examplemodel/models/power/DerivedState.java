package examplemodel.models.power;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.spawn;
import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.SECONDS;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.reflect.TypeToken;
import gov.nasa.jpl.aerie.merlin.framework.Condition;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Derived states are states whose values are computed by some function of one or more source states
 *
 * <p>Derived states are eagerly-derived, meaning they experience value updates when any of their
 * source states' values are updated. These source states "publish" a notification that they have
 * experienced a value change, and the derived state will re-evaluate its value function to update
 * its value.
 *
 * <p>Derived states are inherently discretized. Their values only update when a source state is
 * changed, and right now, only states which publish discretely-valued changes are supported as
 * source states. For state types with an integral relationship, see {@link IntegratedState}, or for
 * continuously-valued states with derived relationships, explore Aerie's linear options for {@code
 * RealResource} operations.
 *
 * @param <V> the value type of the given derived state
 */
public class DerivedState<V> implements PublishingResource<V>, Subscriber {

    private final ConcurrentRegister<V> register;
    private final Supplier<V> valueFunction;
    private final LinkedHashSet<Subscriber> subscribers;

    public DerivedState(final Set<? extends Publisher> sourceStates, final Supplier<V> valueFunction) {
        final var initialValue = valueFunction.get();
        this.register = ConcurrentRegister.create(initialValue);
        this.valueFunction = valueFunction;
        this.subscribers = new LinkedHashSet<>();
        sourceStates.forEach(sourceState -> sourceState.registerSubscriber(this));
    }

    @Override
    public V getDynamics() {
        return register.getDynamics();
    }

    public Condition is(final V value) {
        return register.is(value);
    }

    /**
     * Registers a subscriber on this publisher
     *
     * @param subscriber the object to register as a subscriber
     */
    @Override
    public void registerSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
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

    /** method invoked when object should updates its value, eg by reassessing inputs */
    @Override
    public void update() {
        // could spawn helper task to ensure that this is causally after the transactions are completed
        // for multiple publishers
        // spawn and delay within spawn (to prevent blocking the activity)
        final var currValue = register.get();
        final var newValue = valueFunction.get();
        if (!currValue.equals(newValue)) {
            register.set(newValue);
            updateSubscribers();
            resolveIfNecessary();
        }
    }

    /**
     * A conflict is unlikely but possible; one such scenario is where two separate tasks (activities
     * or daemons) run concurrently, and both the left and right tasks perform set() operations on
     * different source states for this derived state. Both tasks would trigger the update operation
     * for this state, and these would be considered concurrent set() operations upon the underlying
     * register with different values because the left change to the source state is not observable
     * from within the right task. The proper way to resolve this is to perform a conflict check at a
     * time that is causally after both of the left and right tasks (so that any actions undertaken by
     * both are observable) and to update the value given improved observability.
     */
    private void resolveIfNecessary() {
        spawn(() -> {
            do {
                delay(0, SECONDS);
                update();
            } while (this.register.isConflicted());
        });
    }

    public static <V> Builder<V> builder(Class<V> valueTypeClass) {
        return builder(TypeToken.of(valueTypeClass));
    }

    public static <V> Builder<V> builder(TypeToken<V> valueTypeToken) {
        return new Builder<V>(valueTypeToken);
    }

    public static <K, V> ArrayedBuilder1D<K, V> arrayedBuilder1D(Class<V> valueTypeClass) {
        return DerivedState.arrayedBuilder1D(TypeToken.of(valueTypeClass));
    }

    public static <K, V> ArrayedBuilder1D<K, V> arrayedBuilder1D(TypeToken<V> valueTypeToken) {
        return new DerivedState.ArrayedBuilder1D<K, V>(valueTypeToken);
    }

    public static <R, C, V> ArrayedBuilder2D<R, C, V> arrayedBuilder2D(Class<V> valueTypeClass) {
        return DerivedState.arrayedBuilder2D(TypeToken.of(valueTypeClass));
    }

    public static <R, C, V> ArrayedBuilder2D<R, C, V> arrayedBuilder2D(TypeToken<V> valueTypeToken) {
        return new DerivedState.ArrayedBuilder2D<R, C, V>(valueTypeToken);
    }

    public static final class Builder<V> extends StateBuilders.BaseBuilder<Builder<V>, DerivedState<V>, V> {
        /**
         * function specified to calculate the state value
         *
         * <p>the function should only observe any simulation values through states mentioned in the
         * sources list
         *
         * <p>see additional notes at valueFunction()
         */
        private Supplier<V> valueF;

        /** list of input states to the value function */
        private Set<Publisher> sourceStates = new LinkedHashSet<>();

        protected Builder(TypeToken<V> valueType) {
            super(valueType);
        }

        /**
         * specifies the value function that the computed state should use
         *
         * <p>the value function may be state based on some set of input states which must all be listed
         * as dependencies via sourceState() or sourceStates() call. the function must be executable at
         * build() time to fetch an initial value (and so input states must be fully constructed and
         * gettable by then). the function must not have any side effects and must not observe any
         * simulation state except from the enumerated sourceStates. the function must not set up any
         * circular dependencies with input states.
         *
         * @param valueF the function that the state will use to compute its value
         * @return this builder object
         */
        public Builder<V> valueFunction(Supplier<V> valueF) {
            this.valueF = valueF;
            return this;
        }

        /**
         * specifies an additional source state of the value function for the computed state
         *
         * <p>each state that the provided value function depends on must be specified by a
         * sourceState() or sourceStates() call. changes to these input states will trigger eager
         * reevaluation of the value of the state. there must be no circular dependencies with any input
         * states.
         *
         * @return this builder object
         */
        public Builder<V> sourceState(Publisher sourceState) {
            this.sourceStates.add(sourceState);
            return this;
        }

        /**
         * specifies the full set ofsource state of the value function for the computed state
         *
         * <p>each states that the provided value function depends on must be specified by a
         * sourceState() or sourceStates() call. changes to these input states will trigger eager
         * reevaluation of the value of the state. there must be no circular dependencies with any input
         * states.
         *
         * @return this builder object
         */
        public Builder<V> sourceStates(Collection<? extends Publisher> sourceStates) {
            this.sourceStates.addAll(sourceStates);
            return this;
        }

        /**
         * specifies the full set of source state of the value function for the computed state
         *
         * <p>each states that the provided value function depends on must be specified by a
         * sourceState() or sourceStates() call. changes to these input states will trigger eager
         * reevaluation of the value of the state. there must be no circular dependencies with any input
         * states.
         *
         * @return this builder object
         */
        public Builder<V> sourceStates(Publisher... sourceStates) {
            return sourceStates(ImmutableSet.copyOf(sourceStates));
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkNotNull(valueF);
            checkNotNull(sourceStates);
        }

        @Override
        protected Builder<V> getThis() {
            return this;
        }

        @Override
        public DerivedState<V> build() {
            checkBuilderState();
            return new DerivedState<>(ImmutableSet.copyOf(sourceStates), valueF);
        }
    }

    public static final class ArrayedBuilder1D<K, V>
            extends ArrayedStates.BaseBuilder1D<ArrayedBuilder1D<K, V>, K, DerivedState<V>, V> {

        private Function<K, V> keyedValueFunction;
        private Set<Publisher> commonSourceStates = new LinkedHashSet<>();
        private Set<Map<? extends K, ? extends Publisher>> arrayedSourceStates = new LinkedHashSet<>();

        private ArrayedBuilder1D(TypeToken<V> valueType) {
            super(valueType);
        }

        /**
         * Sets the keyed value function that will be used to define the value function for each derived
         * state element of the built arrayed state
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param function a function that accepts an arrayed state key as an input and outputs the
         *     derived/computed value of the state
         * @return this object for fluent builder assertions
         */
        public ArrayedBuilder1D<K, V> keyedValueFunction(Function<K, V> function) {
            checkNotNull(function);
            this.keyedValueFunction = function;
            return getThis();
        }

        /**
         * Adds source states against which all elements of the arrayed state are dependent
         *
         * <p>This method does NOT overwrite existing source states--it simply adds the provided inputs
         * to the existing collection.
         *
         * @param commonSourceStates a collection of states against which all elements of the arrayed
         *     state are dependent
         * @return this object for fluent builder assertions
         */
        public ArrayedBuilder1D<K, V> commonSourceStates(Collection<? extends Publisher> commonSourceStates) {
            checkNotNull(commonSourceStates);
            commonSourceStates.forEach(Preconditions::checkNotNull);
            this.commonSourceStates.addAll(commonSourceStates);
            return this;
        }

        /**
         * Adds source states against which all elements of the arrayed state are dependent
         *
         * <p>This method does NOT overwrite existing source states--it simply adds the provided inputs
         * to the existing collection.
         *
         * @param commonSourceStates a collection of states against which all elements of the arrayed
         *     state are dependent
         * @return this object for fluent builder assertions
         */
        public ArrayedBuilder1D<K, V> commonSourceStates(Publisher... commonSourceStates) {
            checkNotNull(commonSourceStates);
            return commonSourceStates(ImmutableSet.copyOf(commonSourceStates));
        }

        /**
         * Adds arrayed source states against which the elements at the corresponding keys of the
         * arrayed derived state are dependent
         *
         * <p>This method exists to support the definition of more granular derived state dependencies
         * and reduce the overall number of edges in our state graph.
         *
         * <p>Each arrayed source state is expected to have <i>at least</i>the keys of the to-be-built
         * state represented, and in many cases will be an exact match. E.g., an arrayed source state
         * may have keys {@code A}, {@code B}, {@code C}, and {@code D} for a derived arrayed state with
         * keys {@code A}, {@code B}, and {@code C}. In the event of a key mis-match, an {@link
         * IllegalStateException} will be thrown upon invocation of {@link ArrayedBuilder1D#build()}.
         *
         * <p>This method does NOT overwrite existing source states--it simply adds the provided inputs
         * to the existing collection.
         *
         * @param arrayedSourceStates a collection of states against which all elements of the arrayed
         *     state are dependent
         * @return this object for fluent builder assertions
         */
        public ArrayedBuilder1D<K, V> arrayedSourceStates(
                Collection<? extends Map<? extends K, ? extends Publisher>> arrayedSourceStates) {
            checkNotNull(arrayedSourceStates);
            arrayedSourceStates.forEach(Preconditions::checkNotNull);
            this.arrayedSourceStates.addAll(arrayedSourceStates);
            return this;
        }

        /**
         * Adds arrayed source states against which the elements at the corresponding keys of the
         * arrayed derived state sare dependent
         *
         * <p>This method exists to support the definition of more granular derived state dependencies
         * and reduce the overall number of edges in our state graph.
         *
         * <p>Each arrayed source state is expected to have <i>at least</i>the keys of the to-be-built
         * state represented, and in many cases will be an exact match. E.g., an arrayed source state
         * may have keys {@code A}, {@code B}, {@code C}, and {@code D} for a derived arrayed state with
         * keys {@code A}, {@code B}, and {@code C}. In the event of a key mis-match, an {@link
         * IllegalStateException} will be thrown upon invocation of {@link ArrayedBuilder1D#build()}.
         *
         * <p>This method does NOT overwrite existing source states--it simply adds the provided inputs
         * to the existing collection.
         *
         * @param arrayedSourceStates a collection of states against which the arrayed state is
         *     dependent
         * @return this object for fluent builder assertions
         */
        @SafeVarargs
        public final ArrayedBuilder1D<K, V> arrayedSourceStates(
                Map<? extends K, ? extends Publisher>... arrayedSourceStates) {
            checkNotNull(arrayedSourceStates);
            return arrayedSourceStates(ImmutableSet.copyOf(arrayedSourceStates));
        }

        @Override
        protected ArrayedBuilder1D<K, V> getThis() {
            return this;
        }

        @Override
        protected DerivedState<V> createState(K key) {
            final var keySpecificArrayedStateDeps = arrayedSourceStates.stream()
                    .map(arrayedSourceState -> arrayedSourceState.get(key))
                    .collect(toImmutableSet());
            final var sourceStates = Sets.union(keySpecificArrayedStateDeps, commonSourceStates);
            return DerivedState.<V>builder(getValueType())
                    .sourceStates(sourceStates)
                    .valueFunction(() -> keyedValueFunction.apply(key))
                    .build();
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(keyedValueFunction != null, "Keyed value function must be specified");
            arrayedSourceStates.forEach(arrayedSourceState -> checkState(
                    arrayedSourceState.keySet().containsAll(getKeys()),
                    "Expected arrayed source state keys to contain all derived state keys, but didn't"));
        }
    }

    public static final class ArrayedBuilder2D<R, C, V>
            extends ArrayedStates.BaseBuilder2D<ArrayedBuilder2D<R, C, V>, R, C, DerivedState<V>, V> {

        private BiFunction<R, C, V> keyedValueFunction;
        private Set<Publisher> commonSourceStates = new LinkedHashSet<>();
        private Set<Table<? extends R, ? extends C, ? extends Publisher>> arrayedSourceStates = new LinkedHashSet<>();

        private ArrayedBuilder2D(TypeToken<V> valueType) {
            super(valueType);
        }

        /**
         * Sets the keyed value function that will be used to define the value function for each derived
         * state element of the built arrayed state
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param function a function that accepts row and column arrayed state keys as inputs and
         *     outputs the derived/computed value of the state
         * @return this object for fluent builder assertions
         */
        public ArrayedBuilder2D<R, C, V> keyedValueFunction(BiFunction<R, C, V> function) {
            checkNotNull(function);
            this.keyedValueFunction = function;
            return getThis();
        }

        /**
         * Adds source states against which all elements of the arrayed state are dependent
         *
         * <p>This method does NOT overwrite existing source states--it simply adds the provided inputs
         * to the existing collection.
         *
         * @param commonSourceStates a collection of states against which all elements of the arrayed
         *     state are dependent
         * @return this object for fluent builder assertions
         */
        public ArrayedBuilder2D<R, C, V> commonSourceStates(Collection<? extends Publisher> commonSourceStates) {
            checkNotNull(commonSourceStates);
            commonSourceStates.forEach(Preconditions::checkNotNull);
            this.commonSourceStates.addAll(commonSourceStates);
            return this;
        }

        /**
         * Adds source states against which all elements of the arrayed state are dependent
         *
         * <p>This method does NOT overwrite existing source states--it simply adds the provided inputs
         * to the existing collection.
         *
         * @param commonSourceStates a collection of states against which all elements of the arrayed
         *     state are dependent
         * @return this object for fluent builder assertions
         */
        public ArrayedBuilder2D<R, C, V> commonSourceStates(Publisher... commonSourceStates) {
            checkNotNull(commonSourceStates);
            return commonSourceStates(ImmutableSet.copyOf(commonSourceStates));
        }

        /**
         * Adds arrayed source states against which the elements at the corresponding keys of the
         * arrayed derived state are dependent
         *
         * <p>This method exists to support the definition of more granular derived state dependencies
         * and reduce the overall number of edges in our state graph.
         *
         * <p>Each arrayed source state is expected to have <i>at least</i>the keys of the to-be-built
         * state represented, and in many cases will be an exact match. E.g., an arrayed source state
         * may have row keys {@code A}, {@code B}, {@code C}, and {@code D} and column keys {@code 1},
         * {@code 2}, and {@code 3} for a derived arrayed state with row keys {@code A}, {@code B}, and
         * {@code C} and column keys {@code 1} and {@code 2}. In the event of a key mis-match, an {@link
         * IllegalStateException} will be thrown upon invocation of {@link ArrayedBuilder2D#build()}.
         *
         * <p>This method does NOT overwrite existing source states--it simply adds the provided inputs
         * to the existing collection.
         *
         * @param arrayedSourceStates a collection of states against which all elements of the arrayed
         *     state are dependent
         * @return this object for fluent builder assertions
         */
        public ArrayedBuilder2D<R, C, V> arrayedSourceStates(
                Collection<? extends Table<? extends R, ? extends C, ? extends Publisher>> arrayedSourceStates) {
            checkNotNull(arrayedSourceStates);
            arrayedSourceStates.forEach(Preconditions::checkNotNull);
            this.arrayedSourceStates.addAll(arrayedSourceStates);
            return this;
        }

        /**
         * Adds arrayed source states against which the elements at the corresponding keys of the
         * arrayed derived state are dependent
         *
         * <p>This method exists to support the definition of more granular derived state dependencies
         * and reduce the overall number of edges in our state graph.
         *
         * <p>Each arrayed source state is expected to have <i>at least</i>the keys of the to-be-built
         * state represented, and in many cases will be an exact match. E.g., an arrayed source state
         * may have row keys {@code A}, {@code B}, {@code C}, and {@code D} and column keys {@code 1},
         * {@code 2}, and {@code 3} for a derived arrayed state with row keys {@code A}, {@code B}, and
         * {@code C} and column keys {@code 1} and {@code 2}. In the event of a key mis-match, an {@link
         * IllegalStateException} will be thrown upon invocation of {@link ArrayedBuilder2D#build()}.
         *
         * <p>This method does NOT overwrite existing source states--it simply adds the provided inputs
         * to the existing collection.
         *
         * @param arrayedSourceStates a collection of states against which all elements of the arrayed
         *     state are dependent
         * @return this object for fluent builder assertions
         */
        @SafeVarargs
        public final ArrayedBuilder2D<R, C, V> arrayedSourceStates(
                Table<? extends R, ? extends C, ? extends Publisher>... arrayedSourceStates) {
            checkNotNull(arrayedSourceStates);
            return arrayedSourceStates(ImmutableSet.copyOf(arrayedSourceStates));
        }

        @Override
        protected ArrayedBuilder2D<R, C, V> getThis() {
            return this;
        }

        @Override
        protected DerivedState<V> createState(R rowKey, C columnKey) {
            final var keySpecificArrayedStateDeps = arrayedSourceStates.stream()
                    .map(arrayedSourceState -> arrayedSourceState.get(rowKey, columnKey))
                    .collect(toImmutableSet());
            final var sourceStates = Sets.union(keySpecificArrayedStateDeps, commonSourceStates);
            return DerivedState.<V>builder(getValueType())
                    .sourceStates(sourceStates)
                    .valueFunction(() -> keyedValueFunction.apply(rowKey, columnKey))
                    .build();
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(keyedValueFunction != null, "Keyed value function must be specified");
            arrayedSourceStates.forEach(arrayedSourceState -> checkState(
                    arrayedSourceState.rowKeySet().containsAll(getRowKeys()),
                    "Expected arrayed source state row keys to contain all derived state row keys, but didn't"));
            arrayedSourceStates.forEach(arrayedSourceState -> checkState(
                    arrayedSourceState.columnKeySet().containsAll(getColumnKeys()),
                    "Expected arrayed source state column keys to contain all derived state column keys, but didn't"));
        }
    }

    /**
     * creates a new derived state that reports the floating point summation of all given numerical addend states
     *
     * the numerical values of the source addend states are extracted via {@link Number#doubleValue()}. beware of
     * attendant precision loss as values are combined into intermediate doubles in an indeterminate order.
     *
     * an empty summation is considered to be zero valued
     *
     * @param addendSourceStates the list of addend states to sum; must be a numerical type
     * @return a derived state that reports the sum of all addend source states
     */
    @SafeVarargs
    public static DerivedState<Double> sum(PublishingResource<? extends Number>... addendSourceStates) {

        return DerivedState.<Double>builder(Double.class)
                .sourceStates(addendSourceStates)
                .valueFunction(() -> Arrays.stream(addendSourceStates)
                        .map(PublishingResource::get)
                        .map(Number::doubleValue)
                        .reduce(0.0, Double::sum))
                .build();
    }
}

