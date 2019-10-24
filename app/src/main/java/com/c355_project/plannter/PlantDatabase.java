package com.c355_project.plannter;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;

@Database(entities = Plant.class, exportSchema = false, version = 3)
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
            instance.plantDao().insertPlant(new Plant("Beets", seedCompany,
                    4, 6, 4, 52,
                    12,4,52,
                    "General guidelines for Beets."));
            instance.plantDao().insertPlant(new Plant("Broccoli", seedCompany,
                    3, 8, 4, 8,
                    12,5,17,
                    "General guidelines for Broccoli." +
                            "\nProtect from head during Fall crop."));
            instance.plantDao().insertPlant(new Plant("Cabbage", seedCompany,
                    5, 8, 4, 10,
                    14,5,19,
                    "General guidelines for Cabbage." +
                            "\nProtect from head during Fall crop."));
            instance.plantDao().insertPlant(new Plant("Carrots", seedCompany,
                    2, 10, 4, 52,
                    13,3,52,
                    "General guidelines for Carrots." +
                            "\nProtect from head during Fall crop."));
            instance.plantDao().insertPlant(new Plant("Cauliflower", seedCompany,
                    5, 10, 4, 10,
                    13,3,18,
                    "General guidelines for Cauliflower." +
                            "\nProtect from head during Fall crop."));
            instance.plantDao().insertPlant(new Plant("Chard", seedCompany,
                    2, 6, 4, 52,
                    13,8,52,
                    "General guidelines for Chard."));
            instance.plantDao().insertPlant(new Plant("Cucumbers", seedCompany,
                    -3, 6, 5, 1,
                    14,5,52,
                    "General guidelines for Cucumbers."));
            instance.plantDao().insertPlant(new Plant("Green Beans - Bush", seedCompany,
                    -2, 6, 3, 52,
                    12,3,52,
                    "General guidelines for Green Beans - Bush."));
            instance.plantDao().insertPlant(new Plant("Lettuce - Leaf", seedCompany,
                    3, 5, 4, 6,
                    9,4,52,
                    "General guidelines for Lettuce - Leaf."));
            instance.plantDao().insertPlant(new Plant("Melons", seedCompany,
                    -3, 10, 3, 1,
                    15,3,52,
                    "General guidelines for Melons."));
            instance.plantDao().insertPlant(new Plant("Okra", seedCompany,
                    -5, 7, 4, -1,
                    14,6,52,
                    "General guidelines for Okra."));
            instance.plantDao().insertPlant(new Plant("Onion - Sets", seedCompany,
                    7, 10, 6, 52,
                    52,52,52,
                    "General guidelines for Onion - sets." +
                            "\nNot recommended for Fall crop."));
            instance.plantDao().insertPlant(new Plant("Peas", seedCompany,
                    6, 8, 4, 52,
                    13,4,52,
                    "General guidelines for Peas." +
                            "\nProtect from head during Fall crop."));
            instance.plantDao().insertPlant(new Plant("Peppers", seedCompany,
                    -4, 8, 4, 4,
                    16,7,19,
                    "General guidelines for Peppers."));
            instance.plantDao().insertPlant(new Plant("Potatoes", seedCompany,
                    4, 10, 4, 52,
                    15,4,52,
                    "General guidelines for Potatoes."));
            instance.plantDao().insertPlant(new Plant("Pumpkins", seedCompany,
                    -5, 10, 3, -2,
                    14,3,52,
                    "General guidelines for Pumpkins."));
            instance.plantDao().insertPlant(new Plant("Radish/Turnip", seedCompany,
                    5, 4, 3, 52,
                    10,5,52,
                    "General guidelines for Radish/Turnip."));
            instance.plantDao().insertPlant(new Plant("Spinach", seedCompany,
                    6, 5, 3, 52,
                    8,4,52,
                    "General guidelines for Spinach."));
            instance.plantDao().insertPlant(new Plant("Squash - Summer", seedCompany,
                    -3, 6, 4, 1,
                    14,4,52,
                    "General guidelines for Squash - Summer."));
            instance.plantDao().insertPlant(new Plant("Sweet Corn", seedCompany,
                    -2, 10, 3, 52,
                    15,3,52,
                    "General guidelines for Sweet Corn."));
            instance.plantDao().insertPlant(new Plant("Tomatoes", seedCompany,
                    -4, 9, 4, 4,
                    17,7,20,
                    "General guidelines for Tomatoes."));
            return instance;
        }
    }
    public abstract PlantDao plantDao();
}
