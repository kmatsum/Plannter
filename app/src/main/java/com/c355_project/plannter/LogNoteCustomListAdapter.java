package com.c355_project.plannter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        TextView txtNoteID, txtNoteType;

        //Variable Instantiation
        rowView = inflater.inflate(R.layout.lognotecustomlistadapter, null);
        txtNoteID = rowView.findViewById(R.id.txtNoteID);
        txtNoteType = rowView.findViewById(R.id.txtNoteType);
        currNote = Main_window.getCurrLogNoteList().get(position);

        txtNoteID.setText(String.valueOf(currNote.getNoteID()));
        txtNoteType.setText(String.valueOf(currNote.getNoteType()));

        return rowView;
    }
}
