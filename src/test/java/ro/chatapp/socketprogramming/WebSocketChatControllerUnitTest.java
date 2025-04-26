package ro.chatapp.socketprogramming;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ro.chatapp.message.ChatMessage;
import ro.chatapp.message.MessageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebSocketChatControllerUnitTest {

    @Mock
    private MessageService messageService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private WebSocketChatController webSocketChatController;

    @Test
    void sendMessageTest() {
        ChatMessage inputMessage = new ChatMessage();
        inputMessage.setUser("testuser");
        inputMessage.setContent("Hello!");

        ChatMessage result = assertDoesNotThrow(() ->
                webSocketChatController.sendMessage(inputMessage));

        assertNotNull(result);
        assertEquals("testuser", result.getUser());
        assertEquals("Hello!", result.getContent());
        verify(messageService).saveMessage("testuser", "Hello!");
    }
}