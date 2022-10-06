package sparta.seed.campaign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.seed.campaign.domain.AqApiData;
import sparta.seed.campaign.repository.custom.AqRepositoryCustom;

import java.util.Optional;

public interface AqRepository extends JpaRepository<AqApiData,Long> , AqRepositoryCustom {
	Optional<AqApiData> findByDatetimeAndRegion(String dateTime, String region);
}
