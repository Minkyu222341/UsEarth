package sparta.seed.campaign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.seed.campaign.domain.AqApiData;
import sparta.seed.campaign.repository.custom.AqRepositoryCustom;

public interface AqRepository extends JpaRepository<AqApiData,Long> , AqRepositoryCustom {
}
