package de.iav.backend.repository;

import de.iav.backend.model.SpeechContribution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeechContributionRepository  extends MongoRepository<SpeechContribution, String> {
}