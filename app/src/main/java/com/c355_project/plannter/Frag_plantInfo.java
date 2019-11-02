package com.c355_project.plannter;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;



public class Frag_plantInfo extends Fragment implements View.OnClickListener {
    //Main_Window Activity Instantiation
    Main_Window Main_Window;



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

        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnBack).setOnClickListener(this);
    }



//onClick Method ===================================================================================
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

//METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();
    }
}
