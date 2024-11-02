package weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Controller
//@RequestMapping("/v1")
public class WeatherController {

    @Autowired
    private Http http;

    @Autowired
    ObjectMapper objectMapper;

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
            Map<String, Object> data = objectMapper.readValue(res.body(), Map.class);

            model.addAttribute("cod", res.statusCode());

            if(res.statusCode() == 200) {
                Map<String, Object> dataMain = (Map)data.get("main");
                List<Map<String, Object>> weather = (List<Map<String, Object>>)data.get("weather");
                Map<String, Object> wind = (Map)data.get("wind");
                model.addAttribute("name", data.get("name"));
                model.addAttribute("time", DateTimeHelper.convertToLocalTime((int)data.get("dt"), (int)data.get("timezone")));
                model.addAttribute("temp", Math.round((double)dataMain.get("temp")));
                model.addAttribute("description", weather.get(0).get("description"));
                model.addAttribute("icon", weather.get(0).get("icon"));
                model.addAttribute("humidity", dataMain.get("humidity"));
                model.addAttribute("windSpeed", wind.get("speed"));
            }
            else {
                model.addAttribute("message", data.get("message"));
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
            Map<String, List<Map<String, Object>>> dailyData = new HashMap<>();
            String selectedDate = "";

            HttpResponse<String> res= http.get("https://api.openweathermap.org/data/2.5/forecast?q="+ URLEncoder.encode(location, StandardCharsets.UTF_8) +"&units=metric&lang=vi&appid=" + apiKey);
            Map<String, Map<String, Object>> data = objectMapper.readValue(res.body(), Map.class);

            model.addAttribute("cod", res.statusCode());

            if(res.statusCode() == 200) {
                List<Map<String, Object>> listWeather = (List<Map<String, Object>>)data.get("list");
                List<Map<String, Object>> avgWeatherList = new ArrayList<>();
                for (Map<String, Object> object : listWeather) {
                    String day = ((String) object.get("dt_txt")).split(" ")[0];
                    if(dailyData.get(day) != null) {
                        dailyData.get(day).add(object);
                    }
                    else {
                        List<Map<String, Object>> emptyList = new ArrayList<>();
                        emptyList.add(object);
                        dailyData.put(day, emptyList);
                    }
                }

                for (Map.Entry<String, List<Map<String, Object>>> entry : dailyData.entrySet()) {
                    avgWeatherList.add(WeatherHelper.avgWeather(entry.getValue()));
                }
                if(!date.isEmpty()) {
                    selectedDate = date;
                }
                else {
                    selectedDate = dailyData.keySet().toArray()[0].toString();
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
