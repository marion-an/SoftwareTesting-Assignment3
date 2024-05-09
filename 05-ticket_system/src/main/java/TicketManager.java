package support;

// TicketManager class to handle ticket creation and interaction with services
public class TicketManager {
    private support.NotificationService notificationService;
    private support.LogService logService;
    private support.TicketRepository ticketRepository;

    public TicketManager(support.NotificationService notificationService, support.LogService logService, support.TicketRepository ticketRepository) {
        this.notificationService = notificationService;
        this.logService = logService;
        this.ticketRepository = ticketRepository;
    }

    public void createTicket(support.Ticket ticket) throws Exception {
        //Preconditions
        verifyTicketNotNull(ticket);
        verifyValidEmail(ticket);
        verifyValidIssueDescription(ticket);
        verifyPriorityNotNull(ticket);

        logTicketCreation(ticket);
        notifyCustomer(ticket);

        // Save the ticket to the database
        saveTicket(ticket);
    }
    
    // Method to save ticket to a database
    private void saveTicket(support.Ticket ticket) {
        ticketRepository.save(ticket);
    }

    private void logTicketCreation(support.Ticket ticket){
        try {
            // Log the ticket creation
            logService.logTicketCreation(ticket);
        }catch (Exception e){
            System.out.println("Logging the ticket creation failed" + e.getMessage());
        }
    }

    private void notifyCustomer(support.Ticket ticket){
        try {
            // Notify the customer
            notificationService.notifyCustomer(ticket.getCustomerEmail(),
                    "Thank you for your request. Your support ticket has been created and will be processed shortly.");
        }catch (Exception e){
            System.out.println("Notifying the customer failed" + e.getMessage());
        }
    }

    private void verifyValidIssueDescription(support.Ticket ticket) throws Exception {
        String description = ticket.getIssueDescription();
        if(description == null || description.isEmpty()){
            throw new Exception("Issue description of ticket cannot be null or empty");
        }
    }

    private void verifyValidEmail(support.Ticket ticket) throws Exception {
        String email = ticket.getCustomerEmail();
        if (email == null || email.isEmpty()) {
            throw new Exception("Email of ticket cannot be null or empty");
        }
    }

    private void verifyTicketNotNull(support.Ticket ticket){
        if(ticket == null){
            throw new NullPointerException("Ticket cannot be null");
        }
    }

    private void verifyPriorityNotNull(support.Ticket ticket) throws Exception {
        support.TicketPriority priority = ticket.getPriority();
        if(priority == null){
            throw new Exception("Priority of ticket cannot be null");
        }
    }
}
