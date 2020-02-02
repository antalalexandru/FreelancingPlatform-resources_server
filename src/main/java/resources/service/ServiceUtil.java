package resources.service;

import org.springframework.data.domain.Sort;

class ServiceUtil {
    // Util class
    private ServiceUtil () {
    }

    static Sort processSortRequest(String sort) {
        if (sort == null || sort.trim().length() == 0) {
            return Sort.unsorted();
        }
        String[] data = sort.trim().split(":");
        if (data.length == 1 || "asc".equalsIgnoreCase(data[1])) {
            return Sort.by(data[0]).ascending();
        } else {
            return Sort.by(data[0]).descending();
        }
    }
}
