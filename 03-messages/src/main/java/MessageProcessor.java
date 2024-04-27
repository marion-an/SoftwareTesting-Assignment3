import java.util.List;

public class MessageProcessor {

    private final MessageService messageService;

    public MessageProcessor(MessageService messageService) {
        this.messageService = messageService;
    }

    public void processMessages(List<Message> messages) {
        for (Message message : messages) {
            messageService.sendMessage(message.getReceiver(), message.getContent());
        }
    }
}
