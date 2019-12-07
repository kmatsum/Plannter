package com.c355_project.plannter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import java.util.List;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;



public class Frag_plantInfo extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {
//VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    List<Plant> plantList;
    String[] plantNames;

    //GUI Elements
    Spinner     spnrSelectPlant;
    ImageView   imageView;
    TextView    txtSeedIndoors,
                txtWeeksToHarvest,
                txtSeasons,
                txtSeedDistance,
                txtMethod,
                txtSeedCompany,
                txtFirstPlantDate,
                txtHarvestRange,
                txtLastPlantDate,
                txtSeedIndoorsDate,
                txtSeedDepth,
                txtNotes;

//LIFECYCLE METHODS ================================================================================
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
        plantList = Main_Window.getPlantList();

        //Find GUI elements
        txtSeedCompany = view.findViewById(R.id.txtSeedCompany);
        txtFirstPlantDate = view.findViewById(R.id.txtFirstPlantDate);
        txtWeeksToHarvest = view.findViewById(R.id.txtWeeksToHarvest);
        txtHarvestRange = view.findViewById(R.id.txtHarvestRange);
        txtLastPlantDate = view.findViewById(R.id.txtLastPlantDate);
        txtSeedIndoors = view.findViewById(R.id.txtSeedIndoors);
        txtSeedDistance = view.findViewById(R.id.txtSeedDistance);
        txtSeedDepth = view.findViewById(R.id.txtSeedDepth);
        txtNotes = view.findViewById(R.id.txtNotes);
        imageView = view.findViewById(R.id.imageView);
        txtSeedIndoorsDate = view.findViewById(R.id.txtSeedIndoorsDate);
        txtSeasons = view.findViewById(R.id.txtSeasons);
        txtMethod = view.findViewById(R.id.txtMethod);

        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);
        view.findViewById(R.id.btnDelete).setOnClickListener(this);
        view.findViewById(R.id.imgSettingsAddPlants).setOnClickListener(this);
        view.findViewById(R.id.arrowNext).setOnClickListener(this);
        view.findViewById(R.id.arrowPrevious).setOnClickListener(this);

        //Set the spinner adapter and contents
        spnrSelectPlant = view.findViewById(R.id.spnrSelectPlant);
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
    }



//LISTENER METHODS =================================================================================
    public void onClick (View view) {
        Integer id = view.getId();

        //Go back to main menu
        if (id == R.id.btnBack) {
            Main_Window.changeFragment("MainMenu");
        }

        //Go to fragment that adds plants
        else if (id == R.id.imgSettingsAddPlants) {
            Main_Window.changeFragment("SettingsAddPlants");
        }

        //Go back in spinner plant list. Do nothing if position is already the first item.
        else if (id == R.id.btnPrevious || id == R.id.arrowPrevious) {
            int position = spnrSelectPlant.getSelectedItemPosition();
            if (position > 0)
                spnrSelectPlant.setSelection(spnrSelectPlant.getSelectedItemPosition() - 1);
        }

        //Go forward in spinner plant list. Do nothing if position is already the last item.
        else if (id == R.id.btnNext || id == R.id.arrowNext) {
            int position = spnrSelectPlant.getSelectedItemPosition();
            if (position < spnrSelectPlant.getAdapter().getCount() - 1)
                spnrSelectPlant.setSelection(spnrSelectPlant.getSelectedItemPosition() + 1);
        }

        //Delete selected plant. Alert user if only 1 plant is left (and prevent deletion).
        else if (id == R.id.btnDelete) {
            if (spnrSelectPlant.getAdapter().getCount() == 1){
                makeToast("You must have at least 1 plant.");
                return;
            }
            int position = spnrSelectPlant.getSelectedItemPosition();
            Plant plant = plantList.get(position);
            String name = plant.getPlantName();
            Main_Window.deletePlant(plant);
            plantList = Main_Window.getPlantList();
            Main_Window.changeFragment("MainMenu");
            makeToast("Plant " + name + " deleted.");
        }

        //Used for handling exceptions on if the given ViewID and the expected ViewID does not match
        else {
            //Toast Error Information
            makeToast("[ERROR] Menu parameter passed was not found, returning to main menu...");
            System.out.println("[ERROR] Menu parameter passed was not found, returning to main menu...\n");

            Main_Window.changeFragment("MainMenu");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Get selected plant, set all attributes
        Plant plant = plantList.get(position);
        Drawable plantImage = ResourcesCompat.getDrawable(getResources(), plant.getFileID(), null);
        imageView.setImageDrawable(plantImage);

        // TOP BOX =================================================================================
        if (plant.getFirstPlantDate() < 26) { //26 represents half of 52 weeks in the year
            txtSeasons.setText("Spring");
        }

        if (plant.getLastPlantDate() < 26) {
            if (txtSeasons.getText().equals("---")) {
                txtSeasons.setText("Fall");
            } else {
                txtSeasons.append(",\nFall");
            }
        }

        if (plant.getSeedIndoorDate() == 52) {
            txtSeedIndoors.setText("No");
        } else {
            txtSeedIndoors.setText("Yes");
        }

        txtWeeksToHarvest.setText(Integer.toString(plant.getWeeksToHarvest()));

        // GENERAL =================================================================================
        txtSeedCompany.setText(plant.getSeedCompany());

        // PLANTING DATES ==========================================================================
        txtFirstPlantDate.setText(Integer.toString(plant.getFirstPlantDate()));
        txtLastPlantDate.setText(Integer.toString(plant.getLastPlantDate()));
        if (plant.getSeedIndoorDate() == 52) {
            txtSeedIndoorsDate.setText("N/A");
        } else {
            txtSeedIndoorsDate.setText(Integer.toString(plant.getSeedIndoorDate()));
        }
        txtHarvestRange.setText(Integer.toString(plant.getHarvestRange()));

        // PLANTING LAYOUT =========================================================================
        txtSeedDistance.setText(Integer.toString(plant.getDistBetweenPlants()));
        txtSeedDepth.setText(Double.toString(plant.getSeedDepth()));
        if (plant.isRaisedHills()) {
            txtMethod.setText("Raised Hills");
        } else if (plant.isRaisedRows()) {
            txtMethod.setText("Raised Rows");
        } else {
            txtMethod.setText("Flat");
        }

        // NOTES ===================================================================================
        if (plant.getNotes().matches("")){
            txtNotes.setText("No notes.");
        } else {
            txtNotes.setText(plant.getNotes());
        }
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
