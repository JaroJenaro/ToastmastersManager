package de.iav.backend.repository;

import de.iav.backend.model.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends MongoRepository<TimeSlot, String> {

}
