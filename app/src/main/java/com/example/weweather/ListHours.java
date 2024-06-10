package com.example.weweather;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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
    TextView displayCity;
    FirebaseDatabase db;
    private WeatherAsyncTask weatherAsyncTask;
    String city;
    Button prevscreen,mainscreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hours);
        listhours = findViewById(R.id.ListHours);
        displayCity=findViewById(R.id.CityDisplay);
        prevscreen=findViewById(R.id.btn_prevScreen);
        mainscreen=findViewById(R.id.btn_mainScreen);
        String username= getIntent().getExtras().getString("searchUsernameInData");
        prevscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHours.this, ListDays.class);
                intent.putExtra("searchUsernameInData",username);
                startActivity(intent);
            }
        });
        mainscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHours.this, MainActivity.class);
                startActivity(intent);
            }
        });
        DatabaseReference rootRef = db.getInstance().getReference();
        DatabaseReference reference = rootRef.child("Users");
        int DayID = getIntent().getExtras().getInt("searchDayInData", 0);// gets the id of the day
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username= getIntent().getExtras().getString("searchUsernameInData");
                String stringday= getIntent().getExtras().getString("searchStringDay");
                city = dataSnapshot.child("Users").child(username).child("location details").child("city").getValue(String.class);
                displayCity.setText(stringday+" in: "+city);// presents in text view the day entered and the city entered
                Toast.makeText(ListHours.this, "The city is inserted: " + city, Toast.LENGTH_SHORT).show();// presents a toast of the city

                weatherAsyncTask = new WeatherAsyncTask(city, new OnDataRetrievedListener() {
                    @Override
                    public void onDataRetrieved(String jsonData) {
                        // Use jsonData here
                        // Parse JSON data using Gson or other methods
                        Gson gson = new Gson();
                        Forecast forecast = gson.fromJson(jsonData, Forecast.class);//parsing from string to Forecast
                        List<String> timeList = new ArrayList<>();
                        List<String> temperatureList = new ArrayList<>();
                        List<String> conditionList = new ArrayList<>();
                        if (!forecast.forecastData.forecastDays.isEmpty()) {//if the data isn't empty
                            ForecastDay forecastDay = forecast.forecastData.forecastDays.get(DayID); // Get the ForecastDay corresponding with the day entered previously
                            for (Hour hour : forecastDay.hours) {// for each hour in the list of hours within the current day, there are 23 hours
                                timeList.add(hour.time);//add the time to details to list of them set before
                                temperatureList.add(String.valueOf(hour.temperatureC));
                                conditionList.add(String.valueOf(hour.condition.text));
                            }
                        }
                        // Convert List<String> to String[]
                        String[] timeArray = timeList.toArray(new String[0]);
                        String[] temperatureArray = temperatureList.toArray(new String[0]);
                        String[] conditionArray = conditionList.toArray(new String[0]);
                        String[] singleLine = new String[timeArray.length];
                        for (int i = 0; i < timeArray.length; i++) {// a for loop to contain all details in a single array
                            singleLine[i] = "Time: " + timeArray[i] + "\n The temperature: " + temperatureArray[i] + "\n The condition: " + conditionArray[i];
                        }
                        runOnUiThread(new Runnable() {//runonui to presents the data to the user using listview and adapter
                            @Override
                            public void run() {
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListHours.this, R.layout.activity_listview, R.id.textView, singleLine);
                                listhours.setAdapter(arrayAdapter);
                            }
                        });
                    }
                });
                weatherAsyncTask.execute();// the asynch task executes during the run using an asynch task
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
            }
        };
        rootRef.addListenerForSingleValueEvent(valueEventListener);
    }
}