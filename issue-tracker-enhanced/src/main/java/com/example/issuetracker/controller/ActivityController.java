package com.example.issuetracker.controller;

import com.example.issuetracker.model.Activity;
import com.example.issuetracker.repository.ActivityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository activityRepo;

    public ActivityController(ActivityRepository activityRepo) {
        this.activityRepo = activityRepo;
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<Activity>> forIssue(@PathVariable Long issueId) {
        return ResponseEntity.ok(activityRepo.findByIssueIdOrderByCreatedAtDesc(issueId));
    }
}
