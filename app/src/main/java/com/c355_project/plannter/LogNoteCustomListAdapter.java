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

/* LogNoteCustomListAdapter class, created "the right way" following tutorial here:
    https://gist.github.com/cesarferreira/4c4ae3841fee8894ccfd */

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

    public class ViewHolder {
        TextView txtNoteID, txtNoteCaption;
        ImageView imgNoteImage;
        ImageButton btnDeleteNote, btnPlay, btnPause;
        LinearLayout layoutNoteCaption;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup viewGroup) {

        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        ViewHolder holder;
        currNote = Main_window.getCurrLogNoteList().get(position);
        String noteType = currNote.getNoteType();

        // When rowView is not null, we can reuse it directly, there is no need to re-inflate it
        if (rowView == null) {

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();

            // Inflate the correct view based on noteType, set respective variables
            if (noteType.equals("Audio")){
                rowView = inflater.inflate(R.layout.lognotecustomlistadapter_audio, null);
                holder.btnPlay = rowView.findViewById(R.id.btnPlay);
                holder.btnPause = rowView.findViewById(R.id.btnPause);
                holder.layoutNoteCaption = rowView.findViewById(R.id.layoutNoteCaption);
            } else if (noteType.equals("Image")){
                rowView = inflater.inflate(R.layout.lognotecustomlistadapter_image, null);
                holder.imgNoteImage = rowView.findViewById(R.id.imgNoteImage);
                holder.layoutNoteCaption = rowView.findViewById(R.id.layoutNoteCaption);
            } else {
                rowView = inflater.inflate(R.layout.lognotecustomlistadapter_simple, null);
            }

            // Set variables that all noteTypes have
            holder.btnDeleteNote = rowView.findViewById(R.id.btnDeleteNote);
            holder.txtNoteID = rowView.findViewById(R.id.txtNoteID);
            holder.txtNoteCaption = rowView.findViewById(R.id.txtNoteCaption);

            rowView.setTag(holder);

        } else {
            // Get the ViewHolder back to get fast access to holder variables
            holder = (ViewHolder) rowView.getTag();
        }

        if (noteType.equals("Audio")){
            // Hide caption if there is none
            if (currNote.getNoteText().equals("")){
                holder.layoutNoteCaption.setVisibility(View.GONE);
            }
            // Set onClickListeners
            holder.btnPlay.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    /*TODO: play audio*/
                }
            });
            holder.btnPause.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    /*TODO: pause audio*/
                }
            });
        } else if (noteType.equals("Image")){
            // Hide caption if there is none
            if (currNote.getNoteText().equals("")){
                holder.layoutNoteCaption.setVisibility(View.GONE);
            }
            holder.imgNoteImage.setImageURI(Uri.parse(currNote.getNoteFilepath()));
        }

        holder.txtNoteID.setText(String.valueOf(currNote.getNoteID()));
        holder.txtNoteCaption.setText(currNote.getNoteText());

        //Attaches onClickListener to Delete Note Buttons
        holder.btnDeleteNote.setOnClickListener(new View.OnClickListener() {
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
