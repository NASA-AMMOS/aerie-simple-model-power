package powersystem;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Table;
import com.google.common.reflect.TypeToken;
import gov.nasa.jpl.aerie.merlin.framework.Condition;
import gov.nasa.jpl.aerie.merlin.framework.resources.real.RealResource;
import gov.nasa.jpl.aerie.merlin.protocol.types.RealDynamics;
import powersystem.ArrayedStates.BaseBuilder1DWithInitValues;
import powersystem.ArrayedStates.BaseBuilder2DWithInitValues;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Integrated states are states whose values are derived from the integral of another
 * (discretely-valued) publisher state over time plus some constant.
 *
 * <p>Integrated states have time-varying continuous values and also expose methods for discretely
 * setting, adding and subtracting from their value directly. If discrete mutation operations should
 * not be exposed to client code, this may be downcast to a {@code State<Double>}.
 *
 * <p>A few notable limitations currently exist when using integrated states:
 *
 * <ul>
 *   <li>An integrated state may only accept a single publishing state as a source to integrate over
 *       (rather than some function of multiple states). If this is insufficient, clients are
 *       encouraged to create an intermediate state that coalesces one or more source states into a
 *       single integrand state.
 *   <li>Because integrated states are continuously-valued, they cannot be publishing states
 *       themselves. This means that we cannot build eagerly-derived states on top of them. Instead,
 *       we must use {@link gov.nasa.jpl.aerie.contrib.models.SampledResource}s to periodically
 *       update the value of any state which depends upon an integrated state, or we must use
 *       Aerie's linear graph operations to transform the integrated state into a desired derived
 *       state. The latter is not yet supported but will be a future work item.
 * </ul>
 *
 * <p>Note: the mechanics of this state type have not been thoroughly tested, and they are intended
 * to be replaced by an Aerie-provided settable accumulator model. Thus, this state type should not
 * yet be used in production.
 */
public class IntegratedState implements Subscriber, RealResource {

    private final SettableAccumulator accumulator;
    private final PublishingResource<? extends Number> integrandState;

    private IntegratedState(PublishingResource<? extends Number> integrandState, double initialConstantValue) {
        final var initialIntegrandValue = integrandState.get().doubleValue();
        this.accumulator = new SettableAccumulator(initialConstantValue, initialIntegrandValue);
        this.integrandState = integrandState;
        integrandState.registerSubscriber(this);
    }

    @Override
    public RealDynamics getDynamics() {
        return accumulator.area.getDynamics();
    }

    public Condition isBetween(double lower, double upper) {
        return accumulator.area.isBetween(lower, upper);
    }

    public void discretelySet(double newValue) {
        final var currentValue = get();
        final var deltaToAdd = newValue - currentValue;
        discretelyAdd(deltaToAdd);
    }

    public void discretelyAdd(double delta) {
        accumulator.area.add(delta);
    }

    public void discretelySubtract(double delta) {
        discretelyAdd(-delta);
    }

