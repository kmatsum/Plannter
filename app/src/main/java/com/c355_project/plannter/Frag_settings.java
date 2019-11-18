package com.c355_project.plannter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Frag_settings extends Fragment implements View.OnClickListener {
    //VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //Website String
    String website = "https://morningchores.com/frost-dates/";

    //VARIABLES ========================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Main_Window = (Main_Window) getActivity();

        //Attaches onClickListener to Buttons
        view.findViewById(R.id.btnFrostDateIntent).setOnClickListener(this);
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnUpdateSpring).setOnClickListener(this);
        view.findViewById(R.id.btnUpdateFall).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnFrostDateIntent): {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                startActivity(mIntent);
            } break;

            case (R.id.btnBack): {
                Main_Window.changeFragment("MainMenu");
            } break;

            case (R.id.btnUpdateSpring): {
                //Creates References To The User Input Boxes
                EditText txtSpringMonth = Main_Window.findViewById(R.id.springFrostDateMonth);
                EditText txtSpringDay = Main_Window.findViewById(R.id.springFrostDateDay);
                EditText txtSpringYear = Main_Window.findViewById(R.id.springFrostDateYear);

                //User Input Validation
                String springInputDate = (txtSpringMonth.getText().toString() + "/" + txtSpringDay.getText().toString() + "/" + txtSpringYear.getText().toString());
                Main_Window.dateFormat.setLenient(false);
                try {
                    Date springDate = Main_Window.dateFormat.parse(springInputDate);
                    //Updates The Database With The User Inputted Spring Frost Date Values
                    Main_Window.setLastSpringFrostDate(springDate);
                    makeToast("Last Spring Frost Date Successfully Updated!");
                }
                //If Date Is Invalid, Toast The User To Input A Valid Date
                catch (ParseException e) {
                    makeToast("Please Enter A Valid Date");
                    e.printStackTrace();
                }
            } break;

            case (R.id.btnUpdateFall): {
                //Creates References To The User Input Boxes
                EditText txtFallMonth = Main_Window.findViewById(R.id.fallFrostDateMonth);
                EditText txtFallDay = Main_Window.findViewById(R.id.fallFrostDateDay);
                EditText txtFallYear = Main_Window.findViewById(R.id.fallFrostDateYear);

                //User Input Validation
                String fallInputDate = (txtFallMonth.getText().toString() + "/" + txtFallDay.getText().toString() + "/" + txtFallYear.getText().toString());
                Main_Window.dateFormat.setLenient(false);
                try {
                    Date fallDate = Main_Window.dateFormat.parse(fallInputDate);
                    //Updates The Database With The User Inputted Fall Frost Date Values
                    Main_Window.setFirstFallFrostDate(fallDate);
                    makeToast("First Fall Frost Date Successfully Updated!");
                }
                //If Date Is Invalid, Toast The User To Input A Valid Date
                catch (ParseException e) {
                    makeToast("Please Enter A Valid Date");
                    e.printStackTrace();
                }
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



//LISTENER METHODS =================================================================================



    //METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast toast = Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }
}
