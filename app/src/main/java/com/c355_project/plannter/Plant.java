package com.c355_project.plannter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity()
public class    Plant {

    /* VARIABLE DESCRIPTIONS =======================================================================

    id:                 Auto-generated primary key (starts at 1, increments by 1).
    plantName:          Name of plant.
    seedCompany:        Seed company, preloaded plants will have value "General".
    firstPlantDate:     # of weeks BEFORE Avg Last Spring Frost that the plant should be planted.
                        A value greater than 25 means it is not recommended for Spring.
    weeksToHarvest:     # of weeks between planted date and first expected harvest date.
    harvestRange:       # of weeks the plant may be ready to harvest after weeksToHarvest.
    seedIndoorDate:     # of weeks BEFORE Avg Last Spring Frost that the plant should be seeded indoors.
                        A value of 52 means indoor seeding is not required.
    lastPlantDate:      # of weeks BEFORE Avg First Fall Frost that the plant should be planted.
                        A value greater than 25 means it is not recommended for Fall.
    notes:              Optional place to add notes regarding plant
    photoPath:          Filepath to the corresponding picture.
    raisedRows:         Boolean to determine if plant should be planted in raised rows.
    raisedHills:        Boolean to determine if plant should be planted in raised hills.
    distBetweenPlants:  Integer to represent inches between planted hills/rows.
    seedDepth:          Double to represent inches below surface to plant seed.

    ============================================================================================= */

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String plantName;
    private String seedCompany;
    private int firstPlantDate;
    private int weeksToHarvest;
    private int harvestRange;
    private int seedIndoorDate;
    private int lastPlantDate;
    private String notes;
    private String photoPath;
    private boolean raisedRows;
    private boolean raisedHills;
    private int distBetweenPlants;
    private double seedDepth;


//CONSTRUCTOR ======================================================================================
    public Plant(String plantName, String seedCompany, int firstPlantDate, int weeksToHarvest,
                 int harvestRange, int seedIndoorDate, int lastPlantDate, String notes,
                 String photoPath, boolean raisedRows, boolean raisedHills, int distBetweenPlants,
                 double seedDepth) {
        this.plantName = plantName;
        this.seedCompany = seedCompany;
        this.firstPlantDate = firstPlantDate;
        this.weeksToHarvest = weeksToHarvest;
        this.harvestRange = harvestRange;
        this.seedIndoorDate = seedIndoorDate;
        this.lastPlantDate = lastPlantDate;
        this.notes = notes;
        this.photoPath = photoPath;
        this.raisedRows = raisedRows;
        this.raisedHills = raisedHills;
        this.distBetweenPlants = distBetweenPlants;
        this.seedDepth = seedDepth;
    }



//GETS & SETS ======================================================================================
    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setPlantName(String name){
        this.plantName = name;
    }

    public String getPlantName(){
        return this.plantName;
    }

    public void setSeedCompany(String seedCompany) {
        this.seedCompany = seedCompany;
    }

    public String getSeedCompany() {
        return seedCompany;
    }

    public void setFirstPlantDate(int firstPlantDate) {
        this.firstPlantDate = firstPlantDate;
    }

    public int getFirstPlantDate() {
        return firstPlantDate;
    }

    public void setWeeksToHarvest(int weeksToHarvest) {
        this.weeksToHarvest = weeksToHarvest;
    }

    public int getWeeksToHarvest() {
        return weeksToHarvest;
    }

    public void setHarvestRange(int harvestRange) {
        this.harvestRange = harvestRange;
    }

    public int getHarvestRange() {
        return harvestRange;
    }

    public void setSeedIndoorDate(int seedIndoorDate) {
        this.seedIndoorDate = seedIndoorDate;
    }

    public int getSeedIndoorDate() {
        return seedIndoorDate;
    }

    public void setLastPlantDate(int lastPlantDate) {
        this.lastPlantDate = lastPlantDate;
    }

    public int getLastPlantDate() {
        return lastPlantDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean isRaisedRows() {
        return raisedRows;
    }

    public void setRaisedRows(boolean raisedRows) {
        this.raisedRows = raisedRows;
    }

    public boolean isRaisedHills() {
        return raisedHills;
    }

    public void setRaisedHills(boolean raisedHills) {
        this.raisedHills = raisedHills;
    }

    public int getDistBetweenPlants() {
        return distBetweenPlants;
    }

    public void setDistBetweenPlants(int distBetweenPlants) {
        this.distBetweenPlants = distBetweenPlants;
    }

    public double getSeedDepth() {
        return seedDepth;
    }

    public void setSeedDepth(double seedDepth) {
        this.seedDepth = seedDepth;
    }
}