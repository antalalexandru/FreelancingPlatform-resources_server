package resources.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resources.data.entity.ActivationToken;

import java.util.Optional;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {
    Optional<ActivationToken> findByUserIdEquals(Long userId);
}
