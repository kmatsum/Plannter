package com.c355_project.plannter;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.io.File;
import java.util.Calendar;


@Database(entities = {Plant.class, PlantDate.class}, exportSchema = false, version = 8)

public abstract class PlantDatabase extends RoomDatabase {
    //Method that Returns the information for the Database Instance
    public static synchronized PlantDatabase getInstance (Context context) {
        //Define Variables
        PlantDatabase databaseInstance;
        String DatabaseFilePath = "./data/data/com.c355_project.plannter/databases/", DB_NAME = "plant_db";

        //Instantiate a File Object with the File Path for the Database
        File file = new File (DatabaseFilePath + DB_NAME);

        //Check if the File Exists with the given FilePath
        if (file.exists()) {
            //Set the instance of the database to the Database file
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), PlantDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            return databaseInstance;
        } else {
            //Set the instance of the database, but since it does not exist, it will create a new file
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    PlantDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();

            //Insert default plant dates ===========================================================
            int year = Calendar.getInstance().get(Calendar.YEAR);
            //last spring frost date
            databaseInstance.plantDao().insertPlantDate(new PlantDate(year, 4,30, "springFrostDate"));
            //first fall frost date
            databaseInstance.plantDao().insertPlantDate(new PlantDate(year, 10, 9, "fallFrostDate"));

            //Insert default plants ================================================================
            
            String seedCompany = "General";

            databaseInstance.plantDao().insertPlant(new Plant("Beets", seedCompany,
                    4, 6, 4, 52,
                    12, "General guidelines for Beets.",
                    R.drawable.beets, true, false, 18));

            databaseInstance.plantDao().insertPlant(new Plant("Broccoli", seedCompany,
                    3, 8, 4, 8,
                    12,"General guidelines for Broccoli." +
                            "\nProtect from head during Fall crop.",
                    R.drawable.broccoli, false, false, 30));

            databaseInstance.plantDao().insertPlant(new Plant("Cabbage", seedCompany,
                    5, 8, 4, 10,
                    14,"General guidelines for Cabbage." +
                            "\nProtect from head during Fall crop.",
                    R.drawable.cabbage, false, false, 30));

            databaseInstance.plantDao().insertPlant(new Plant("Carrots", seedCompany,
                    2, 10, 4, 52,
                    13,"General guidelines for Carrots." +
                            "\nProtect from head during Fall crop.",
                    R.drawable.carrot, true, false, 24));

            databaseInstance.plantDao().insertPlant(new Plant("Cauliflower", seedCompany,
                    5, 10, 4, 10,
                    13,"General guidelines for Cauliflower." +
                            "\nProtect from head during Fall crop.",
                    R.drawable.cauliflower, false, false, 30));

            databaseInstance.plantDao().insertPlant(new Plant("Chard", seedCompany,
                    2, 6, 4, 52,
                    13,"General guidelines for Chard.",
                    R.drawable.chard, true, false, 18));

            databaseInstance.plantDao().insertPlant(new Plant("Cucumbers", seedCompany,
                    -3, 6, 5, 1,
                    14,"General guidelines for Cucumbers.",
                    R.drawable.cucumber, false, true, 60));

            databaseInstance.plantDao().insertPlant(new Plant("Green Beans - Bush", seedCompany,
                    -2, 6, 3, 52,
                    12,"General guidelines for Green Beans - Bush.",
                    R.drawable.greenbeans, true, false, 24));

            databaseInstance.plantDao().insertPlant(new Plant("Lettuce - Leaf", seedCompany,
                    3, 5, 4, 6,
                    9,"General guidelines for Lettuce - Leaf.",
                    R.drawable.lettuce, true, false, 18));

            databaseInstance.plantDao().insertPlant(new Plant("Melons", seedCompany,
                    -3, 10, 3, 1,
                    15,"General guidelines for Melons.",
                    R.drawable.melon, false, true, 72));

            databaseInstance.plantDao().insertPlant(new Plant("Okra", seedCompany,
                    -5, 7, 4, -1,
                    14,"General guidelines for Okra.",
                    R.drawable.okra, true, false, 40));

            databaseInstance.plantDao().insertPlant(new Plant("Onion - Sets", seedCompany,
                    7, 10, 6, 52,
                    52,"General guidelines for Onion - sets." +
                            "\nNot recommended for Fall crop.",
                    R.drawable.onion, true, false, 18));

            databaseInstance.plantDao().insertPlant(new Plant("Peas", seedCompany,
                    6, 8, 4, 52,
                    13,"General guidelines for Peas." +
                            "\nProtect from head during Fall crop.",
                    R.drawable.peas, true, false, 30));

            databaseInstance.plantDao().insertPlant(new Plant("Peppers", seedCompany,
                    -4, 8, 4, 4,
                    16,"General guidelines for Peppers.",
                    R.drawable.peppers, false, false, 30));

            databaseInstance.plantDao().insertPlant(new Plant("Potatoes", seedCompany,
                    4, 10, 4, 52,
                    15,"General guidelines for Potatoes.",
                    R.drawable.potato, true, false, 36));

            databaseInstance.plantDao().insertPlant(new Plant("Pumpkins", seedCompany,
                    -5, 10, 3, -2,
                    14,"General guidelines for Pumpkins.",
                    R.drawable.pumpkins, false, true, 72));

            databaseInstance.plantDao().insertPlant(new Plant("Radish/Turnip", seedCompany,
                    5, 4, 3, 52,
                    10,"General guidelines for Radish/Turnip.",
                    R.drawable.radish, true, false, 18));

            databaseInstance.plantDao().insertPlant(new Plant("Spinach", seedCompany,
                    6, 5, 3, 52,
                    8,"General guidelines for Spinach.",
                    R.drawable.spinach, true, false, 18));

            databaseInstance.plantDao().insertPlant(new Plant("Squash - Summer", seedCompany,
                    -3, 6, 4, 1,
                    14,"General guidelines for Squash - Summer.",
                    R.drawable.squash, false, true, 48));

            databaseInstance.plantDao().insertPlant(new Plant("Sweet Corn", seedCompany,
                    -2, 10, 3, 52,
                    15,"General guidelines for Sweet Corn.",
                    R.drawable.corn, false, false, 30));

            databaseInstance.plantDao().insertPlant(new Plant("Tomatoes", seedCompany,
                    -4, 9, 4, 4,
                    17,"General guidelines for Tomatoes.",
                    R.drawable.tomato, false, false, 40));

            return databaseInstance;
        }
    }



    //Constructor: [ESSENTIAL CODE]
    public abstract PlantDao plantDao();
}
