package weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import weather.service.Http;
import weather.utils.DateTimeHelper;
import weather.utils.WeatherHelper;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;

@Controller
public class WeatherController {

    @Autowired
    private Http http;

    @Value("${weather.api.key}")
    private String apiKey;

    @GetMapping("/")
    public String getIndex(Model model) {
        return "redirect:/weather";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam(defaultValue = "") String location, Model model) {
        model.addAttribute("location", location);

        if(location.isEmpty()) {
            return "weather";
        }
        try{
            HttpResponse<String> res= http.get("https://api.openweathermap.org/data/2.5/weather?q="+ URLEncoder.encode(location, StandardCharsets.UTF_8) +"&units=metric&lang=vi&appid=" + apiKey);
            JSONObject data = new JSONObject(res.body());

            model.addAttribute("cod", res.statusCode());

            if(res.statusCode() == 200) {
                JSONObject dataMain = data.getJSONObject("main");
                JSONArray weather = data.getJSONArray("weather");
                JSONObject wind = data.getJSONObject("wind");
                model.addAttribute("name", data.getString("name"));
                model.addAttribute("time", DateTimeHelper.convertToLocalTime(data.getInt("dt"), data.getInt("timezone")));
                model.addAttribute("temp", Math.round(dataMain.getDouble("temp")));
                model.addAttribute("description", weather.getJSONObject(0).getString("description"));
                model.addAttribute("icon", weather.getJSONObject(0).getString("icon"));
                model.addAttribute("humidity", dataMain.getNumber("humidity"));
                model.addAttribute("windSpeed", wind.getNumber("speed"));
            }
            else {
                model.addAttribute("message", data.getString("message"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "weather";
    }

    @GetMapping("/daily")
    public String getDaily(@RequestParam(defaultValue = "")  String location, @RequestParam(defaultValue = "")  String date, Model model) {

        model.addAttribute("location", location);
        if(location.isEmpty()) {
            return "daily";
        }
        try{
            Map<String, List<JSONObject>> dailyData = new HashMap<>();
            String selectedDate = "";

            HttpResponse<String> res= http.get("https://api.openweathermap.org/data/2.5/forecast?q="+ URLEncoder.encode(location, StandardCharsets.UTF_8) +"&units=metric&lang=vi&appid=" + apiKey);
//            Map<String, Map<String, Object>> data = objectMapper.readValue(res.body(), Map.class);
            JSONObject data = new JSONObject(res.body());
            model.addAttribute("cod", res.statusCode());

            if(res.statusCode() == 200) {
                JSONArray listWeather = data.getJSONArray("list");
                List<Map<String, Object>> avgWeatherList = new ArrayList<>();
                for (int i =0; i < listWeather.length(); i++) {
                    JSONObject object = listWeather.getJSONObject(i);
                    String day = object.getString("dt_txt").split(" ")[0];
                    if(dailyData.get(day) != null) {
                        dailyData.get(day).add(object);
                    }
                    else {
                        List<JSONObject> emptyList = new ArrayList<>();
                        emptyList.add(object);
                        dailyData.put(day, emptyList);
                    }
                }

                for (Map.Entry<String, List<JSONObject>> entry : dailyData.entrySet()) {
                    avgWeatherList.add(WeatherHelper.avgWeather(entry.getValue()));
                }
                avgWeatherList.sort((m1, m2) -> {
                    return DateTimeHelper.sort((String) m1.get("date"), (String)m2.get("date"));
                });
                if(!date.isEmpty()) {
                    selectedDate = date;
                }
                else {
                    selectedDate = avgWeatherList.get(0).get("date").toString();
                }
                model.addAttribute("mainAvgInfo", WeatherHelper.avgWeather(dailyData.get(selectedDate)));
                model.addAttribute("weathers", WeatherHelper.getWeatherDetail(dailyData.get(selectedDate)));
                model.addAttribute("city", data.get("city"));
                model.addAttribute("selectedDate", selectedDate);
                model.addAttribute("date", DateTimeHelper.formatTime(selectedDate + " 00:00:00", "dd/MM/YYYY"));
                model.addAttribute("avgWeatherList", avgWeatherList);
            }
            else {
                model.addAttribute("message", data.get("message"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return "daily";
    }
}
