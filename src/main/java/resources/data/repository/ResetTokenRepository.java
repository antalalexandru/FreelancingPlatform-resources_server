package resources.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resources.data.entity.ResetToken;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByUserIdEquals(Long userId);

    Optional<ResetToken> findByTokenEquals(String token);
}
