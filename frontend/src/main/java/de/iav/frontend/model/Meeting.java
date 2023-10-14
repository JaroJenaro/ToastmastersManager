package de.iav.frontend.model;

import java.util.List;

public record Meeting(
        String id,
        String dateTime,
        String location,
        List<SpeechContribution> speechContributionList
) {
}
