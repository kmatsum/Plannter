package com.c355_project.plannter;

import androidx.room.Entity;

@Entity(primaryKeys = {"noteID", "logID"})
public class Note {

    /* VARIABLE DESCRIPTIONS =======================================================================

    noteID:             Auto-generated primary key (starts at 1, increments by 1).
    logID:              Foreign key to corresponding Log object.
    noteType:           String representing the type of note object. Values are limited to:
                        1) Simple - no image or audio recording, just noteText (noteFilepath = null).
                        2) Image - an image, with or without noteText.
                        3) Audio - an audio recording, with or without noteText.
    noteText:           A string of text notes entered by the user (optional if type = Image or Audio).
    noteFilepath:       The filepath to the corresponding media file (null when type = Simple).

    ============================================================================================= */

    private int noteID;
    private int logID;
    private String noteType;
    private String noteText;
    private String noteFilepath;

//CONSTRUCTOR ======================================================================================

    public Note(int logID, String noteType, String noteText, String noteFilepath) {
        this.logID = logID;
        this.noteType = noteType;
        this.noteText = noteText;
        this.noteFilepath = noteFilepath;
    }

//GETS & SETS ======================================================================================

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteFilepath() {
        return noteFilepath;
    }

    public void setNoteFilepath(String noteFilepath) {
        this.noteFilepath = noteFilepath;
    }
}
