package com.c355_project.plannter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Plant{

    /* VARIABLE DESCRIPTIONS =======================================================================

    id:             Auto-generated primary key (starts at 1, increments by 1)
    plantName:      Name of plant
    seedCompany:    Seed company, preloaded plants will have value "General"
    firstPlantDate: # of weeks BEFORE Avg Last Frost Date that the plant should be planted
    harvestMinDate: Minimum # of weeks AFTER Avg Last Frost Date that the plant can be harvested
    harvestRange:   # of weeks the plant may be ready to harvest after harvestMinDate
    seedIndoorDate: # of weeks BEFORE Avg Last Frost Date that the plant should be seeded indoors
                    This is not always required. Thus, assign a value of 52 if not required.
    notes:          Optional place to add notes regarding plant

    ============================================================================================= */

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String plantName;
    private String seedCompany;
    private int firstPlantDate;
    private int harvestMinDate;
    private int harvestRange;
    private int seedIndoorDate;
    private String notes;

    //CONSTRUCTOR ==================================================================================
    public Plant(String plantName, String seedCompany, int firstPlantDate, int harvestMinDate, int harvestRange, int seedIndoorDate, String notes){
        this.plantName = plantName;
        this.seedCompany = seedCompany;
        this.firstPlantDate = firstPlantDate;
        this.harvestMinDate = harvestMinDate;
        this.harvestRange = harvestRange;
        this.seedIndoorDate = seedIndoorDate;
        this.notes = notes;
    }

    //GETS & SETS ==================================================================================
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

    public void setHarvestMinDate(int harvestMinDate) {
        this.harvestMinDate = harvestMinDate;
    }

    public int getHarvestMinDate() {
        return harvestMinDate;
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

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }
}