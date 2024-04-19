package com.example.weweather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastData {
    @SerializedName("forecastday")
    public List<ForecastDay> forecastDays;
}
