package groundwar.render.event;

@FunctionalInterface
public interface EventHandler<T extends Event> {

    /**
     * Handle an event of this handler's type.
     *
     * @param event the event that occurred
     */
    void handle(T event);

}
