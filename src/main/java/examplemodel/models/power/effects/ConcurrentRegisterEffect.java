package examplemodel.models.power.effects;

import gov.nasa.jpl.aerie.merlin.protocol.model.EffectTrait;
import java.util.Objects;
import java.util.Optional;

public class ConcurrentRegisterEffect<T> {

    public final T newValue;
    public final boolean conflicted;

    private ConcurrentRegisterEffect(final T newValue, final boolean conflicted) {
        this.newValue = newValue;
        this.conflicted = conflicted;
    }

    private static final ConcurrentRegisterEffect<?> EMPTY = new ConcurrentRegisterEffect<>(null, false);
    private static final ConcurrentRegisterEffect<?> CONFLICTED = new ConcurrentRegisterEffect<>(null, true);

    @SuppressWarnings("unchecked")
    public static <T> ConcurrentRegisterEffect<T> doNothing() {
        return (ConcurrentRegisterEffect<T>) EMPTY;
    }

    public static <T> ConcurrentRegisterEffect<T> set(final T newValue) {
        return new ConcurrentRegisterEffect<>(Objects.requireNonNull(newValue), false);
    }

    @SuppressWarnings("unchecked")
    public static <T> ConcurrentRegisterEffect<T> conflict() {
        return (ConcurrentRegisterEffect<T>) CONFLICTED;
    }

    @Override
    public String toString() {
        return (this.newValue != null) ? "set(%s)".formatted(this.newValue) : "noop()";
    }

    public static final class Trait<T> implements EffectTrait<ConcurrentRegisterEffect<T>> {
        @Override
        public ConcurrentRegisterEffect<T> empty() {
            return ConcurrentRegisterEffect.doNothing();
        }

        @Override
        public ConcurrentRegisterEffect<T> sequentially(
                final ConcurrentRegisterEffect<T> prefix, final ConcurrentRegisterEffect<T> suffix) {
            // If `suffix` isn't a no-op, it overrules `prefix`.
            if (suffix.conflicted || suffix.newValue != null) {
                return suffix;
            } else {
                return prefix;
            }
        }

        @Override
        public ConcurrentRegisterEffect<T> concurrently(
                final ConcurrentRegisterEffect<T> left, final ConcurrentRegisterEffect<T> right) {

            final Optional<T> leftValue = Optional.ofNullable(left.newValue);
            final Optional<T> rightValue = Optional.ofNullable(right.newValue);

            final boolean bothPresent = leftValue.isPresent() && rightValue.isPresent();
            final boolean onePresent = leftValue.isPresent() || rightValue.isPresent();
            final Optional<T> nextValue;

            if (bothPresent) {
                // if both left and right share the same value, we can proceed without a conflict
                nextValue = leftValue.get().equals(rightValue.get()) ? leftValue : Optional.empty();
            } else if (onePresent) {
                nextValue = leftValue.or(() -> rightValue);
            } else {
                nextValue = Optional.empty();
            }

            if (nextValue.isPresent()) {
                return ConcurrentRegisterEffect.set(nextValue.get());
            } else {
                return ConcurrentRegisterEffect.conflict();
            }
        }
    }
}

