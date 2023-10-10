package de.iav.backend.repository;

import de.iav.backend.model.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends MongoRepository<TimeSlot, String> {


    Optional<TimeSlot> findByTitleAndRed(String title, String red);
    Optional<TimeSlot> findByTitleAndDescription(String title, String description);


    List<TimeSlot> findAllByTitleEqualsIgnoreCase(String title);
    List<TimeSlot> findAllByDescriptionEqualsIgnoreCase(String description);
    List<TimeSlot> findAllByGreenEqualsIgnoreCase(String title);
    List<TimeSlot> findAllByAmberEqualsIgnoreCase(String title);
    List<TimeSlot> findAllByRedEqualsIgnoreCase(String title);

}
