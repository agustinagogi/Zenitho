package com.zenitho.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Habilita la configuración de WebSockets
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    // Configura un broker de mensajes de memoria, las apps pueden mandar mensajes a /app y el broker reenviará esos mensajes a /topic
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    // Espone un endpoint de WebSocket en /ws. Es lo que el frontend usará para conectarse
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").withSockJS();
    }
}
