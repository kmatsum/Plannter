package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
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
    public static String ROOT_MEDIA_LOCATION,
            TEMP_MEDIA_LOCATION,
            PLANT_MEDIA_LOCATION,
            LOG_MEDIA_LOCATION,
            DB_NAME = "plannter.db",
            DATABASE_DIRECTORY = "./data/data/" + BuildConfig.APPLICATION_ID + "/databases/";

    //Date Formatter
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    //Fragments
    Frag_mainMenu           Frag_mainMenu;
    Frag_settings           Frag_settings;
    Frag_addPlants          Frag_addPlants;
    Frag_plantInfo          Frag_plantInfo;
    Frag_plantDate          Frag_plantDate;
    Frag_plantLog           Frag_plantLog;
    Frag_addNotes           Frag_addNotes;
    Frag_logNote            Frag_logNote;

    //Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //LastFrostDate
    String lastSpringFrostDate;
    String firstFallFrostDate;

    //Public Object Lists
    List<Plant> PlantList;
    List<Log>   LogList;

    //PlantHarvest
    Date userInputDate;

    // currLog
    /*  This variable is set when the note button is clicked on a specific log. It is only
        available to modify via get and set methods. Every time it is updated, the corresponding
        currLogNoteList is also updated (and only accessible via get and set methods).
     */

    private Log currLog;
    private List<Note>  currLogNoteList;
    MediaPlayer player;

//Lifecycle Methods ================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        //Fragment Instantiation
        Frag_mainMenu           = new Frag_mainMenu();
        Frag_settings           = new Frag_settings();
        Frag_addPlants          = new Frag_addPlants();
        Frag_plantInfo          = new Frag_plantInfo();
        Frag_plantDate          = new Frag_plantDate();
        Frag_plantLog       = new Frag_plantLog();
        Frag_logNote            = new Frag_logNote();
        Frag_addNotes           = new Frag_addNotes();

        // Set internal location to store all files, adding a subfolder called "media"
        File ext_folder = this.getFilesDir();
        File media_loc = new File(ext_folder, "media");
        media_loc.mkdir();
        ROOT_MEDIA_LOCATION =  media_loc.getAbsolutePath() + "/";

        // Set internal location to store temp media files
        File temp_loc = new File(media_loc, "Temp");
        temp_loc.mkdir();
        TEMP_MEDIA_LOCATION =  temp_loc.getAbsolutePath() + "/";

        // Set plant storage within root storage location
        File plant_loc = new File(media_loc, "Plants");
        plant_loc.mkdir();
        PLANT_MEDIA_LOCATION = plant_loc.getAbsolutePath() + "/";

        // Set plant storage within root storage location
        File log_loc = new File(media_loc, "Logs");
        log_loc.mkdir();
        LOG_MEDIA_LOCATION = log_loc.getAbsolutePath() + "/";

        //Get/Set Default Fall and Spring Plant Date Shared Preferences
        int year = Calendar.getInstance().get(Calendar.YEAR);
        pref = getSharedPreferences("PREF", Context.MODE_PRIVATE);
        lastSpringFrostDate = pref.getString("Spring", "04/30/" + year);
        firstFallFrostDate = pref.getString("Fall", "10/09/" + year);

        // Update Local List Variables
        editTransaction("UpdateAllLists", null);

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

            case "PlantLog": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO PLANTLOGS");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_plantLog).commit();
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

            case "LogNote": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO LOGNOTE");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_logNote).commit();
            } break;

            case "AddPlants": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO ADDPLANTS");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_addPlants).commit();
            } break;

            case "AddNotes": {
                System.out.println("=============================================================");
                System.out.println("SWITCH THE FRAGMENT TO ADDNOTES");
                System.out.println("=============================================================");

                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_addNotes).commit();
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

        //Reset Frost Dates
        int year = Calendar.getInstance().get(Calendar.YEAR);
        setFirstFallFrostDate(parseDateString("10/09/" + year));
        setLastSpringFrostDate(parseDateString("04/30/" + year));

        //Delete everything in Plant and Log folders
        deleteDirectory(new File(PLANT_MEDIA_LOCATION));
        deleteDirectory(new File(LOG_MEDIA_LOCATION));

        //Recreate Plant and Log folders
        new File(PLANT_MEDIA_LOCATION).mkdir();
        new File(LOG_MEDIA_LOCATION).mkdir();

        //Delete Database File
        this.getApplicationContext().deleteDatabase(DB_NAME);

        //Recreate database
        editTransaction("UpdateAllLists", null);
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

    // Method to delete passed folder and all files in passed folder
    // Internal files must be deleted first before the folder can be deleted
    public void deleteDirectory(File directoryToBeDeleted){
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
        // Update Console
        System.out.println("Main_Window FOLDER OR FILE DELETED: " + directoryToBeDeleted.getAbsolutePath());
    }


