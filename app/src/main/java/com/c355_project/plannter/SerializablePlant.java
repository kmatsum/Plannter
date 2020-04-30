package com.c355_project.plannter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializablePlant implements Serializable {

// VARIABLES =======================================================================================

    String plantName;
    String seedCompany;
    int firstPlantDate;
    int weeksToHarvest;
    int harvestRange;
    int seedIndoorDate;
    int lastPlantDate;
    String notes;
    String photoPath;
    boolean raisedRows;
    boolean raisedHills;
    int distBetweenPlants;
    double seedDepth;

// CONSTRUCTOR =====================================================================================

    public SerializablePlant(String plantName, String seedCompany, int firstPlantDate,
                             int weeksToHarvest, int harvestRange, int seedIndoorDate, int lastPlantDate,
                             String notes, String photoPath, boolean raisedRows, boolean raisedHills,
                             int distBetweenPlants, double seedDepth)
    {
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

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArray);
        objectOutputStream.writeObject(SerializablePlant.this);
        System.out.println("[DEBUG]: serializablePlant was Serialized...");
        return byteArray.toByteArray();
    }
}