package org.zyy.campusdemobackend.Campus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zyy.campusdemobackend.Campus.model.Event;
import org.zyy.campusdemobackend.Campus.repository.EventRepository;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}