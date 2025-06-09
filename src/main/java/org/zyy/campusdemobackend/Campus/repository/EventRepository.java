package org.zyy.campusdemobackend.Campus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zyy.campusdemobackend.Campus.model.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {

}
