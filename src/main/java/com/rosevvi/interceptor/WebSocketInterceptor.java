package com.rosevvi.interceptor;

import com.rosevvi.config.BaseContext;
import com.rosevvi.tools.JsonWebToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: rosevvi
 * @date: 2023/3/29 11:19
 * @version: 1.0
 * @description:
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {
    public static final JsonWebToken jsonWebToken =new JsonWebToken();
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            log.info("*****beforeHandshake******");
            HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            BaseContext.setThreadLocal(jsonWebToken.getCalim(httpServletRequest.getHeader("Sec-WebSocket-Protocol")));
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("******afterHandshake******");
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        servletResponse.addHeader("Sec-WebSocket-Protocol",httpServletRequest.getHeader("Sec-WebSocket-Protocol"));
    }
}
