package com.khalil.kilimosmart.controller;

import com.khalil.kilimosmart.model.Weather;
import com.khalil.kilimosmart.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin("*")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public Weather getWeather(@RequestParam String city) {
        return weatherService.getWeatherByCity(city);
    }
}
