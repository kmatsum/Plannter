package com.c355_project.plannter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class Frag_settingsAddPlants extends Fragment implements View.OnClickListener {
//VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;
    TextView txtName,
            txtSeedCompany,
            txtFirstPlantDate,
            txtWeeksToHarvest,
            txtHarvestRange,
            txtLastPlantDate,
            txtSeedIndoors,
            txtSeedDistance,
            txtSeedDepth,
            txtNotes;
    CheckBox cbSpring,
            cbFall;
    ToggleButton toggleButton;
    RadioGroup rgMethod;
    RadioButton rbFlat,
            rbRaisedHills,
            rbRaisedRows;

    public Frag_settingsAddPlants() {
        // Required empty public constructor
    }

//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_add_plants, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Main_Window = (Main_Window) getActivity();

        //Attaches onClickListener to Buttons
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnSave).setOnClickListener(this);
        view.findViewById(R.id.toggleButton).setOnClickListener(this);

        //Find GUI elements
        txtName = view.findViewById(R.id.txtName);
        txtSeedCompany = view.findViewById(R.id.txtSeedCompany);
        txtFirstPlantDate = view.findViewById(R.id.txtFirstPlantDate);
        txtWeeksToHarvest = view.findViewById(R.id.txtWeeksToHarvest);
        txtHarvestRange = view.findViewById(R.id.txtHarvestRange);
        txtLastPlantDate = view.findViewById(R.id.txtLastPlantDate);
        txtSeedIndoors = view.findViewById(R.id.txtSeedIndoors);
        txtSeedDistance = view.findViewById(R.id.txtSeedDistance);
        txtSeedDepth = view.findViewById(R.id.txtSeedDepth);
        txtNotes = view.findViewById(R.id.txtNotes);
        cbFall = view.findViewById(R.id.cbFall);
        cbSpring = view.findViewById(R.id.cbSpring);
        toggleButton = view.findViewById(R.id.toggleButton);
        rgMethod = view.findViewById(R.id.rgMethod);
        rbFlat = view.findViewById(R.id.rbFlat);
        rbRaisedHills = view.findViewById(R.id.rbRaisedHills);
        rbRaisedRows = view.findViewById(R.id.rbRaisedRows);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnBack): {
                Main_Window.changeFragment("PlantInfo");
            } break;

            case (R.id.toggleButton): {
                if (toggleButton.isChecked()){
                    txtSeedIndoors.setVisibility(View.VISIBLE);
                } else {
                    txtSeedIndoors.setText("");
                    txtSeedIndoors.setVisibility(View.INVISIBLE);
                }
            } break;

            case (R.id.btnSave): {
                // INPUT VALIDATION ================================================================
                if (txtName.getText().toString().matches("")){
                    makeToast("Please enter plant name!");
                    txtName.requestFocus();
                    return;
                }

                if (!cbFall.isChecked() && !cbSpring.isChecked()){
                    makeToast("Please check Spring, Fall, or both!");
                    cbSpring.requestFocus();
                    return;
                }

                if (txtFirstPlantDate.getText().toString().matches("")){
                    makeToast("Please enter a first plant date!");
                    txtFirstPlantDate.requestFocus();
                    return;
                } else if (Integer.parseInt(txtFirstPlantDate.getText().toString()) > 51){
                    makeToast("First plant date must be less than 52 weeks!");
                    txtFirstPlantDate.requestFocus();
                    return;
                }

                if (txtWeeksToHarvest.getText().toString().matches("")){
                    makeToast("Please enter weeks to harvest!");
                    txtWeeksToHarvest.requestFocus();
                    return;
                } else if (Integer.parseInt(txtWeeksToHarvest.getText().toString()) > 51){
                    makeToast("Weeks to harvest must be less than 52 weeks!");
                    txtWeeksToHarvest.requestFocus();
                    return;
                }

                if (txtHarvestRange.getText().toString().matches("")){
                    makeToast("Please enter harvest range!");
                    txtHarvestRange.requestFocus();
                    return;
                } else if (Integer.parseInt(txtHarvestRange.getText().toString()) > 29){
                    makeToast("Harvest Range must be less than 30 weeks!");
                    txtHarvestRange.requestFocus();
                    return;
                }

                if (txtLastPlantDate.getText().toString().matches("")){
                    makeToast("Please enter last plant date!");
                    txtLastPlantDate.requestFocus();
                    return;
                } else if (Integer.parseInt(txtLastPlantDate.getText().toString()) > 51){
                    makeToast("Last plant date must be less than 52 weeks!");
                    txtLastPlantDate.requestFocus();
                    return;
                }

                if (txtSeedIndoors.getText().toString().matches("") && toggleButton.isChecked()){
                    makeToast("Please toggle off seed indoors or enter the number of weeks!");
                    txtSeedIndoors.requestFocus();
                    return;
                } else if (!toggleButton.isChecked()){
                    txtSeedIndoors.setText("52");
                }

                if (txtSeedDistance.getText().toString().matches("")){
                    makeToast("Please enter seed distance!");
                    txtSeedDistance.requestFocus();
                    return;
                } else if (Integer.parseInt(txtSeedDistance.getText().toString()) > 48){
                    makeToast("Seed distance must be less than 49 inches (4 feet)!");
                    txtSeedDistance.requestFocus();
                    return;
                }

                if (txtSeedDepth.getText().toString().matches("")){
                    makeToast("Please enter seed depth!");
                    txtSeedDepth.requestFocus();
                    return;
                } else if (Integer.parseInt(txtSeedDepth.getText().toString()) > 48){
                    makeToast("Seed depth must be less than 49 inches (4 feet)!");
                    txtSeedDepth.requestFocus();
                    return;
                }

                // TEMP OBJECT CREATION ============================================================
                Plant tempPlant = new Plant(txtName.getText().toString().trim(), txtSeedCompany.getText().toString().trim(),
                        Integer.parseInt(txtFirstPlantDate.getText().toString().trim()),
                        Integer.parseInt(txtWeeksToHarvest.getText().toString().trim()),
                        Integer.parseInt(txtHarvestRange.getText().toString().trim()),
                        Integer.parseInt(txtSeedIndoors.getText().toString().trim()),
                        Integer.parseInt(txtLastPlantDate.getText().toString().trim()),txtNotes.getText().toString().trim(),
                        R.drawable.plant, rbFlat.isChecked(),
                        rbRaisedHills.isChecked() && !rbRaisedRows.isChecked(),
                        Integer.parseInt(txtSeedDistance.getText().toString().trim()),
                        Double.parseDouble(txtSeedDepth.getText().toString().trim()));

                // INSERT NEW PLANT ================================================================
                Main_Window.insertPlant(tempPlant);
                makeToast("Plant " + txtName.getText().toString().trim() + " has been added!");

                // Return
                Main_Window.changeFragment("PlantInfo");

                resetGUI();

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

    public void resetGUI(){
        txtName.setText("");
        txtSeedCompany.setText("");
        cbSpring.setChecked(true);
        cbFall.setChecked(true);
        txtFirstPlantDate.setText("");
        txtWeeksToHarvest.setText("");
        txtHarvestRange.setText("");
        txtLastPlantDate.setText("");
        txtSeedIndoors.setVisibility(View.INVISIBLE);
        txtSeedIndoors.setText("52");
        toggleButton.setChecked(false);
        txtSeedDistance.setText("");
        txtSeedDepth.setText("");
        rbFlat.setChecked(true);
        txtNotes.setText("");
    }
}
