package com.example.weweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

public class ListDays extends AppCompatActivity {

    Button day1,day2,day3,day4,day5,day6,day7,history,prevscreen,mainscreen;
    String dayarray[] = {"Sunday's weather", "Monday's weather", "Tuesday's weather", "Wednesday's weather", "Thursday's weather", "Friday's weather","Saturday's weather"};
    FirebaseDatabase db;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_days);
        day1=findViewById(R.id.btn_day1);
        day2=findViewById(R.id.btn_day2);
        day3=findViewById(R.id.btn_day3);
        day4=findViewById(R.id.btn_day4);
        day5=findViewById(R.id.btn_day5);
        day6=findViewById(R.id.btn_day6);
        day7=findViewById(R.id.btn_day7);
        history=findViewById(R.id.btn_history);
        prevscreen=findViewById(R.id.btn_prevScreen);
        mainscreen=findViewById(R.id.btn_mainScreen);
        Button[] btndays= {day1,day2,day3,day4,day5,day6,day7};
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        String username= getIntent().getExtras().getString("searchUsernameInData");

        int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < btndays.length; i++) {// a for loop for each of the days buttons
            btndays[i].setText(writeDay(currentDayOfWeek+i,dayarray));
            SaveDay(btndays[i],i,username);
        }
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDays.this, HistoryActivity.class);
                intent.putExtra("searchUsernameInData",username);
                startActivity(intent);
            }
        });

        prevscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDays.this, MapActivity.class);
                intent.putExtra("searchUsernameInData",username);
                startActivity(intent);
            }
        });
        mainscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDays.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public String writeDay(int day,String[] arrayDays){// the day of the week is an int 0-7 so in the for loop the function could get numbers higher than 7
        if(day>7){
            return arrayDays[day-8];//in order for the index to be within the limits of the array
        }
        else
            return arrayDays[day-1];// another -1 to be in the index of array 0-6
    }
    public void SaveDay(Button button,int id,String username){// for each of the days buttons it will move the user to next activity with the id of the day and username
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDays.this, ListHours.class);
                intent.putExtra("searchDayInData", id);
                intent.putExtra("searchUsernameInData",username);
                startActivity(intent);
            }
        });

    }
}