package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class Main_Window extends AppCompatActivity {
//VARIABLES ========================================================================================

    //Date Formatter
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    //Fragments
    Frag_mainMenu           Frag_mainMenu;
    Frag_settings           Frag_settings;
    Frag_settingsAddPlants  Frag_settingsAddPlants;
    Frag_plantInfo          Frag_plantInfo;
    Frag_plantDate          Frag_plantDate;
    Frag_plantHarvest       Frag_plantHarvest;
    Frag_dateByPlant Frag_dateByPlant;

    //LastFrostDate
    PlantDate lastSpringFrostDate;
    PlantDate firstFallFrostDate;

    //Plant List
    List<Plant> PlantList;
    List<Plant> harvestableCrops;

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
        Frag_plantHarvest       = new Frag_plantHarvest();
        Frag_dateByPlant = new Frag_dateByPlant();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                    //Get plants
                    PlantList = PlantDatabase.getInstance(getApplicationContext()).plantDao().getAllPlants();
                    //harvestableCrops = PlantDatabase.getInstance(getApplicationContext()).plantDao().getPlantName(getWeeksTilHarvest());
                    //Get plant dates
                    lastSpringFrostDate = PlantDatabase.getInstance(getApplicationContext()).plantDao().getSpringFrostDate();
                    firstFallFrostDate = PlantDatabase.getInstance(getApplicationContext()).plantDao().getFallFrostDate();

                //[DEBUG] Print all the plant names
                    System.out.println("------------------------------------");
                    for (int i = 0; i < PlantList.size(); i++){
                        System.out.println(PlantList.get(i).getPlantName());
                    }
                    System.out.println("------------------------------------");

                    //[DEBUG] Print all the plant dates
                    System.out.println("------------------------------------");
                    System.out.println(lastSpringFrostDate.getDate().toString());
                    System.out.println(firstFallFrostDate.getDate().toString());
                    System.out.println("------------------------------------");

            }
        });

        //Replace to first fragment
        changeFragment("MainMenu");
    }



//METHODS ==========================================================================================
    public void changeFragment(String menuFragment) {
        switch (menuFragment) {
            case "MainMenu": {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_mainMenu).commit();
            } break;

            case "PlantDate": {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_plantDate).commit();
            } break;

            case "PlantByPlant": {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_dateByPlant).commit();
            } break;

            case "PlantInfo": {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_plantInfo).commit();
            } break;

            case "PlantHarvest": {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_plantHarvest).commit();
            } break;

            case "Settings": {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_settings).commit();
            } break;

            case "SettingsAddPlants": {
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



//GET AND SET METHODS ==============================================================================
    public List<Plant> getPlantList() {
        return PlantList;
    }

    public void setPlantList(List<Plant> xPlantList) {
        PlantList = xPlantList;
        /*TODO
        Here we will use a PLANT object parameter, then have the DATABASE INSTANCE insert that plant
        object. Then we will update the PLANTLIST with the whole database.
         */
    }

    public Date getLastSpringFrostDate() {
        return lastSpringFrostDate.getDate();
    }

    public void setLastSpringFrostDate(Date xLastSpringFrostDate) {
        this.lastSpringFrostDate.setDate(xLastSpringFrostDate);

        //Open Thread to update the Database
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //Set the database using the Date object parameter
                PlantDatabase.getInstance(getApplicationContext()).plantDao().updatePlant(lastSpringFrostDate);

                //[DEBUG] Print all the plant dates
                System.out.println("------------------------------------");
                System.out.println(lastSpringFrostDate.getDate().toString());
                System.out.println("------------------------------------");
            }
        });
    }

    public Date getFirstFallFrostDate() {
        return firstFallFrostDate.getDate();
    }

    public void setFirstFallFrostDate(Date xFirstFallFrostDate) {
        this.firstFallFrostDate.setDate(xFirstFallFrostDate);

        //Open Thread to update the Database
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //Set the database using the Date object parameter
                PlantDatabase.getInstance(getApplicationContext()).plantDao().updatePlant(firstFallFrostDate);

                //[DEBUG] Print all the plant dates
                System.out.println("------------------------------------");
                System.out.println(firstFallFrostDate.getDate().toString());
                System.out.println("------------------------------------");
            }
        });
    }

    public Date getUserInputDate() {
        return userInputDate;
    }

    public void setUserInputDate(Date userInputDate) {
        this.userInputDate = userInputDate;
    }

}


