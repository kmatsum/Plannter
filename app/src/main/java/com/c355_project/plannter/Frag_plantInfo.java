package com.c355_project.plannter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Frag_plantInfo extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {

//VARIABLES ========================================================================================
    // Intent request codes
    private static final int    REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private static final int    REQUEST_MAKE_DISCOVERABLE = 10;
    private static final int    REQUEST_ENABLE_BT = 11;

    // Permissions
    String[] PERMISSIONS = {android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN, android.Manifest.permission.ACCESS_FINE_LOCATION};

    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    // Plant Object List
    List<Plant> plantList;
    String[]    plantNames;
    Date        FallFrostDate,
                SpringFrostDate;

    // GUI Elements
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
				
    BluetoothService
                bluetoothService;

    //Date Format
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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
        plantList = Main_Window.PlantList;
        SpringFrostDate = Main_Window.getLastSpringFrostDate();
        FallFrostDate = Main_Window.getFirstFallFrostDate();

        //Implements hardware back button to take user back to the Main Menu
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Main_Window.changeFragment("MainMenu");            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

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
        imageView = view.findViewById(R.id.imgCrop);
        txtSeedIndoorsDate = view.findViewById(R.id.txtSeedIndoorsDate);
        txtSeasons = view.findViewById(R.id.txtSeasons);
        txtMethod = view.findViewById(R.id.txtMethod);

        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);
        view.findViewById(R.id.btnSharePlant).setOnClickListener(this);
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

        //Adds banner ad to UI
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onDestroyView() {
        System.out.println("[DEBUG]: Frag_plantInfo.onDestroyView(): Called");
        if (bluetoothService != null) {
            System.out.println("[DEBUG]: Frag_plantInfo.onDestroyView(): (BluetoothService != null), stopping the BluetoothService and un-instantiating it");
            bluetoothService.stopBluetooth();
            bluetoothService = null;
        }
        super.onDestroyView();
    }

    //LISTENER METHODS =================================================================================
    public void onClick (View view) {
        Integer id = view.getId();

        //Go to fragment that adds plants
        if (id == R.id.imgSettingsAddPlants) {
            Main_Window.changeFragment("AddPlants");
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

            // Ensure there will be at least 1 plant after deletion
            if (spnrSelectPlant.getAdapter().getCount() == 1){
                makeToast("You must have at least 1 plant.");
            } else {
                openConfirmationDialog(Main_Window);
            }
        }

        //Share Plant Via Bluetooth Button
        else if (id == R.id.btnSharePlant) {
            //TODO: Add Sharing Plant Functionality Here
            bluetoothService = new BluetoothService(this, "SERVER");

            //Check if Bluetooth is available
            //If the getDeviceState returns false, then bluetooth is not supported
            if (bluetoothService.getDeviceState()) {
                //Bluetooth is supported
                if (bluetoothService.enableBluetooth()) {
                    int grantedPermissionCounter = checkBluetoothPermissions();
                    System.out.println("[DEBUG]: onClick.CASE(R.id.btnSharePlant): if(bluetoothService.enableBluetooth(): grantedPermissionCounter = " + grantedPermissionCounter + " PERMISSIONS.length = " + PERMISSIONS.length);
                    //Check for Bluetooth Permissions
                    if (grantedPermissionCounter == PERMISSIONS.length) {
                        bluetoothService.makeThisDeviceDiscoverable();
                    }
                }
            } else {
                //TODO: Bluetooth is NOT Available
                Main_Window.makeToast("Bluetooth is not available on your device.");
            }
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
        Drawable plantImage = Drawable.createFromPath(plant.getPhotoPath());
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

        txtFirstPlantDate.setText(dateFormat.format(calculatePlantDate(plant.getFirstPlantDate(), SpringFrostDate)));
        txtLastPlantDate.setText(dateFormat.format(calculatePlantDate(plant.getLastPlantDate(), FallFrostDate)));
        if (plant.getSeedIndoorDate() == 52) {
            txtSeedIndoorsDate.setText("N/A");
        } else {
            txtSeedIndoorsDate.setText(Integer.toString(plant.getSeedIndoorDate()));
        }
        txtHarvestRange.setText(Integer.toString(plant.getHarvestRange())+" weeks");

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

    //onActivityResult =================================================================================
    /*
    This onActivityResult is mainly used for Bluetooth
    */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case (REQUEST_ENABLE_BT): {
                System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[REQUEST_ENABLE_BT]");
                //Check for Bluetooth Permissions
                if (checkBluetoothPermissions() == PERMISSIONS.length) {
                    bluetoothService.makeThisDeviceDiscoverable();
                }
            } break;
            case (REQUEST_MAKE_DISCOVERABLE): {
                System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[REQUEST_MAKE_DISCOVERABLE]: called with result code: " + resultCode);
                if (resultCode == 300) {
                    System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[REQUEST_MAKE_DISCOVERABLE] invoked an Activity.RESULT_OK");

                    //Grab and set the Plant we want to send via Bluetooth
                    int position = spnrSelectPlant.getSelectedItemPosition();
                    Plant sendThisPlant = plantList.get(position);

                    bluetoothService.startBluetoothServerThread(sendThisPlant);
                } else {
                    System.out.println("[DEBUG]: Frag_plantInfo.onActivityResult.case[REQUEST_MAKE_DISCOVERABLE] DID NOT invoke an Activity.RESULT_OK");
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
                bluetoothService.makeThisDeviceDiscoverable();
            } else {
                //Permissions Denied
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned false, ALL PERMISSIONS NOT GRANTED");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast toast = Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

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

    public Date calculatePlantDate(int distanceFromFrostDate, Date xFrostDate) {
        //Create the resulting date variable and provide it a default date value
        Date resultingDate;

        //Create a Calendar Object, then cast the Date object into the calendar object.
        Calendar c = Calendar.getInstance();
        c.setTime(xFrostDate);

        //(Subtract) Add the number of weeks from the set date to get a resulting calendar date
        c.add( Calendar.WEEK_OF_YEAR, -(distanceFromFrostDate) );

        //Cast the Calendar Date to a Date to return
        resultingDate = c.getTime();

        return resultingDate;
    }

    private void openConfirmationDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Are You Sure You Want To Delete Plant " + plantList.get(spnrSelectPlant.getSelectedItemPosition()).getPlantName() + "?")
                .setMessage(Html.fromHtml("This action cannot be undone."))

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete plant from database
                        int position = spnrSelectPlant.getSelectedItemPosition();
                        Plant plant = plantList.get(position);
                        Main_Window.editTransaction("DeletePlant", plant);

                        //Update plant list
                        plantList = Main_Window.PlantList;
                        Main_Window.changeFragment("MainMenu");
                    }
                })

                // A null listener allows the button to close the dialog and take no further action.
                .setNegativeButton("Cancel", null)
                .setIcon(R.drawable.ic_dialog_warning)
                .show();
    }
}
