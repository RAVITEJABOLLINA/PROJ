package com.example.issuetracker.controller;

import com.example.issuetracker.model.*;
import com.example.issuetracker.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepo;
    private final IssueRepository issueRepo;

    public CommentController(CommentRepository commentRepo, IssueRepository issueRepo) {
        this.commentRepo = commentRepo;
        this.issueRepo = issueRepo;
    }

    @GetMapping("/issue/{issueId}")
    public ResponseEntity<List<Comment>> byIssue(@PathVariable Long issueId) {
        Optional<Issue> oi = issueRepo.findById(issueId);
        if (oi.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(commentRepo.findByIssue(oi.get()));
    }

    @PostMapping("/issue/{issueId}")
    public ResponseEntity<?> add(@PathVariable Long issueId, @RequestBody Comment comment, Authentication auth) {
        Optional<Issue> oi = issueRepo.findById(issueId);
        if (oi.isEmpty()) return ResponseEntity.notFound().build();
        comment.setIssue(oi.get());
        comment.setAuthor(auth.getName());
        Comment saved = commentRepo.save(comment);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        Optional<Comment> oc = commentRepo.findById(id);
        if (oc.isEmpty()) return ResponseEntity.notFound().build();
        Comment c = oc.get();
        // allow author or admin to delete
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!auth.getName().equals(c.getAuthor()) && !isAdmin) {
            return ResponseEntity.status(403).body("forbidden");
        }
        commentRepo.delete(c);
        return ResponseEntity.noContent().build();
    }
}
