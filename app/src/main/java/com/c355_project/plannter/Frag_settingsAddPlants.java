package com.c355_project.plannter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Frag_settingsAddPlants extends Fragment implements View.OnClickListener {
//VARIABLES ========================================================================================

    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //GUI Elements
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

    // Photo handling
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_IMAGE = 1999;
    Bitmap photo = null;

    // Temp object
    Plant tempPlant = null;

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

        //Implements hardware back button to take user back to Plant Info
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Main_Window.changeFragment("PlantInfo");            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        //Attaches onClickListener to Buttons
        view.findViewById(R.id.btnTakePicture).setOnClickListener(this);
        view.findViewById(R.id.btnOpenGallery).setOnClickListener(this);
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

//LISTENER METHODS =================================================================================
    @Override
    public void onClick(View view) {
        //Determines how to respond to the click
        switch (view.getId()) {
            //Take Picture
            case (R.id.btnTakePicture):{
                // Send Intent to camera, response handled below in onActivityResult method
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } break;

            //Download Image
            case (R.id.btnOpenGallery):{
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
            } break;

            //Toggle the seed indoors textbox
            case (R.id.toggleButton): {
                if (toggleButton.isChecked()){
                    txtSeedIndoors.setVisibility(View.VISIBLE);
                } else {
                    txtSeedIndoors.setText("");
                    txtSeedIndoors.setVisibility(View.INVISIBLE);
                }
            } break;

            /*  TODO: Add a "Get Plant Info From Bluetooth" Button.
                - Create the Intent which calls the Device List Activity class
                - Then startActivityForResult
                - Retrieve the result of the activity, then start the "Client Side Connection of Bluetooth"
            */

            case (R.id.btnSave): {
                // INPUT VALIDATION ================================================================
                if (txtName.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter plant name!");
                    txtName.requestFocus();
                    return;
                }

                if (!cbFall.isChecked() && !cbSpring.isChecked()){
                    Main_Window.makeToast("Please check Spring, Fall, or both!");
                    cbSpring.requestFocus();
                    return;
                }

                if (txtFirstPlantDate.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter a first plant date!");
                    txtFirstPlantDate.requestFocus();
                    return;
                } else if (Integer.parseInt(txtFirstPlantDate.getText().toString()) > 51){
                    Main_Window.makeToast("First plant date must be less than 52 weeks!");
                    txtFirstPlantDate.requestFocus();
                    return;
                }

                if (txtWeeksToHarvest.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter weeks to harvest!");
                    txtWeeksToHarvest.requestFocus();
                    return;
                } else if (Integer.parseInt(txtWeeksToHarvest.getText().toString()) > 51){
                    Main_Window.makeToast("Weeks to harvest must be less than 52 weeks!");
                    txtWeeksToHarvest.requestFocus();
                    return;
                }

                if (txtHarvestRange.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter harvest range!");
                    txtHarvestRange.requestFocus();
                    return;
                } else if (Integer.parseInt(txtHarvestRange.getText().toString()) > 29){
                    Main_Window.makeToast("Harvest Range must be less than 30 weeks!");
                    txtHarvestRange.requestFocus();
                    return;
                }

                if (txtLastPlantDate.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter last plant date!");
                    txtLastPlantDate.requestFocus();
                    return;
                } else if (Integer.parseInt(txtLastPlantDate.getText().toString()) > 51){
                    Main_Window.makeToast("Last plant date must be less than 52 weeks!");
                    txtLastPlantDate.requestFocus();
                    return;
                }

                if (txtSeedIndoors.getText().toString().matches("") && toggleButton.isChecked()){
                    Main_Window.makeToast("Please toggle off seed indoors or enter the number of weeks!");
                    txtSeedIndoors.requestFocus();
                    return;
                } else if (!toggleButton.isChecked()){
                    txtSeedIndoors.setText("52");
                }

                if (txtSeedDistance.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter seed distance!");
                    txtSeedDistance.requestFocus();
                    return;
                } else if (Integer.parseInt(txtSeedDistance.getText().toString()) > 48){
                    Main_Window.makeToast("Seed distance must be less than 49 inches (4 feet)!");
                    txtSeedDistance.requestFocus();
                    return;
                }

                if (txtSeedDepth.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter seed depth!");
                    txtSeedDepth.requestFocus();
                    return;
                } else if (Integer.parseInt(txtSeedDepth.getText().toString()) > 48){
                    Main_Window.makeToast("Seed depth must be less than 49 inches (4 feet)!");
                    txtSeedDepth.requestFocus();
                    return;
                }

                //Save default photo if user didn't take their own
                if (photo == null) {
                    photo = ((BitmapDrawable) ResourcesCompat.getDrawable(Main_Window.getResources(), R.drawable.plant, null)).getBitmap();
                }

                // TEMP OBJECT CREATION ============================================================
                tempPlant = new Plant(txtName.getText().toString().trim(), txtSeedCompany.getText().toString().trim(),
                        Integer.parseInt(txtFirstPlantDate.getText().toString().trim()),
                        Integer.parseInt(txtWeeksToHarvest.getText().toString().trim()),
                        Integer.parseInt(txtHarvestRange.getText().toString().trim()),
                        Integer.parseInt(txtSeedIndoors.getText().toString().trim()),
                        Integer.parseInt(txtLastPlantDate.getText().toString().trim()),txtNotes.getText().toString().trim(),
                        "", rbFlat.isChecked(),
                        rbRaisedHills.isChecked() && !rbRaisedRows.isChecked(),
                        Integer.parseInt(txtSeedDistance.getText().toString().trim()),
                        Double.parseDouble(txtSeedDepth.getText().toString().trim()));

                // INSERT NEW PLANT ================================================================
                Main_Window.editTransaction("InsertPlant", tempPlant);

                resetGUI();

            } break;

            //Used for handling exceptions on if the given ViewID and the expected ViewID does not match
            default: {
                //Toast Error Information
                Main_Window.makeToast("[ERROR] Menu parameter passed was not found, returning to main menu...");
                System.out.println("[ERROR] Menu parameter passed was not found, returning to main menu...\n");

                Main_Window.changeFragment("MainMenu");
            }
        }
    }



//METHODS ==========================================================================================

    private void resetGUI(){
        //Resets the GUI to blank input
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

    // Method to respond to intents
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Respond to the camera intent
        // Check requestCode and resultCode
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            try {
                // Unpack photo from intent
                photo = (Bitmap) data.getExtras().get("data");
                if (photo == null)
                    throw new Exception("Photo is Null");
            } catch (Exception e) {
                //Display an error
                Main_Window.makeToast("Error reading image.");
                Log.e("CAMERA_REQUEST Intent", "Exception: ", e);
            }
        }

        // Respond to the gallery intent
        // Followed tutorial here: https://medium.com/@pednekarshashank33/android-10s-scoped-storage-image-picker-gallery-camera-d3dcca427bbf
        // Check requestCode and resultCode
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                // Unpack photo from intent
                Uri selectedImageUri = data.getData();
                ParcelFileDescriptor pfd = Main_Window.getContentResolver().openFileDescriptor(selectedImageUri, "r");
                photo = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                pfd.close();

                if (photo == null)
                    throw new Exception("Photo is Null");
            } catch (Exception e) {
                //Display an error
                Main_Window.makeToast("Error reading selected image.");
                Log.e("PICK_IMAGE Intent", "Exception: ", e);
            }
        }
    }
}
