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
            String seedCompany = "General";
            instance.plantDao().insertPlant(new Plant("Onion - Sets", seedCompany,
                    6, 4, 6, 52,
                    "General guidelines for Onion - sets."));
            instance.plantDao().insertPlant(new Plant("Peas", seedCompany,
                    5, 3, 4, 52,
                    "General guidelines for Peas."));
            instance.plantDao().insertPlant(new Plant("Spinach", seedCompany,
                    5, -1, 3, 52,
                    "General guidelines for Spinach."));
            instance.plantDao().insertPlant(new Plant("Cabbage", seedCompany,
                    4, 4, 4, 9,
                    "General guidelines for Cabbage."));
            instance.plantDao().insertPlant(new Plant("Cauliflower", seedCompany,
                    4, 6, 4, 9,
                    "General guidelines for Cauliflower."));
            return instance;
        }
    }
    public abstract PlantDao plantDao();
}
