package com.khalil.kilimosmart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.khalil.kilimosmart.model.Weather;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public Weather getWeatherByCity(String city) {
        String url = apiUrl + "?q=" + city + "&units=metric&appid=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Weather> response = restTemplate.getForEntity(url, Weather.class);
        return response.getBody();
    }
}
