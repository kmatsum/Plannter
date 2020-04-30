package com.c355_project.plannter;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Frag_addPlants extends Fragment implements View.OnClickListener {

//VARIABLES ========================================================================================

    // Intent request codes
    private static final int    REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private static final int    REQUEST_TAKE_PICTURE = 2;
    private static final int    REQUEST_VIEW_GALLERY = 3;
    private static final int    REQUEST_ENABLE_BT = 11;
    private static final int    BLUETOOTH_REQUEST_CONNECT = 21;
    private static final String SELECTED_DEVICE = "Selected Device";

    // Permissions
    String[] PERMISSIONS = {
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.ACCESS_FINE_LOCATION};

    private String[] CAM_PERMISSION = {Manifest.permission.CAMERA};

    //Main_Window Activity Instantiation
    Main_Window     Main_Window;

    //GUI Elements
    TextView        txtName,
                    txtSeedCompany,
                    txtFirstPlantDate,
                    txtWeeksToHarvest,
                    txtHarvestRange,
                    txtLastPlantDate,
                    txtSeedIndoors,
                    txtSeedDistance,
                    txtSeedDepth,
                    txtNotes;
    ToggleButton    toggleButton;
    RadioGroup      rgMethod;
    RadioButton     rbFlat,
                    rbRaisedHills,
                    rbRaisedRows;

    BluetoothService
                    bluetoothService;

    // Photo handling
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_IMAGE = 1999;
    Bitmap photo = null;

    // Temp object
    Plant tempPlant = null;

    public Frag_addPlants() {
        // Required empty public constructor
    }

//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_plants, container, false);
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
        view.findViewById(R.id.btnGetFromBluetooth).setOnClickListener(this);

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
                if (Main_Window.checkSelfPermission(CAM_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(CAM_PERMISSION, REQUEST_TAKE_PICTURE);
                    System.out.println("[DEBUG] Requesting permissions.");
                } else {
                    // Send Intent to camera, response handled in onActivityResult method
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } break;

            //Download Image
            case (R.id.btnOpenGallery):{
                if (Main_Window.checkSelfPermission(CAM_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(CAM_PERMISSION, REQUEST_VIEW_GALLERY);
                    System.out.println("[DEBUG] Requesting permissions.");
                } else {
                    // Send Intent to camera, response handled in onActivityResult method
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
                }
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

            case (R.id.btnGetFromBluetooth): {
                System.out.println("[DEBUG]: Frag_addPlants.onClick(): btnGetFromBluetooth Clicked! Instantiating BluetoothService!");
                bluetoothService = new BluetoothService(this, "CLIENT");

                //Check if Bluetooth is available
                //If the getDeviceState returns false, then bluetooth is not supported
                if (bluetoothService.getDeviceState()) {
                    System.out.println("[DEBUG]: Frag_addPlants.onClick(): Bluetooth IS supported!");
                    //Bluetooth is supported
                    if (bluetoothService.enableBluetooth()) {
                        System.out.println("[DEBUG]: Frag_addPlants.onClick(): Bluetooth IS Enabled!");

                        //Check for Bluetooth Permissions
                        if (checkBluetoothPermissions() == PERMISSIONS.length) {
                            System.out.println("[DEBUG]: Frag_addPlants.onClick(): Permissions were all provided! Starting the BluetoothDeviceList ActivityForResult!");
                            Intent serverIntent = new Intent(getActivity(), BluetoothDeviceList.class);
                            startActivityForResult(serverIntent, BLUETOOTH_REQUEST_CONNECT);
                        }
                    }
                } else {
                    //Bluetooth is NOT Available
                    Main_Window.makeToast("Bluetooth is not available on your device.");
                }
            } break;

            case (R.id.btnSave): {
                // INPUT VALIDATION ================================================================
                if (txtName.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter plant name!");
                    txtName.requestFocus();
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
                } else if (Integer.parseInt(txtSeedDistance.getText().toString()) > 119){
                    Main_Window.makeToast("Seed distance must be less than 120 inches (10 feet)!");
                    txtSeedDistance.requestFocus();
                    return;
                }

                if (txtSeedDepth.getText().toString().matches("")){
                    Main_Window.makeToast("Please enter seed depth!");
                    txtSeedDepth.requestFocus();
                    return;
                } else if (Double.parseDouble(txtSeedDepth.getText().toString()) > 48){
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


//onResult METHODS =================================================================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case (CAMERA_REQUEST): {
                // Respond to the camera intent
                // Check requestCode and resultCode
                if (resultCode == Activity.RESULT_OK)
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
            } break;

            case (PICK_IMAGE): {
                // Respond to the gallery intent
                // Followed tutorial here: https://medium.com/@pednekarshashank33/android-10s-scoped-storage-image-picker-gallery-camera-d3dcca427bbf
                // Check requestCode and resultCode
                if (resultCode == Activity.RESULT_OK) {
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
            } break;

            case (REQUEST_ENABLE_BT): {
                System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[REQUEST_ENABLE_BT]");
                //Check for Bluetooth Permissions
                if (checkBluetoothPermissions() == PERMISSIONS.length) {
                    System.out.println("[DEBUG]: Frag_addPlants.onActivityResult(): Permissions were all provided! Starting the BluetoothDeviceList ActivityForResult!");
                    Intent serverIntent = new Intent(getActivity(), BluetoothDeviceList.class);
                    startActivityForResult(serverIntent, BLUETOOTH_REQUEST_CONNECT);
                }
            } break;

            case (BLUETOOTH_REQUEST_CONNECT): {
                System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[BLUETOOTH_REQUEST_CONNECT]");
                if (resultCode == Activity.RESULT_OK) {
                    System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[BLUETOOTH_REQUEST_CONNECT].RESULT_OK");
                    Bundle receivedData = data.getExtras();
                    BluetoothDevice connectToThisDevice = (BluetoothDevice) receivedData.get(SELECTED_DEVICE);
                    System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[BLUETOOTH_REQUEST_CONNECT].RESULT_OK: Received: " + connectToThisDevice.getName() + " as the target BluetoothDevice!");

                    bluetoothService.startBluetoothClientThread(connectToThisDevice);
                    System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[BLUETOOTH_REQUEST_CONNECT].RESULT_OK: bluetoothService.startBluetoothClientThread was called!");
                }
            } break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("[DEBUG]: onRequestPermissionResult Called!");
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (verifyPermissions(grantResults)) {
                //All Permissions Granted
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned true, ALL PERMISSIONS GRANTED");
                System.out.println("[DEBUG]: Frag_addPlants.onRequestPermissionResult(): Permissions were all provided! Starting the BluetoothDeviceList ActivityForResult!");
                Intent serverIntent = new Intent(getActivity(), BluetoothDeviceList.class);
                startActivityForResult(serverIntent, BLUETOOTH_REQUEST_CONNECT);
            } else {
                //Permissions Denied
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned false, ALL PERMISSIONS NOT GRANTED");
            }
        } else if (requestCode == REQUEST_TAKE_PICTURE) {
            if (verifyPermissions(grantResults)) {
                //All Permissions Granted
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned true, ALL PERMISSIONS GRANTED");
                // Send Intent to camera, response handled in onActivityResult method
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                //Permissions Denied
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned false, ALL PERMISSIONS NOT GRANTED");
                Main_Window.makeToast("You must grant camera permissions to take a picture.");
            }
        } else if (requestCode == REQUEST_VIEW_GALLERY) {
            if (verifyPermissions(grantResults)) {
                //All Permissions Granted
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned true, ALL PERMISSIONS GRANTED");
                // Send Intent to camera, response handled in onActivityResult method
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
            } else {
                //Permissions Denied
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned false, ALL PERMISSIONS NOT GRANTED");
                Main_Window.makeToast("You must grant camera permissions to open gallery.");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//METHODS ==========================================================================================
    private boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1){
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public int checkBluetoothPermissions () {
        System.out.println("[DEBUG]: Frag_plantInfo.checkBluetoothPermissions() Called");
        //This will check for permission
        int permissionGrantedCounter = 0;
        for (String str : PERMISSIONS) {
            System.out.println("[DEBUG]: Frag_plantInfo.checkBluetoothPermissions(): Checking for " + str + " Permission...");
            if (Main_Window.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(PERMISSIONS, REQUEST_BLUETOOTH_PERMISSIONS);
                System.out.println("[DEBUG]: Frag_plantInfo.checkBluetoothPermissions(): Permission " + str + " not granted, requesting Permission...");
                return permissionGrantedCounter;
            } else {
                //This will be invoked if the permission in this round of the For Loop is granted
                //When all permission are granted, then the counter will be 3, since there are only 3 permissions to ask for...
                System.out.println("[DEBUG]: Frag_plantInfo.checkBluetoothPermissions(): Permission " + str + " granted... ");
                permissionGrantedCounter++;
            }
        }
        return permissionGrantedCounter;
    }

    private void resetGUI(){
        //Resets the GUI to blank input
        txtName.setText("");
        txtSeedCompany.setText("");
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
