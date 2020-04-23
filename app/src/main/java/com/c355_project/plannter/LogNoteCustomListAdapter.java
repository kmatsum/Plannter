package com.c355_project.plannter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogNoteCustomListAdapter extends BaseAdapter {

// CONSTRUCTOR =====================================================================================
    public LogNoteCustomListAdapter(Main_Window main_window) {
        Main_window = main_window;
        inflater = (LayoutInflater)Main_window.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

// VARIABLES =======================================================================================
    // Global Variable Declarations
    private LayoutInflater inflater = null;
    Main_Window Main_window;

    // GUI Elements
    TextView txtNoteID, txtNoteCaption;
    ImageView imgNoteImage;
    ImageButton btnDeleteNote, btnPlay, btnPause;
    LinearLayout layoutNoteCaption;

    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {

        // Get current note
        final Note currNote = Main_window.getCurrLogNoteList().get(position);
        String noteType = currNote.getNoteType();

        // Inflate the correct view based on noteType, and connect type-unique GUI elements
        if (noteType.equals("Audio")){
            rowView = inflater.inflate(R.layout.lognotecustomlistadapter_audio, null);
            btnPlay = rowView.findViewById(R.id.btnPlay);
            btnPause = rowView.findViewById(R.id.btnPause);
            layoutNoteCaption = rowView.findViewById(R.id.layoutNoteCaption);
        } else if (noteType.equals("Image")){
            rowView = inflater.inflate(R.layout.lognotecustomlistadapter_image, null);
            imgNoteImage = rowView.findViewById(R.id.imgNoteImage);
            layoutNoteCaption = rowView.findViewById(R.id.layoutNoteCaption);
        } else {
            rowView = inflater.inflate(R.layout.lognotecustomlistadapter_simple, null);
        }

        // Connect GUI Elements that all noteTypes have
        btnDeleteNote = rowView.findViewById(R.id.btnDeleteNote);
        txtNoteID = rowView.findViewById(R.id.txtNoteID);
        txtNoteCaption = rowView.findViewById(R.id.txtNoteCaption);

        // Set type-unique GUI elements
        if (noteType.equals("Audio")){
            // Hide caption if there is none
            if (currNote.getNoteText().equals("")){
                layoutNoteCaption.setVisibility(View.GONE);
            }
            // Set onClickListeners
            btnPlay.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    /*TODO: play audio*/
                }
            });
            btnPause.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    /*TODO: pause audio*/
                }
            });
        } else if (noteType.equals("Image")){
            // Hide caption if there is none
            if (currNote.getNoteText().equals("")){
                layoutNoteCaption.setVisibility(View.GONE);
            }
            imgNoteImage.setImageURI(Uri.parse(currNote.getNoteFilepath()));
        }

        // Set GUI Elements that all noteTypes have
        txtNoteID.setText(String.valueOf(currNote.getNoteID()));
        txtNoteCaption.setText(currNote.getNoteText());

        // Attach onClickListener to Delete Note Button
        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmationDialog(Main_window, currNote);
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        return Main_window.getCurrLogNoteList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    // METHODS =========================================================================================
    private void openConfirmationDialog(Context context, final Note note) {
        new AlertDialog.Builder(context)
                .setTitle("Are you sure you want to delete note " + note.getNoteID() + ": " + note.getNoteType() + "?")
                .setMessage(Html.fromHtml("This action cannot be undone."))

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Main_window.changeFragment("PlantHistory");
                        Main_window.editTransaction("DeleteNote", note);
                        Main_window.makeToast("Log " + note.getNoteID() + ": " + note.getNoteType() + " deleted.");
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(R.drawable.ic_dialog_warning)
                .show();
    }
}
