import java.util.ArrayList;
import java.util.List;

public class MessageProcessor {

    private final MessageService messageService;

    public MessageProcessor(MessageService messageService) {
        this.messageService = messageService;
    }

    public List<Message> processMessages(List<Message> messages) {
        List<Message> sent = new ArrayList<>();
        for (Message message : messages) {
            messageService.sendMessage(message.getReceiver(), message.getContent());
            sent.add(message);
        }
        return sent;
    }

}
