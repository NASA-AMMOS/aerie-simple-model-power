package genericpowersystem.models.power;


/**
 * interface of an object that can be triggered to update its value
 */

public interface Subscriber {
    /**
     * method invoked when object should updates its value, eg by reassessing inputs
     */
    void update();
}
