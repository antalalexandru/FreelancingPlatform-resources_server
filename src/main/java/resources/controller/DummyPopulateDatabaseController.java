package resources.controller;

import de.svenjacobs.loremipsum.LoremIpsum;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resources.data.entity.Project;
import resources.data.entity.Tag;
import resources.data.repository.ProjectRepository;
import resources.data.repository.TagRepository;

import java.sql.Date;
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

    @PostMapping("/project")
    public void populateDatabaseWithProjects() {

        LoremIpsum loremIpsum = new LoremIpsum();

        List<Tag> tags = new ArrayList<>(tagRepository.findAll());

        for(int i = 0; i < 100; i++) {

            Project project = new Project();

            project.setName(LoremIpsumUtils.getRandomSentence(5 + ThreadLocalRandom.current().nextInt(7)));
            project.setDescription(LoremIpsumUtils.getRandomParagraphs(1 + ThreadLocalRandom.current().nextInt(10)));
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

    private static final class LoremIpsumUtils {
        private static final LoremIpsum loremIpsum = new LoremIpsum();

        private static String getRandomSentence(final int wordsCount) {
            List<String> words = new ArrayList<>(Arrays.asList(
                  loremIpsum.getWords(wordsCount, ThreadLocalRandom.current().nextInt(40)).split(" "))
            ).stream().map(String::toLowerCase).collect(Collectors.toList());
            Collections.shuffle(words);
            return StringUtils.capitalize(String.join(" ", words));
        }

        private static String getRandomParagraphs(final int paragraphsCount) {
            return "<p>" + IntStream.range(0, paragraphsCount)
                  .mapToObj(val -> getRandomSentence(5 + ThreadLocalRandom.current().nextInt(20)))
                  .collect(Collectors.joining("</p><p>")) + "</p>";
        }

    }

}
