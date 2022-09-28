package sparta.seed.campaign.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import sparta.seed.campaign.repository.custom.AqRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static sparta.seed.campaign.domain.QAqApiData.aqApiData;
@RequiredArgsConstructor
public class AqRepositoryImpl implements AqRepositoryCustom {

  private final JPAQueryFactory queryFactory;


  public Double airQualityData(String category, String datetime) {
    Double average = 0.0;
    double averageResult;
    List<Double> fetch = queryFactory.select(aqApiData.amount)
            .from(aqApiData)
            .where(aqApiData.category.eq(category), aqApiData.datetime.eq(datetime))
            .fetch();

    for (Double amount : fetch) {
      average += amount;
    }
    averageResult = average / fetch.size();

    return averageResult;
  }
}
