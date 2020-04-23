package com.c355_project.plannter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class LogNoteCustomListAdapter extends BaseAdapter {
    //VARIABLES=====================================================================================
    //Global Variable Declarations
    private LayoutInflater inflater = null;
    Main_Window Main_window;

    Note currNote;

    public LogNoteCustomListAdapter(Main_Window main_window)
    {
        Main_window = main_window;
        inflater = (LayoutInflater)Main_window.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //Variable Declaration
        View rowView;
        TextView txtNoteID, txtNoteType, txtNoteCaption;
        ImageButton btnDeleteNote;

        //Variable Instantiation
        rowView = inflater.inflate(R.layout.lognotecustomlistadapter, null);
        btnDeleteNote = rowView.findViewById(R.id.btnDeleteNote);
        txtNoteID = rowView.findViewById(R.id.txtNoteID);
        txtNoteType = rowView.findViewById(R.id.txtNoteType);
        txtNoteCaption = rowView.findViewById(R.id.txtNoteCaption);
        currNote = Main_window.getCurrLogNoteList().get(position);

        txtNoteID.setText(String.valueOf(currNote.getNoteID()));
        txtNoteType.setText(String.valueOf(currNote.getNoteType()));
        txtNoteCaption.setText(currNote.getNoteText());

        //Attaches onClickListener to Delete Note Buttons
        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmationDialog(Main_window, currNote);
            }
        });

        return rowView;
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
