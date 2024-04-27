import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageProcessorTest {

    @Mock
    MessageService messageService;

    @Test
    void numberOfInvocation() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "receiver", "content");
        Message message2 = new Message("sender2", "receiver2", "content2");
        Message message3 = new Message("sender3", "receiver3", "content3");

        List<Message> messages = Arrays.asList(message, message2, message3);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        messageProcessor.processMessages(messages);

        verify(messageService, times(3)).sendMessage(anyString(), anyString());
        verify(messageService).sendMessage(message.getReceiver(), message.getContent());
        verify(messageService).sendMessage(message2.getReceiver(), message2.getContent());
        verify(messageService).sendMessage(message3.getReceiver(), message3.getContent());
    }

    @Test
    void noInvocation() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        List<Message> messages = new ArrayList<>();

        messageProcessor.processMessages(messages);

        verify(messageService, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void oneInvocation() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "receiver", "content");

        List<Message> messages = Arrays.asList(message);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        messageProcessor.processMessages(messages);

        verify(messageService, times(1)).sendMessage(anyString(), anyString());
        verify(messageService).sendMessage(message.getReceiver(), message.getContent());
    }

    @Test
    void multipleInvocation() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "receiver", "content");

        List<Message> messages = Arrays.asList(message, message);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        messageProcessor.processMessages(messages);

        verify(messageService, times(2)).sendMessage(anyString(), anyString());
        verify(messageService, times(2)).sendMessage(message.getReceiver(), message.getContent());
    }

}