package resources.data.specitications;

import org.springframework.data.jpa.domain.Specification;
import resources.data.entity.Project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectSpecificationBuilder {


    public static Specification<Project> build(String query) {
        List<Specification<Project>> specifications = new ArrayList<>();

        Pattern pattern = Pattern.compile("(\\w+?)([~<>:])([\\w, ]+);");
        Matcher matcher = pattern.matcher(query + ";");
        while (matcher.find()) {
            Object value;
            if ("tags".equalsIgnoreCase(matcher.group(1))) {
                // custom mapping for tags attribute
                value = new HashSet<>(Arrays.asList(matcher.group(3).split(",")));
                System.out.println("query for tags:");
                new HashSet<>(Arrays.asList(matcher.group(3).split(","))).forEach(System.out::println);
            } else {
                value = matcher.group(3);
            }
            System.out.println(matcher.group(1) + " ^ " + matcher.group(2) + " ^ " + value);
            specifications.add(new ProjectSpecification(new SearchCriteria(matcher.group(1), matcher.group(2), value)));
        }

        if (specifications.size() == 0) {
            return null;
        }
        Specification<Project> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }

}
