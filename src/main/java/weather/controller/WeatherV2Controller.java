package weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import weather.payload.FiveDaysRequest;
import weather.payload.WeatherRequest;
import weather.service.Http;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class WeatherV2Controller {

    @Autowired
    private Http http;

    @Value("${weather.api.key}")
    private String apiKey;


    @GetMapping("/weather")
    public ResponseEntity<?> getWeather(@ModelAttribute WeatherRequest req) {
        HttpResponse<?> res= http.get("https://api.openweathermap.org/data/2.5/weather?"+ req.toQueryString() +"&appid=" + apiKey);

        return ResponseEntity.status(res.statusCode())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(res.body());
    }

    @GetMapping("/coordinates")
    public ResponseEntity<?> getCoordinates(@RequestParam String location) {
        HttpResponse<?> res= http.get("https://api.openweathermap.org/geo/1.0/direct?q="+ location +"&appid=" + apiKey);

        return ResponseEntity.status(res.statusCode())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(res.body());
    }

    @GetMapping("/weathers")
    public ResponseEntity<?> get5Day(@ModelAttribute FiveDaysRequest req) {
        HttpResponse<?> res= http.get("https://api.openweathermap.org/data/2.5/forecast?"+ req.toQueryString() +"&appid="+apiKey);

        return ResponseEntity.status(res.statusCode())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(res.body());
    }
}
