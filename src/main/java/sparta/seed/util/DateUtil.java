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

  private static class TIME_MAXIMUM {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;
  }

  public String weekOfMonth() throws ParseException {
    LocalDate now = LocalDate.now();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(String.valueOf(now));
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return String.valueOf(cal.get(Calendar.WEEK_OF_MONTH));
  }

  public List<LocalDate> scopeOfStats(MissionSearchCondition condition) {
    String startDate = condition.getStartDate();
    String endDate = condition.getEndDate();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    List<LocalDate> result = new ArrayList<>();
    result.add(start);
    result.add(end);
    return result;
  }

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
