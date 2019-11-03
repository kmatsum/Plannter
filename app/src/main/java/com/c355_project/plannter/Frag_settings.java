package com.c355_project.plannter;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Frag_settings extends Fragment implements View.OnClickListener {
    Main_Window Main_Window;
    String website = "https://morningchores.com/frost-dates/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Main_Window = (Main_Window) getActivity();

        //Attaches onClickListener to Buttons
        view.findViewById(R.id.btnFrostDateIntent).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnFrostDateIntent): {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                startActivity(mIntent);
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
