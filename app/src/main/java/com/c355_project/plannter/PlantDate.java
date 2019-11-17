package com.c355_project.plannter;

import android.view.Gravity;
import android.widget.Toast;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity()
public class PlantDate {

    /* VARIABLE DESCRIPTIONS =======================================================================

    id:                     Auto-generated primary key (starts at 1, increments by 1).
    year:                   Year to be stored as YYYY.
    month:                  Month to be stored as MM.
    day:                    Day to be stored as dd.
    title:                  Type of date, such as Spring Frost and Fall Frost.

    ============================================================================================= */

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int year;
    private int month;
    private int day;
    private String title;

    public PlantDate(int year, int month, int day, String title) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Function to return the object in a date format
    public Date getDate(){
        //Date Formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(this.month + "/" + this.day + "/" + this.year);
        } catch (ParseException e){
            e.printStackTrace();
            try {
                date = dateFormat.parse("1/1/2019");
            } catch (ParseException e2){
                e2.printStackTrace();
            }
        }
        return date;
    }
}


