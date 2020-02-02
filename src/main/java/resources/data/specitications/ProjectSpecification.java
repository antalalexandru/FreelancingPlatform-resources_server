package resources.data.specitications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import resources.data.entity.Project;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

@AllArgsConstructor
public class ProjectSpecification implements Specification<Project> {

    private SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
            // check if date
            if (Pattern.compile("[0-9]{4,}-[0-9]{2,}-[0-9]{2,}").matcher(searchCriteria.getValue().toString()).find()) {
                Path<Date> dateCreatedPath = root.get(searchCriteria.getKey());
                try {
                    return criteriaBuilder.greaterThanOrEqualTo(dateCreatedPath, new SimpleDateFormat("yyyy-MM-dd").parse(searchCriteria.getValue().toString()));
                } catch (ParseException e) {
                    throw new RuntimeException("Bad request!"); // TODO
                }
            }
            return criteriaBuilder.ge(root.get(searchCriteria.getKey()), Integer.valueOf(searchCriteria.getValue().toString().trim()));
        }

        if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
            // check if date
            if (Pattern.compile("[0-9]{4,}-[0-9]{2,}-[0-9]{2,}").matcher(searchCriteria.getValue().toString()).find()) {
                Path<Date> dateCreatedPath = root.get(searchCriteria.getKey());
                try {
                    return criteriaBuilder.lessThanOrEqualTo(dateCreatedPath, new SimpleDateFormat("yyyy-MM-dd").parse(searchCriteria.getValue().toString()));
                } catch (ParseException e) {
                    throw new RuntimeException("Bad request!"); // TODO
                }
            }
            return criteriaBuilder.le(root.get(searchCriteria.getKey()), Integer.valueOf(searchCriteria.getValue().toString().trim()));
        }

        if (searchCriteria.getOperation().equalsIgnoreCase("~")) {
            return criteriaBuilder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue().toString() + "%");
        }

        if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(searchCriteria.getKey()).getJavaType() == Set.class) {
                System.out.println(" !!!!! checking ");
                return root.join("tags").get("name").in(searchCriteria.getValue());
            }
            return criteriaBuilder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
        }
        return null;
    }
}
