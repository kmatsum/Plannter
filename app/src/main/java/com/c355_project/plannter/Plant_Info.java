package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Plant_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        //Set the spinner adapter and contents
        Spinner spnrSelectPlant = findViewById(R.id.spnrSelectPlant);
        String[] PlaceHolderSpinnerDropdown = {"Carrots", "Beets"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, R.layout.spinner_item, PlaceHolderSpinnerDropdown);
        spnrSelectPlant.setAdapter(adapter);


    }
}
