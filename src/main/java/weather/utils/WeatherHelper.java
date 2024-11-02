package weather.utils;

import java.util.*;

public class WeatherHelper {
    public static Map<String, Object> avgWeather(List<Map<String, Object>> weatherData) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> mainWeather = new HashMap<>();

        result.put("temp", 0.0);
        result.put("humidity", 0.0);
        result.put("temp_max", 0.0);
        result.put("temp_min", 9999.0);
        result.put("format_time", DateTimeHelper.formatTime(weatherData.get(0).get("dt_txt").toString().split(" ")[0] + " 00:00:00", "EEE, d MMM"));
        result.put("date", weatherData.get(0).get("dt_txt").toString().split(" ")[0]);

        for (Map<String, Object> weather : weatherData) {
            if (((Map<String, Object>) weather.get("main")).get("temp") instanceof Integer){
                result.put("temp", ((Double) result.get("temp")) + ((Map<String, Integer>) weather.get("main")).get("temp")) ;
            }
            else {
                result.put("temp", ((Double) result.get("temp")) + (Double)((Map<String, Object>) weather.get("main")).get("temp")) ;
            }
            if(((Map<String, Object>) weather.get("main")).get("temp_max") instanceof Double){
                result.put("temp_max", Math.max((Double) result.get("temp_max"), ((Map<String, Double>) weather.get("main")).get("temp_max")));
            }
            else {
                result.put("temp_max", Math.max((Double) result.get("temp_max"), ((Map<String, Integer>) weather.get("main")).get("temp_max")));
            }
            if(((Map<String, Object>) weather.get("main")).get("temp_min") instanceof Double){
                result.put("temp_min", Math.min((Double) result.get("temp_min"), ((Map<String, Double>) weather.get("main")).get("temp_min")));
            }
            else {
                result.put("temp_min", Math.min((Double) result.get("temp_min"), ((Map<String, Integer>) weather.get("main")).get("temp_min")));
            }
            if(((Map<String, Integer>) weather.get("main")).get("humidity") instanceof Integer){
                result.put("humidity", ((Double) result.get("humidity")) + ((Map<String, Integer>) weather.get("main")).get("humidity"));
            }
            else {
                result.put("humidity", ((Double) result.get("humidity")) + ((Map<String, Integer>) weather.get("main")).get("humidity"));
            }

            String wt = ((List<Map<String, String>>) weather.get("weather")).get(0).get("main");
            if(mainWeather.get(wt) == null) {
                mainWeather.put(wt, 1);
            }
            else {
                mainWeather.put(wt, 1);
            }
        }
        String mainWt = Collections.max(mainWeather.entrySet(), Map.Entry.comparingByValue()).getKey();
        result.put("weather", mainWt);
        result.put("temp", Math.round(((double) result.get("temp")) / weatherData.size()));
        result.put("humidity", Math.round((double) result.get("humidity")) / weatherData.size());
        result.put("temp_max", Math.round((double) result.get("temp_max")));
        result.put("temp_min", Math.round((double) result.get("temp_min")));
        return result;
    }

    public static List<Map<String, Object>> getWeatherDetail(List<Map<String, Object>> weatherData) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> weather : weatherData) {
            Map<String, Object> weatherDetail = new HashMap<>();
            weatherDetail.put("temp", Math.round(((Map<String, Double>) weather.get("main")).get("temp")));
            weatherDetail.put("icon", ((List<Map<String, Integer>>) weather.get("weather")).get(0).get("icon"));
            weatherDetail.put("time", DateTimeHelper.formatTime((String) weather.get("dt_txt"), "H:mm"));
            weatherDetail.put("amPm", DateTimeHelper.formatTime((String) weather.get("dt_txt"), "a"));
            result.add(weatherDetail);
        }

        return result;
    }
}
