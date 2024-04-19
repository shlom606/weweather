package com.example.weweather;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {
    @SerializedName("forecast")
    public ForecastData forecastData;
}

