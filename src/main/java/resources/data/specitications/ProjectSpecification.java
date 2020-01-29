package resources.data.specitications;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import resources.data.entity.Project;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Set;

@AllArgsConstructor
public class ProjectSpecification implements Specification<Project> {
    private SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        }

        if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        }

        if (searchCriteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(searchCriteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
            }

            if (root.get(searchCriteria.getKey()).getJavaType() == Set.class) {
                return root.join("tags").get("name").in(searchCriteria.getValue());
            }

            return criteriaBuilder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
        }
        return null;
    }
}
