package sparta.seed.util;

import org.springframework.stereotype.Component;
import sparta.seed.mission.domain.dto.requestdto.MissionSearchCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class DateUtil {
  public String dateStatus(String startDate, String endDate) throws ParseException {
    String todayfm = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Date start = new Date(dateFormat.parse(startDate).getTime()); //시작날짜
    Date end = new Date(dateFormat.parse(endDate).getTime()); // 종료날짜
    Date today = new Date(dateFormat.parse(todayfm).getTime()); // 현재 시점

    int startCompare = start.compareTo(today);
    int endCompare = end.compareTo(today);

    if (startCompare > 0) {
      return "before";
    }
    if (endCompare < 0) {
      return "end";
    }
    return "ongoing";
  }
}
