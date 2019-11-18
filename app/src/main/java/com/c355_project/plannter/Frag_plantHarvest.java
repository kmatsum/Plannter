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

        //txtDisplayCrops.setText(Main_Window.getHarvestableCrops());
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
