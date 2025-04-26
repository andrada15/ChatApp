package ro.chatapp.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ro.chatapp.entities.User;
import ro.chatapp.message.UserRepository;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    User user = new User("Alice", "alice@example.com", "password");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateLoginTest() {

        when(userRepository.findByEmailAddress("alice@example.com")).thenReturn(Optional.of(user));

        boolean result = loginService.validateLogin("alice@example.com", "password");

        assertTrue(result);
        verify(userRepository).findByEmailAddress("alice@example.com");
    }

    @Test
    void registerUserTest() {
        when(userRepository.findByEmailAddress("alice@example.com")).thenReturn(Optional.empty());

        boolean result = loginService.registerUser(user, "password");

        assertTrue(result);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertNotNull(saved.getPublicKey(), "Public key should be set");
        assertNotNull(saved.getPrivateKey(), "Private key should be set");

        assertDoesNotThrow(() -> Base64.getDecoder().decode(saved.getPublicKey()));
        assertDoesNotThrow(() -> Base64.getDecoder().decode(saved.getPrivateKey()));
    }
}