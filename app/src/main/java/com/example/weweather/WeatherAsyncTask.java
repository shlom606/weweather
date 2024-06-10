package com.example.weweather;


import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WeatherAsyncTask {
    private OnDataRetrievedListener mListener;
    private static final String API_KEY = "f0fda6ff26b54b21a8765838241704";  // the api link from weatherapi.com i got
    private String QUERY;
    private static final int FORECAST_DAYS = 7;  // Number of forecast days set to 7

    public WeatherAsyncTask(String city, OnDataRetrievedListener listener) {
        this.QUERY=city;
        this.mListener = listener;

    }

    public void execute() {// the execute function runs with asynch task to get the containment of the api link onto a string
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {// using okhttpclient to check the link request response
                final OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://api.weatherapi.com/v1/forecast.json?key="+API_KEY+"&"+"q="+QUERY+"&days="+FORECAST_DAYS)
                        .build();
                try (okhttp3.Response response = client.newCall(request).execute()) {
                    String jsonData = response.body().string();
                    Log.d(TAG,"https://api.weatherapi.com/v1/forecast.json?key="+API_KEY+"&"+"q="+QUERY+"&days="+FORECAST_DAYS);

                    // Notify the listener with jsonData
                    mListener.onDataRetrieved(jsonData);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
