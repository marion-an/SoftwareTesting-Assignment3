package zest;

import java.util.AbstractQueue;
import java.util.ArrayList;

public class PaymentProcessor {
    private EventPublisher eventPublisher;
    private TransactionService transactionService;
    private FraudDetectionService fraudDetectionService;
    private int processCallCounter = 0;
    private int fraudCounter = 0;
    private ArrayList<Transaction> calledWith = new ArrayList<>();
    public PaymentProcessor(EventPublisher publisher, TransactionService service, FraudDetectionService fraudService) {
        this.eventPublisher = publisher;
        this.transactionService = service;
        this.fraudDetectionService = fraudService;
    }

    public void processPayment(Transaction transaction) {
        addProcessCallCounter();
        addCalledWith(transaction);
        if (fraudDetectionService.evaluateTransaction(transaction)) {
            transaction.setEvaluated(true);
            transactionService.processTransaction(transaction);
            transaction.setProcessed(true);
            eventPublisher.publishTransactionComplete(transaction);
            transaction.setPublished(true);
        }else{
            addFraudCounter();
        }
    }

    public EventPublisher getEventPublisher() {
        return eventPublisher;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public FraudDetectionService getFraudDetectionService() {
        return fraudDetectionService;
    }

    public int getProcessCallCounter() {
        return processCallCounter;
    }

    public void setProcessCallCounter(int processCallCounter) {
        this.processCallCounter = processCallCounter;
    }

    public void addProcessCallCounter(){
        this.processCallCounter++;
    }

    public int getFraudCounter() {
        return fraudCounter;
    }

    public void setFraudCounter(int fraudCounter) {
        this.fraudCounter = fraudCounter;
    }

    public void addFraudCounter(){
        this.fraudCounter++;
    }

    public ArrayList<Transaction> getCalledWith() {
        return calledWith;
    }

    public void setCalledWith(ArrayList<Transaction> calledWith) {
        this.calledWith = calledWith;
    }

    public void addCalledWith(Transaction transaction){
        this.calledWith.add(transaction);
    }

}
