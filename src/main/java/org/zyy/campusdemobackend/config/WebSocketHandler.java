// src/main/java/org/zyy/campusdemobackend/config/WebSocketHandler.java
package org.zyy.campusdemobackend.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.springframework.stereotype.Component;
import org.zyy.campusdemobackend.Campus.service.MessageService;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            sessions.put(userId, session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        try {
            JSONObject json = JSON.parseObject(payload);
            String from = json.getString("from");
            String to = json.getString("to");
            String text = json.getString("text");
            String time = json.getString("time");

            // 1. 消息持久化
            messageService.saveMessage(Integer.valueOf(from), Integer.valueOf(to), text, time);

            // 2. 实时转发
            WebSocketSession toSession = sessions.get(to);
            if (toSession != null && toSession.isOpen()) {
                JSONObject resp = new JSONObject();
                resp.put("from", from);
                resp.put("to", to);
                resp.put("text", text);
                resp.put("time", time);
                toSession.sendMessage(new TextMessage(resp.toJSONString()));
            }
        } catch (Exception e) {
            System.out.println("收到非JSON消息: " + payload);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        if (userId != null) {
            sessions.remove(userId);
        }
    }
}