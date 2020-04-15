package com.c355_project.plannter;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Dao
public abstract class PlannterDatabaseDao {

/*
    There are 2 types of methods, EXTERNAL and INTERNAL:

    1) EXTERNAL methods are designed to be called outside of PlannterDatabaseDao.java and maintain data
       integrity. These methods are public, and can be either abstract (overridden by Room due
       to an Annotation denoted by "@") or concrete (defined in this class).

    2) INTERNAL methods should only be called from PlannterDatabaseDao.java to maintain data integrity.
       Their method names start with an underline "_". If it is a concrete method, it is
       declared private and thus inaccessible. If it is an abstract method to be later overridden
       by Room, it is impossible to be declared private. Therefore, it is up to the coders not to
       call these methods marked by underscores outside of PlannterDatabaseDao.java.

 */


// PLANT METHODS ===================================================================================

    // EXTERNAL METHODS ============================================================================

    // Method to insert passed plant
    // Returns Room auto-generated plantID
    public void insertPlant(Plant plant, Bitmap photo) {

        // Insert log and get corresponding id
        long id = _insertPlant(plant);

        // Create corresponding directory
        File f = new File(Main_Window.PLANT_MEDIA_LOCATION, String.valueOf(id));
        f.mkdir();
        String filePath = f.getAbsoluteFile() + "/" +  id + ".png";

        // Export photo to corresponding directory
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            photo.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update plant object
        plant.setPhotoPath(filePath);
        plant.setPlantID((int)id);
        updatePlant(plant);

        // Update console
        System.out.println("PlannterDatabaseDao INSERT Plant Operation Completed\r\n\t\tPlant ID: " +
                plant.getPlantID() + "\tPlant Name: " + plant.getPlantName());
    }

    // Method to delete passed plant and corresponding logs and notes
    public void deletePlant(Plant plant){
        // Delete all corresponding logs, which will delete all corresponding notes
        _deleteAllLogsForPlant(plant);

        // Delete plant
        _deletePlant(plant);

        // Delete its corresponding photo folder from internal storage
        _deleteFolder(new File(Main_Window.PLANT_MEDIA_LOCATION + "/" + plant.getPlantID()));

        // Update console
        System.out.println("PlannterDatabaseDao DELETE Plant Operation Completed\r\n\t\tPlant ID: "
                + plant.getPlantID() + "\tPlant Name: " + plant.getPlantName());
    }

    // Method to update passed plant
    public void updatePlant(Plant plant){
        // Update plant
        _updatePlant(plant);

        // Update console
        System.out.println("PlannterDatabaseDao UPDATE Plant Operation Completed\r\n\t\tPlant ID: "
                + plant.getPlantID() + "\tPlant Name: " + plant.getPlantName());
    }

    // Method to return all plants
    @Query("SELECT * FROM Plant")
    abstract List<Plant> getAllPlants();

    // INTERNAL METHODS ============================================================================

    // Method that shouldn't be called outside this class to insert a plant
    // EXCEPTION TO THE PREVIOUS LINE: this method is called in PlannterDatabase.java
    // Returns Room auto-generated plantID
    @Insert
    abstract long _insertPlant(Plant plant);

    // Method that shouldn't be called outside this class to delete a plant
    @Delete
    abstract void _deletePlant(Plant plant);

    // Method that shouldn't be called outside this class to update passed plant
    @Update
    abstract void _updatePlant(Plant plant);

// LOG METHODS =====================================================================================

    // EXTERNAL METHODS ============================================================================

    // Method to insert passed log
    // Returns Room auto-generated logID
    public void insertLog(Log log) {
        // Insert log
        long logID = _insertLog(log);

        // Create corresponding directory
        new File(Main_Window.LOG_MEDIA_LOCATION, String.valueOf(logID)).mkdir();

        // Update console
        System.out.println("PlannterDatabaseDao INSERT Log Operation Completed\r\n\t\tLog ID: "
                + logID + "\tPlant ID: " + log.getPlantID());
    }

    // Method to delete passed log and corresponding notes
    public void deleteLog(Log log){
        // Delete all corresponding notes
        _deleteAllNotesForLog(log);

        // Update Console
        System.out.println("PlannterDatabaseDao DELETE All Notes For Log With ID " + log.getPlantID() + " Completed");

        // Delete log
        _deleteLog(log);

        // Delete its corresponding photo folder from internal storage
        _deleteFolder(new File(Main_Window.LOG_MEDIA_LOCATION + "/" + log.getLogID()));

        // Update console
        System.out.println("PlannterDatabaseDao DELETE Log Operation Completed\r\n\t\tLog ID: "
                + log.getLogID() + "\tPlant ID: " + log.getPlantID());
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
        // Update Console
        System.out.println("PlannterDatabaseDao DELETE All Logs For Plant With ID " + plant.getPlantID() + " Completed");
    }

// NOTE METHODS ====================================================================================

    // EXTERNAL METHODS ============================================================================

    // Method to insert passed note
    // Returns noteID generated in PlannterDatabaseDao.java
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

        // Create corresponding directory
        new File(Main_Window.LOG_MEDIA_LOCATION, note.getLogID() + "/" + note.getNoteID()).mkdir();

        // Update console
        System.out.println("PlannterDatabaseDao INSERT Note Operation Completed\r\n\t\tNote ID: "
                + note.getNoteID() + "\tLog ID: " + note.getLogID() + "\tNote Type: " + note.getNoteType());

        return lastNoteID + 1;
    }

    // Method to delete passed note
    public void deleteNote(Note note){
        // Delete note
        _deleteNote(note);

        // Delete its corresponding photo folder from internal storage
        _deleteFolder(new File(Main_Window.LOG_MEDIA_LOCATION + "/" + note.getLogID() + "/" + note.getNoteID()));

        // Update console
        System.out.println("PlannterDatabaseDao DELETE Note Operation Completed\r\n\t\tNote ID: "
                + note.getNoteID() + "\tLog ID: " + note.getLogID() + "\tNote Type: " + note.getNoteType());
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

    // Method that shouldn't be called outside this class to delete all notes for passed log
    private void _deleteAllNotesForLog(Log log){
        // Get all corresponding notes
        List<Note> plantNotes = getAllNotesForLog(log);
        // Delete logs
        for (Note note : plantNotes){
            deleteNote(note);
        }
        // Update Console
        System.out.println("PlannterDatabaseDao DELETE All Notes For Log With ID " + log.getLogID() + " Completed");
    }

    // HELPER METHODS ==============================================================================
    // Method to delete passed folder and all files in passed folder
    // Internal files must be deleted first before the folder can be deleted
    public void _deleteFolder(File folder){
        String[] files = folder.list();
        if (files != null){
            for(String s: files){
                File currentFile = new File(folder.getPath(),s);
                currentFile.delete();
                // Update Console
                System.out.println("PlannterDatabaseDao FILE DELETED: " + currentFile.getAbsolutePath());
            }
        }
        folder.delete();
        // Update Console
        System.out.println("PlannterDatabaseDao FOLDER DELETED: " + folder.getAbsolutePath());
    }
}
