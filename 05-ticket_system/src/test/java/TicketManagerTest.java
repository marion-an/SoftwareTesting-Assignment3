package support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketManagerTest{
    @Mock
    support.NotificationService notificationService;
    @Mock
    support.LogService logService;
    @Mock
    support.TicketRepository ticketRepository;

    private support.TicketManager ticketManager;

    @BeforeEach
    void setUp(){
        this.ticketManager = new support.TicketManager(notificationService, logService, ticketRepository);
    }

    @Test
    void verifyLogTicketCreationCalled() throws Exception {
        support.Ticket ticket1 = new support.Ticket("ticket1@gmail.com", "issued description", support.TicketPriority.NORMAL);
        support.Ticket ticket2 = new support.Ticket("ticket2@gmail.com", "issued description", support.TicketPriority.URGENT);

        ticketManager.createTicket(ticket1);
        ticketManager.createTicket(ticket2);

        verify(logService, times(2)).logTicketCreation(any(support.Ticket.class));
        verify(logService).logTicketCreation(ticket1);
        verify(logService).logTicketCreation(ticket2);
    }

    @Test
    void verifyNotifyingCustomerCalled() throws Exception {
        support.Ticket ticket1 = new support.Ticket("ticket1@gmail.com", "issued description", support.TicketPriority.NORMAL);
        support.Ticket ticket2 = new support.Ticket("ticket2@gmail.com", "issued description", support.TicketPriority.URGENT);

        ticketManager.createTicket(ticket1);
        ticketManager.createTicket(ticket2);

        verify(notificationService).notifyCustomer("ticket1@gmail.com", "Thank you for your request. Your support ticket has been created and will be processed shortly.");
        verify(notificationService).notifyCustomer("ticket2@gmail.com", "Thank you for your request. Your support ticket has been created and will be processed shortly.");
    }

    @Test
    void verifyTicketIsSaved() throws Exception {
        support.Ticket ticket1 = new support.Ticket("ticket1@gmail.com", "issued description", support.TicketPriority.NORMAL);
        support.Ticket ticket2 = new support.Ticket("ticket2@gmail.com", "issued description", support.TicketPriority.URGENT);

        doNothing().when(logService).logTicketCreation(ticket1);
        doNothing().when(logService).logTicketCreation(ticket2);

        doNothing().when(notificationService).notifyCustomer("ticket1@gmail.com", "Thank you for your request. Your support ticket has been created and will be processed shortly.");
        doNothing().when(notificationService).notifyCustomer("ticket2@gmail.com", "Thank you for your request. Your support ticket has been created and will be processed shortly.");

        ticketManager.createTicket(ticket1);
        ticketManager.createTicket(ticket2);

        verify(ticketRepository, times(2)).save(any(support.Ticket.class));
        verify(ticketRepository).save(ticket1);
        verify(ticketRepository).save(ticket2);
    }

    @Test
    void createTicketAndTicketIsNull(){
        Exception exception = assertThrows(Exception.class, () -> ticketManager.createTicket(null));
        assertEquals("Ticket cannot be null", exception.getMessage());
    }

    @Test
    void createTicketAndEmailIsNullOrEmpty(){
        support.Ticket ticketEmailIsNull = new support.Ticket(null, "issued description", support.TicketPriority.NORMAL);
        support.Ticket ticketEmailIsEmptyString = new support.Ticket("", "issued description", support.TicketPriority.NORMAL);

        Exception exceptionEmailIsNull = assertThrows(Exception.class, () -> ticketManager.createTicket(ticketEmailIsNull));
        assertEquals("Email of ticket cannot be null or empty", exceptionEmailIsNull.getMessage());

        Exception exceptionEmailIsEmptyString = assertThrows(Exception.class, () -> ticketManager.createTicket(ticketEmailIsEmptyString));
        assertEquals("Email of ticket cannot be null or empty", exceptionEmailIsEmptyString.getMessage());
    }

    @Test
    void createTicketAndIssueDescriptionIsNullOrEmpty(){
        support.Ticket ticketIssueDescriptionIsNull = new support.Ticket("email@gmail.com", null, support.TicketPriority.NORMAL);
        support.Ticket ticketIssueDescriptionIsEmptyString = new support.Ticket("email@gmail.com", "", support.TicketPriority.NORMAL);

        Exception exceptionIssueDescriptionIsNull = assertThrows(Exception.class, () -> ticketManager.createTicket(ticketIssueDescriptionIsNull));
        assertEquals("Issue description of ticket cannot be null or empty", exceptionIssueDescriptionIsNull.getMessage());

        Exception exceptionIssueDescriptionIsEmptyString = assertThrows(Exception.class, () -> ticketManager.createTicket(ticketIssueDescriptionIsEmptyString));
        assertEquals("Issue description of ticket cannot be null or empty", exceptionIssueDescriptionIsEmptyString.getMessage());
    }

    @Test
    void createTicketAndPriorityIsNull(){
        support.Ticket ticketPriorityIsNull = new support.Ticket("email@gmail.com", "description", null);

        Exception exceptionPriorityIsNull = assertThrows(Exception.class, () -> ticketManager.createTicket(ticketPriorityIsNull));
        assertEquals("Priority of ticket cannot be null", exceptionPriorityIsNull.getMessage());
    }

    @Test
    void createTicketLogServiceFails() throws Exception {
        support.Ticket ticket1 = new support.Ticket("ticket1@gmail.com", "issued description", support.TicketPriority.NORMAL);
        support.Ticket ticket2 = new support.Ticket("ticket2@gmail.com", "issued description", support.TicketPriority.URGENT);

        doThrow(new RuntimeException()).when(notificationService).notifyCustomer("ticket1@gmail.com", "Thank you for your request. Your support ticket has been created and will be processed shortly.");

        ticketManager.createTicket(ticket1);
        ticketManager.createTicket(ticket2);

        verify(ticketRepository, times(2)).save(any(support.Ticket.class));
        verify(ticketRepository).save(ticket1);
        verify(ticketRepository).save(ticket2);
    }

    @Test
    void createTicketNotificationServiceFails() throws Exception {
        support.Ticket ticket1 = new support.Ticket("ticket1@gmail.com", "issued description", support.TicketPriority.NORMAL);
        support.Ticket ticket2 = new support.Ticket("ticket2@gmail.com", "issued description", support.TicketPriority.URGENT);

        doThrow(new RuntimeException()).when(logService).logTicketCreation(ticket1);

        ticketManager.createTicket(ticket1);
        ticketManager.createTicket(ticket2);

        verify(ticketRepository, times(2)).save(any(support.Ticket.class));
        verify(ticketRepository).save(ticket1);
        verify(ticketRepository).save(ticket2);
    }


}