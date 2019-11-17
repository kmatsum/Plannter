package com.c355_project.plannter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.List;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;



public class Frag_plantHarvest extends Fragment implements View.OnClickListener {
//VARIABLES ========================================================================================
    TextView txtDisplayDate;
    TextView txtDisplayCrops;
    String Day;
    String Month;
    String Year;
    Button btnBack;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //Plant Database List
    List<Plant> PlantDatabase;



//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_harvest, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        //Adds banner ad to UI
//        AdView adView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//        adView.loadAd(adRequest);

        //Set the Main_Window Variable to the current activity object
        Main_Window = (Main_Window) getActivity();

        //Set the PlantDatabase from the Activity
        PlantDatabase = Main_Window.getPlantList();

        //[DEBUG] Print all the plant names
        System.out.println("------------------------------------");
        for (int i = 0; i < PlantDatabase.size(); i++){
            System.out.println(PlantDatabase.get(i).getPlantName());
        }
        System.out.println("------------------------------------");

        view.findViewById(R.id.btnBackToDate).setOnClickListener(this);

        txtDisplayDate = view.findViewById(R.id.txtDisplayDate);
        txtDisplayCrops = view.findViewById(R.id.txtDisplayCrops);
        btnBack = view.findViewById(R.id.btnBackToDate);
        txtDisplayDate.setText(sdf.format(Main_Window.getUserInputDate()));
//
//        if (monthAndDay >= 719 & monthAndDay < 726)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("7/19-7/25"));
//        }
//        else if (monthAndDay >= 726 & monthAndDay <=731)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("7/26-7/30"));
//        }
//        else if (monthAndDay == 81)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("8/1"));
//        }
//        else if (monthAndDay >= 82 & monthAndDay < 89)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("8/2-8/8"));
//        }
//        else if (monthAndDay == 89)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("8/9"));
//        }
//        else if (monthAndDay >= 810 & monthAndDay < 816)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("8/10-8/15"));
//        }
//        else if (monthAndDay >= 816 & monthAndDay < 823)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("8/16-8/22"));
//        }
//        else if (monthAndDay >= 823 & monthAndDay < 830)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("8/23-8/29"));
//        }
//        else if (monthAndDay == 830)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("8/30"));
//        }
//        else if (monthAndDay == 91 || monthAndDay == 92 || monthAndDay == 93 || monthAndDay == 94 || monthAndDay == 95)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("9/1-9/5"));
//        }
//        else if (monthAndDay == 96 || monthAndDay == 97 || monthAndDay == 98 || monthAndDay == 99 || monthAndDay == 910 || monthAndDay == 911 || monthAndDay == 912)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("9/6-9/12"));
//        }
//        else if(monthAndDay == 913)
//        {
//            txtDisplayCrops.setText(getIntent().getStringExtra("9/13"));
//        }
//        else
//        {
//            makeToast("No Plants are able to be harvested at this time");
//        }
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent goBack = new Intent(getApplicationContext(), Plant_Date_Screen.class);
//                //startActivity(goBack);
//            }
//        });
    }



//LISTENER METHODS =================================================================================
    public void onClick (View view) {
        switch (view.getId()) {
            case (R.id.btnBackToDate): {
                Main_Window.changeFragment("PlantDate");
            } break;

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
        Toast toast = Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }
}
