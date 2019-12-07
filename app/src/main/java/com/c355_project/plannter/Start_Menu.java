package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.MobileAds;


public class Start_Menu extends AppCompatActivity {
//LIFECYCLE METHODS ================================================================================
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