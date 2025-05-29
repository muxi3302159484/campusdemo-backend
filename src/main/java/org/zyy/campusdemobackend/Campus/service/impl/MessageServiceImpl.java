// src/main/java/org/zyy/campusdemobackend/service/impl/MessageServiceImpl.java
package org.zyy.campusdemobackend.Campus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zyy.campusdemobackend.Campus.model.Message;
import org.zyy.campusdemobackend.Campus.repository.MessageRepository;
import org.zyy.campusdemobackend.Campus.service.MessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void saveMessage(Integer from, Integer to, String text, String time) {
        Message message = new Message();
        message.setSenderId(from);
        message.setReceiverId(to);
        message.setContent(text);
        message.setSentAt(LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME));
        messageRepository.save(message);
    }
}