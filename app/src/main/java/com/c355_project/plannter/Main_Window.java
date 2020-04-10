package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main_Window extends AppCompatActivity {
//VARIABLES ========================================================================================

    // Location to store all files
    public static String ROOT_STORAGE_LOCATION;
    public static String PLANT_PHOTO_STORAGE_LOCATION;
    public static String DATABASE_DIRECTORY = "./data/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    public static String DB_NAME = "plant_db";

    //Date Formatter
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    //Fragments
    Frag_mainMenu           Frag_mainMenu;
    Frag_settings           Frag_settings;
    Frag_settingsAddPlants  Frag_settingsAddPlants;
    Frag_plantInfo          Frag_plantInfo;
    Frag_plantDate          Frag_plantDate;
    Frag_plantHistory       Frag_plantHistory;

    //Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //LastFrostDate
    String lastSpringFrostDate;
    String firstFallFrostDate;

    //Plant List
    List<Plant> PlantList;

    //Tracking Variables
    long id; //temp variable to store plant id
    boolean isAsyncTaskRunning = false; //used in editTransaction to keep track of DatabaseTransaction AsyncTask

    //PlantHarvest
    Date userInputDate;

//Lifecycle Methods ================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        //Fragment Instantiation
        Frag_mainMenu           = new Frag_mainMenu();
        Frag_settings           = new Frag_settings();
        Frag_settingsAddPlants  = new Frag_settingsAddPlants();
        Frag_plantInfo          = new Frag_plantInfo();
        Frag_plantDate          = new Frag_plantDate();
        Frag_plantHistory       = new Frag_plantHistory();

        // Set internal location to store all files, adding a subfolder called "media"
        File ext_folder = this.getFilesDir();
        File storage_loc = new File(ext_folder, "media");
        storage_loc.mkdir();
        ROOT_STORAGE_LOCATION =  storage_loc.getAbsolutePath() + "/";
        // Set plant photo storage within root storage location
        File photo_loc = new File(storage_loc, "plant_photos");
        photo_loc.mkdir();
        PLANT_PHOTO_STORAGE_LOCATION = photo_loc.getAbsolutePath() + "/";

        //Get/Set Default Fall and Spring Plant Date Shared Preferences
        int year = Calendar.getInstance().get(Calendar.YEAR);
        pref = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        lastSpringFrostDate = pref.getString("Spring", "04/30/" + year);
        firstFallFrostDate = pref.getString("Fall", "10/09/" + year);

        // Update Local Plant List Variable
        editTransaction("GetPlantList", null);

        //Replace to first fragment
        changeFragment("MainMenu");
    }



