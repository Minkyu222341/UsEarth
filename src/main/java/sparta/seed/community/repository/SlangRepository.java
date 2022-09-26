package sparta.seed.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.seed.community.domain.Slang;

public interface SlangRepository extends JpaRepository<Slang,Long> {
}
