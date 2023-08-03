package powersystem;

import gov.nasa.jpl.aerie.merlin.framework.resources.discrete.DiscreteResource;
import gov.nasa.jpl.aerie.merlin.framework.resources.real.RealResource;
/**
 * A publishing resource is a resource type which is capable of "publishing" value updates to
 * subscribers
 *
 * <p>Publishing resources are expected to track their {@link Subscriber} objects (i.e., dependants)
 * directly. This is likely best accomplished in implementing classes via some variation of the
 * observer design pattern. Sample implementations keep track of their dependants in a {@link
 * java.util.LinkedHashSet} (to guarantee deterministic ordering) and expose mechanisms for
 * registering new subsrcibers.
 *
 * <p>When a publishing resource experiences a value change (by direct mutation, by re-evaluating a
 * value function, etc.), it is expected to propagate that change to all of its subscribers by
 * prompting them to update. This enables the use of eagerly-derived resources.
 *
 * <p>Note that, currently, publishing resources do not actually send a payload upon value changes;
 * they simply trigger updates in subscribers so that subscribers can re-evaluate themselves. We may
 * expand this interface in the future to support actual payloads with publish operations.
 *
 * @param <V> the value type of the publishing resource
 */
public interface PublishingResource<V> extends Publisher, DiscreteResource<V> {} //RealResource {}
