package examplemodel.models.power;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.reflect.TypeToken;
import org.checkerframework.checker.nullness.qual.Nullable;
public final class StateBuilders {

    public abstract static class BaseBuilder<B extends BaseBuilder<B, S, V>, S, V> {

        /**
         * The type token that captures the value type of the state (to support reification if needed)
         */
        private final TypeToken<V> valueType;

        /**
         * Protected constructor
         *
         * @param valueType a type token that captures the value type of the state
         */
        protected BaseBuilder(TypeToken<V> valueType) {
            checkNotNull(valueType);
            this.valueType = valueType;
        }

        /**
         * Protected valueType getter to support state construction in subtypes
         *
         * @return the type token that captures the value type of the state (to support reification if
         *     needed)
         */
        protected final TypeToken<V> getValueType() {
            return this.valueType;
        }

        /**
         * Returns this object for fluent builder assertions
         *
         * <p>Only leaf builder subtypes (concrete) should implement this method. It exists to support
         * the curiously-recurring template pattern and the return of subtyped builder objects from
         * supertype builder methods.
         *
         * <p>Note: any implementation of this method need only be {@code return this}.
         *
         * @return this object for fluent builder assertions
         */
        protected abstract B getThis();

        /**
         * A helper method for checking the state of the builder prior to assembly/build
         *
         * <p>This method will check that the state of the builder is sufficient for building (usually
         * by enforcing that various builder fields are non-null), and it will eagerly throw {@link
         * IllegalStateException}s if the builder is not in state to support building.
         *
         * <p>This method should only be invoked prior to an assembly or build operation when all inputs
         * should be provided. This lets builder subtype authors ensure that clients did not
         * accidentally leave out builder statements prior to a {@code build()} call.
         *
         * <p>In general, each builder subtype should override this method, use its supertype's
         * implementation ({@code super.checkBuilderState()}), and then perform its own checks against
         * builder inputs that are being accumulated at that subtype level. This allows subtypes to not
         * worry about all of the necessary builder fields from supertypes (e.g., basename, registrar)
         * and lets them focus on arguments provided at their level (e.g., default values). And, {@code
         * build()} methods defined in supertypes can invoke this to carry out state-checking operations
         * without needing to know all of the specific state tracked in the subtypes.
         */
        protected void checkBuilderState() {}

        public abstract S build();
    }

    public abstract static class BaseBuilderWithInitValue<B extends BaseBuilderWithInitValue<B, S, V>, S, V>
            extends BaseBuilder<B, S, V> {

        private V initialValue;

        public BaseBuilderWithInitValue(TypeToken<V> valueType) {
            super(valueType);
        }

        public B initialValue(V initialValue) {
            this.initialValue = initialValue;
            return getThis();
        }

        public @Nullable V getInitialValue() {
            return initialValue;
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(initialValue != null, "Initial value must be provided");
        }
    }
}

