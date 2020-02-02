package resources.controller;

import de.svenjacobs.loremipsum.LoremIpsum;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resources.data.entity.Group;
import resources.data.entity.Project;
import resources.data.entity.Tag;
import resources.data.entity.User;
import resources.data.repository.ProjectRepository;
import resources.data.repository.TagRepository;
import resources.data.repository.UserRepository;

import java.util.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/dummy_populate")
public class DummyPopulateDatabaseController {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/project")
    public void populateDatabaseWithProjects() {
/*
        LoremIpsum loremIpsum = new LoremIpsum();

        List<Tag> tags = new ArrayList<>(tagRepository.findAll());

        for(int i = 0; i < 100; i++) {

            Project project = new Project();

            project.setName(loremIpsum.getWords(ThreadLocalRandom.current().nextInt(30)));
            project.setDescription(loremIpsum.getParagraphs(ThreadLocalRandom.current().nextInt(4)));
            project.setAuthorId(1);
            project.setEnrolled(ThreadLocalRandom.current().nextInt(100));
            project.setSubmitted(new Date(ThreadLocalRandom.current().nextLong(
                    Instant.now().minus(Duration.ofDays(40)).getEpochSecond(),
                    Instant.now().minus(Duration.ofDays(10)).getEpochSecond()
            ) * 1000));
            project.setEndDate(new Date(ThreadLocalRandom.current().nextLong(
                    Instant.now().plus(Duration.ofDays(10)).getEpochSecond(),
                    Instant.now().plus(Duration.ofDays(25)).getEpochSecond()
            ) * 1000));

            if (Math.random() < 0.2) {
                project.setPaymentLowerBound(10 + 10 * ((long) (Math.random() * 10)));
                project.setPaymentUpperBound(0);
            } else {
                project.setPaymentLowerBound(10 + 10 * ((long) (Math.random() * 30)));
                project.setPaymentUpperBound(project.getPaymentLowerBound() + 10 + 10 * ((long) (Math.random() * 20)));
            }

            int numberOfTags = ThreadLocalRandom.current().nextInt(0, 6);

            if (numberOfTags == 0) {
                project.setTags(new HashSet<>());
            } else {
                Set<Tag> tagsSet = new HashSet<>();
                Collections.shuffle(tags);
                for(int j = 0; j < numberOfTags; j++) {
                    tagsSet.add(tags.get(j));
                }
                project.setTags(tagsSet);
            }


            projectRepository.save(project);
        }*/
    }

    @PostMapping("/user")
    public void populateDatabaseWithUsers() {
        for(int i = 0; i < 100; i++) {
            userRepository.save(User.builder()
                    .username(generateRandomUsername())
                    .email("inexistent_email_" + i + "@wlg.ro")
                    .group(Group.builder().id(2).build())
                    .is_enabled(true)
                    .password(passwordEncoder.encode("1234"))
                    .build());
        }
    }

    private static String generateRandomUsername() {
        int length = 5 + ThreadLocalRandom.current().nextInt(10);
        return RandomStringUtils.random(length, true, false);
    }

}
