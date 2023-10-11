package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "speech_contributions")
@Builder
public class SpeechContribution {
    @MongoId
    String id;
    TimeSlot timeSlot;
    User user;
    String stoppedTime;
}