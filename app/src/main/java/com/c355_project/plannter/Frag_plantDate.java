package com.c355_project.plannter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.List;

import java.util.List;


public class Frag_plantDate extends Fragment implements View.OnClickListener {
//VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //Variables
    CalendarView calendarView;
    TextView txtCropHarvest;
    RadioButton rbtnHarvest, rbtnPlant;
    String Month, Day, Year, concatMonthAndDay;
    int monthAndDay;
    //Plant Database List
    List<Plant> PlantDatabase;



//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plant_date, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Main_Window = (Main_Window) getActivity();

        PlantDatabase = Main_Window.getPlantList();

        //[DEBUG] Print all the plant names
        System.out.println("------------------------------------");
        for (int i = 0; i < PlantDatabase.size(); i++){
            System.out.println(PlantDatabase.get(i).getPlantName());
        }
        System.out.println("------------------------------------");


        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
      
        //Adds banner ad to UI
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
      
        rbtnHarvest = view.findViewById(R.id.rbtnHarvest);
        calendarView = view.findViewById(R.id.calendarView);
        rbtnHarvest = view.findViewById(R.id.rbtnHarvest);
        txtCropHarvest = view.findViewById(R.id.txtCropHarvest);
        calendarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                //Concat both month and day so comparison is easier and code is cleaner
                Month = String.valueOf(month + 1);
                Day = String.valueOf(day);
                Year = String.valueOf(year);
                concatMonthAndDay = Month + Day;
                monthAndDay = Integer.parseInt(concatMonthAndDay);
            }
        });
    }

    //onClick Method ===================================================================================
    public void onClick(View view) {
        Main_Window Main_Window = (Main_Window) getActivity();
        switch (view.getId()) {
            case (R.id.btnBack): {
                Main_Window.changeFragment("MainMenu");
            }
            break;

            case (R.id.btnNext): {
                if (rbtnHarvest.isChecked()) {
                    btnChecker(rbtnHarvest);
                }
                if (rbtnPlant.isChecked()) {
                    btnChecker(rbtnPlant);
                } else {
                    makeToast("No Radio button is selected, please select one to move forward");
                }
            }
            break;

            //Used for handling exceptions on if the given ViewID and the expected ViewID does not match
            default: {
                //Toast Error Information
                makeToast("[ERROR] Menu parameter passed was not found, returning to main menu...");
                System.out.println("[ERROR] Menu parameter passed was not found, returning to main menu...\n");

                Main_Window.changeFragment("MainMenu");
            }
        }
    }

//METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
    }

    public void btnChecker(RadioButton rbtn) {
//        if (rbtn == rbtnHarvest & rbtn.isChecked()) {
//            if (monthAndDay >= 719 & monthAndDay < 726) {
//                putExtra(openPlantHarvestScreen, "7/19-7/25", "You can harvest.....\n Tomatoes \n Peppers \n Cucumbers \n Squash");
//            } else if (monthAndDay >= 726 & monthAndDay <= 731) {
//                putExtra(openPlantHarvestScreen, "7/26-7/30", "You can harvest..... \n Tomatoes \n Peppers \n Cucumbers \n Okra \n Squash \n Chard");
//            } else if (monthAndDay == 81) {
//                putExtra(openPlantHarvestScreen, "8/1", "You can harvest..... \n Tomatoes \n Peppers \n Cucumbers \n Okra \n Squash \n Chard");
//            } else if (monthAndDay >= 82 & monthAndDay < 89) {
//                putExtra(openPlantHarvestScreen, "8/2-8/8", "You can harvest..... \n Tomatoes \n Peppers \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Beets \n Green Beans - Bush \n Radish or Turnip");
//            } else if (monthAndDay == 89) {
//                putExtra(openPlantHarvestScreen, "8/9", "You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Peas \n Beets \n Green Beans - Bush \n Radish or Turnip");
//            } else if (monthAndDay >= 810 & monthAndDay < 816) {
//                putExtra(openPlantHarvestScreen, "8/10-8/15", "You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Peas \n Beets \n Green Beans - Bush \n Radish or Turnip");
//            } else if (monthAndDay >= 816 & monthAndDay < 823) {
//                putExtra(openPlantHarvestScreen, "8/16-8/22", "You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Pumpkins \n Chard \n Peas \n Beets \n Broccoli \n Green Beans - Bush \n Radish or Turnip \n Lettuce - Leaf");
//            } else if (monthAndDay >= 823 & monthAndDay < 830) {
//                putExtra(openPlantHarvestScreen, "8/23-8/29", "You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Beets \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
//            } else if (monthAndDay == 830) {
//                putExtra(openPlantHarvestScreen, "8/30", "You can harvest..... \n Tomatoes \n Melons \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
//            } else if (monthAndDay == 91 || monthAndDay == 92 || monthAndDay == 93 || monthAndDay == 94 || monthAndDay == 95) {
//                putExtra(openPlantHarvestScreen, "9/1-9/5", "You can harvest..... \n Tomatoes \n Melons \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");
//            } else if (monthAndDay == 96 || monthAndDay == 97 || monthAndDay == 98 || monthAndDay == 99 || monthAndDay == 910 || monthAndDay == 911 || monthAndDay == 912) {
//                putExtra(openPlantHarvestScreen, "9/6-9/12", "You can harvest..... \n Carrots \n Cauliflower \n Chard \n Broccoli \n Lettuce - Leaf \n Spinach");
//            } else if (monthAndDay == 913) {
//                putExtra(openPlantHarvestScreen, "9/13", "You can Harvest..... \n Chard \n Broccoli \n Spinach");
//            } else {
//                makeToast("No Plants are able to be harvested at this time");
//            }
//        }
        if (rbtn == rbtnPlant & rbtnPlant.isChecked()) {

        } else {
            makeToast("No radio button is selected");
        }
    }
}

