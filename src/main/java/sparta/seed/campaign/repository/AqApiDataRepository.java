package sparta.seed.campaign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.seed.campaign.domain.AqApiData;

public interface AqApiDataRepository extends JpaRepository<AqApiData,Long> {
}
