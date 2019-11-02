package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.MobileAds;




public class Start_Menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        MobileAds.initialize(this);
    }



//CLICK METHOD =====================================================================================
    public void toMainMenu (View view) {
        Intent toMainMenu = new Intent(getApplicationContext(), Main_Window.class);
        startActivity(toMainMenu);
    }
}



//METHODS ==========================================================================================



//How to insert values into the db during runtime
//===============================================

//import android.os.AsyncTask;
//AsyncTask.execute(new Runnable() {
//@Override
//public void run() {
//        //Insert a plant
//        PlantDatabase.getInstance(getApplicationContext()).plantDao().insertPlant(new Plant("Potato"));
//        PlantDatabase.getInstance(getApplicationContext()).plantDao().insertPlant(new Plant("Tomato"));
//        PlantDatabase.getInstance(getApplicationContext()).plantDao().insertPlant(new Plant("Carrot"));
//        PlantDatabase.getInstance(getApplicationContext()).plantDao().insertPlant(new Plant("Parsley"));
//
//        //Get plants
//        List<Plant> list = PlantDatabase.getInstance(getApplicationContext()).plantDao().getAll();
//        System.out.println("------------------------------------");
//        for (int i = 0; i < list.size(); i++){
//        System.out.println(list.get(i).getPlantName());
//        }
//        System.out.println("------------------------------------");
//
//        }
//        });