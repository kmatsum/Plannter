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
                        if (monthAndDay < 726)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest.....\n Tomatoes \n Peppers \n Cucumbers \n Squash");
                        }
                        if (monthAndDay >= 726 & monthAndDay <=731)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Cucumbers \n Okra \n Squash \n Chard");
                        }
                        if (monthAndDay == 81)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Cucumbers \n Okra \n Squash \n Chard");
                        }
                        if (monthAndDay >= 82 & monthAndDay < 89)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Beets \n Green Beans - Bush \n Radish or Turnip");
                        }
                        if (monthAndDay == 89)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Peas \n Beets \n Green Beans - Bush \n Radish or Turnip");
                        }
                        if (monthAndDay >= 810 & monthAndDay < 816)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Peas \n Beets \n Green Beans - Bush \n Radish or Turnip");
                        }
                        if (monthAndDay >= 816 & monthAndDay < 823)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Pumpkins \n Chard \n Peas \n Beets \n Broccoli \n Green Beans - Bush \n Radish or Turnip \n Lettuce - Leaf");
                        }
                        if (monthAndDay >= 823 & monthAndDay < 830)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Beets \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
                        }
                        if (monthAndDay == 830)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Melons \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
                        }
                        if (monthAndDay == 91 || monthAndDay == 92 || monthAndDay == 93 || monthAndDay == 94 || monthAndDay == 95)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Tomatoes \n Melons \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
                        }
                        if (monthAndDay == 96 || monthAndDay == 97 || monthAndDay == 98 || monthAndDay == 99 || monthAndDay == 910 || monthAndDay == 911 || monthAndDay == 912)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can harvest..... \n Carrots \n Cauliflower \n Chard \n Broccoli \n Lettuce - Leaf \n Spinach");
                        }
                        if(monthAndDay == 913)
                        {
                            txtCropHarvest.setText("");
                            txtCropHarvest.setText("You can Harvest..... \n Chard \n Broccoli \n Spinach");
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