//GET AND SET METHODS ==============================================================================

    /*
        Method to call AsyncTask to interact with the database, but wait until the AsyncTask
        finishes before moving on in the main thread (hence variable isAsyncTaskRunning).
     */
    public void editTransaction(String xTransactionType, Object xObject){
        // Start Transaction
        new DatabaseTransaction(xTransactionType, xObject).execute();
    }

    public Log getCurrLog() {
        return currLog;
    }

    public void setCurrLog(Log currLog) {
        this.currLog = currLog;
        // Update currLogNoteList
        editTransaction("GetNotesForCurrLog", null);
    }

    public List<Note> getCurrLogNoteList() {
        return currLogNoteList;
    }

    public void setCurrLogNoteList(List<Note> currLogNoteList) {
        this.currLogNoteList = currLogNoteList;
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

    public void playAudio(String filePath){
        // Reset/create the Media Player
        stopAudio();
        player = new MediaPlayer();
        try {
            player.setDataSource(filePath);
            player.setLooping(false);
            player.prepare();
            player.start();
        } catch (Exception e){
            System.out.println("Main_Window - playAudio Exception:");
            makeToast("Error reading the recording.");
            return;
        }
    }
    public void stopAudio(){
        try {
            // Check that the player isn't null and is currently playing
            if (player != null && player.isPlaying()){
                // Stop the player
                player.stop();
                player.release();
            }
            player = null;
        } catch (Exception e){
            System.out.println("Main_Window - stopAudio Exception:");
            e.printStackTrace();
            return;
        }
    }

//ASYNC TASK =======================================================================================
    /*
        An AsyncTask to update Main_Window's local List variables from the database.
        Creates the database file using default plant list if one doesn't exist.

        This Async Task is its own class in order to utilize onPreExecute and onPostExecute to add
        a loading box so the app will wait while updating plant/log/note lists.
   */

    public class DatabaseTransaction extends AsyncTask {

        ProgressDialog progress;
        String transactionType;
        Object object;

        public DatabaseTransaction(String xTransactionType, Object xObject) {
            super();
            transactionType = xTransactionType;
            object = xObject;
            progress = new ProgressDialog(Main_Window.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Make user wait
            progress.setTitle("Loading " + transactionType + " Transaction.");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            System.out.println("\r\n=============================================================");
            System.out.println("\r\nDATABASE TRANSACTION BEGINNING");
            switch (transactionType){
                case ("InsertPlant"): {

                    // Get plant photo
                    Bitmap photo = Frag_addPlants.photo;

                    // Call DAO to insert plant
                    PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().insertPlant((Plant) object, photo);

                    // Update Frag_addPlants class photo variable to null
                    // This is required as the fragment is never recycled
                    Frag_addPlants.photo = null;
                    Main_Window.this.changeFragment("PlantInfo");

                } break;

                case ("DeletePlant"): {
                    // Call DAO to delete plant
                    PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().deletePlant((Plant) object);
                } break;

                case ("InsertLog"): {
                    // Call DAO to insert log
                    PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().insertLog((Log) object);
                } break;

                case ("DeleteLog"): {
                    // Call DAO to delete log
                    PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().deleteLog((Log) object);
                } break;

                case ("InsertNote"): {
                    // Parse note
                    Note note = (Note) object;

                    // If IMAGE Note (must pass photo from Frag_addNotes)
                    if (note.getNoteType() == "Image"){

                        // Get note photo
                        Bitmap noteImage = Frag_addNotes.noteImage;

                        // Call DAO to insert note
                        PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().insertNote(note, noteImage);
                    }

                    // Else (it must be SIMPLE or AUDIO), and neither need a passed photo
                    else {
                        // Call DAO to insert note
                        PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().insertNote(note, null);
                    }

                    // Update currLogNoteList
                    setCurrLogNoteList(PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().getAllNotesForLog(currLog));

                    // Update Frag_addNotes class noteImage variable to null
                    // This is required as the fragment is never recycled
                    Frag_addNotes.noteImage = null;

                    changeFragment("LogNote");
                } break;

                case ("DeleteNote"): {
                    // Call DAO to delete note
                    PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().deleteNote((Note) object);
                    // Update currLogNoteList
                    setCurrLogNoteList(PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().getAllNotesForLog(currLog));
                } break;

                case ("GetNotesForCurrLog"): {
                    // Call DAO to get List of Notes
                    setCurrLogNoteList(PlannterDatabase.getInstance(getApplicationContext()).plannterDatabaseDao().getAllNotesForLog(currLog));
                } break;

                case ("UpdateAllLists"): {
                    System.out.println("doInBackground() Updating Plant, Log, and Note Lists");
                } break;

            }

            // Clear temp directory
            File tempDir = new File (Main_Window.TEMP_MEDIA_LOCATION);
            if (tempDir.isDirectory()){
                deleteDirectory(tempDir);
                tempDir.mkdir();
            }

            // Update All Lists
            PlantList = PlannterDatabase.getInstance(Main_Window.this).plannterDatabaseDao().getAllPlants();
            LogList = PlannterDatabase.getInstance(Main_Window.this).plannterDatabaseDao().getAllLogs();

            System.out.println("\r\nDATABASE TRANSACTION ENDING");
            System.out.println("\r\n=============================================================");

            return null;
        }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        // Dismiss the dialog
        progress.dismiss();
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


