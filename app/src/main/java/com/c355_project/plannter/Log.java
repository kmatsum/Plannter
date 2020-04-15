package com.c355_project.plannter;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Log {

    /* VARIABLE DESCRIPTIONS =======================================================================

    logID:              Auto-generated primary key (starts at 1, increments by 1).
    plantID:            Foreign key to corresponding Plant object.
    notes:              ArrayList of corresponding Note objects, dynamically read from the database
                        at runtime. This value is ignored by Room and NOT kept track of in the
                        database. A custom query is run to get the results based on matching logIDs.

    ============================================================================================= */

    @PrimaryKey(autoGenerate = true)
    private int logID;
    private int plantID;
    @Ignore
    private ArrayList<Note> notes;

//CONSTRUCTOR ======================================================================================
    public Log(int logID, int plantID) {
        this.logID = logID;
        this.plantID = plantID;
    }

//GETS & SETS ======================================================================================

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public int getPlantID() {
        return plantID;
    }

    public void setPlantID(int plantID) {
        this.plantID = plantID;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }
}
