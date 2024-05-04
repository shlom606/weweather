package com.example.weweather;

import static android.content.ContentValues.TAG;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    FirebaseDatabase db;

    private WeatherAsyncTask weatherAsyncTask;

    String city = "Rehovot",username;  // Example City

    //googlemaps api key: AIzaSyCAIN8WUhbR2l9AtcXzykYKP46FemN4hk4


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hours);
        listhours = findViewById(R.id.ListHours);
        returnPrev = findViewById(R.id.btn_returnprev);
        DatabaseReference rootRef = db.getInstance().getReference();
        DatabaseReference reference = rootRef.child("Users");
        int DayID = getIntent().getExtras().getInt("searchDayInData", 0);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username= getIntent().getExtras().getString("searchUsernameInData");
                Toast.makeText(ListHours.this, "The user is: " + username, Toast.LENGTH_SHORT).show();
                city = dataSnapshot.child("Users").child(username).child("location details").child("city").getValue(String.class);
                Toast.makeText(ListHours.this, "The city is inserted: " + city, Toast.LENGTH_SHORT).show();

                weatherAsyncTask = new WeatherAsyncTask(city, new OnDataRetrievedListener() {
                    @Override
                    public void onDataRetrieved(String jsonData) {
                        // Use jsonData here
                        // Parse JSON data using Gson or other methods
                        Gson gson = new Gson();
                        Forecast forecast = gson.fromJson(jsonData, Forecast.class);

                        List<String> timeList = new ArrayList<>();
                        List<String> temperatureList = new ArrayList<>();
                        List<String> conditionList = new ArrayList<>();
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
                        String[] conditionArray = conditionList.toArray(new String[0]);
                        String[] singleLine = new String[timeArray.length];
                        for (int i = 0; i < timeArray.length; i++) {
                            singleLine[i] = "Time: " + timeArray[i] + "\n The temperature: " + temperatureArray[i] + "\n The condition: " + conditionArray[i];
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        rootRef.addListenerForSingleValueEvent(valueEventListener);
//    }
//    private final LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//
//
//            Toast.makeText(ListHours.this, "Latitude: " + latitude + "\nLongitude: " + longitude, Toast.LENGTH_SHORT).show();
//            // Optional: Stop location updates after receiving the first location
//            locationManager.removeUpdates(locationListener);
//        }
//        @Override
//        public void onProviderDisabled(@NonNull String provider) {}
//
//        @Override
//        public void onProviderEnabled(@NonNull String provider) {}
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//    };
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//                // Request location updates
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            } else {
//                // Permission denied
//                Toast.makeText(this, "Location permission is required to get the current location", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    }
}