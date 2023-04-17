package com.rosevvi.config;

import com.rosevvi.Handler.MyWebSocketHandler;
import com.rosevvi.interceptor.WebSocketInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author: rosevvi
 * @date: 2023/3/29 11:21
 * @version: 1.0
 * @description:
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //设置跨域
        registry.addHandler(webSocketHandler(),"websocket").addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*");
    }
    public MyWebSocketHandler webSocketHandler(){
        return new MyWebSocketHandler();
    }
}
