package resources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ResourcesServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourcesServerApplication.class, args);
    }
}
