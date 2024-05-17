import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventPublisherTest {

    @Mock
    EventListener listener;

    @Test
    void numberOfInvocationA() {
        List<EventListener> listeners = new ArrayList<>();
        EventPublisher eventPublisher = new EventPublisher(listeners);

        eventPublisher.subscribe(listener);
        eventPublisher.subscribe(listener);
        eventPublisher.subscribe(listener);

        Order order = new Order("orderId", 1);

        doNothing().when(listener).onOrderPlaced(order);

        eventPublisher.publishOrderToAllListeners(order);

        verify(listener, times(3)).onOrderPlaced(order);
    }

    @Test
    void noInvocationA() {
        List<EventListener> listeners = new ArrayList<>();
        EventPublisher eventPublisher = new EventPublisher(listeners);

        Order order = new Order("orderId", 1);


        eventPublisher.publishOrderToAllListeners(order);

        verify(listener, never()).onOrderPlaced(any());
    }

    @Test
    void onInvocationA() {
        List<EventListener> listeners = new ArrayList<>();
        EventPublisher eventPublisher = new EventPublisher(listeners);

        eventPublisher.subscribe(listener);

        Order order = new Order("orderId", 1);

        doNothing().when(listener).onOrderPlaced(order);

        eventPublisher.publishOrderToAllListeners(order);

        verify(listener, times(1)).onOrderPlaced(order);
    }

    @Test
    void rightOrderContentB() {
        List<EventListener> listeners = new ArrayList<>();
        EventPublisher eventPublisher = new EventPublisher(listeners);

        eventPublisher.subscribe(listener);

        Order order = new Order("order1", 1.234);

        doNothing().when(listener).onOrderPlaced(any());

        eventPublisher.publishOrderToAllListeners(order);

        ArgumentCaptor<Order> receiveCaptor = ArgumentCaptor.forClass(Order.class);

        verify(listener).onOrderPlaced(receiveCaptor.capture());

        assertEquals("order1", receiveCaptor.getValue().getOrderId());
        assertEquals(1.234, receiveCaptor.getValue().getAmount());
    }


    @Test
    void nullContentB() {
        List<EventListener> listeners = new ArrayList<>();
        EventPublisher eventPublisher = new EventPublisher(listeners);

        eventPublisher.subscribe(listener);

        doNothing().when(listener).onOrderPlaced(any());

        eventPublisher.publishOrderToAllListeners(null);

        ArgumentCaptor<Order> receiveCaptor = ArgumentCaptor.forClass(Order.class);

        verify(listener).onOrderPlaced(receiveCaptor.capture());

        assertEquals(null, receiveCaptor.getValue());
    }


    @Test
    void multipleListenersB() {
        List<EventListener> listeners = new ArrayList<>();
        EventPublisher eventPublisher = new EventPublisher(listeners);

        eventPublisher.subscribe(listener);
        eventPublisher.subscribe(listener);
        eventPublisher.subscribe(listener);

        Order order = new Order("order1", 1.234);

        doNothing().when(listener).onOrderPlaced(any());

        eventPublisher.publishOrderToAllListeners(order);

        ArgumentCaptor<Order> receiveCaptor = ArgumentCaptor.forClass(Order.class);

        verify(listener, times(3)).onOrderPlaced(receiveCaptor.capture());

        List<Order> receiver = receiveCaptor.getAllValues();

        assertEquals(3, receiver.size());
        assertEquals(order, receiver.get(0));
        assertEquals(order, receiver.get(1));
        assertEquals(order, receiver.get(2));

    }


}