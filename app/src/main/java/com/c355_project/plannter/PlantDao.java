package com.c355_project.plannter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface PlantDao {
    @Insert
    void insertPlantDate(PlantDate plantDate);
    @Insert
    void insertPlant(Plant plant);
//    @Update
//    void updatePlant(Plant plant);
//    @Delete
//    void deletePlant(Plant plant);
    @Query("SELECT * FROM Plant")
    List<Plant> getAllPlants();
    @Query("SELECT * FROM PlantDate")
    List<PlantDate> getAllPlantDates();
    @Query("SELECT * FROM PlantDate WHERE title = 'springFrostDate'")
    PlantDate getSpringFrostDate();
    @Query("SELECT * FROM PlantDate WHERE title = 'fallFrostDate'")
    PlantDate getFallFrostDate();
}
