package com.c355_project.plannter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlantDao {

    // PLANTS ======================================================================================
    @Insert
    long insertPlant(Plant plant);
    @Delete
    void deletePlant(Plant plant);
    @Update
    void updatePlant(Plant plant);
    @Query("SELECT * FROM Plant ORDER BY PlantName")
    List<Plant> getAllPlants();

    // LOGS ========================================================================================
    @Insert
    long insertLog(Log log);
    @Delete
    void deleteLog(Log log);
    @Query("SELECT * FROM Log WHERE plantID = :plantID")
    List<Log> getAllLogsForPlant(int plantID);

    // NOTES =======================================================================================
    @Insert
    long insertNote(Note note);
    @Delete
    void deleteNote(Note note);
    @Query("SELECT * FROM Note WHERE logID = :logID")
    List<Note> getAllNotesForLog(int logID);
}
