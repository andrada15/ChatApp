package ro.chatapp.message;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.chatapp.entities.Message;
import ro.chatapp.entities.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceUnitTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;



    @Test
    void saveMessageTest() {
        String username = "testUser";
        String content = "Hello world";
        User user = new User();
        user.setName(username);

        Message savedMessage = new Message();
        savedMessage.setUser(user);
        savedMessage.setContent(content);
        savedMessage.setTimestamp(LocalDateTime.now());

        when(userRepository.findByName(username)).thenReturn(Optional.of(user));
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        Message result = assertDoesNotThrow(() -> messageService.saveMessage(username, content));

        assertNotNull(result);
        assertEquals(content, result.getContent());
        assertEquals(user, result.getUser());
        verify(userRepository).findByName(username);
        verify(messageRepository).save(any(Message.class));
    }

}