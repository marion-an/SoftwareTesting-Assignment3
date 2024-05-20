package zest;

import java.util.ArrayList;
import java.util.List;

public class EventPublisher {
    private List<AuditService> listeners = new ArrayList<>();
    private int amountPublished = 0;

    public void subscribe(AuditService listener) {
        listeners.add(listener);
    }

    public void publishTransactionComplete(Transaction transaction) {
        for (AuditService listener : listeners) {
            amountPublished++;
            listener.onTransactionComplete(transaction);
        }
    }
    public List<AuditService> getListeners() {
        return listeners;
    }

    public void setListeners(List<AuditService> listeners) {
        this.listeners = listeners;
    }

    public int getAmountPublished() {
        return amountPublished;
    }
}
