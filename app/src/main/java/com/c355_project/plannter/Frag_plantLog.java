package com.c355_project.plannter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Frag_plantLog extends Fragment implements View.OnClickListener {

//VARIABLES ========================================================================================

    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    // GUI Elements
    ListView lv;

    /*
    TODO: add instructions to instruct how to open notes
     */

//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_log, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Provide values for variables needed to be set on activity start
        Main_Window = (Main_Window) getActivity();

        //Implements hardware back button to take user back to the Main Menu
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Main_Window.changeFragment("MainMenu");            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        //Find GUI Elements
        lv = view.findViewById(R.id.lv);

        //Set all OnClickListeners needed for this View

        // Setup list adapter
        PlantLogCustomListAdapter adapter = new PlantLogCustomListAdapter(Main_Window);
        lv.setAdapter(adapter);

        // If there are no logs...
        if (adapter.getCount() == 0){
            openConfirmationDialog(Main_Window);
        }

        //Adds banner ad to UI
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }


//LISTENER METHODS =================================================================================
    @Override
    public void onClick (View view) {

    }

//METHODS ==========================================================================================
    private void openConfirmationDialog(Context context) {
        new AlertDialog.Builder(context)
            .setTitle("You have no logs!")
            .setMessage(Html.fromHtml("Add a new log on the Plant By Date screen."))

            .setPositiveButton("Open Plant By Date", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Main_Window.changeFragment("PlantDate");
                }
            })

            .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Main_Window.changeFragment("MainMenu");
                }
            })
            .setIcon(R.drawable.ic_dialog_warning)
            .show();
    }
}
