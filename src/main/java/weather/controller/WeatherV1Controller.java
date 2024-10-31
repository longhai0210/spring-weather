package weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import weather.service.Http;

import java.net.http.HttpResponse;
import java.util.Map;

@Controller
//@RequestMapping("/v1")
public class WeatherV1Controller {

    @Autowired
    private Http http;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${weather.api.key}")
    private String apiKey;

    @GetMapping("/v1/weather")
    public String getWeather(@RequestParam(defaultValue = "") String location, Model model) {
        try{

        System.out.println(location);
        HttpResponse<String> locationRes= http.get("https://api.openweathermap.org/geo/1.0/direct?q="+ location +"&limit=1&appid=" + apiKey);
        Map<String, Object>[] obj = objectMapper.readValue(locationRes.body(), Map[].class);
        HttpResponse<String> res= http.get("https://api.openweathermap.org/data/2.5/weather?"+ obj[0].get("lat") +obj[0].get("lon") +"&units=metric&lang=vi&appid=" + apiKey);
        Map<String, Object> data = objectMapper.readValue(res.body(), Map.class);
        Map<String, Object> dataMain = (Map)data.get("main");
        model.addAttribute("city", data.get("name"));
        model.addAttribute("temperature", dataMain.get("temp"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "weather/v1/CurrentWeather";
    }

    @GetMapping("/v2/weather")
    public String getWeatherV2(Model model) {
        return "weather/v2/index";
    }

    @GetMapping("/v2/weathers")
    public String getWeathersV2(Model model) {
        return "weather/v2/DailyWeather";
    }
}
