package com.c355_project.plannter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class Frag_plantDate extends Fragment implements View.OnClickListener, CalendarView.OnDateChangeListener {
    //VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //View Variables
    CalendarView calendarViewInLayout;
    Calendar today, lastPlantDate, firstPlantDate, harvestRangeMin, harvestRangeMax;
    TextView txtCropHarvest;
    String Month, Day, Year, Concat;
    SimpleDateFormat simpleDateFormat;
    Date selectedDate;
    Button btnNext;
    Spinner spnPlants;

    //Plant Database List
    List<Plant> PlantDatabase;
    List<String> PlantNames;
    ArrayAdapter<String> adapter;

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
        PlantNames = new ArrayList<>();

        //[DEBUG] Print all the plant names
        System.out.println("------------------------------------");
        for (int i = 0; i < PlantDatabase.size(); i++) {
            System.out.println(PlantDatabase.get(i).getPlantName());
        }
        System.out.println("------------------------------------");


        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);

//        //Adds banner ad to UI
//        AdView adView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//        adView.loadAd(adRequest);

        calendarViewInLayout = view.findViewById(R.id.calendarView);
        txtCropHarvest = view.findViewById(R.id.txtCropHarvest);
        calendarViewInLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        calendarViewInLayout.setOnDateChangeListener(this);
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        today = Calendar.getInstance();
        harvestRangeMin = Calendar.getInstance();
        harvestRangeMax = Calendar.getInstance();
        btnNext = view.findViewById(R.id.btnNext);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, PlantNames);
        for (int i = 0; i < PlantDatabase.size(); i++) {

            PlantNames.add(PlantDatabase.get(i).getPlantName());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPlants = view.findViewById(R.id.spnPlants);
        spnPlants.setAdapter(adapter);

        spnPlants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    calendarViewInLayout.setMinDate(calulateFirstPlantDate(i));
                    calendarViewInLayout.setMaxDate(calulateLastPlantDate(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    //LISTENER METHODS =================================================================================
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnBack): {
                Main_Window.changeFragment("MainMenu");
            }
            break;

            case (R.id.btnNext): {
                txtCropHarvest.setText("Selected Date: " + Main_Window.getUserInputDate() + "\n" + "Expect to Harvest Between: " + harvestRangeMin.getTime() + "-" + harvestRangeMax.getTime());

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

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
        //Concat both month and day so comparison is easier and code is cleaner
//        Month = String.valueOf(month + 1);
//        Day = String.valueOf(day);
//        Year = String.valueOf(year);
        Concat = month + "/" + day + "/" + year;

        System.out.println(Concat);
        {
            try {
                selectedDate = simpleDateFormat.parse(Concat);
                Main_Window.setUserInputDate(selectedDate);
                harvestRangeMin.setTime(selectedDate);
                harvestRangeMin.add(Calendar.DAY_OF_MONTH , (PlantDatabase.get(spnPlants.getSelectedItemPosition()).getWeeksToHarvest() * 7));
                harvestRangeMax.setTime(harvestRangeMin.getTime());
                harvestRangeMax.add(Calendar.DAY_OF_MONTH, (PlantDatabase.get(spnPlants.getSelectedItemPosition()).getHarvestRange() * 7));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    //METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast toast = Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    //Get frost date from DB and check ranges
//           if((fallFrost.getTime() - selectedDate.getTime()) >= 83 || (fallFrost.getTime() - selectedDate.getTime()) <= 76) {
//               Main_Window.setHarvestableCrops("You can harvest.....\n Tomatoes \n Peppers \n Cucumbers \n Squash"); }
//           else if ((fallFrost.getTime() - selectedDate.getTime()) >= 75 || (fallFrost.getTime() - selectedDate.getTime()) <= 68) {
//                Main_Window.setHarvestableCrops("You can harvest..... \n Tomatoes \n Peppers \n Cucumbers \n Okra \n Squash \n Chard"); }
//           else if ((fallFrost.getTime() - selectedDate.getTime()) >= 67 || (fallFrost.getTime() - selectedDate.getTime()) <= 60) {
//                Main_Window.setHarvestableCrops("You can harvest..... \n Tomatoes \n Peppers \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Beets \n Green Beans - Bush \n Radish or Turnip");}
//           else if ((fallFrost.getTime() - selectedDate.getTime()) >= 59 || (fallFrost.getTime() - selectedDate.getTime()) <= 52) {
//                Main_Window.setHarvestableCrops("You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Squash \n Chard \n Peas \n Beets \n Green Beans - Bush \n Radish or Turnip");}
//           else if ((fallFrost.getTime() - selectedDate.getTime()) >= 51 || (fallFrost.getTime() - selectedDate.getTime()) <= 44) {
//                Main_Window.setHarvestableCrops("You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Cucumber \n Okra \n Pumpkins \n Chard \n Peas \n Beets \n Broccoli \n Green Beans - Bush \n Radish or Turnip \n Lettuce - Leaf");}
//           else if ((fallFrost.getTime() - selectedDate.getTime()) >= 43 || (fallFrost.getTime() - selectedDate.getTime()) <= 36) {
//                Main_Window.setHarvestableCrops("You can harvest..... \n Tomatoes \n Peppers \n Melons \n Potatoes \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Beets \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");}
//           else if ((fallFrost.getTime() - selectedDate.getTime()) >= 35 || (fallFrost.getTime() - selectedDate.getTime()) <= 28) {
//                Main_Window.setHarvestableCrops("You can harvest..... \n Tomatoes \n Melons \n Sweet Corn \n Cabbage \n Okra \n Pumpkins \n Carrots \n Cauliflower \n Chard \n Peas \n Broccoli \n Radish or Turnip \n Lettuce - Leaf \n Spinach");}
//           else if ((fallFrost.getTime() - selectedDate.getTime()) >= 27 || (fallFrost.getTime() - selectedDate.getTime()) <= 20) {
//                Main_Window.setHarvestableCrops("You can harvest..... \n Carrots \n Cauliflower \n Chard \n Broccoli \n Lettuce - Leaf \n Spinach");}
//           else if ((fallFrost.getTime() - selectedDate.getTime()) >= 27 || (fallFrost.getTime() - selectedDate.getTime()) <= 20) {
//                Main_Window.setHarvestableCrops("You can Harvest..... \n Chard \n Broccoli \n Spinach");}
//           else {
//                makeToast("No Plants are able to be harvested at this time");
//            }
    public long calulateLastPlantDate(int spnPosition)
    {
        int daysAfterLastPlant = PlantDatabase.get(spnPosition).getLastPlantDate() * 7;
        lastPlantDate = Calendar.getInstance();
        lastPlantDate.setTime(Main_Window.getFirstFallFrostDate());
        lastPlantDate.add(Calendar.DAY_OF_MONTH, -daysAfterLastPlant);
        Date lpd = lastPlantDate.getTime();
        return lpd.getTime();
    }
    public long calulateFirstPlantDate(int spnPosition)
    {
        int daysBeforelastSpringFrost = PlantDatabase.get(spnPosition).getFirstPlantDate() * 7;
        firstPlantDate = Calendar.getInstance();
        firstPlantDate.setTime(Main_Window.getLastSpringFrostDate());
        firstPlantDate.add(Calendar.DAY_OF_MONTH, -daysBeforelastSpringFrost);
        Date lpd = firstPlantDate.getTime();
        return lpd.getTime();
    }
}
/*TODO
Loop through to calculate what plants can be harvested when -> Weeks to Harvest  times 7
 */
