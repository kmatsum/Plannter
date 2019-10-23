package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Main_Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }



    public void menuItemClicked (View view) {
        switch (view.getId()) {
            case (R.id.btnPlantByDate): {
                Intent openPlantDate = new Intent(getApplicationContext(), Plant_Date_Screen.class);
                startActivity(openPlantDate);
            } break;

            case (R.id.btnPlantCrop): {
                //Put the intent code for PlantCrop Here
                Intent openPlantCrop;
            } break;

            case (R.id.btnPlantInfo): {
                //Put the intent code needed for PlantInfo Screen Here
                Intent openPlantInfo = new Intent(getApplicationContext(), Plant_Info.class);
                startActivity(openPlantInfo);
            } break;

            //Used for handling exceptions on if the given ViewID and the expected ViewID does not match
            default: {
                //Toast Error Information
                MakeToast("[ERROR] Menu request did not function correctly, try again!");
            }
        }
    }
    public void MakeToast(String Message)
    {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }
}
