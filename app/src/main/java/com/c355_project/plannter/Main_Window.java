package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class Main_Window extends AppCompatActivity {
//VARIABLES ========================================================================================

    String harvestableCrops;

    //Date Formatter
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    //Fragments
    Frag_mainMenu Frag_mainMenu;
    Frag_settings Frag_settings;
    Frag_settingsAddPlants Frag_settingsAddPlants;
    Frag_plantInfo Frag_plantInfo;
    Frag_plantDate Frag_plantDate;
    Frag_plantHarvest Frag_plantHarvest;
    Frag_plantByPlant Frag_plantByPlant;

    //LastFrostDate
    Date lastSpringFrostDate;
    Date lastFallFrostDate;

    {
        try {
            lastSpringFrostDate = dateFormat.parse("04/29/2019");
            lastFallFrostDate = dateFormat.parse("10/08/2019");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Plant List
    List<Plant> PlantList;


    //PlantHarvest
    Date userInputDate;



//Lifecycle Methods ================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        //Fragment Instantiation
        Frag_mainMenu       = new Frag_mainMenu();
        Frag_settings       = new Frag_settings();
        Frag_settingsAddPlants = new Frag_settingsAddPlants();
        Frag_plantInfo      = new Frag_plantInfo();
        Frag_plantDate      = new Frag_plantDate();
        Frag_plantHarvest   = new Frag_plantHarvest();
        Frag_plantByPlant   = new Frag_plantByPlant();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                    //Get plants
                    PlantList = PlantDatabase.getInstance(getApplicationContext()).plantDao().getAll();

                    //[DEBUG] Print all the plant names
                    System.out.println("------------------------------------");
                    for (int i = 0; i < PlantList.size(); i++){
                        System.out.println(PlantList.get(i).getPlantName());
                    }
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
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentWindow, Frag_plantByPlant).commit();
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
    }

    public Date getLastSpringFrostDate() {
        return lastSpringFrostDate;
    }

    public void setLastSpringFrostDate(Date lastSpringFrostDate) {
        this.lastSpringFrostDate = lastSpringFrostDate;
    }

    public Date getLastFallFrostDate() {
        return lastFallFrostDate;
    }

    public void setLastFallFrostDate(Date lastFallFrostDate) {
        this.lastFallFrostDate = lastFallFrostDate;
    }

    public Date getUserInputDate() {
        return userInputDate;
    }

    public void setUserInputDate(Date userInputDate) {
        this.userInputDate = userInputDate;
    }

    public String getHarvestableCrops() {
        return harvestableCrops;
    }

    public void setHarvestableCrops(String harvestableCrops) {
        this.harvestableCrops = harvestableCrops;
    }
}


