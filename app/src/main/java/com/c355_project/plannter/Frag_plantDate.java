package com.c355_project.plannter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class Frag_plantDate extends Fragment implements View.OnClickListener {

    //Variables
    CalendarView calendarView;
    TextView txtCropHarvest;
    RadioButton rbtnHarvest, rbtnPlant;
    String Month, Day, Year, concatMonthAndDay;
    int monthAndDay;
    List<Plant> plantDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_plant_date, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Main_Window main_window = new Main_Window();
        plantDB = main_window.getPlantList();
        System.out.println("------------------------------------");
        for (int i = 0; i < plantDB.size(); i++){
            System.out.println(plantDB.get(i).getPlantName());
        }
        System.out.println("------------------------------------");

        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);

    }



//onClick Method ===================================================================================
    public void onClick (View view) {
        Main_Window Main_Window = (Main_Window) getActivity();
        switch (view.getId()) {
            case (R.id.btnBack): {

            } break;

            case (R.id.btnNext): {

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
