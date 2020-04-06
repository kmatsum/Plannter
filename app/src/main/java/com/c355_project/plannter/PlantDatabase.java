package com.c355_project.plannter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Database(entities = {Plant.class}, exportSchema = false, version = 12)

public abstract class PlantDatabase extends RoomDatabase {
    //Method that Returns the information for the Database Instance
    public static synchronized PlantDatabase getInstance (Context context) {
        //Define Variables
        PlantDatabase databaseInstance;
        String DatabaseFilePath = "./data/data/" + R.class.getPackage().getName() + "/databases/", DB_NAME = "plant_db";

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

            //Insert default plants ================================================================

            //Create all photos
            String[] filePaths = createPhotos(context);
            
            String seedCompany = "General";

            databaseInstance.plantDao().insertPlant(new Plant("Beets", seedCompany,
                    4, 6, 4, 52,
                    12, "General guidelines for Beets.", filePaths[0],
                    true, false, 18, .5));

            databaseInstance.plantDao().insertPlant(new Plant("Broccoli", seedCompany,
                    3, 8, 4, 8,
                    12,"General guidelines for Broccoli." +
                            "\nProtect from head during Fall crop.", filePaths[1],
                    false, false, 30, 0));

            databaseInstance.plantDao().insertPlant(new Plant("Cabbage", seedCompany,
                    5, 8, 4, 10,
                    14,"General guidelines for Cabbage." +
                            "\nProtect from head during Fall crop.", filePaths[2],
                    false, false, 30, 0));

            databaseInstance.plantDao().insertPlant(new Plant("Carrots", seedCompany,
                    2, 10, 4, 52,
                    13,"General guidelines for Carrots." +
                            "\nProtect from head during Fall crop.", filePaths[3],
                    true, false, 24, .24));

            databaseInstance.plantDao().insertPlant(new Plant("Cauliflower", seedCompany,
                    5, 10, 4, 10,
                    13,"General guidelines for Cauliflower." +
                            "\nProtect from head during Fall crop.", filePaths[4],
                    false, false, 30, 0));

            databaseInstance.plantDao().insertPlant(new Plant("Chard", seedCompany,
                    2, 6, 4, 52,
                    13,"General guidelines for Chard.", filePaths[5],
                    true, false, 18, .5));

            databaseInstance.plantDao().insertPlant(new Plant("Cucumbers", seedCompany,
                    -3, 6, 5, 1,
                    14,"General guidelines for Cucumbers.", filePaths[6],
                    false, true, 60, 1));

            databaseInstance.plantDao().insertPlant(new Plant("Green Beans - Bush", seedCompany,
                    -2, 6, 3, 52,
                    12,"General guidelines for Green Beans - Bush.", filePaths[7],
                    true, false, 24, 1));

            databaseInstance.plantDao().insertPlant(new Plant("Lettuce - Leaf", seedCompany,
                    3, 5, 4, 6,
                    9,"General guidelines for Lettuce - Leaf.", filePaths[8],
                    true, false, 18, 0.1875));

            databaseInstance.plantDao().insertPlant(new Plant("Melons", seedCompany,
                    -3, 10, 3, 1,
                    15,"General guidelines for Melons.", filePaths[9],
                    false, true, 72, 1));

            databaseInstance.plantDao().insertPlant(new Plant("Okra", seedCompany,
                    -5, 7, 4, -1,
                    14,"General guidelines for Okra.", filePaths[10],
                    true, false, 40, 1));

            databaseInstance.plantDao().insertPlant(new Plant("Onion - Sets", seedCompany,
                    7, 10, 6, 52,
                    52,"General guidelines for Onion - sets." +
                            "\nNot recommended for Fall crop.", filePaths[11],
                    true, false, 18, 1));

            databaseInstance.plantDao().insertPlant(new Plant("Peas", seedCompany,
                    6, 8, 4, 52,
                    13,"General guidelines for Peas." +
                            "\nProtect from head during Fall crop.", filePaths[12],
                    true, false, 30, 1.5));

            databaseInstance.plantDao().insertPlant(new Plant("Peppers", seedCompany,
                    -4, 8, 4, 4,
                    16,"General guidelines for Peppers.", filePaths[13],
                    false, false, 30, 0));

            databaseInstance.plantDao().insertPlant(new Plant("Potatoes", seedCompany,
                    4, 10, 4, 52,
                    15,"General guidelines for Potatoes.", filePaths[14],
                    true, false, 36, 3));

            databaseInstance.plantDao().insertPlant(new Plant("Pumpkins", seedCompany,
                    -5, 10, 3, -2,
                    14,"General guidelines for Pumpkins.", filePaths[15],
                    false, true, 72, 1));

            databaseInstance.plantDao().insertPlant(new Plant("Radish/Turnip", seedCompany,
                    5, 4, 3, 52,
                    10,"General guidelines for Radish/Turnip.", filePaths[16],
                    true, false, 18, .5));

            databaseInstance.plantDao().insertPlant(new Plant("Spinach", seedCompany,
                    6, 5, 3, 52,
                    8,"General guidelines for Spinach.", filePaths[17],
                    true, false, 18, .5));

            databaseInstance.plantDao().insertPlant(new Plant("Squash - Summer", seedCompany,
                    -3, 6, 4, 1,
                    14,"General guidelines for Squash - Summer.", filePaths[18],
                    false, true, 48, .75));

            databaseInstance.plantDao().insertPlant(new Plant("Sweet Corn", seedCompany,
                    -2, 10, 3, 52,
                    15,"General guidelines for Sweet Corn.", filePaths[19],
                    false, false, 30, 2));

            databaseInstance.plantDao().insertPlant(new Plant("Tomatoes", seedCompany,
                    -4, 9, 4, 4,
                    17,"General guidelines for Tomatoes.", filePaths[20],
                    false, false, 40, 0));

            return databaseInstance;
        }
    }

    //Static method to clear media directory and create all default files
    //Only be run once per database initialization
    public static String[] createPhotos(Context context){
        String[] filePaths = new String[21];
        int[] drawableIds = {R.drawable.beets, R.drawable.broccoli, R.drawable.cabbage,
                            R.drawable.carrot, R.drawable.cauliflower, R.drawable.chard,
                            R.drawable.cucumber, R.drawable.greenbeans, R.drawable.lettuce,
                            R.drawable.melon, R.drawable.okra, R.drawable.onion,
                            R.drawable.peas, R.drawable.peppers, R.drawable.potato,
                            R.drawable.pumpkins, R.drawable.radish, R.drawable.spinach,
                            R.drawable.squash, R.drawable.corn, R.drawable.tomato
        };

        //Create directories in which to store photos
        Bitmap photo;
        for (int i = 1; i <= 21; i++){
            File f = new File(Main_Window.PLANT_PHOTO_STORAGE_LOCATION, String.valueOf(i));
            f.mkdir();
            filePaths[i-1] = f.getAbsoluteFile() + "/photo.jpg";
            photo = ((BitmapDrawable) ResourcesCompat.getDrawable(context.getResources(), drawableIds[i-1], null)).getBitmap();
            // Export photo in storage location
            try (FileOutputStream out = new FileOutputStream(filePaths[i-1])) {
                photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePaths;
    }

    //Constructor: [ESSENTIAL CODE]
    public abstract PlantDao plantDao();
}
