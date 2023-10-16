package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "meetings")
@Builder
@CompoundIndex(name = "onlyOneDateTimeLocation", def = "{'dateTime': 1, 'location': 1}", unique = true)
public class Meeting {
    @MongoId
    String id;
    String dateTime;
    String location;
    @DBRef
    List<SpeechContribution> speechContributionList;
}