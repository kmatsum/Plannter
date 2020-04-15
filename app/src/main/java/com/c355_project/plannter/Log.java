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
    plantName:          Name of crop planted in log.
    plantDate:          Date that the logged crop was planted on.
    harvestRange:       Expected harvest range of logged plant.
    notes:              ArrayList of corresponding Note objects, dynamically read from the database
                        at runtime. This value is ignored by Room and NOT kept track of in the
                        database. A custom query is run to get the results based on matching logIDs.

    ============================================================================================= */

    @PrimaryKey(autoGenerate = true)
    private int logID;
    private int plantID;
    private String plantName;
    private String plantDate;
    private String harvestRange;
    @Ignore
    private ArrayList<Note> notes;

//CONSTRUCTOR ======================================================================================
    public Log(int plantID, String plantName, String plantDate, String harvestRange) {
        this.plantID = plantID;
        this.plantName = plantName;
        this.plantDate = plantDate;
        this.harvestRange = harvestRange;
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

    public String getPlantName() { return this.plantName; }

    public void setPlantName(String plantName) { this.plantName = plantName; }

    public String getPlantDate() { return this.plantDate; }

    public void setPlantDate(String plantDate) { this.plantDate = plantDate; }

    public String getHarvestRange() { return harvestRange; }

    public void setHarvestRange(String harvestRange) { this.harvestRange = harvestRange; }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }
}
