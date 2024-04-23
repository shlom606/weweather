package com.example.weweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.TimeZone;

public class ListDays extends AppCompatActivity {

    Button day1,day2,day3,day4,day5,day6,day7;

    String dayarray[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday"};
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
        Button[] btndays= {day1,day2,day3,day4,day5,day6,day7};
        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        
        int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < btndays.length; i++) {
            btndays[i].setText(writeDay(currentDayOfWeek+i,dayarray));
            SaveDay(btndays[i],i);
        }


        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDays.this, ListHours.class);
                startActivity(intent);
            }
        });
    }
    public String writeDay(int day,String[] arrayDays){
        if(day>7){
            return arrayDays[day-8];// another -1 to be in the index of array 0-6
        }
        else
            return arrayDays[day-1];
    }
    public void SaveDay(Button button,int id){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDays.this, ListHours.class);
                intent.putExtra("searchDayInData", id);
                //intent.putExtra("instituteId", 22);
                startActivity(intent);
            }
        });

    }
}