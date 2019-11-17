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

import java.util.Calendar;
import java.util.Date;


public class Frag_settings extends Fragment implements View.OnClickListener {
//VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //Website String
    String website = "https://morningchores.com/frost-dates/";

    //Spring Frost Date
        Date SpringFrost;
    //Fall Frost Date
        Date FallFrost;

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
                EditText txtSpringMonth = view.findViewById(R.id.springFrostDateMonth);
                EditText txtSpringDay = view.findViewById(R.id.springFrostDateDay);
                EditText txtSpringYear = view.findViewById(R.id.springFrostDateYear);

                //Stores The User Input as Integers
                int springMonth = Integer.parseInt(txtSpringMonth.getText().toString());
                int springDay = Integer.parseInt(txtSpringDay.getText().toString());
                int springYear = Integer.parseInt(txtSpringYear.getText().toString());

                //Merges The 3 Integers Into A Single Calender Object
                Calendar springFrostDate = Calendar.getInstance();
                springFrostDate.set(Calendar.DATE, springDay);
                springFrostDate.set(Calendar.MONTH, springMonth);
                springFrostDate.set(Calendar.YEAR, springYear);
                springFrostDate.set(Calendar.HOUR, 0);
                springFrostDate.set(Calendar.MINUTE, 0);
                springFrostDate.set(Calendar.SECOND, 0);

                //Updates The Database With The User Inputted Spring Frost Date
                
            } break;

            case (R.id.btnUpdateFall): {

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
