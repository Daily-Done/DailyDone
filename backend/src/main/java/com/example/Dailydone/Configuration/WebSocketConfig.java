package com.example.Dailydone.Configuration;

import com.example.Dailydone.Security.CustomHandshakeHandler;
import com.example.Dailydone.Security.JwtHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;
    @Autowired
    private CustomHandshakeHandler customHandshakeHandler;
     @Override
     public void configureMessageBroker(MessageBrokerRegistry config) {
         config.enableSimpleBroker("/topic"); // All users subscribe here
         config.setApplicationDestinationPrefixes("/app");
         config.setUserDestinationPrefix("/user");// Client → Server
     }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(customHandshakeHandler)
                .withSockJS();
    }
}
