package com.c355_project.plannter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.List;



public class Frag_plantByPlant extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {
//VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //Plant Object List
    List<Plant> plantList;
    String[]    plantNames;

    //GUI Elements
    ImageView   imageView;
    TextView    txtSpringFrost,
                txtFallFrost,
                txtPlantStart,
                txtPlantEnd,
                txtHarvestStart,
                txtHarvestEnd;
    Spinner     spnrSelectPlant;

    //Date Format
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");



//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_by_plant, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Main_Window = (Main_Window) getActivity();
        plantList = Main_Window.getPlantList();

        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);

        //Set all the view
        imageView = view.findViewById(R.id.imageView);
        txtSpringFrost = view.findViewById(R.id.txtSpringFrost);
        txtFallFrost = view.findViewById(R.id.txtFallFrost);

        txtPlantStart = view.findViewById(R.id.txtPlantStart);
        txtPlantEnd = view.findViewById(R.id.txtPlantEnd);
        txtHarvestStart = view.findViewById(R.id.txtHarvestStart);
        txtHarvestEnd = view.findViewById(R.id.txtHarvestEnd);

        spnrSelectPlant = view.findViewById(R.id.spnrSelectPlant);

        //Set the spinner adapter and contents
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


        //Set some default text
        txtSpringFrost.setText(dateFormat.format(Main_Window.getLastSpringFrostDate()));
        txtFallFrost.setText(dateFormat.format(Main_Window.getFirstFallFrostDate()));
    }



//LISTENER METHODS =================================================================================
    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case (R.id.btnBack): {
                Main_Window.changeFragment("MainMenu");
            } break;

            case (R.id.btnNext): {
                int position = spnrSelectPlant.getSelectedItemPosition();
                if (position < spnrSelectPlant.getAdapter().getCount() - 1) {
                    spnrSelectPlant.setSelection(spnrSelectPlant.getSelectedItemPosition() + 1);
                }
            } break;

            case (R.id.btnPrevious): {
                int position = spnrSelectPlant.getSelectedItemPosition();
                if (position > 0) {
                    spnrSelectPlant.setSelection(spnrSelectPlant.getSelectedItemPosition() - 1);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Plant selectedPlant = plantList.get(position);
        Drawable plantImage = ResourcesCompat.getDrawable(getResources(), selectedPlant.getFileID(), null);
        imageView.setImageDrawable(plantImage);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }



//METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast toast = Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }
}
