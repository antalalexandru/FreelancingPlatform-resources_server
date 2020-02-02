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
            String field = matcher.group(1);
            Object value;
            if ("tags".equalsIgnoreCase(matcher.group(1))) {
                // custom mapping for tags attribute
                value = new HashSet<>(Arrays.asList(matcher.group(3).split(",")));
            } else {
                value = matcher.group(3);
            }
            specifications.add(new ProjectSpecification(new SearchCriteria(field, matcher.group(2), value)));
            if (field.equalsIgnoreCase("paymentLowerBound") || field.equalsIgnoreCase("paymentUpperBound")) {
                specifications.add((Specification<Project>) (root, q, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(field), 0));
            }
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