package com.c355_project.plannter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
    CalendarView        calendarViewInLayout;
    Calendar            today,
                        lastPlantDate,
                        firstPlantDate,
                        harvestRangeMin,
                        harvestRangeMax;
    TextView            txtCropHarvest;
    String              Concat,
                        selectedPlantName;
    SimpleDateFormat    simpleDateFormat;
    Date                selectedDate;
    Button              btnNext;
    Spinner             spnPlants;
    int                 dialogIcon;

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

        //Implements hardware back button to take user back to the Main Menu
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Main_Window.changeFragment("MainMenu");            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        PlantDatabase = Main_Window.getPlantList();
        PlantNames = new ArrayList<>();

        //[DEBUG] Print all the plant names
        System.out.println("------------------------------------");
        for (int i = 0; i < PlantDatabase.size(); i++) {
            System.out.println(PlantDatabase.get(i).getPlantName());
        }
        System.out.println("------------------------------------");


        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnCalculate).setOnClickListener(this);
        view.findViewById(R.id.btnAddLog).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);
        view.findViewById(R.id.arrowNext).setOnClickListener(this);
        view.findViewById(R.id.arrowPrevious).setOnClickListener(this);

        //Adds banner ad to UI
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        calendarViewInLayout = view.findViewById(R.id.calendarView);
        txtCropHarvest = view.findViewById(R.id.txtCropHarvest);
        calendarViewInLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        calendarViewInLayout.setOnDateChangeListener(this);
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        today = Calendar.getInstance();
        harvestRangeMin = Calendar.getInstance();
        harvestRangeMax = Calendar.getInstance();
        btnNext = view.findViewById(R.id.btnCalculate);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, PlantNames);
        for (int i = 0; i < PlantDatabase.size(); i++) {

            PlantNames.add(PlantDatabase.get(i).getPlantName());
        }
        spnPlants = view.findViewById(R.id.spnPlants);
        spnPlants.setAdapter(adapter);
        spnPlants.setSelected(false);

        spnPlants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Sets Selected Plant Name to the user chosen plant
                    selectedPlantName = PlantDatabase.get(i).getPlantName();
                    calendarViewInLayout.setMinDate(0);
                    calendarViewInLayout.setMinDate(calculateFirstPlantDate(i));
                    calendarViewInLayout.setMaxDate(0);
                    calendarViewInLayout.setMaxDate(calculateLastPlantDate(i));
                    //Sets The Calendar Focus To The First Possible Plant Date
                    calendarViewInLayout.setDate(calendarViewInLayout.getMinDate(), true, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    //LISTENER METHODS =================================================================================
    public void onClick(View view) {
        Integer id = view.getId();

        //Go back in spinner plant list. Do nothing if position is already the first item.
        if (id == R.id.btnPrevious || id == R.id.arrowPrevious) {
            int position = spnPlants.getSelectedItemPosition();
            if (position > 0)
                spnPlants.setSelection(spnPlants.getSelectedItemPosition() - 1);
        }

        //Go forward in spinner plant list. Do nothing if position is already the last item.
        else if (id == R.id.btnNext || id == R.id.arrowNext) {
            int position = spnPlants.getSelectedItemPosition();
            if (position < spnPlants.getAdapter().getCount() - 1)
                spnPlants.setSelection(spnPlants.getSelectedItemPosition() + 1);
        }

        else if (id == R.id.btnCalculate){
            if(selectedDate == null)
            {
                Date minPlantDate = new Date(calendarViewInLayout.getMinDate());
                selectedDate = minPlantDate;
                setHarvestRanges();
            }
            else
                setHarvestRanges();
            txtCropHarvest.setText("Selected Date: " + simpleDateFormat.format(selectedDate) + "\n" + "Expect to Harvest Between: " + simpleDateFormat.format(harvestRangeMin.getTime()) + "-" + simpleDateFormat.format(harvestRangeMax.getTime()));

        }

        else if (id == R.id.btnAddLog) {
            if(selectedDate == null) {
                Date minPlantDate = new Date(calendarViewInLayout.getMinDate());
                selectedDate = minPlantDate;
            }
            makeToast("Add Log Button Clicked");
            openConfirmationDialog(selectedPlantName ,simpleDateFormat.format(selectedDate), Main_Window);
        }

        //Used for handling exceptions on if the given ViewID and the expected ViewID does not match
        else {
            //Toast Error Information
            makeToast("[ERROR] Menu parameter passed was not found, returning to main menu...");
            System.out.println("[ERROR] Menu parameter passed was not found, returning to main menu...\n");

            Main_Window.changeFragment("MainMenu");
        }
    }
    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
        //Concat both month and day so comparison is easier and code is cleaner
        Concat = (month + 1) + "/" + day + "/" + year;

        System.out.println(Concat);
        {
            try {
                selectedDate = simpleDateFormat.parse(Concat);
                Main_Window.setUserInputDate(selectedDate);
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

    public long calculateLastPlantDate(int spnPosition)
    {
        int daysAfterLastPlant = PlantDatabase.get(spnPosition).getLastPlantDate() * 7;
        lastPlantDate = Calendar.getInstance();
        lastPlantDate.setTime(Main_Window.getFirstFallFrostDate());
        lastPlantDate.add(Calendar.DAY_OF_MONTH, -daysAfterLastPlant);
        Date lpd = lastPlantDate.getTime();
        return lpd.getTime();
    }
    public long calculateFirstPlantDate(int spnPosition)
    {
        int daysBeforelastSpringFrost = PlantDatabase.get(spnPosition).getFirstPlantDate() * 7;
        firstPlantDate = Calendar.getInstance();
        firstPlantDate.setTime(Main_Window.getLastSpringFrostDate());
        firstPlantDate.add(Calendar.DAY_OF_MONTH, -daysBeforelastSpringFrost);
        Date lpd = firstPlantDate.getTime();
        return lpd.getTime();
    }
    public void setHarvestRanges()
    {
        harvestRangeMin.setTime(selectedDate);
        harvestRangeMin.add(Calendar.DAY_OF_MONTH, PlantDatabase.get(spnPlants.getSelectedItemPosition()).getWeeksToHarvest() * 7);
        harvestRangeMax.setTime(harvestRangeMin.getTime());
        harvestRangeMax.add(Calendar.DAY_OF_MONTH, PlantDatabase.get(spnPlants.getSelectedItemPosition()).getHarvestRange() * 7);
    }

    protected void openConfirmationDialog(String potentialCropName, String potentialPlantDate, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Add Plant to Log?")
                .setMessage(Html.fromHtml("Are you sure you want to add <b>" + potentialCropName.toLowerCase() + "</b> planted on <b>" + potentialPlantDate + "</b> to your plant log?"))

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Add New Plant To Log
                        //DEBUG
                        makeToast("Plant Add Button Clicked");
                    }
                })

                // A null listener allows the button to close the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_dialog_check_box)
                .show();
    }
}
