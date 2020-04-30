package com.c355_project.plannter;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class Frag_logNote extends Fragment implements View.OnClickListener{

//VARIABLES ========================================================================================

    // Main_Window Activity Instantiation
    Main_Window Main_Window;

    // GUI Elements
    ListView notesListView;
    ImageView imgAddNote;

    // Object variables
    Log currLog;

//LIFECYCLE METHODS ================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Provide values for variables needed to be set on activity start
        Main_Window = (Main_Window) getActivity();

        //Implements hardware back button to take user back to the Main Menu
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Main_Window.stopAudio();
                Main_Window.changeFragment("PlantLog");            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        //Find GUI Elements
        notesListView = view.findViewById(R.id.notesListView);
        imgAddNote = view.findViewById(R.id.imgAddNote);

        //Set all OnClickListeners needed for this View
        imgAddNote.setOnClickListener(this);

        // Setup current objects
        currLog = Main_Window.getCurrLog();

        // Update NoteList for Current Log
        Main_Window.editTransaction("GetNotesForCurrLog", null);

        // Setup list adapter
        LogNoteCustomListAdapter adapter = new LogNoteCustomListAdapter(Main_Window);
        notesListView.setAdapter(adapter);

        // If there are no notes...
        if (adapter.getCount() == 0){
            Main_Window.makeToast("You have no notes for log: " + currLog.getPlantName());
        }
    }

//LISTENER METHODS =================================================================================

    @Override
    public void onClick (View view) {
        switch (view.getId()){

            case (R.id.imgAddNote): {
                Main_Window.stopAudio();
                Main_Window.changeFragment("AddNotes");
            } break;

        }

    }
}
