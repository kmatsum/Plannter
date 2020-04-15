package com.c355_project.plannter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Frag_settings extends Fragment implements View.OnClickListener {
//VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //Frost Dates
    Date    FallFrostDate,
            SpringFrostDate;

    //GUI Elements
    TextView    txtSpringFrost,
                txtFallFrost;
    EditText    txtSpringFrostMonth,
                txtSpringFrostDay,
                txtSpringFrostYear,
                txtFallFrostMonth,
                txtFallFrostDay,
                txtFallFrostYear;

    //Date Format
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    //Website String
    String website = "https://morningchores.com/frost-dates/";

//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Main_Window = (Main_Window) getActivity();

        //Implements hardware back button to take user back to the Main Menu
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Main_Window.changeFragment("MainMenu");            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        //Find GUI Elements
        txtSpringFrost = view.findViewById(R.id.txtCurrentSpringFrost);
        txtFallFrost = view.findViewById(R.id.txtCurrentFallFrost);
        txtFallFrostDay = view.findViewById(R.id.fallFrostDateDay);
        txtFallFrostMonth = view.findViewById(R.id.fallFrostDateMonth);
        txtFallFrostYear = view.findViewById(R.id.fallFrostDateYear);
        txtSpringFrostDay = view.findViewById(R.id.springFrostDateDay);
        txtSpringFrostMonth = view.findViewById(R.id.springFrostDateMonth);
        txtSpringFrostYear = view.findViewById(R.id.springFrostDateYear);
        SpringFrostDate = Main_Window.getLastSpringFrostDate();
        FallFrostDate = Main_Window.getFirstFallFrostDate();

        //Attaches onClickListener to Buttons
        view.findViewById(R.id.btnFrostDateIntent).setOnClickListener(this);
        view.findViewById(R.id.btnUpdateSpring).setOnClickListener(this);
        view.findViewById(R.id.btnUpdateFall).setOnClickListener(this);
        view.findViewById(R.id.btnResetDB).setOnClickListener(this);

        //Display the stored Spring and Fall Frost Dates
        txtSpringFrost.setText(dateFormat.format(SpringFrostDate));
        txtFallFrost.setText(dateFormat.format(FallFrostDate));

        //Adds banner ad to UI
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        //Automatically moves focus to spring frost day when spring frost month is full
        txtSpringFrostMonth.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(txtSpringFrostMonth.getText().toString().length()==2) {
                    txtSpringFrostDay.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });

        //Automatically moves focus to spring frost year when spring frost day is full
        txtSpringFrostDay.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if (txtSpringFrostDay.getText().toString().length()==2) {
                    txtSpringFrostYear.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });

        //Automatically hides keyboard when spring frost year is full
        txtSpringFrostYear.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if (txtSpringFrostYear.getText().toString().length()==4) {

                    hideKeyboardFrom(Main_Window,view);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });

        //Automatically moves focus to fall frost day when fall frost month is full
        txtFallFrostMonth.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if(txtFallFrostMonth.getText().toString().length()==2) {
                    txtFallFrostDay.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });

        //Automatically moves focus to fall frost year when fall frost day is full
        txtFallFrostDay.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if (txtFallFrostDay.getText().toString().length()==2) {
                    txtFallFrostYear.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });

        //Automatically hides keyboard when fall frost year is full
        txtFallFrostYear.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count) {
                if (txtFallFrostYear.getText().toString().length()==4) {

                    hideKeyboardFrom(Main_Window,view);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
    }


//LISTENER METHODS =================================================================================
    @Override
    public void onClick(View view) {
        //Determines how to respond to the click
        switch (view.getId()) {
            case (R.id.btnFrostDateIntent): {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                startActivity(mIntent);
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
                    txtSpringMonth.setText("");
                    txtSpringDay.setText("");
                    txtSpringYear.setText("");
                    txtSpringMonth.requestFocus();
                    makeToast("Last Spring Frost Date Successfully Updated!");
                }
                //If Date Is Invalid, Toast The User To Input A Valid Date
                catch (ParseException e) {
                    makeToast("Please Enter A Valid Date");
                    e.printStackTrace();
                }

                //Update the Spring Frost Date on GUI
                SpringFrostDate = Main_Window.getLastSpringFrostDate();
                txtSpringFrost.setText(dateFormat.format(SpringFrostDate));
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
                    txtFallMonth.setText("");
                    txtFallDay.setText("");
                    txtFallYear.setText("");
                    txtFallMonth.requestFocus();
                    makeToast("First Fall Frost Date Successfully Updated!");
                }
                //If Date Is Invalid, Toast The User To Input A Valid Date
                catch (ParseException e) {
                    makeToast("Please Enter A Valid Date");
                    e.printStackTrace();
                }
                //Update the Fall Frost Date on GUI
                FallFrostDate = Main_Window.getFirstFallFrostDate();
                txtFallFrost.setText(dateFormat.format(FallFrostDate));
            } break;

            case (R.id.btnResetDB): {
                openConfirmationDialog(Main_Window);
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

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void openConfirmationDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Are You Sure You Want To Reset?")
                .setMessage(Html.fromHtml("Doing so will remove any custom plants and frost dates."))

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Main_Window.resetPlantDB();
                        //Update the class local Spring and Fall Frost Dates
                        SpringFrostDate = Main_Window.getLastSpringFrostDate();
                        FallFrostDate = Main_Window.getFirstFallFrostDate();
                        txtSpringFrost.setText(dateFormat.format(SpringFrostDate));
                        txtFallFrost.setText(dateFormat.format(FallFrostDate));
                    }
                })

                // A null listener allows the button to close the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_dialog_warning)
                .show();
    }
}
