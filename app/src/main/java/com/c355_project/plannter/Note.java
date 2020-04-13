package com.c355_project.plannter;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Log.class,
        parentColumns = "logID",
        childColumns = "logID",
        onDelete = CASCADE))
public class Note {

    /* VARIABLE DESCRIPTIONS =======================================================================

    noteID:             Auto-generated primary key (starts at 1, increments by 1).

    ============================================================================================= */

    @PrimaryKey(autoGenerate = true)
    private int noteID;
    private int logID;
}
