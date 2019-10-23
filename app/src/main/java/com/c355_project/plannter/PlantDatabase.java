package com.c355_project.plannter;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;

@Database(entities = Plant.class, exportSchema = false, version = 1)
public abstract class PlantDatabase extends RoomDatabase {
    private static final String DB_NAME = "plant_db";
    private static PlantDatabase instance;

    public static synchronized PlantDatabase getInstance(Context context){
        File file = new File("./data/data/com.c355_project.plannter/databases/" + DB_NAME);

        if (file.exists()){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlantDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            return instance;
        } else {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PlantDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            instance.plantDao().insertPlant(new Plant("Broccoli"));
            return instance;
        }
    }
    public abstract PlantDao plantDao();
}
