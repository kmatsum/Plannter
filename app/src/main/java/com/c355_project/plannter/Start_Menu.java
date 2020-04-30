/*

Background:     This project was created as part of CNIT 35500 Software Development for Mobile
                Computers during the Fall 2019 semester. It was updated as part of CNIT 42500
                Software Development for Mobile Computers II during the Spring 2020 semester.

Authors:        Rachel Rettig, Kazushi Matsumoto, Jon Clark, and Paul Mueller

Description:    This plant planning application, known as Plannter, allows the user to look up
                general planting information (planting depth, harvest time, etc.) for a set of
                default plants. Users have the ability to add and remove plants. Users can share and
                receive custom plants over Bluetooth from peers with the same app. Users can also
                create logs to keep track of planted plants. Users can add text, audio, and image
                notes to each log.

 */

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