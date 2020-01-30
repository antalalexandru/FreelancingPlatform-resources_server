package resources.data.specitications;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
