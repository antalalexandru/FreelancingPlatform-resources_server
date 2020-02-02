package resources.controller;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resources.data.entity.Application;
import resources.data.entity.Group;
import resources.data.entity.Project;
import resources.data.entity.Tag;
import resources.data.entity.User;
import resources.data.repository.ApplicationRepository;
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

    @Autowired
    private ApplicationRepository applicationRepository;

    @PostMapping("/project")
    public void populateDatabaseWithProjects() {

        Lorem lorem = LoremIpsum.getInstance();

        List<Tag> tags = new ArrayList<>(tagRepository.findAll());
        List<User> users = new ArrayList<>(userRepository.findAll());

        for(int i = 0; i < 1275; i++) {

            Collections.shuffle(users);

            Project project = new Project();

            project.setName(lorem.getWords(5, 15));
            project.setDescription(lorem.getHtmlParagraphs(5, 10));
            project.setAuthor(users.get(0));
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

            List<Application> applications = new ArrayList<>();

            Project savedProject = projectRepository.save(project);

            for (int j = 0; j < project.getEnrolled(); j++) {
                applications.add(Application.builder()
                        .date(new Date())
                        .description(lorem.getHtmlParagraphs(2, 4))
                        .projectId(savedProject.getId())
                        .user(users.get(j + 1))
                        .build());
            }

            List<Application> savedApplications = applicationRepository.saveAll(applications);

            if (Math.random() < 0.2) {
                Collections.shuffle(savedApplications);
                savedProject.setSelectedApplication(savedApplications.get(0));
                projectRepository.save(savedProject);
            }

        }
    }

    @PostMapping("/user")
    public void populateDatabaseWithUsers() {
        List<User> users = new ArrayList<>();
        for(int i = 100; i < 10000; i++) {
            users.add(User.builder()
                    .username(generateRandomUsername())
                    .email("inexistent_email_" + i + "@wlg.ro")
                    .group(Group.builder().id(2).build())
                    .is_enabled(true)
                    .password(passwordEncoder.encode("1234"))
                    .build());
        }
        userRepository.saveAll(users);
    }

    private static String generateRandomUsername() {
        int length = 5 + ThreadLocalRandom.current().nextInt(15);
        return RandomStringUtils.random(length, true, false);
    }

}
