package com.c355_project.plannter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class Plant_Date_Screen extends AppCompatActivity {

    CalendarView calendarView;
    TextView txtCropHarvest;
    RadioButton rbtnHarvest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_date_ui);

        calendarView = findViewById(R.id.calendarView);
        txtCropHarvest = findViewById(R.id.txtCropHarvest);
        rbtnHarvest = findViewById(R.id.rbtnHarvest);

        calendarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                String Month = String.valueOf(month + 1);
                String Day = String.valueOf(day);
                String concatMonthAndDay = Month + Day;
                int monthAndDay = Integer.parseInt(concatMonthAndDay);
                if (rbtnHarvest.isChecked())
                {
                    if (monthAndDay >= 719 & monthAndDay <=913)
                    {
                        if (monthAndDay >= 719 & monthAndDay < 726)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest.....\n Tomatoes \n Peppers \n Cucumbers \n Squash");
                        }
                        if ((monthAndDay >= 726 & monthAndDay <=731) || (monthAndDay >= 81))
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Cucumbers \n Okra \n Squash \n Chard");
                        }
                        if (monthAndDay >= 82 & monthAndDay <=89)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Beets \n Green Beans - Bush \n Radish or Turnip");
                        }
                        if (monthAndDay >= 82 & monthAndDay <=830)
                        {
                            txtCropHarvest.setText("You can harvest..... \nCabbage");
                        }
                        if (monthAndDay >= 719 & monthAndDay <=816)
                        {
                            txtCropHarvest.setText("You can harvest..... \n Cucumbers");
                        }
                        if (monthAndDay >= 726 & monthAndDay <=830)
                        {
                            txtCropHarvest.setText("Okra");
                        }
                        if (monthAndDay >= 816 & monthAndDay <=830)
                        {
                            txtCropHarvest.setText("Pumpkins");
                        }
                        if (monthAndDay >= 719 & monthAndDay <=89)
                        {
                            txtCropHarvest.setText("Tomatoes \n Peppers");
                        }
                    }
                    else
                    {
                        MakeToast("No Plants are able to be harvested at this time");
                    }

                }
            }
        });
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
                MakeToast("[ERROR] Menu request did not function correctly, try again!");
            } break;
        }
    }

    public void MakeToast(String Message)
    {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }
}
