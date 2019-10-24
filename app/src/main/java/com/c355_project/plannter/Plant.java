package com.c355_project.plannter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class Plant{

    /* VARIABLE DESCRIPTIONS =======================================================================

    id:                 Auto-generated primary key (starts at 1, increments by 1).
    plantName:          Name of plant.
    seedCompany:        Seed company, preloaded plants will have value "General".
    firstPlantDate:     # of weeks BEFORE Avg Last Spring Frost that the plant should be planted.
                        A value of 52 means the crop is not recommended for Spring.
    weeksToHarvest:     # of weeks between planted date and first expected harvest date.
    spHarvestRange:     # of weeks the plant may be ready to harvest after weeksToHarvest.
                        A value of 52 means the crop is not recommended for Spring.
    spSeedIndoorDate:   # of weeks BEFORE Avg Last Spring Frost that the plant should be seeded indoors
                        A value of 52 means indoor seeding is not required,
                        or the crop is not recommended for Spring.
    lastPlantDate:      # of weeks BEFORE Avg First Fall Frost that the plant should be planted
                        A value of 52 means the crop is not recommended for Fall.
    faHarvestRange:     # of weeks the plant may be ready to harvest after faWeeksToHarvest
                        A value of 52 means the crop is not recommended for Fall.
    faSeedIndoorDate:   # of weeks BEFORE Avg First Fall Frost that the plant should be seeded indoors
                        A value of 52 means indoor seeding is not required,
                        or the crop is not recommended for Fall.
    notes:              Optional place to add notes regarding plant

    ============================================================================================= */

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String plantName;
    private String seedCompany;
    private int firstPlantDate;
    private int weeksToHarvest;
    private int spHarvestRange;
    private int spSeedIndoorDate;
    private int lastPlantDate;
    private int faHarvestRange;
    private int faSeedIndoorDate;
    private String notes;

    //CONSTRUCTOR ==================================================================================


    public Plant(String plantName, String seedCompany, int firstPlantDate,
                 int weeksToHarvest, int spHarvestRange, int spSeedIndoorDate,
                 int lastPlantDate, int faHarvestRange, int faSeedIndoorDate, String notes) {
        this.plantName = plantName;
        this.seedCompany = seedCompany;
        this.firstPlantDate = firstPlantDate;
        this.weeksToHarvest = weeksToHarvest;
        this.spHarvestRange = spHarvestRange;
        this.spSeedIndoorDate = spSeedIndoorDate;
        this.lastPlantDate = lastPlantDate;
        this.faHarvestRange = faHarvestRange;
        this.faSeedIndoorDate = faSeedIndoorDate;
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

    public void setWeeksToHarvest(int weeksToHarvest) {
        this.weeksToHarvest = weeksToHarvest;
    }

    public int getWeeksToHarvest() {
        return weeksToHarvest;
    }

    public void setSpHarvestRange(int spHarvestRange) {
        this.spHarvestRange = spHarvestRange;
    }

    public int getSpHarvestRange() {
        return spHarvestRange;
    }

    public void setSpSeedIndoorDate(int spSeedIndoorDate) {
        this.spSeedIndoorDate = spSeedIndoorDate;
    }

    public int getSpSeedIndoorDate() {
        return spSeedIndoorDate;
    }

    public void setLastPlantDate(int lastPlantDate) {
        this.lastPlantDate = lastPlantDate;
    }

    public int getLastPlantDate() {
        return lastPlantDate;
    }

    public void setFaHarvestRange(int faHarvestRange) {
        this.faHarvestRange = faHarvestRange;
    }

    public int getFaHarvestRange() {
        return faHarvestRange;
    }

    public void setFaSeedIndoorDate(int faSeedIndoorDate) {
        this.faSeedIndoorDate = faSeedIndoorDate;
    }

    public int getFaSeedIndoorDate() {
        return faSeedIndoorDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }
}