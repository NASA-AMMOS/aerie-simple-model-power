package examplemodel.models.power;

import java.util.Set;

/**
 * This interface is common to objects which manage their own set of listeners and update those listeners eagerly
 */
public interface Publisher {

    /**
     * Registers a subscriber on this publisher
     *
     * @param subscriber the object to register as a subscriber
     */
    void registerSubscriber(Subscriber subscriber);

    /**
     * Returns the (unique) set of objects that are registered as subscriber on the current publisher
     *
     * @return the unique set of subscribers
     */
    Set<Subscriber> getSubscribers();

    /**
     * Triggers an update to the subscribers on the current publisher
     */
    default void updateSubscribers() {
        getSubscribers().forEach(Subscriber::update);
    }
}
