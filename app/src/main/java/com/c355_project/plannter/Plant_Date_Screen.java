package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Switch;
import android.widget.Toast;

public class Plant_Date_Screen extends AppCompatActivity {

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_date_ui);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnBack): {
                Intent openMainMenu = new Intent(getApplicationContext(), Main_Menu.class);
                startActivity(openMainMenu);
            } break;

            case (R.id.btnNext): {

            } break;

            default: {
                Toast.makeText(this, "[ERROR] Menu request did not function correctly, try again!", Toast.LENGTH_SHORT).show();
            } break;
        }
    }
}
