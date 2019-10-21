package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
    }



    public void toMainMenu (View view) {
        Intent toMainMenu = new Intent(getApplicationContext(), Main_Menu.class);
        startActivity(toMainMenu);
    }
}

