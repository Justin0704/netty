package cn.enjoyedu.stomp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 开启使用stomp协议来传输基于消息broker的消息
 * 这是控制器支持使用@MessageMapping
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * 注册Stomp协议的节点（endpoint），并映射指定的url
         * 添加一个访问断点"/endpointMark"，客户端打开双通道时需要的url
         * 允许所有的域名跨域访问，指定使用SockJS协议
         */
        registry.addEndpoint("/endpointMark")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         * 配置一个消息代理
         * mass负责群聊
         * queue负责单聊
         */
        registry.enableSimpleBroker("/mass","/queue");
        /**
         * 一对一的用户，请求发到/queue
         */
        registry.setUserDestinationPrefix("/queue");
    }
}
