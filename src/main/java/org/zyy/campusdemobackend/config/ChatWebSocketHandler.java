package org.zyy.campusdemobackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.ConcurrentHashMap;
import org.zyy.campusdemobackend.dto.MessageDTO;
public class ChatWebSocketHandler extends TextWebSocketHandler {
    // userId -> session
    private static final ConcurrentHashMap<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = getUserId(session);
        if (userId != null) userSessions.put(userId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = getUserId(session);
        if (userId != null) userSessions.remove(userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // 用 Jackson 解析 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        MessageDTO msg = objectMapper.readValue(payload, MessageDTO.class);

        // 持久化到数据库（伪代码）
        // messageService.save(msg);

        // 推送给目标用户
        WebSocketSession toSession = userSessions.get(msg.getTo());
        if (toSession != null && toSession.isOpen()) {
            toSession.sendMessage(new TextMessage(payload));
        }
    }

    private Long getUserId(WebSocketSession session) {
        // 从 queryString 解析 userId
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("userId=")) {
            return Long.valueOf(query.substring(7));
        }
        return null;
    }
}