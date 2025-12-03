package com.example.issuetracker.repository;

import com.example.issuetracker.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByIssueIdOrderByCreatedAtDesc(Long issueId);
}