//METHODS ==========================================================================================
    public void changeFragment(String menuFragment) {
        switch (menuFragment) {
            case "MainMenu": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO MAINMENU");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_mainMenu).commit();
            } break;

            case "PlantDate": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO PLANTDATE");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_plantDate).commit();
            } break;

            case "PlantHistory": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO PLANTHISTORY");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_plantHistory).commit();
            } break;

            case "PlantInfo": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO PLANTINFO");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_plantInfo).commit();
            } break;
            case "Settings": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO SETTINGS");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_settings).commit();
            } break;

            case "SettingsAddPlants": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO SETTINGSADDPLANTS");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_settingsAddPlants).commit();
            } break;

            default: {
                //Toast Error Information
                makeToast("[ERROR] Menu parameter passed was not found, returning to main menu...");
                System.out.println("[ERROR] Menu parameter passed was not found, returning to main menu...\n");
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_mainMenu).commit();
            }
        }
    }

    public void makeToast(String Message) {
        Toast toast = Toast.makeText(this, Message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    //Resets Database to Default and Resets frost dates in Shared preferences.
    public void resetPlantDB(){
        //Resets Frost Dates
        int year = Calendar.getInstance().get(Calendar.YEAR);
        setFirstFallFrostDate(parseDateString("10/09/" + year));
        setLastSpringFrostDate(parseDateString("04/30/" + year));

        //Delete Database File
        File db = new File (Main_Window.DATABASE_DIRECTORY, Main_Window.DB_NAME);
        db.delete();

        //Recreate database
        editTransaction("GetPlantList", null);
    }

    // Helper method to convert passed String in MM/dd/yyyy format to Date
    public Date parseDateString(String xDate){
        Date date = null;
        try {
            date = dateFormat.parse(xDate);
        } catch (ParseException e){
            e.printStackTrace();
            try {
                date = dateFormat.parse("01/01/2020");
            } catch (ParseException e2){
                e2.printStackTrace();
            }
        }
        return date;
    }


//GET AND SET METHODS ==============================================================================
    public List<Plant> getPlantList() {
        return PlantList;
    }

    /*
        Method to call AsyncTask to interact with the database, but wait until the AsyncTask
        finishes before moving on in the main thread (hence variable isAsyncTaskRunning).
     */
    public long editTransaction(String xTransactionType, Plant xPlant){
        System.out.println("--------------------------------------------------------------------------\r\neditTransaction() id PRE " + id);

        // Start Transaction
        new DatabaseTransaction(xTransactionType, xPlant).execute();

        // Wait until transaction is done
        while(isAsyncTaskRunning){
            try {
                Thread.sleep(50);
                System.out.println("editTransaction() AsyncTask Running Check: waiting.........");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Update User
        if (xPlant != null){
            makeToast(xPlant.getPlantName() + " Transaction Complete");
        }

        System.out.println("editTransaction() id POST " + id);
        return id;
    }

    public Date getLastSpringFrostDate() {
        return parseDateString(lastSpringFrostDate);
    }

    public void setLastSpringFrostDate(Date xLastSpringFrostDate) {
        // Save to local variable
        this.lastSpringFrostDate = dateFormat.format(xLastSpringFrostDate);
        // Save to shared preferences
        editor = pref.edit();
        editor.putString("Spring", lastSpringFrostDate);
        editor.apply();
    }

    public Date getFirstFallFrostDate() {
        return parseDateString(firstFallFrostDate);
    }

    public void setFirstFallFrostDate(Date xFirstFallFrostDate) {
        // Save to local variable
        this.firstFallFrostDate = dateFormat.format(xFirstFallFrostDate);
        // Save to shared preferences
        editor = pref.edit();
        editor.putString("Fall", firstFallFrostDate);
        editor.apply();
    }

    public Date getUserInputDate() {
        return userInputDate;
    }

    public void setUserInputDate(Date userInputDate) {
        this.userInputDate = userInputDate;
    }

//ASYNC TASK =======================================================================================
    /*
        An AsyncTask to update Main_Window's local PlantList variable from the database.
        Creates the database file using default plant list if one doesn't exist.

        This Async Task is its own class in order to utilize onPreExecute and onPostExecute to add
        a loading box so the app will wait while updating plants.
   */
    public class DatabaseTransaction extends AsyncTask {

        ProgressDialog progress;
        String transactionType;
        Plant plant;

        public DatabaseTransaction(String xTransactionType, Plant xPlant) {
            super();
            transactionType = xTransactionType;
            plant = xPlant;
            progress = new ProgressDialog(Main_Window.this);
            id = 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("DatabaseTransaction.onPreExecute() Called");
            progress.setTitle("Loading Default Plants");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            isAsyncTaskRunning = true;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            // Make User Wait
            System.out.println("DatabaseTransaction.doInBackground() Called");


            switch (transactionType){

                case ("InsertPlant"): {
                    System.out.println("doInBackground() Inserting Plant");
                    id = PlantDatabase.getInstance(getApplicationContext()).plantDao().insertPlant(plant);
                    System.out.println("doInBackground() plant id " + id);
                } break;

                case ("UpdatePlant"): {
                    PlantDatabase.getInstance(getApplicationContext()).plantDao().updatePlant(plant);
                } break;

                case ("DeletePlant"): {
                    PlantDatabase.getInstance(getApplicationContext()).plantDao().deletePlant(plant);
                } break;

                case ("GetPlantList"): {
                    System.out.println("doInBackground() Getting Plant Array List");
                } break;
            }

            //Get plants
            PlantList = PlantDatabase.getInstance(Main_Window.this).plantDao().getAllPlants();
            isAsyncTaskRunning = false;

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("DatabaseTransaction.onPostExecute() Called");
            // Dismiss the dialog
            progress.dismiss();
        }
    }
}


