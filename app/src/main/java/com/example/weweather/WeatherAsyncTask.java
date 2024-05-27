package com.example.weweather;


import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WeatherAsyncTask {
    private OnDataRetrievedListener mListener;
    private static final String API_KEY = "f0fda6ff26b54b21a8765838241704";  // Replaced with your WeatherAPI key
    private String QUERY;
    private static final int FORECAST_DAYS = 7;  // Number of forecast days

    public WeatherAsyncTask(String city, OnDataRetrievedListener listener) {
        this.QUERY=city;
        this.mListener = listener;

    }

    public void execute() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.weatherapi.com/v1/forecast.json?key="+API_KEY+"&"+"q="+QUERY+"&days="+FORECAST_DAYS)
                        .build();
                try (okhttp3.Response response = client.newCall(request).execute()) {
                    String jsonData = response.body().string();
                    Log.d(TAG,"https://api.weatherapi.com/v1/forecast.json?key="+API_KEY+"&"+"q="+QUERY+"&days="+FORECAST_DAYS);
                    //Log.d(TAG, "API Body Response: " + jsonData);

                    // Notify the listener with jsonData
                    mListener.onDataRetrieved(jsonData);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
