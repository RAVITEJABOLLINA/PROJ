package com.example.issuetracker.repository;

import com.example.issuetracker.model.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    Page<Issue> findByStatus(String status, Pageable pageable);
    Page<Issue> findByAssignee(String assignee, Pageable pageable);
    Page<Issue> findByStatusAndAssignee(String status, String assignee, Pageable pageable);
}
