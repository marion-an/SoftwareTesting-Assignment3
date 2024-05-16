import java.util.List;

public class EventPublisher {
    private final List<EventListener> listeners;

    public EventPublisher(List<EventListener> listeners) {
        this.listeners = listeners;
    }


    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void publishOrderToAllListeners(Order order) {
        for (EventListener listener : listeners) {
            listener.onOrderPlaced(order);
        }
    }
}
