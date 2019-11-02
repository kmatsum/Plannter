package com.c355_project.plannter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class Frag_mainMenu extends Fragment implements View.OnClickListener {

    //Main_Window Activity Instantiation
    Main_Window Main_Window;



    //LIFECYCLE METHODS ================================================================================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_menu, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Main_Window = (Main_Window) getActivity();

        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnPlantByDate).setOnClickListener(this);
        view.findViewById(R.id.btnPlantCrop).setOnClickListener(this);
        view.findViewById(R.id.btnPlantInfo).setOnClickListener(this);
        view.findViewById(R.id.imgSettings).setOnClickListener(this);

        //Adds banner ad to UI
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }



//onClick METHOD ===================================================================================
    @Override
    public void onClick (View view) {
        Main_Window Main_Window = (Main_Window) getActivity();

        switch (view.getId()) {
            case (R.id.btnPlantByDate): {
                Main_Window.changeFragment("PlantDate");
            } break;

            case (R.id.btnPlantCrop): {

            } break;

            case (R.id.btnPlantInfo): {
                Main_Window.changeFragment("PlantInfo");
            } break;

            case (R.id.imgSettings): {

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
        Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
    }
}