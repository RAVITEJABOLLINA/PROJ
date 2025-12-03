package com.example.issuetracker.service;

import com.example.issuetracker.model.*;
import com.example.issuetracker.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IssueService {

    private final IssueRepository issueRepo;
    private final ActivityRepository activityRepo;

    public IssueService(IssueRepository issueRepo, ActivityRepository activityRepo) {
        this.issueRepo = issueRepo;
        this.activityRepo = activityRepo;
    }

    public Page<Issue> list(String status, String assignee, Pageable pageable) {
        if (status != null && assignee != null) return issueRepo.findByStatusAndAssignee(status, assignee, pageable);
        if (status != null) return issueRepo.findByStatus(status, pageable);
        if (assignee != null) return issueRepo.findByAssignee(assignee, pageable);
        return issueRepo.findAll(pageable);
    }

    @Transactional
    public Issue create(Issue issue, String actor) {
        Issue saved = issueRepo.save(issue);
        Activity a = new Activity();
        a.setIssueId(saved.getId());
        a.setActor(actor);
        a.setAction("CREATED");
        a.setDetail("Issue created: " + saved.getTitle());
        activityRepo.save(a);
        return saved;
    }

    @Transactional
    public Optional<Issue> update(Long id, Issue update, String actor) {
        return issueRepo.findById(id).map(existing -> {
            String before = existing.getStatus() + "/" + existing.getPriority() + "/" + existing.getAssignee();
            existing.setTitle(update.getTitle());
            existing.setDescription(update.getDescription());
            existing.setStatus(update.getStatus());
            existing.setPriority(update.getPriority());
            existing.setAssignee(update.getAssignee());
            Issue s = issueRepo.save(existing);

            Activity a = new Activity();
            a.setIssueId(s.getId());
            a.setActor(actor);
            a.setAction("UPDATED");
            a.setDetail("Before: " + before + " After: " + s.getStatus() + "/" + s.getPriority() + "/" + s.getAssignee());
            activityRepo.save(a);

            return s;
        });
    }

    @Transactional
    public boolean delete(Long id, String actor) {
        return issueRepo.findById(id).map(existing -> {
            issueRepo.delete(existing);
            Activity a = new Activity();
            a.setIssueId(id);
            a.setActor(actor);
            a.setAction("DELETED");
            a.setDetail("Issue deleted: " + existing.getTitle());
            activityRepo.save(a);
            return true;
        }).orElse(false);
    }
}
