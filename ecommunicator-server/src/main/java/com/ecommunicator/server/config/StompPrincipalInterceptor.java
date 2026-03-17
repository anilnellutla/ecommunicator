package com.ecommunicator.server.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;

/**
 * Sets a Principal on each STOMP session (using the session ID as the name)
 * so Spring's user-destination routing works without authentication.
 *
 * Without a Principal, convertAndSendToUser() cannot route private messages.
 */
public class StompPrincipalInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            if (sessionId != null && accessor.getUser() == null) {
                accessor.setUser(new SessionPrincipal(sessionId));
            }
        }
        return message;
    }

    /** Simple Principal backed by the STOMP session ID. */
    public record SessionPrincipal(String name) implements Principal {
        @Override public String getName() { return name; }
    }
}
