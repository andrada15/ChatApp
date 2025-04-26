package ro.chatapp.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginPage.class)
class LoginPageUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService loginService;

    private static final String usernameTest = "usernameTest";
    private static final String emailTest = "emailTest@gmail.com";
    private static final String passwordTest = "passwordTest";
    private static final String confirmPasswordTest = "passwordTest";

    @BeforeEach
    void setUp() {
        Mockito.reset(loginService);
    }

    @Test
    void showLoginFormTest() {
        assertDoesNotThrow(() -> mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
        );
    }

    @Test
    void showLoginFormWithErrorTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(post("/login")
                                .param("username", "")
                                .param("password", ""))
                        .andExpect(status().isOk())
                        .andExpect(view().name("login"))
                        .andExpect(model().attributeExists("errorMessage"))
        );
    }

    @Test
    void processLoginTest() {
        when(loginService.validateLogin(usernameTest, passwordTest)).thenReturn(true);

        assertDoesNotThrow(() -> mockMvc.perform(post("/login")
                .param("username", usernameTest)
                .param("password", passwordTest))
        );

        verify(loginService).validateLogin(usernameTest, passwordTest);
    }

    @Test
    void showRegisterFormTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(get("/register"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("register"))
        );
    }

    @Test
    void processRegisterTest() {
        assertDoesNotThrow(() ->
                mockMvc.perform(get("/register"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("register"))
        );
    }
}