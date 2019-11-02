package com.c355_project.plannter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Plant_Harvest_Screen extends AppCompatActivity {

    //Variables
    TextView txtDisplayDate;
    TextView txtDisplayCrops;
    String Day;
    String Month;
    String Year;
    Button btnBack;
    String concatMonthAndDay;
    int monthAndDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_harvest_screen);

        txtDisplayDate = findViewById(R.id.txtDisplayDate);
        txtDisplayCrops = findViewById(R.id.txtDisplayCrops);
        btnBack = findViewById(R.id.btnGoBack);
        Day = getIntent().getStringExtra("Day");
        Month = getIntent().getStringExtra("Month");
        Year = getIntent().getStringExtra("Year");
        txtDisplayDate.setText(Month + "/" + Day + "/" + Year);
        concatMonthAndDay = Month + Day;
        monthAndDay = Integer.parseInt(concatMonthAndDay);

        if (monthAndDay >= 719 & monthAndDay < 726)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("7/19-7/25"));
        }
        else if (monthAndDay >= 726 & monthAndDay <=731)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("7/26-7/30"));
        }
        else if (monthAndDay == 81)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("8/1"));
        }
        else if (monthAndDay >= 82 & monthAndDay < 89)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("8/2-8/8"));
        }
        else if (monthAndDay == 89)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("8/9"));
        }
        else if (monthAndDay >= 810 & monthAndDay < 816)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("8/10-8/15"));
        }
        else if (monthAndDay >= 816 & monthAndDay < 823)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("8/16-8/22"));
        }
        else if (monthAndDay >= 823 & monthAndDay < 830)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("8/23-8/29"));
        }
        else if (monthAndDay == 830)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("8/30"));
        }
        else if (monthAndDay == 91 || monthAndDay == 92 || monthAndDay == 93 || monthAndDay == 94 || monthAndDay == 95)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("9/1-9/5"));
        }
        else if (monthAndDay == 96 || monthAndDay == 97 || monthAndDay == 98 || monthAndDay == 99 || monthAndDay == 910 || monthAndDay == 911 || monthAndDay == 912)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("9/6-9/12"));
        }
        else if(monthAndDay == 913)
        {
            txtDisplayCrops.setText(getIntent().getStringExtra("9/13"));
        }
        else
        {
            makeToast("No Plants are able to be harvested at this time");
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBack = new Intent(getApplicationContext(), Plant_Date_Screen.class);
                startActivity(goBack);
            }
        });
    }



//METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }
}
