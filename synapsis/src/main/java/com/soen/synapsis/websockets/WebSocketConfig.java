package com.soen.synapsis.websockets;

import com.soen.synapsis.utility.ExcludeFromGeneratedTestReport;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures the Spring WebSocket Message Broker.
 * Relates to WebSockets
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker registry.
     * Enables SimpleBroker with two destinations: "/queue" and "/specific".
     * Sets the application destination prefix to "/app".
     * @param config the message broker registry configuration object.
     */
    @Override
    @ExcludeFromGeneratedTestReport
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue", "/specific");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers a STOMP endpoint at "/ws" with SockJS support.
     * @param registry the STOMP endpoint registry.
     */
    @Override
    @ExcludeFromGeneratedTestReport
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .withSockJS();
    }

}