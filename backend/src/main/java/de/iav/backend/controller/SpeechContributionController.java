package de.iav.backend.controller;

import de.iav.backend.model.SpeechContributionDTO;
import de.iav.backend.model.SpeechContributionIn;
import de.iav.backend.service.SpeechContributionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/toast-master-manager/speech-contributions")
public class SpeechContributionController {

    private final SpeechContributionService speechContributionService;

    public SpeechContributionController(SpeechContributionService speechContributionService) {
        this.speechContributionService = speechContributionService;
    }

    @GetMapping
    public List<SpeechContributionDTO> getAllSpeechContributions() {
        return speechContributionService.getAllSpeechContributions();
    }

    @GetMapping("/{id}")
    public SpeechContributionDTO getSpeechContributionById(@PathVariable String id) {
        return speechContributionService.getSpeechContributionById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpeechContributionDTO createSpeechContribution(@Valid @RequestBody SpeechContributionIn speechContributionIn) {
        return speechContributionService.addSpeechContribution(speechContributionIn);
    }

    @PutMapping("/{id}")
    public SpeechContributionDTO updateSpeechContribution(@PathVariable String id, @Valid @RequestBody SpeechContributionIn speechContributionIn) {
        return speechContributionService.updateSpeechContribution(speechContributionIn, id);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSpeechContribution(@PathVariable String id) {
        speechContributionService.deleteSpeechContribution(id);
    }
}