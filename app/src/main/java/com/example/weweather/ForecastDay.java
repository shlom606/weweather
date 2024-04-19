package com.example.weweather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastDay {
    @SerializedName("hour")
    public List<Hour> hours;
}
