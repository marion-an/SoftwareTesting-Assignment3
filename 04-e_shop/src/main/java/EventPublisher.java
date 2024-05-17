import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private final List<EventListener> listeners;

    public EventPublisher(List<EventListener> listeners) {
        this.listeners = listeners;
    }


    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public List<Order> publishOrderToAllListeners(Order order) {
        List<Order> published = new ArrayList<>();
        for (EventListener listener : listeners) {
            listener.onOrderPlaced(order);
            published.add(order);
        }
        return published;
    }
}
