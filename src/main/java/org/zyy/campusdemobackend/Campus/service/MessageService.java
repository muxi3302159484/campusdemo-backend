// src/main/java/org/zyy/campusdemobackend/service/MessageService.java
package org.zyy.campusdemobackend.Campus.service;

public interface MessageService {
    void saveMessage(Integer from, Integer to, String text, String time);
}