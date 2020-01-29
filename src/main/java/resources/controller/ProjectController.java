package resources.controller;

import de.svenjacobs.loremipsum.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import resources.data.entity.Project;
import resources.data.entity.Tag;
import resources.data.repository.ProjectRepository;
import resources.data.repository.TagRepository;
import resources.data.specitications.ProjectSpecificationBuilder;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TagRepository tagRepository;

    private static final LoremIpsum loremIpsum = new LoremIpsum();

    @GetMapping
    public List<Project> getProjects(@RequestParam(value = "query", required = false, defaultValue = "") String query) {
        ProjectSpecificationBuilder projectSpecificationBuilder = new ProjectSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(query + ",");
        while (matcher.find()) {
            if (!"tags".equalsIgnoreCase(matcher.group(1))) {
                projectSpecificationBuilder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            } else {
                System.out.println("tags ... ");
                projectSpecificationBuilder.with(matcher.group(1), matcher.group(2), new HashSet<>(Arrays.asList(matcher.group(3).split(","))));

            }
        }

        Specification<Project> spec = projectSpecificationBuilder.build();
        return projectRepository.findAll(spec);
    }

    @PostMapping
    public void addProjects() {

        List<Tag> tags = new ArrayList<>(tagRepository.findAll());

        List<Project> projects = new ArrayList<>();

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
        }

    }

}
