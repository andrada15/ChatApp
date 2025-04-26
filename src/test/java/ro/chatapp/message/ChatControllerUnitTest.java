package ro.chatapp.message;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.chatapp.endtoendencryption.Encryption;
import ro.chatapp.entities.User;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
class ChatControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessageService messageService;

    @MockitoBean
    private Encryption rsaEncryption;

    @MockitoBean
    private UserRepository userRepository;

    private static final String username = "username@gmail.com";
    private static final String message = "chat";

    @BeforeEach
    void setUp() {
        Mockito.reset(messageService, rsaEncryption, userRepository);
    }

    @Test
    void showChatPageTest() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", username);
        User user = new User("Alice", username, "pass");
        user.setPrivateKey(Base64.getEncoder().encodeToString("key".getBytes()));
        when(userRepository.findByEmailAddress(username)).thenReturn(Optional.of(user));
        when(messageService.getAllMessages()).thenReturn(List.of());
        when(rsaEncryption.decrypt(anyString(), anyString())).thenReturn("hello");

        assertDoesNotThrow(() ->
                mockMvc.perform(get("/chat").session(session))
                        .andExpect(status().isOk())
                        .andExpect(view().name(message))
                        .andExpect(model().attribute("username", username)
        )
        );
    }

    @Test
    void showChatPageTestRedirectsToLoginPage() {
        assertDoesNotThrow(() ->
                mockMvc.perform(get("/chat"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/login"))
        );
    }

    @Test
    void sendMessageTest() {
        User user = new User("Alice", username, "pass");
        user.setPublicKey(Base64.getEncoder().encodeToString("pubkey".getBytes()));

        when(userRepository.findByEmailAddress(username)).thenReturn(Optional.of(user));
        when(rsaEncryption.encrypt(eq("hi"), anyString())).thenReturn("encrypted");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/chat/send")
                            .param("user", username)
                            .param("message", "hi"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/chat"));
        });

        verify(rsaEncryption).encrypt("hi", user.getPublicKey());
        verify(messageService).saveMessage(username, "encrypted");
    }
}