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
    long insertPlant(Plant plant);
    @Delete
    void deletePlant(Plant plant);
    @Update
    void updatePlant(Plant plant);
    @Query("SELECT * FROM Plant ORDER BY PlantName")
    List<Plant> getAllPlants();
}
