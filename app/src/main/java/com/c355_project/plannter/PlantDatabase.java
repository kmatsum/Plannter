package com.c355_project.plannter;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.io.File;



@Database (entities = Plant.class, exportSchema = false, version = 1)
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
            
            String seedCompany = "General";
            //Populate the Database
            databaseInstance.plantDao().insertPlant(new Plant("Onion - Sets", seedCompany,
                    6, 4, 6, 52,
                    "General guidelines for Onion - sets."));
            databaseInstance.plantDao().insertPlant(new Plant("Peas", seedCompany,
                    5, 3, 4, 52,
                    "General guidelines for Peas."));
            databaseInstance.plantDao().insertPlant(new Plant("Spinach", seedCompany,
                    5, -1, 3, 52,
                    "General guidelines for Spinach."));
            databaseInstance.plantDao().insertPlant(new Plant("Cabbage", seedCompany,
                    4, 4, 4, 9,
                    "General guidelines for Cabbage."));
            databaseInstance.plantDao().insertPlant(new Plant("Cauliflower", seedCompany,
                    4, 6, 4, 9,
                    "General guidelines for Cauliflower."));
          
            return databaseInstance;
        }
    }



    //Constructor: [ESSENTIAL CODE]
    public abstract PlantDao plantDao();
}
