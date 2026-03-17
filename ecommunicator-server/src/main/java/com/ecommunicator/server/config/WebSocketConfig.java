package com.ecommunicator.server.config;

import com.ecommunicator.server.handler.MediaWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * Configures STOMP over WebSocket and the in-process message broker.
 * Also registers the raw binary /media endpoint here to avoid conflicts
 * between @EnableWebSocket and @EnableWebSocketMessageBroker.
 *
 * Control channel:  ws://host:8080/ws-native  (STOMP, Java client)
 * Control channel:  ws://host:8080/ws          (STOMP + SockJS, browser)
 * Media channel:    ws://host:8080/media        (raw binary)
 */
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private final MediaWebSocketHandler mediaWebSocketHandler;

    public WebSocketConfig(@Lazy MediaWebSocketHandler mediaWebSocketHandler) {
        this.mediaWebSocketHandler = mediaWebSocketHandler;
    }

    // ── Raw binary endpoint ───────────────────────────────────────────────────

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mediaWebSocketHandler, "/media")
                .setAllowedOriginPatterns("*");
    }

    // ── STOMP broker ──────────────────────────────────────────────────────────

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/user");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.addEndpoint("/ws-native")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new StompPrincipalInterceptor());
        registration.taskExecutor()
                .corePoolSize(4)
                .maxPoolSize(16)
                .queueCapacity(100);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor()
                .corePoolSize(4)
                .maxPoolSize(16);
    }
}
