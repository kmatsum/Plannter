package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void toMainMenu (View view) {
        Intent toMainMenu = new Intent(getApplicationContext(), Start_Screen.class);
        startActivity(toMainMenu);
    }
}

