package de.iav.backend.repository;

import de.iav.backend.model.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String>{

     Optional<Meeting> findByDateTimeIsIgnoreCaseAndLocationIgnoreCase(String dateTime, String location);
}