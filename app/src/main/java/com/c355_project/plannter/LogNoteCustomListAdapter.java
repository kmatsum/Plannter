package com.c355_project.plannter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LogNoteCustomListAdapter extends BaseAdapter {
    //VARIABLES=====================================================================================
    //Global Variable Declarations
    private LayoutInflater inflater = null;
    private Context context;
    Main_Window Main_window;

    public LogNoteCustomListAdapter(Main_Window main_window)
    {
        context = main_window;
        Main_window = main_window;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //Variable Declaration
        View rowView;
        TextView txtNoteType;

        //Variable Instantiation
        rowView = inflater.inflate(R.layout.lognotecustomlistadapter, null);
        txtNoteType = rowView.findViewById(R.id.txtNoteType);
        List<Note> NoteList = Main_window.NoteList;

        txtNoteType.setText(NoteList.get(i).getNoteType());

        Main_window.makeToast("Note type: " + NoteList.get(i).getNoteType());

        return rowView;
    }
}
