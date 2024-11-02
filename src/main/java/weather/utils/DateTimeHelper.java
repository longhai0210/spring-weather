package weather.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {
    public static String convertToLocalTime(long timestamp, int timezoneOffsetInSeconds) {
        // Tạo đối tượng Instant từ timestamp
        Instant instant = Instant.ofEpochSecond(timestamp);

        // Tạo ZoneOffset từ timezone offset (tính bằng giây)
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(timezoneOffsetInSeconds);

        // Chuyển đổi sang LocalTime với múi giờ đã điều chỉnh
        LocalTime localTime = instant.atOffset(offset).toLocalTime();

        // Định dạng LocalTime thành chuỗi "hh:mm"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localTime.format(formatter);
    }

    public static String formatTime (String dateString, String format) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(format);

        LocalDateTime date = LocalDateTime.parse(dateString, inputFormatter);
        String formattedDate = date.format(outputFormatter);

        return formattedDate;
    }

    public static int sort(String d1, String d2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(d1, formatter);
        LocalDate date2 = LocalDate.parse(d2, formatter);

        return date1.compareTo(date2);
    }
}