    @Override
    public void update() {
        final double newIntegrandValue = integrandState.get().doubleValue();
        final double currentIntegrandValue = accumulator.integrand.get();
        final double deltaToAdd = newIntegrandValue - currentIntegrandValue;
        accumulator.integrand.add(deltaToAdd);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static <K> ArrayedBuilder1D<K> arrayedBuilder1D() {
        return new ArrayedBuilder1D<>();
    }

    public static <R, C> ArrayedBuilder2D<R, C> arrayedBuilder2D() {
        return new ArrayedBuilder2D<>();
    }

    public static final class Builder extends StateBuilders.BaseBuilderWithInitValue<Builder, IntegratedState, Double> {

        private PublishingResource<? extends Number> integrandState;

        private Builder() {
            super(TypeToken.of(Double.class));
        }

        public Builder integrandState(PublishingResource<? extends Number> integrandState) {
            checkNotNull(integrandState);
            this.integrandState = integrandState;
            return this;
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(integrandState != null, "Integrand state must be specified");
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public IntegratedState build() {
            checkBuilderState();
            return new IntegratedState(integrandState, getInitialValue());
        }
    }

    public static final class ArrayedBuilder1D<K>
            extends BaseBuilder1DWithInitValues<ArrayedBuilder1D<K>, K, IntegratedState, Double> {

        /**
         * The function used to assign a specific publishing state (integrand) to each integrated state
         * element of the arrayed state
         */
        private Function<K, ? extends PublishingResource<? extends Number>> integrandStateFunction;

        private ArrayedBuilder1D() {
            super(Double.class);
        }

        /**
         * A function that outputs an integrand state for each key provided
         *
         * <p>Note: users should generally prefer to use {@link
         * ArrayedBuilder1D#arrayedIntegrandState(Map)} if they already have an arrayed state with
         * matching keys that is to be used as the source for the arrayed integrated state. This
         * function is a catch-all for cases where that assumption is not true.
         *
         * @param integrandStateFunction a function which accepts the key of an integrated state that is
         *     to-be-built and returns the source publishing state (i.e., the integrand) against which
         *     the integrated state should be integrating
         * @return this object for fluent builder assertions
         */
        public final ArrayedBuilder1D<K> keyedIntegrandStates(
                Function<K, ? extends PublishingResource<? extends Number>> integrandStateFunction) {
            checkNotNull(this.integrandStateFunction);
            this.integrandStateFunction = integrandStateFunction;
            return this;
        }

        /**
         * Allows you to specify source integrand states directly from another arrayed state
         *
         * <p>This method is just a shortcut for {@code keyedIntegrandStates()} if your integrand states
         * are already an arrayed state with the same key set.
         *
         * <p>The input arrayed integrand state must contain at least all keys that are present in this
         * builder. If not, an {@code IllegalStateException} will be thrown at build time.
         *
         * @param arrayedIntegrandState an arrayed state whose values should be used as the source
         *     states (i.e., integrands) for the arrayed integrated state to be built by this builder
         * @return this object for fluent builder assertions
         */
        public final ArrayedBuilder1D<K> arrayedIntegrandState(
                Map<? extends K, ? extends PublishingResource<? extends Number>> arrayedIntegrandState) {
            checkNotNull(arrayedIntegrandState);
            this.integrandStateFunction = (key) -> Optional.ofNullable(arrayedIntegrandState.get(key))
                    .orElseThrow(() -> new IllegalStateException(
                            "Expected key `" + key + "` to be present in input arrayedIntegrandState, but was not."));
            return this;
        }

        @Override
        protected ArrayedBuilder1D<K> getThis() {
            return this;
        }

        @Override
        protected IntegratedState createState(K key, Double initialValue) {
            return IntegratedState.builder()
                    .integrandState(integrandStateFunction.apply(key))
                    .initialValue(initialValue)
                    .build();
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(integrandStateFunction != null, "Integrand state function must be specified");
        }
    }

    public static final class ArrayedBuilder2D<R, C>
            extends BaseBuilder2DWithInitValues<ArrayedBuilder2D<R, C>, R, C, IntegratedState, Double> {

        /**
         * The function used to assign a specific publishing state (integrand) to each integrated state
         * element of the arrayed state
         */
        private BiFunction<R, C, ? extends PublishingResource<? extends Number>> integrandStateFunction;

        private ArrayedBuilder2D() {
            super(Double.class);
        }

        /**
         * A function that outputs an integrand state for each row and column key pairing provided
         *
         * <p>Note: users should generally prefer to use {@link
         * ArrayedBuilder2D#arrayedIntegrandState(Table)} if they already have an arrayed state with
         * matching keys that is to be used as the source for the arrayed integrated state. This
         * function is a catch-all for cases where that assumption is not true.
         *
         * @param integrandStateFunction a bi-function which accepts the row and column keys for an
         *     integrated state that is to-be-built and returns the source publishing state (integrand)
         *     against which the integrated state should be integrating
         * @return this object for fluent builder assertions
         */
        public final ArrayedBuilder2D<R, C> keyedIntegrandStates(
                BiFunction<R, C, ? extends PublishingResource<? extends Number>> integrandStateFunction) {
            checkNotNull(integrandStateFunction);
            this.integrandStateFunction = integrandStateFunction;
            return this;
        }

        /**
         * Allows you to specify integrand states directly from another arrayed state
         *
         * <p>This method is just a shortcut for {@code keyedIntegrandStates()} if your integrand states
         * are already an arrayed state with the same row and column key sets.
         *
         * <p>The input arrayed integrand state must contain at least all keys that are present in this
         * builder. If not, an {@code IllegalStateException} will be thrown at build time.
         *
         * @param arrayedIntegrandState an arrayed state whose values should be used as the integrand
         *     states for the arrayed integrated state to be built by this builder
         * @return this object for fluent builder assertions
         */
        public final ArrayedBuilder2D<R, C> arrayedIntegrandState(
                Table<? extends R, ? extends C, ? extends PublishingResource<? extends Number>> arrayedIntegrandState) {
            checkNotNull(arrayedIntegrandState);
            this.integrandStateFunction =
                    (rowKey, colKey) -> Optional.ofNullable(arrayedIntegrandState.get(rowKey, colKey))
                            .orElseThrow(() -> new IllegalStateException("Expected row-column key pair <`"
                                    + rowKey
                                    + "`, `"
                                    + colKey
                                    + "`>"
                                    + " to be present in input arrayedIntegrandState, but was not."));
            return this;
        }

        @Override
        protected ArrayedBuilder2D<R, C> getThis() {
            return this;
        }

        @Override
        protected IntegratedState createState(R rowKey, C columnKey, Double initialValue) {
            return IntegratedState.builder()
                    .integrandState(integrandStateFunction.apply(rowKey, columnKey))
                    .initialValue(initialValue)
                    .build();
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(integrandStateFunction != null, "Integrand state function must be specified");
        }
    }
}
