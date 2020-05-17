package dev.dgomes.backend.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.net.InetAddress;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WsUserInterceptor());
        //registration.setInterceptors(new WsUserInterceptor());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("*")
                .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        try{
            InetAddress address = InetAddress.getByName("rabbitmq");
            boolean reachable = address.isReachable(10000);

            if (reachable){
                registry.setApplicationDestinationPrefixes("/app")
                        //.setUserDestinationPrefix("/topic/user/queue")
                        .enableStompBrokerRelay("/topic")
                        .setRelayHost("rabbitmq")
                        .setRelayPort(61613);
            } else{
                // for running testes without rabbutmq
                registry.setApplicationDestinationPrefixes("/app")
                        .enableSimpleBroker("/topic");
            }
        } catch (Exception e){
            e.printStackTrace();
            registry.setApplicationDestinationPrefixes("/app")
                    .enableSimpleBroker("/topic");
        }

    }
}
