package com.example.weweather;

import com.google.gson.annotations.SerializedName;

public class Hour {
    @SerializedName("time")
    public String time;

    @SerializedName("temp_c")
    public double temperatureC;

    @SerializedName("condition")
    public Condition condition;
}
