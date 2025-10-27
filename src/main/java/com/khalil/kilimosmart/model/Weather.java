package com.khalil.kilimosmart.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    private Main main;
    private String name;

    public Weather() {}

    public Weather(Main main, String name) {
        this.main = main;
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Nested Class
    public static class Main {
        private double temp;
        private int humidity;

        // Constructors
        public Main() {}

        public Main(double temp, int humidity) {
            this.temp = temp;
            this.humidity = humidity;
        }

        // Getters and Setters
        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }
}
