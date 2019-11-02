package com.c355_project.plannter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.annotation.XmlRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;


public class Frag_plantInfo extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {
  
//VARIABLES ========================================================================================
    List<Plant> plantList;
    String[] plantNames;

    //GUI Elements
    ImageView imageView;
    TextView txtWeeksToHarvest;

    //Main_Window Activity Instantiation
    Main_Window Main_Window;



//LIFECYCLE METHODS ================================================================================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plant_info, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      
        Main_Window = (Main_Window) getActivity();
        plantList = Main_Window.getPlantList();
      
        imageView = view.findViewById(R.id.imageView);
        txtWeeksToHarvest = view.findViewById(R.id.txtWeeksToHarvest);

        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnBack).setOnClickListener(this);

        //Set the spinner adapter and contents
        Spinner spnrSelectPlant = view.findViewById(R.id.spnrSelectPlant);
        plantNames = new String[plantList.size()];
        for (int i = 0; i < plantList.size(); i++){
            plantNames[i] = plantList.get(i).getPlantName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), R.layout.spinner_item, plantNames);
        spnrSelectPlant.setAdapter(adapter);
        spnrSelectPlant.setOnItemSelectedListener(this);

//        //Adds banner ad to UI
//        AdView adView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//        adView.loadAd(adRequest);
    }



    //LISTENER METHODS =================================================================================
    public void onClick (View view) {
        switch (view.getId()) {
            case (R.id.btnBack): {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO MAINMENU");
                System.out.println("=============================================================");

                Main_Window.changeFragment("MainMenu");
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Plant plant = plantList.get(position);
        Drawable plantImage = ResourcesCompat.getDrawable(getResources(), plant.getFileID(), null);
        imageView.setImageDrawable(plantImage);
        txtWeeksToHarvest.setText(Integer.toString(plant.getWeeksToHarvest()));
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

//METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
    }
}
