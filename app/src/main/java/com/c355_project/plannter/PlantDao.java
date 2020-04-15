package com.c355_project.plannter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public abstract class PlantDao {

/*
    There are 2 types of methods, EXTERNAL and INTERNAL:

    1) EXTERNAL methods are designed to be called outside of PlantDao.java and maintain data
       integrity. These methods are public, and can be either abstract (overridden by Room due
       to an Annotation denoted by "@") or concrete (defined in this class).

    2) INTERNAL methods should only be called from PlantDao.java to maintain data integrity.
       Their method names start with an underline "_". If it is a concrete method, it is
       declared private and thus inaccessible. If it is an abstract method to be later overridden
       by Room, it is impossible to be declared private. Therefore, it is up to the coders not to
       call these methods marked by underscores outside of PlantDao.java.

 */


// PLANT METHODS ===================================================================================

    // EXTERNAL METHODS ============================================================================

    // Method to insert passed plant
    // Returns Room auto-generated plantID
    public long insertPlant(Plant plant) {
        // Insert log
        return _insertPlant(plant);
    }

    // Method to delete passed plant and corresponding logs and notes
    public void deletePlant(Plant plant){
        // Delete all corresponding logs, which will delete all corresponding notes
        _deleteAllLogsForPlant(plant);

        // Delete plant
        _deletePlant(plant);

        /*TODO: add logic for deleting corresponding plant folder*/
    }

    // Method to update passed plant
    @Update
    abstract void updatePlant(Plant plant);

    // Method to return all plants
    @Query("SELECT * FROM Plant")
    abstract List<Plant> getAllPlants();

    // INTERNAL METHODS ============================================================================

    // Method that shouldn't be called outside this class to insert a plant
    // Returns Room auto-generated plantID
    @Insert
    abstract long _insertPlant(Plant plant);

    // Method that shouldn't be called outside this class to delete a plant
    @Delete
    abstract void _deletePlant(Plant plant);

// LOG METHODS =====================================================================================

    // EXTERNAL METHODS ============================================================================

    // Method to insert passed log
    // Returns Room auto-generated logID
    public long insertLog(Log log) {
        // Insert log
        return _insertLog(log);
    }

    // Method to delete passed log and corresponding notes
    public void deleteLog(Log log){
        // Delete all corresponding notes
        _deleteAllNotesForLog(log.getLogID());

        // Delete log
        _deleteLog(log);

        /*TODO: add logic for deleting corresponding log folder*/
    }

    // Method to return all logs
    @Query("SELECT * FROM Log")
    abstract List<Log> getAllLogs();

    // Method to return all logs with a certain plantID
    @Query("SELECT * FROM Log WHERE plantID = :plantID")
    abstract List<Log> getAllLogsForPlant(int plantID);

    // Method to return all logs for passed plant
    public List<Log> getAllLogsForPlant(Plant plant){
        int plantID = plant.getPlantID();
        return getAllLogsForPlant(plantID);
    }


    // INTERNAL METHODS ============================================================================

    // Method that shouldn't be called outside this class to insert a log
    // Returns Room auto-generated logID
    @Insert
    abstract long _insertLog(Log log);

    // Method that shouldn't be called outside this class to delete a log
    @Delete
    abstract void _deleteLog(Log log);

    // Method that shouldn't be called outside this class to delete all logs for passed plant
    private void _deleteAllLogsForPlant(Plant plant){
        // Get all corresponding logs
        List<Log> plantLogs = getAllLogsForPlant(plant);
        // Delete notes
        for (Log log : plantLogs){
            deleteLog(log);
        }
    }

// NOTE METHODS ====================================================================================

    // EXTERNAL METHODS ============================================================================

    // Method to insert passed note
    // Returns noteID generated in PlantDao.java
    public long insertNote(Note note){
        // Determine what noteID should be based on logID
        List<Note> logNotes = getAllNotesForLog(note.getLogID());
        // Get the last NoteID for the current log
        int lastNoteID;
        if (logNotes.size() != 0){
            lastNoteID = logNotes.get(logNotes.size() - 1).getNoteID();
        } else {
            lastNoteID = 0;
        }
        // Set NoteID to lastNoteID incremented by 1
        note.setNoteID(lastNoteID + 1);
        // Insert note
        _insertNote(note);

        return lastNoteID + 1;
    }

    // Method to delete passed note
    public void deleteNote(Note note){
        // Delete note
        _deleteNote(note);

        /*TODO: add logic for deleting corresponding note folder*/
    }

    // Method to return all notes
    @Query("SELECT * FROM Note")
    abstract List<Note> getAllNotes();

    // Method to return all notes with a certain logID
    @Query("SELECT * FROM Note WHERE logID = :logID")
    abstract List<Note> getAllNotesForLog(int logID);

    // Method to return all notes for passed log
    public List<Note> getAllNotesForLog(Log log){
        int logID = log.getLogID();
        return getAllNotesForLog(logID);
    }

    // INTERNAL METHODS ============================================================================

    // Method that shouldn't be called outside this class to insert a note
    @Insert
    abstract void _insertNote(Note note);

    // Method that shouldn't be called outside this class to delete a note
    @Delete
    abstract void _deleteNote(Note note);

    // Method that shouldn't be called outside this class to delete all notes with a certain logID
    @Query("DELETE FROM Note WHERE logID = :logID")
    abstract void _deleteAllNotesForLog(int logID);
}
