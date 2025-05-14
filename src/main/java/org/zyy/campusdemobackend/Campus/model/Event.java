// src/main/java/org/zyy/campusdemobackend/Campus/model/Event.java
package org.zyy.campusdemobackend.Campus.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "organizer_id")
    private Integer organizerId;

    @Column(name = "organizer_type")
    private String organizerType;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "location")
    private String location;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "description")
    private String description;

    @Column(name = "school_id")
    private Integer schoolId;

    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public Integer getOrganizerId() { return organizerId; }
    public void setOrganizerId(Integer organizerId) { this.organizerId = organizerId; }
    public String getOrganizerType() { return organizerType; }
    public void setOrganizerType(String organizerType) { this.organizerType = organizerType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getSchoolId() { return schoolId; }
    public void setSchoolId(Integer schoolId) { this.schoolId = schoolId; }
}