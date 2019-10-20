package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;

public class Plant_Date_Screen extends AppCompatActivity {

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_date_ui);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setBackgroundColor(Color.WHITE);
    }
}
