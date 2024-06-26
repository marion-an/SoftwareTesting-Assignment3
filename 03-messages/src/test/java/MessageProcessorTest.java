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
class MessageProcessorTest {

    @Mock
    MessageService messageService;

    @Test
    void numberOfInvocationA() {
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
    void noInvocationA() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        List<Message> messages = new ArrayList<>();

        messageProcessor.processMessages(messages);

        verify(messageService, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void oneInvocationA() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "receiver", "content");

        List<Message> messages = Arrays.asList(message);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        messageProcessor.processMessages(messages);

        verify(messageService, times(1)).sendMessage(anyString(), anyString());
        verify(messageService).sendMessage(message.getReceiver(), message.getContent());
    }

    @Test
    void multipleInvocationA() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "receiver", "content");

        List<Message> messages = Arrays.asList(message, message);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        messageProcessor.processMessages(messages);

        verify(messageService, times(2)).sendMessage(anyString(), anyString());
        verify(messageService, times(2)).sendMessage(message.getReceiver(), message.getContent());
    }

    @Test
    void rightContentB() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "receiver", "content");

        List<Message> messages = Arrays.asList(message);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        messageProcessor.processMessages(messages);

        ArgumentCaptor<String> receiverCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);


        verify(messageService).sendMessage(receiverCaptor.capture(), contentCaptor.capture());

        String receiver = receiverCaptor.getValue();
        String content = contentCaptor.getValue();

        assertEquals("receiver", receiver);
        assertEquals("content", content);
    }

    @Test
    void nullContentB() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", null, null);

        List<Message> messages = Arrays.asList(message);

        doNothing().when(messageService).sendMessage(null, null);

        messageProcessor.processMessages(messages);

        ArgumentCaptor<String> receiverCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);


        verify(messageService).sendMessage(receiverCaptor.capture(), contentCaptor.capture());

        String receiver = receiverCaptor.getValue();
        String content = contentCaptor.getValue();

        assertEquals(null, receiver);
        assertEquals(null, content);
    }

    @Test
    void rightContentMultipleInvocationB() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "Ronald", "software");
        Message message2 = new Message("sender2", "Robert", "Testing");
        Message message3 = new Message("sender3", "Ruedi", "Module FS24");


        List<Message> messages = Arrays.asList(message, message2, message3, message3, message2);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        messageProcessor.processMessages(messages);

        ArgumentCaptor<String> receiverCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);


        verify(messageService, times(5)).sendMessage(receiverCaptor.capture(), contentCaptor.capture());

        List<String> receivers = receiverCaptor.getAllValues();
        List<String> contents = contentCaptor.getAllValues();

        assertEquals("Ronald", receivers.get(0));
        assertEquals("Robert", receivers.get(1));
        assertEquals("Ruedi", receivers.get(2));
        assertEquals("Ruedi", receivers.get(3));
        assertEquals("Robert", receivers.get(4));

        assertEquals("software", contents.get(0));
        assertEquals("Testing", contents.get(1));
        assertEquals("Module FS24", contents.get(2));
        assertEquals("Module FS24", contents.get(3));
        assertEquals("Testing", contents.get(4));
    }


    @Test
    void rightContentC() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "receiver", "content");

        List<Message> messages = Arrays.asList(message);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        List<Message> sent = messageProcessor.processMessages(messages);

        assertEquals(1, sent.size());
        assertEquals("receiver", sent.get(0).getReceiver());
        assertEquals("content", sent.get(0).getContent());

    }

    @Test
    void nullContentC() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", null, null);

        List<Message> messages = Arrays.asList(message);

        doNothing().when(messageService).sendMessage(null, null);

        List<Message> sent = messageProcessor.processMessages(messages);

        assertEquals(1, sent.size());
        assertEquals(null, sent.get(0).getReceiver());
        assertEquals(null, sent.get(0).getContent());
    }

    @Test
    void rightContentMultipleInvocationC() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        Message message = new Message("sender", "Ronald", "software");
        Message message2 = new Message("sender2", "Robert", "Testing");
        Message message3 = new Message("sender3", "Ruedi", "Module FS24");


        List<Message> messages = Arrays.asList(message, message2, message3, message3, message2);

        doNothing().when(messageService).sendMessage(anyString(), anyString());

        List<Message> sent = messageProcessor.processMessages(messages);

        assertEquals(5, sent.size());

        assertEquals("Ronald", sent.get(0).getReceiver());
        assertEquals("Robert", sent.get(1).getReceiver());
        assertEquals("Ruedi", sent.get(2).getReceiver());
        assertEquals("Ruedi", sent.get(3).getReceiver());
        assertEquals("Robert", sent.get(4).getReceiver());

        assertEquals("software", sent.get(0).getContent());
        assertEquals("Testing", sent.get(1).getContent());
        assertEquals("Module FS24", sent.get(2).getContent());
        assertEquals("Module FS24", sent.get(3).getContent());
        assertEquals("Testing", sent.get(4).getContent());
    }

    @Test
    void emptyReturnC() {
        MessageProcessor messageProcessor = new MessageProcessor(messageService);

        List<Message> messages = new ArrayList<>();

        List<Message> sent = messageProcessor.processMessages(messages);

        assertEquals(0, sent.size());
    }
}