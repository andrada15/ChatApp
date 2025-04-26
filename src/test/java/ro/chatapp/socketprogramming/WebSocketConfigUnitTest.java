package ro.chatapp.socketprogramming;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.mockito.Mockito.*;

class WebSocketConfigUnitTest {

    @Test
    void configureMessageBrokerTest() {
        WebSocketConfig config = new WebSocketConfig();
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);

        config.configureMessageBroker(registry);

        verify(registry).enableSimpleBroker("/topic");
        verify(registry).setApplicationDestinationPrefixes("/app");
    }

    @Test
    void registerStompEndpointsTest() {
        WebSocketConfig config = new WebSocketConfig();
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        StompWebSocketEndpointRegistration stompWebSocketEndpointRegistration = mock(StompWebSocketEndpointRegistration.class);
        SockJsServiceRegistration sockJsServiceRegistration = mock(SockJsServiceRegistration.class);

        when(registry.addEndpoint("/ws")).thenReturn(stompWebSocketEndpointRegistration);
        when(stompWebSocketEndpointRegistration.withSockJS()).thenReturn(sockJsServiceRegistration);

        config.registerStompEndpoints(registry);

        verify(registry).addEndpoint("/ws");
        verify(stompWebSocketEndpointRegistration).withSockJS();
    }
}