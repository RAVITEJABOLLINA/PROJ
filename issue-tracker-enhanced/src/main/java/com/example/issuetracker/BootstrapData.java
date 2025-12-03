package com.example.issuetracker;

import com.example.issuetracker.model.*;
import com.example.issuetracker.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepo;
    private final IssueRepository issueRepo;
    private final CommentRepository commentRepo;
    private final ActivityRepository activityRepo;
    private final BCryptPasswordEncoder encoder;

    public BootstrapData(UserRepository userRepo, IssueRepository issueRepo,
                         CommentRepository commentRepo, ActivityRepository activityRepo,
                         BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.issueRepo = issueRepo;
        this.commentRepo = commentRepo;
        this.activityRepo = activityRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.count() == 0) {
            User admin = new User("admin", encoder.encode("adminpass"), "ROLE_ADMIN,ROLE_DEVELOPER");
            User dev = new User("dev", encoder.encode("devpass"), "ROLE_DEVELOPER");
            User rep = new User("reporter", encoder.encode("reporterpass"), "ROLE_REPORTER");
            userRepo.save(admin);
            userRepo.save(dev);
            userRepo.save(rep);
        }

        if (issueRepo.count() == 0) {
            Issue a = new Issue();
            a.setTitle("Sample: NullPointer in Login");
            a.setDescription("App throws NPE when profile missing");
            a.setPriority("HIGH");
            a.setAssignee("dev");
            issueRepo.save(a);

            Issue b = new Issue();
            b.setTitle("UI: misaligned button");
            b.setDescription("Button overlaps on small screens");
            b.setPriority("LOW");
            b.setAssignee("reporter");
            issueRepo.save(b);
        }
    }
}
