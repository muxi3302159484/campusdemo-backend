// 文件：src/main/java/org/zyy/campusdemobackend/config/WebSocketHandshakeInterceptor.java
package org.zyy.campusdemobackend.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 假设 userId 通过参数传递 ws://localhost:8080/ws/chat?userId=123
        String query = request.getURI().getQuery();
        if (query != null && query.contains("userId=")) {
            String userId = query.replaceAll(".*userId=([^&]*).*", "$1");
            attributes.put("userId", userId);
        }
        // 也可以从 Header/Cookie 获取
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}