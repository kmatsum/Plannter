package com.c355_project.plannter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Plant{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String plantName;

    //CONSTRUCTORS =================================================================================
    public Plant(String plantName){
        this.plantName = plantName;
    }

    //GETS & SETS ==================================================================================
    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setPlantName(String name){
        this.plantName = name;
    }

    public String getPlantName(){
        return this.plantName;
    }
}