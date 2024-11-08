package weather.utils;

import org.json.JSONObject;

import java.util.*;

public class WeatherHelper {
    public static Map<String, Object> avgWeather(List<JSONObject> weatherData) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> mainWeather = new HashMap<>();

        result.put("temp", 0.0);
        result.put("humidity", 0.0);
        result.put("temp_max", 0.0);
        result.put("temp_min", 9999.0);
        result.put("format_time", DateTimeHelper.formatTime(weatherData.get(0).getString("dt_txt").split(" ")[0] + " 00:00:00", "EEE, d MMM"));
        result.put("date", weatherData.get(0).getString("dt_txt").split(" ")[0]);

        for (JSONObject weather : weatherData) {
            if (weather.getJSONObject("main").get("temp") instanceof Integer){
                result.put("temp", (Double)result.get("temp") + weather.getJSONObject("main").getInt("temp")) ;
            }
            else {
                result.put("temp", (Double) result.get("temp") + weather.getJSONObject("main").getDouble("temp")) ;
            }
            if(weather.getJSONObject("main").get("temp_max") instanceof Double){
                result.put("temp_max", Math.max((Double) result.get("temp_max"), weather.getJSONObject("main").getDouble("temp_max")));
            }
            else {
                result.put("temp_max", Math.max((Double) result.get("temp_max"), weather.getJSONObject("main").getInt("temp_max")));
            }
            if(weather.getJSONObject("main").get("temp_min") instanceof Double){
                result.put("temp_min", Math.min((Double) result.get("temp_min"), weather.getJSONObject("main").getDouble("temp_min")));
            }
            else {
                result.put("temp_min", Math.min((Double) result.get("temp_min"), weather.getJSONObject("main").getInt("temp_min")));
            }
            if(weather.getJSONObject("main").get("humidity") instanceof Integer){
                result.put("humidity", ((Double) result.get("humidity")) + weather.getJSONObject("main").getInt("humidity"));
            }
            else {
                result.put("humidity", ((Double) result.get("humidity")) +  weather.getJSONObject("main").getDouble("humidity"));
            }

            String wt = weather.getJSONArray("weather").getJSONObject(0).getString("main");
            if(mainWeather.get(wt) == null) {
                mainWeather.put(wt, 1);
            }
            else {
                mainWeather.put(wt, 1);
            }
        }
        String mainWt = Collections.max(mainWeather.entrySet(), Map.Entry.comparingByValue()).getKey();
        result.put("weather", mainWt);
        result.put("temp", Math.round((double) result.get("temp") / weatherData.size()));
        result.put("humidity", Math.round((double) result.get("humidity") / weatherData.size()));
        result.put("temp_max", Math.round((double) result.get("temp_max")));
        result.put("temp_min", Math.round((double) result.get("temp_min")));
        return result;
    }

    public static List<Map<String, Object>> getWeatherDetail(List<JSONObject> weatherData) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (JSONObject weather : weatherData) {
            Map<String, Object> weatherDetail = new HashMap<>();
            if(weather.getJSONObject("main").get("temp") instanceof Double){
                weatherDetail.put("temp", Math.round( weather.getJSONObject("main").getDouble("temp")));
            }
            else {
                weatherDetail.put("temp", Math.round(weather.getJSONObject("main").getDouble("temp")));
            }
            weatherDetail.put("icon", weather.getJSONArray("weather").getJSONObject(0).getString("icon"));
            weatherDetail.put("time", DateTimeHelper.formatTime(weather.getString("dt_txt"), "H:mm"));
            weatherDetail.put("amPm", DateTimeHelper.formatTime(weather.getString("dt_txt"), "a"));
            result.add(weatherDetail);
        }

        return result;
    }
}
