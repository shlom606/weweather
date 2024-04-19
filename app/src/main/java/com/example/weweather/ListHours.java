package com.example.weweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListHours extends AppCompatActivity {
    ListView listhours;
    Button returnPrev;
    private WeatherAsyncTask weatherAsyncTask;

    private static final String BASE_URL = "https://api.weatherapi.com/v1/";
    private static final String API_KEY = "f0fda6ff26b54b21a8765838241704";  // Replaced with your WeatherAPI key
     String QUERY = "Rehovot";  // Example city


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hours);
        listhours=findViewById(R.id.ListHours);
        returnPrev=findViewById(R.id.btn_returnprev);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);

            return;
        }

        // Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        returnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHours.this, ListDays.class);
                startActivity(intent);
            }
        });
        //String subName = getIntent().getStringExtra("searchDayInData");
        int DayID = getIntent().getIntExtra("searchDayInData", 0);



        weatherAsyncTask = new WeatherAsyncTask(QUERY,new OnDataRetrievedListener() {
            @Override
            public void onDataRetrieved(String jsonData) {
                // Use jsonData here
                // Parse JSON data using Gson or other methods
                Gson gson = new Gson();
                Forecast forecast = gson.fromJson(jsonData, Forecast.class);

                List<String> timeList = new ArrayList<>();
                List<String> temperatureList = new ArrayList<>();
                List<String> conditionList=new ArrayList<>();
                if (!forecast.forecastData.forecastDays.isEmpty()) {
                    ForecastDay forecastDay = forecast.forecastData.forecastDays.get(DayID); // Get the first ForecastDay

                    for (Hour hour : forecastDay.hours) {
                        timeList.add(hour.time);
                        temperatureList.add(String.valueOf(hour.temperatureC));
                        conditionList.add(String.valueOf(hour.condition.text));
                    }
                }

                // Convert List<String> to String[]
                String[] timeArray = timeList.toArray(new String[0]);
                String[] temperatureArray = temperatureList.toArray(new String[0]);
                String[] conditionArray=conditionList.toArray(new String[0]);
                String[] singleLine=new String[timeArray.length];
                for (int i = 0; i < timeArray.length; i++) {
                    singleLine[i]="time: "+timeArray[i]+", the temperature: "+temperatureArray[i]+",the condition: "+conditionArray[i];
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //dateText.setText(Arrays.toString(timeArray));
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListHours.this, R.layout.activity_listview, R.id.textView, singleLine);
                        listhours.setAdapter(arrayAdapter);
                    }
                });

            }
        });
        weatherAsyncTask.execute();
    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Update UI with latitude and longitude
            QUERY=getCity(latitude,longitude);

            // Optional: Stop location updates after receiving the first location
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {}

        @Override
        public void onProviderEnabled(@NonNull String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
    public String getCity(double lats, double lons) {

        Geocoder geocoder;
        double lat = lats;
        double lon = lons;
        geocoder = new Geocoder(ListHours.this, Locale.getDefault());
        List<android.location.Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {

            e.printStackTrace();
        }

        if (addresses != null) {
            String city = addresses.get(0).getLocality();
            return city;
        } else {
            return "failed";
        }
    }


}