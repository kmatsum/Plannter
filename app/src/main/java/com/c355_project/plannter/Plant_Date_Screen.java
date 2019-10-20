package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.DatePicker;

import java.util.Calendar;

public class Plant_Date_Screen extends AppCompatActivity {

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant__date__screen);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setBackgroundColor(Color.WHITE);
    }
}
