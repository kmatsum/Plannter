package com.c355_project.plannter;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Plant.class,
        parentColumns = "plantID",
        childColumns = "plantID",
        onDelete = CASCADE))
public class Log {

    /* VARIABLE DESCRIPTIONS =======================================================================

    logID:              Auto-generated primary key (starts at 1, increments by 1).

    ============================================================================================= */

    @PrimaryKey(autoGenerate = true)
    private int logID;
    private int plantID;
    @Ignore
    ArrayList<Note> notes;
}
