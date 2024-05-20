package zest;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublishTransActionCompleteTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private FraudDetectionService fraudDetectionService;

    @Test
    void testCallAllListeners(){
        EventPublisher eventPublisher = Mockito.spy(EventPublisher.class);
        AuditService auditService = Mockito.spy(new AuditService() {
            @Override
            public void onTransactionComplete(Transaction transaction) {

            }
        });
        ArrayList<AuditService> listeners = new ArrayList<>();
        eventPublisher.subscribe(auditService);
        eventPublisher.subscribe(auditService);
        eventPublisher.subscribe(auditService);
        Transaction transaction = new Transaction();
        eventPublisher.publishTransactionComplete(transaction);
        verify(eventPublisher,times(1)).publishTransactionComplete(transaction);
        verify(eventPublisher,times(3)).subscribe(auditService);
        verify(auditService,times(3)).onTransactionComplete(transaction);
    }

    @Test
    void testCallAllNoListeners(){
        EventPublisher eventPublisher = Mockito.spy(EventPublisher.class);

        Transaction transaction = new Transaction();
        eventPublisher.publishTransactionComplete(transaction);

        verify(eventPublisher,times(1)).publishTransactionComplete(transaction);
        assertTrue(eventPublisher.getListeners().isEmpty());
        verify(eventPublisher,times(0)).subscribe(Mockito.any());
    }

    @Test
    void testCallAllOneListeners(){
        EventPublisher eventPublisher = Mockito.spy(EventPublisher.class);

        Transaction transaction = new Transaction();

        AuditService auditService = Mockito.spy(new AuditService() {
            @Override
            public void onTransactionComplete(Transaction transaction) {

            }
        });

        eventPublisher.subscribe(auditService);
        eventPublisher.publishTransactionComplete(transaction);

        verify(eventPublisher,times(1)).publishTransactionComplete(transaction);
        verify(auditService,times(1)).onTransactionComplete(transaction);
        verify(eventPublisher,times(1)).subscribe(auditService);
    }

    @Test
    void testCallWithNullTransaction(){

        EventPublisher eventPublisher = Mockito.spy(EventPublisher.class);

        Transaction transaction = null;
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);

        AuditService auditService = Mockito.spy(new AuditService() {
            @Override
            public void onTransactionComplete(Transaction transaction) {

            }
        });

        eventPublisher.subscribe(auditService);
        eventPublisher.publishTransactionComplete(transaction);

        verify(auditService).onTransactionComplete(transactionArgumentCaptor.capture());
        assertNull(transactionArgumentCaptor.getValue());
    }

    @Test
    void testCallCorrectTransaction(){

        EventPublisher eventPublisher = Mockito.spy(EventPublisher.class);

        Transaction transaction = new Transaction();
        transaction.setId("XJ1");
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);

        AuditService auditService = Mockito.spy(new AuditService() {
            @Override
            public void onTransactionComplete(Transaction transaction) {

            }
        });

        eventPublisher.subscribe(auditService);
        eventPublisher.publishTransactionComplete(transaction);

        verify(auditService).onTransactionComplete(transactionArgumentCaptor.capture());
        assertEquals("XJ1",transactionArgumentCaptor.getValue().getId());
    }


    @Test
    void testFunctionCalled(){
        EventPublisher eventPublisher = new EventPublisher();
        PaymentProcessor paymentProcessor = new PaymentProcessor(eventPublisher, transactionService, fraudDetectionService);
        Transaction transaction = new Transaction();
        transaction.setId("CK4");

        paymentProcessor.processPayment(transaction);

        assertEquals(1,paymentProcessor.getProcessCallCounter());
    }

    @Test
    void testArgumentsPassedCorrectly(){
        EventPublisher eventPublisher = new EventPublisher();
        PaymentProcessor paymentProcessor = new PaymentProcessor(eventPublisher, transactionService, fraudDetectionService);
        Transaction transaction = new Transaction();
        transaction.setId("CK4");

        paymentProcessor.processPayment(transaction);

        assertEquals("CK4",paymentProcessor.getCalledWith().get(0).getId());
    }

    @Test
    void testNullArgument(){
        EventPublisher eventPublisher = new EventPublisher();
        PaymentProcessor paymentProcessor = new PaymentProcessor(eventPublisher, transactionService, fraudDetectionService);

        paymentProcessor.processPayment(null);

        assertNull(paymentProcessor.getCalledWith().get(0));
    }

    @Test
    void testMultipleCalls(){
        EventPublisher eventPublisher = new EventPublisher();
        PaymentProcessor paymentProcessor = new PaymentProcessor(eventPublisher, transactionService, fraudDetectionService);
        Transaction transaction = new Transaction();
        transaction.setId("ABC");
        Transaction transaction2 = new Transaction();
        transaction2.setId("123");
        Mockito.when(fraudDetectionService.evaluateTransaction(Mockito.any())).thenReturn(true);
        AuditService auditService = Mockito.spy(new AuditService() {
            @Override
            public void onTransactionComplete(Transaction transaction) {

            }
        });
        eventPublisher.subscribe(auditService);
        paymentProcessor.processPayment(transaction);
        paymentProcessor.processPayment(transaction2);
        assertEquals(2,paymentProcessor.getEventPublisher().getAmountPublished());
        assertEquals(2,paymentProcessor.getProcessCallCounter());
        assertEquals("ABC",paymentProcessor.getCalledWith().get(0).getId());
        assertEquals("123",paymentProcessor.getCalledWith().get(1).getId());
    }

    @Test
    void testMultipleCallsWithFraud(){
        EventPublisher eventPublisher = new EventPublisher();
        PaymentProcessor paymentProcessor = new PaymentProcessor(eventPublisher, transactionService, fraudDetectionService);
        Transaction transaction = new Transaction();
        transaction.setId("ABC");
        Transaction transaction2 = new Transaction();
        transaction2.setId("123");

        Mockito.when(fraudDetectionService.evaluateTransaction(Mockito.any())).thenReturn(false);
        AuditService auditService = Mockito.spy(new AuditService() {
            @Override
            public void onTransactionComplete(Transaction transaction) {

            }
        });
        eventPublisher.subscribe(auditService);

        paymentProcessor.processPayment(transaction);
        paymentProcessor.processPayment(transaction2);
        assertEquals(2,paymentProcessor.getProcessCallCounter());
        assertEquals(2,paymentProcessor.getFraudCounter());
        assertEquals(0,paymentProcessor.getEventPublisher().getAmountPublished());
    }
}
