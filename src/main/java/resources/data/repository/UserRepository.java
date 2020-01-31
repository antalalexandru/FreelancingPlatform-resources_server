package resources.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resources.data.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameEqualsOrEmailEquals(String username, String email);
}
