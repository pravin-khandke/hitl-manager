package com.hitl.manager.adapter.web;

import com.hitl.manager.domain.model.*;
import com.hitl.manager.domain.port.*;
import com.hitl.manager.domain.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;

@Controller
@RequestMapping("/simulator")
public class SimulatorController {
    private final IncidentRepository incidentRepository;
    private final IncidentEvaluationService evaluationService;
    private final OperatorInteractionService interactionService;
    private final FeedbackProcessingService feedbackService;
    private final BlockProgressionService blockProgression;
    private final FeedbackStore feedbackStore;

    public SimulatorController(IncidentRepository incidentRepository,
            IncidentEvaluationService evaluationService,
            OperatorInteractionService interactionService,
            FeedbackProcessingService feedbackService,
            BlockProgressionService blockProgression,
            FeedbackStore feedbackStore) {
        this.incidentRepository = incidentRepository;
        this.evaluationService = evaluationService;
        this.interactionService = interactionService;
        this.feedbackService = feedbackService;
        this.blockProgression = blockProgression;
        this.feedbackStore = feedbackStore;
    }

    @GetMapping
    public String simulator(Model model) {
        blockProgression.nextUnprocessed().ifPresentOrElse(incident -> {
            AISuggestion suggestion = evaluationService.evaluate(incident);
            feedbackStore.saveSuggestion(suggestion);
            model.addAttribute("incident", incident);
            model.addAttribute("suggestion", suggestion);
            model.addAttribute("currentBlock", blockProgression.getCurrentBlock());
        }, () -> model.addAttribute("complete", true));
        return "simulator";
    }

    @PostMapping("/act")
    public String act(@RequestParam UUID incidentId, @RequestParam UUID suggestionId,
                      @RequestParam String decision, @RequestParam(required = false) String modification,
                      @RequestParam(defaultValue = "5000") int responseTimeMs,
                      Model model, RedirectAttributes redirectAttributes) {
        Decision d = Decision.valueOf(decision.toUpperCase());
        OperatorAction action = interactionService.recordAction(suggestionId, incidentId,
                d, modification, responseTimeMs, "operator-1");
        feedbackService.processFeedback(action);

        incidentRepository.findById(incidentId).ifPresent(inc -> {
            inc.markProcessed();
            incidentRepository.save(inc);
        });

        redirectAttributes.addFlashAttribute("lastDecision", d.name());
        return "redirect:/simulator";
    }

    @PostMapping("/reset")
    public String reset() {
        return "redirect:/simulator";
    }
}
