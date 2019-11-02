package com.c355_project.plannter;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



public class Frag_plantByPlant extends Fragment implements View.OnClickListener {
//VARIABLES ========================================================================================

    //Main_Window Activity Instantiation
    Main_Window Main_Window;



//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_plant_by_plant, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Main_Window = (Main_Window) getActivity();

        //Set all OnClickListeners needed for this View


//        //Adds banner ad to UI
//        AdView adView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//        adView.loadAd(adRequest);
    }



//LISTENER METHODS =================================================================================
    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case (R.id.btnPlantByDate): {
                Main_Window.changeFragment("PlantDate");
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
}
