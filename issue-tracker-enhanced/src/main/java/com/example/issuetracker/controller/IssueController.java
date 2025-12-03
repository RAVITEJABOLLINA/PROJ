package com.example.issuetracker.controller;

import com.example.issuetracker.model.Issue;
import com.example.issuetracker.repository.IssueRepository;
import com.example.issuetracker.service.IssueService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;
    private final IssueRepository issueRepo;

    public IssueController(IssueService issueService, IssueRepository issueRepo) {
        this.issueService = issueService;
        this.issueRepo = issueRepo;
    }

    @GetMapping
    public Page<Issue> list(@RequestParam(required = false) String status,
                            @RequestParam(required = false) String assignee,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(s[1]), s[0]));
        return issueService.list(status, assignee, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> get(@PathVariable Long id) {
        return issueRepo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Issue> create(@RequestBody Issue issue, Authentication auth) {
        String actor = auth.getName();
        Issue created = issueService.create(issue, actor);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Issue update, Authentication auth) {
        // Anyone authenticated can update, but deleting is restricted to ROLE_ADMIN
        String actor = auth.getName();
        Optional<Issue> o = issueService.update(id, update, actor);
        return o.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        // only admin can delete
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return ResponseEntity.status(403).body("forbidden");
        }
        boolean ok = issueService.delete(id, auth.getName());
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
