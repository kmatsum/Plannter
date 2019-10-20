package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start_Screen extends AppCompatActivity {
Button btnPlantDate;
Button btnPlantCrop;
Button btnCropInfo;
Intent openPlantDateScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__screen);

        btnPlantDate = findViewById(R.id.btnPlantDate);
        btnPlantCrop = findViewById(R.id.btnPlantCrop);
        btnCropInfo = findViewById(R.id.btnCropInfo);

        btnPlantDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlantDateScreen = new Intent(getApplicationContext(), Plant_Date_Screen.class);
                startActivity(openPlantDateScreen);
            }
        });

        btnPlantCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnCropInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
