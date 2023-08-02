package examplemodel.models.power;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.reflect.TypeToken;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ArrayedStates {

    /**
     * A base arrayed state builder class that does not even specify its number of dimensions
     *
     * @param <B> the builder subtype (see curiously recurring template pattern)
     * @param <V> the value type of the states to be assembled via this builder
     */
    public abstract static class BaseBuilder<B extends BaseBuilder<B, V>, V> {

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
            this.valueType = valueType;
        }

        /**
         * Protected constructor
         *
         * @param valueType a class token that captures the value type of the state
         */
        protected BaseBuilder(Class<V> valueType) {
            this.valueType = TypeToken.of(valueType);
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
    }

    /**
     * An abstract builder for 1D arrayed states
     *
     * @param <B> the builder subtype (see curiously recurring template pattern)
     * @param <K> the type of the keys of the arrayed state
     * @param <S> the type of state that will comprise each element of the arrayed state
     * @param <V> the value type of the states to be assembled via this builder
     */
    public abstract static class BaseBuilder1D<B extends BaseBuilder1D<B, K, S, V>, K, S, V> extends BaseBuilder<B, V> {

        /** The set of arrayed state keys */
        private Set<K> keys;

        /**
         * Protected constructor
         *
         * @param valueType a type token that captures the value type of the state
         */
        protected BaseBuilder1D(TypeToken<V> valueType) {
            super(valueType);
        }

        /**
         * Protected constructor
         *
         * @param valueType a class token that captures the value type of the state
         */
        protected BaseBuilder1D(Class<V> valueType) {
            super(valueType);
        }

        /**
         * Sets the keys for the arrayed state
         *
         * <p>Note: this method will throw an {@code IllegalArgumentException} if duplicate elements are
         * provided.
         *
         * <p>This method will throw an {@code IllegalArgumentException} if 0 keys are provided.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param keys a collection of keys
         * @return this object for fluent builder assertions
         */
        public final B keys(Iterable<? extends K> keys) {
            checkNotNull(keys);
            checkArgument(!containsDuplicates(keys), "Input keys must not contain duplicates");
            final var keySet = ImmutableSet.<K>copyOf(keys);
            checkArgument(!keySet.isEmpty(), "Must provide 1+ key");
            this.keys = keySet;
            return getThis();
        }

        /**
         * Sets the keys for the arrayed state
         *
         * <p>Note: this method will throw an {@code IllegalArgumentException} if duplicate elements are
         * provided.
         *
         * <p>This method will throw an {@code IllegalArgumentException} if 0 keys are provided.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param keys variable-argument sequence of keys
         * @return this object for fluent builder assertions
         */
        @SafeVarargs
        public final B keys(K... keys) {
            checkNotNull(keys);
            return keys(ImmutableSet.copyOf(keys));
        }

        /**
         * Protected keys getter to support arrayed state construction in subtypes
         *
         * @return the keys of the arrayed state (or null if not yet specified)
         */
        protected final @Nullable Set<K> getKeys() {
            return this.keys;
        }

        /**
         * Creates a state with the provided key and other information accessible via protected getters
         *
         * <p>This must be implemented in the leaf builder class unless it is overridden and deprecated
         * by another subtype in between this layer and the leaf builder (e.g., {@link
         * BaseBuilder1DWithInitValues}). In that case, the intermediate subtype should create a more
         * specific {@code createState()} method with additional arguments required for state
         * construction (e.g., default value).
         *
         * @param key the key to use in creating the state
         * @return a state element to be slotted into the overall arrayed state at the given key
         */
        protected abstract S createState(K key);

        /**
         * Builds a 1D arrayed state from the input builder arguments
         *
         * <p>Checks that the builder state is valid and assembles a 1D arrayed state
         *
         * @return an immutable map keyed by the specified keys with states as values
         */
        public ImmutableMap<K, S> build() {
            checkBuilderState();
            return Maps.toMap(keys, this::createState);
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(keys != null, "Keys must be specified");
        }
    }

    /**
     * An abstract builder for 1D arrayed states with support for initial value specification
     *
     * @param <B> the builder subtype (see curiously recurring template pattern)
     * @param <K> the type of the keys of the arrayed state
     * @param <S> the type of state that will comprise each element of the arrayed state
     * @param <V> the value type of the states to be assembled via this builder
     */
    public abstract static class BaseBuilder1DWithInitValues<B extends BaseBuilder1DWithInitValues<B, K, S, V>, K, S, V>
            extends BaseBuilder1D<B, K, S, V> {

        /** The function that produces initial values for each key */
        private Function<K, V> initialValueFunction;

        /** A map of default values that will override the initialValueFunction for each key present */
        private Map<K, V> initialOverrides = ImmutableMap.of();

        /**
         * Protected constructor
         *
         * @param valueType a type token that captures the value type of the state
         */
        protected BaseBuilder1DWithInitValues(TypeToken<V> valueType) {
            super(valueType);
        }

        /**
         * Protected constructor
         *
         * @param valueType a class token that captures the value type of the state
         */
        protected BaseBuilder1DWithInitValues(Class<V> valueType) {
            super(valueType);
        }

        /**
         * Sets the initial value for all arrayed state elements to this static value
         *
         * <p>Note: {@code initialValue()} and {@code initialValueFunction()} are mutually exclusive.
         *
         * <p>Note: this is essentially converted into a initial value function ({@code Function<K, V>})
         * that always returns the same initial value.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method or by
         * {@code initialValueFunction()}.
         *
         * @param value the value to which all arrayed state elements should be initialized
         * @return this object for fluent builder assertions
         */
        public final B initialValue(V value) {
            checkNotNull(value);
            // convert the static value to a Function<K, V> that always returns that static value
            this.initialValueFunction = (key) -> value;
            return getThis();
        }

        /**
         * Sets the function that should be used to set the initials based upon the arrayed state key
         *
         * <p>Note: {@code initialValue()} and {@code initialValueFunction()} are mutually exclusive.
         *
         * <p>This should be a pure function. I.e., it should not mutate any state or rely upon things
         * that may change between this specifier and the {@code build()} operation.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method or by
         * {@code initialValue()}.
         *
         * @param function the keyed function used to set initial values
         * @return this object for fluent builder assertions
         */
        public final B initialValueFunction(Function<K, V> function) {
            checkNotNull(function);
            this.initialValueFunction = function;
            return getThis();
        }

        /**
         * Provides a map of keys to initial values that override other initial specifications
         *
         * <p>For any key present in both the key set and this override map, the associated override
         * value will be used in lieu of the initial as would be specified by either {@code
         * initialValue()} or {@code initialValueFunction()}.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param overrides a map of initial values that will override the initialValueFunction for each
         *     key present
         * @return this object for fluent builder assertions
         */
        public final B initialOverrides(Map<K, V> overrides) {
            checkNotNull(overrides);
            this.initialOverrides = overrides;
            return getThis();
        }

        /**
         * Helper method for getting the initial value for a given arrayed state key
         *
         * <p>This method will look for an override and use it if present. Otherwise, it will use the
         * initial value function (or static value that has been converted to a function).
         *
         * @param key the key for which a initial value is desired
         * @return the initial value for the given key
         */
        protected final V getInitialValue(K key) {
            checkNotNull(key);
            checkState(
                    initialValueFunction != null,
                    "Cannot compute initial value because initial value function is null");
            return initialOverrides.containsKey(key) ? initialOverrides.get(key) : initialValueFunction.apply(key);
        }

        /**
         * Builders that create states requiring initial values must use {@link
         * BaseBuilder1DWithInitValues#createState(Object, Object)} instead.
         */
        @Override
        @Deprecated
        protected final S createState(K key) {
            throw new UnsupportedOperationException("Users should instead use `createState(K key, V initialValue)`");
        }

        /**
         * Creates a state with the provided key, initial value and other information accessible via
         * protected getters
         *
         * <p>Replaces {@link BaseBuilder1DWithInitValues#createState(Object)} as the required state
         * construction in leaf subtypes
         *
         * @param key the key to use in creating the state
         * @param initialValue the initial value to use in creating the state
         * @return a state element to be slotted into the overall arrayed state at the given key
         */
        protected abstract S createState(K key, V initialValue);

        @Override
        public ImmutableMap<K, S> build() {
            checkBuilderState();
            return Maps.toMap(getKeys(), (key) -> createState(key, getInitialValue(key)));
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(initialValueFunction != null, "Default values or function must be specified");
        }
    }

    /**
     * An abstract builder for 2D arrayed states
     *
     * @param <B> the builder subtype (see curiously recurring template pattern)
     * @param <R> the type of the row keys of the arrayed state
     * @param <C> the type of the column keys of the arrayed state
     * @param <S> the type of state that will comprise each element of the arrayed state
     * @param <V> the value type of the states to be assembled via this builder
     */
    public abstract static class BaseBuilder2D<B extends BaseBuilder2D<B, R, C, S, V>, R, C, S, V>
            extends BaseBuilder<B, V> {

        /** The set of keys that will comprise the rows of the arrayed state */
        private Set<R> rowKeys;

        /** The set of keys that will comprise the columns of the arrayed state */
        private Set<C> columnKeys;

        /**
         * Protected constructor
         *
         * @param valueType a type token that captures the value type of the state
         */
        protected BaseBuilder2D(TypeToken<V> valueType) {
            super(valueType);
        }

        /**
         * Protected constructor
         *
         * @param valueType a class token that captures the value type of the state
         */
        protected BaseBuilder2D(Class<V> valueType) {
            super(valueType);
        }

        /**
         * Sets the row keys for the arrayed state
         *
         * <p>Note: this method will throw an {@code IllegalArgumentException} if duplicate elements are
         * provided.
         *
         * <p>This method will throw an {@code IllegalArgumentException} if 0 keys are provided.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param rowKeys a collection of keys
         * @return this object for fluent builder assertions
         */
        public final B rowKeys(Iterable<? extends R> rowKeys) {
            checkNotNull(rowKeys);
            checkArgument(!containsDuplicates(rowKeys), "Input row keys must not contain duplicates");
            final var keySet = ImmutableSet.<R>copyOf(rowKeys);
            checkArgument(!keySet.isEmpty(), "Must provide 1+ row key");
            this.rowKeys = keySet;
            return getThis();
        }

        /**
         * Sets the row keys for the arrayed state
         *
         * <p>Note: this method will throw an {@code IllegalArgumentException} if duplicate elements are
         * provided.
         *
         * <p>This method will throw an {@code IllegalArgumentException} if 0 keys are provided.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param rowKeys a variable argument sequence of keys
         * @return this object for fluent builder assertions
         */
        @SafeVarargs
        public final B rowKeys(R... rowKeys) {
            checkNotNull(rowKeys);
            return rowKeys(ImmutableSet.copyOf(rowKeys));
        }

        /**
         * Sets the column keys for the arrayed state
         *
         * <p>Note: this method will throw an {@code IllegalArgumentException} if duplicate elements are
         * provided.
         *
         * <p>This method will throw an {@code IllegalArgumentException} if 0 keys are provided.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param columnKeys a collection of keys
         * @return this object for fluent builder assertions
         */
        public final B columnKeys(Iterable<? extends C> columnKeys) {
            checkNotNull(columnKeys);
            checkArgument(!containsDuplicates(columnKeys), "Input column keys must not contain duplicates");
            final var keySet = ImmutableSet.<C>copyOf(columnKeys);
            checkArgument(!keySet.isEmpty(), "Must provide 1+ column key");
            this.columnKeys = keySet;
            return getThis();
        }

        /**
         * Sets the column keys for the arrayed state
         *
         * <p>Note: this method will throw an {@code IllegalArgumentException} if duplicate elements are
         * provided.
         *
         * <p>This method will throw an {@code IllegalArgumentException} if 0 keys are provided.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param columnKeys a variable argument sequence of keys
         * @return this object for fluent builder assertions
         */
        @SafeVarargs
        public final B columnKeys(C... columnKeys) {
            checkNotNull(columnKeys);
            return columnKeys(ImmutableSet.copyOf(columnKeys));
        }

        /**
         * Protected row keys getter to support arrayed state construction in subtypes
         *
         * @return the row keys of the arrayed state (or null if not yet specified)
         */
        protected final @Nullable Set<R> getRowKeys() {
            return this.rowKeys;
        }

        /**
         * Protected column keys getter to support arrayed state construction in subtypes
         *
         * @return the column keys of the arrayed state (or null if not yet specified)
         */
        protected final @Nullable Set<C> getColumnKeys() {
            return this.columnKeys;
        }

        /**
         * Creates a state with the provided row and column keys and other information accessible via
         * protected getters
         *
         * <p>This must be implemented in the leaf builder class unless it is overridden and deprecated
         * by another subtype in between this layer and the leaf builder (e.g., {@link
         * BaseBuilder2DWithInitValues}). In that case, the intermediate subtype should create a more
         * specific {@code createState()} method with additional arguments required for state
         * construction (e.g., initial value).
         *
         * @param rowKey the key to use in creating the state
         * @param columnKey the column key to use in creating the state
         * @return a state element to be slotted into the overall arrayed state with the given row and
         *     column keys
         */
        protected abstract S createState(R rowKey, C columnKey);

        /**
         * Builds a 2D arrayed state from the input builder arguments
         *
         * <p>Checks that the builder state is valid and assembles a 2D arrayed state.
         *
         * @return an immutable table keyed by the specified row and column keys with states as values
         */
        public ImmutableTable<R, C, S> build() {
            checkBuilderState();
            final var tableBuilder = ImmutableTable.<R, C, S>builder();
            for (final var rowKey : rowKeys) {
                for (final var columnKey : columnKeys) {
                    tableBuilder.put(rowKey, columnKey, createState(rowKey, columnKey));
                }
            }
            return tableBuilder.build();
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(rowKeys != null, "Row keys must be specified");
            checkState(columnKeys != null, "Column keys must be specified");
        }
    }

    /**
     * An abstract builder for 2D arrayed states with support for initial value specification
     *
     * @param <B> the builder subtype (see curiously recurring template pattern)
     * @param <R> the type of the row keys of the arrayed state
     * @param <C> the type of the column keys of the arrayed state
     * @param <S> the type of state that will comprise each element of the arrayed state
     * @param <V> the value type of the states to be assembled via this builder
     */
    public abstract static class BaseBuilder2DWithInitValues<
            B extends BaseBuilder2DWithInitValues<B, R, C, S, V>, R, C, S, V>
            extends BaseBuilder2D<B, R, C, S, V> {

        /** The function that produces initial values for each row-column key pairing */
        private BiFunction<R, C, V> initialValueFunction;

        /**
         * A table of initial values that will override the initialValueFunction for each row-column key
         * pairing present
         */
        private Table<R, C, V> initialOverrides = ImmutableTable.of();

        /**
         * Protected constructor
         *
         * @param valueType a type token that captures the value type of the state
         */
        protected BaseBuilder2DWithInitValues(TypeToken<V> valueType) {
            super(valueType);
        }

        /**
         * Protected constructor
         *
         * @param valueType a class token that captures the value type of the state
         */
        protected BaseBuilder2DWithInitValues(Class<V> valueType) {
            super(valueType);
        }

        /**
         * Sets the initial value for all arrayed state elements to this static value
         *
         * <p>Note: {@code initialValue()} and {@code initialValueFunction()} are mutually exclusive.
         *
         * <p>Note: this is essentially converted into a initial value function ({@code BiFunction<R, R
         * V>}) that always returns the same initial value.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method or
         * {@code initialValueFunction()}.
         *
         * @param value the value to which all arrayed state elements should be initialized
         * @return this object for fluent builder assertions
         */
        public final B initialValue(V value) {
            checkNotNull(value);
            // convert the static value to a BiFunction<R, C, V> that always returns that static value
            this.initialValueFunction = (row, column) -> value;
            return getThis();
        }

        /**
         * Sets the function that should be used to set the initials based upon the arrayed state row
         * and column keys
         *
         * <p>Note: {@code initialValue()} and {@code initialValueFunction()} are mutually exclusive.
         *
         * <p>This should be a pure function. I.e., it should not mutate any state or rely upon things
         * that may change between this specifier and the {@code build()} operation.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method or
         * {@code initialValue()}.
         *
         * @param function the keyed function used to set initial values
         * @return this object for fluent builder assertions
         */
        public final B initialValueFunction(BiFunction<R, C, V> function) {
            checkNotNull(function);
            this.initialValueFunction = function;
            return getThis();
        }

        /**
         * Provides a table of row, columns, and initial values that override other initial
         * specifications
         *
         * <p>For any row-column pairing present in this override table, the associated value will be
         * used in lieu of the initial as would be specified by either {@code initialValue()} or {@code
         * initialValueFunction()}.
         *
         * <p>This builder property may be overwritten by subsequent invocations of this method.
         *
         * @param overrides a table of initial values that will override the initialValueFunction for
         *     each row-column pairing present
         * @return this object for fluent builder assertions
         */
        public final B initialOverrides(Table<R, C, V> overrides) {
            checkNotNull(overrides);
            this.initialOverrides = overrides;
            return getThis();
        }

        /**
         * Helper method for getting the initial value for the given arrayed state row and column keys
         *
         * <p>This method will look for an override and use it if present. Otherwise, it will use the
         * initial value function (or static value that has been converted to a function).
         *
         * @param rowKey the row key for which a initial value is desired
         * @param columnKey the column key for which a initial value is desired
         * @return the initial value for the given row and column keys
         */
        protected final V getInitialValue(R rowKey, C columnKey) {
            checkNotNull(rowKey);
            checkNotNull(columnKey);
            checkState(
                    initialValueFunction != null,
                    "Cannot compute initial value because initial value function is null");

            // look for an override, or if not, use the initial value function
            return initialOverrides.contains(rowKey, columnKey)
                    ? initialOverrides.get(rowKey, columnKey)
                    : initialValueFunction.apply(rowKey, columnKey);
        }

        /**
         * Builders that create states requiring initial values must use {@link
         * BaseBuilder1DWithInitValues#createState(Object, Object)} instead.
         */
        @Override
        @Deprecated
        protected final S createState(R rowKey, C columnKey) {
            throw new UnsupportedOperationException(
                    "Users should instead use `createState(R rowKey, C columnKey, V initialValue)`");
        }

        /**
         * Creates a state with the provided row key, column key, initial value and other information
         * accessible via protected getters
         *
         * <p>Replaces {@link BaseBuilder2DWithInitValues#createState(Object, Object)} as the required
         * state construction in leaf subtypes.
         *
         * @param rowKey the key to use in creating the state
         * @param columnKey the column key to use in creating the state
         * @param initialValue the initial value to use in creating the state
         * @return a state element to be slotted into the overall arrayed state at the given key
         */
        protected abstract S createState(R rowKey, C columnKey, V initialValue);

        @Override
        public ImmutableTable<R, C, S> build() {
            checkBuilderState();
            final var tableBuilder = ImmutableTable.<R, C, S>builder();
            for (final var row : getRowKeys()) {
                for (final var column : getColumnKeys()) {
                    tableBuilder.put(row, column, createState(row, column, getInitialValue(row, column)));
                }
            }
            return tableBuilder.build();
        }

        @Override
        protected void checkBuilderState() {
            super.checkBuilderState();
            checkState(initialValueFunction != null, "Default values or function must be specified");
        }
    }

    /**
     * Helper method for determining if an iterable contains duplicate elements
     *
     * @param iterable the iterable to check for duplicate elements
     * @return true if there are duplicates, false if not
     */
    private static boolean containsDuplicates(Iterable<?> iterable) {
        checkNotNull(iterable);
        final var multiset = ImmutableMultiset.copyOf(iterable);
        return multiset.elementSet().size() != multiset.size();
    }
}

