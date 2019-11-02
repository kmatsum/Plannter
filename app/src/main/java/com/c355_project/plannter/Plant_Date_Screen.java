package com.c355_project.plannter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class Plant_Date_Screen extends AppCompatActivity {

    //Variables
    CalendarView calendarView;
    TextView txtCropHarvest;
    RadioButton rbtnHarvest;
    String Month, Day, Year, concatMonthAndDay;
    int monthAndDay;
    Intent openPlantHarvestScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_date_ui);

        calendarView = findViewById(R.id.calendarView);
        rbtnHarvest = findViewById(R.id.rbtnHarvest);
        txtCropHarvest = findViewById(R.id.txtCropHarvest);
        openPlantHarvestScreen = new Intent(getApplicationContext(), Plant_Harvest_Screen.class);

        calendarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                //Concat both month and day so comparison is easier and code is cleaner
                Month = String.valueOf(month + 1);
                Day = String.valueOf(day);
                Year = String.valueOf(year);
                concatMonthAndDay = Month + Day;
                monthAndDay = Integer.parseInt(concatMonthAndDay);

                //If radio button is checked, check to see date range to determine what can be harvested
                if (rbtnHarvest.isChecked())
                {
                        if (monthAndDay >= 719 & monthAndDay < 726)
                        {
                            putExtra(openPlantHarvestScreen,"7/19-7/25","You can harvest.....\n Tomatoes \n Peppers \n Cucumbers \n Squash");
                        }
                        else if (monthAndDay >= 726 & monthAndDay <=731)
                        {
                            putExtra(openPlantHarvestScreen,"7/26-7/30","You can harvest..... \n Tomatoes \n Peppers \n Cucumbers \n Okra \n Squash \n Chard");
                        }
                        else if (monthAndDay == 81)
                        {
                            putExtra(openPlantHarvestScreen,"8/1","You can harvest..... \n Tomatoes \n Peppers \n Cucumbers \n Okra \n Squash \n Chard");
                        }
                        else if (monthAndDay >= 82 & monthAndDay < 89)
                        {
                            putExtra(openPlantHarvestScreen,"8/2-8/8","You can harvest..... \n Tomatoes \n Peppers \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Beets \n Green Beans - Bush \n Radish or Turnip");
                        }
                        else if (monthAndDay == 89)
                        {
                            putExtra(openPlantHarvestScreen,"8/9","You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Peas \n Beets \n Green Beans - Bush \n Radish or Turnip");
                        }
                        else if (monthAndDay >= 810 & monthAndDay < 816)
                        {
                            putExtra(openPlantHarvestScreen,"8/10-8/15","You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Peas \n Beets \n Green Beans - Bush \n Radish or Turnip");
                        }
                        else if (monthAndDay >= 816 & monthAndDay < 823)
                        {
                            putExtra(openPlantHarvestScreen,"8/16-8/22","You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Pumpkins \n Chard \n Peas \n Beets \n Broccoli \n Green Beans - Bush \n Radish or Turnip \n Lettuce - Leaf");
                        }
                        else if (monthAndDay >= 823 & monthAndDay < 830)
                        {
                            putExtra(openPlantHarvestScreen,"8/23-8/29","You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Beets \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
                        }
                        else if (monthAndDay == 830)
                        {
                            putExtra(openPlantHarvestScreen,"8/30","You can harvest..... \n Tomatoes \n Melons \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
                        }
                        else if (monthAndDay == 91 || monthAndDay == 92 || monthAndDay == 93 || monthAndDay == 94 || monthAndDay == 95)
                        {
                            putExtra(openPlantHarvestScreen,"9/1-9/5","You can harvest..... \n Tomatoes \n Melons \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
                        }
                        else if (monthAndDay == 96 || monthAndDay == 97 || monthAndDay == 98 || monthAndDay == 99 || monthAndDay == 910 || monthAndDay == 911 || monthAndDay == 912)
                        {
                            putExtra(openPlantHarvestScreen,"9/6-9/12","You can harvest..... \n Carrots \n Cauliflower \n Chard \n Broccoli \n Lettuce - Leaf \n Spinach");
                        }
                        else if(monthAndDay == 913)
                        {
                            putExtra(openPlantHarvestScreen,"9/13","You can Harvest..... \n Chard \n Broccoli \n Spinach");
                        }
                        else
                        {
                            makeToast("No Plants are able to be harvested at this time");
                        }

                }
            }
        });
    }



//CLICK METHOD =====================================================================================
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnBack): {
                Intent openMainMenu = new Intent(getApplicationContext(), Main_Menu.class);
                startActivity(openMainMenu);
            } break;

            case (R.id.btnNext): {
                putExtra(openPlantHarvestScreen,"Month", Month);
                putExtra(openPlantHarvestScreen,"Day", Day);
                putExtra(openPlantHarvestScreen,"Year", Year);
                startActivity(openPlantHarvestScreen);
            } break;

            default: {
                makeToast("[ERROR] Menu request did not function correctly, try again!");
            } break;
        }
    }



//METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    public void putExtra(Intent intent, String name, String value) {
        intent.putExtra(name, value);
    }
}
