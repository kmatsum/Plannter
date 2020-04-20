package com.c355_project.plannter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;

/*
    What Is Done:
        - Added Frag_Camera, eventually to be able to see the photos we have taken of our plants
        - Frag_logNote is the code for the fragment dedicated to creating notes
        - LogNoteCustomListAdapter is the adapter for the list view in the frag_logNote
        - Added a couple more png files to populate image buttons and switch the resource of said image buttons on the record button
   TODO:
    - I have a file provider xml added but being that the notes are a non-static folder
      i wasnt sure that it was going to work I know roughly how to sift through the file
      folders like that just not with file provider
    - Recording button works and saves where it should however its not saving like the file tree
      design that we specified, for example i recorded two audio files and they both stored in the
      1st folder and no second was created
    - Camera button will open up the camera but does not save it anywhere
    - Text button has no functionality behind it yet
    - Finally list view is not populating with the notes saved, which from the code looks like it
      updates after every saved note. I used the log section for reference on that part just havent
      gotten it working just yet
 */

public class Frag_logNote extends Fragment implements View.OnClickListener {
    //VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;
    ImageButton imgRecord, imgPencil, imgCamera;
    private MediaRecorder plantNoteRecorder;
    private Context context;
    Uri contentUri = null, photoUri = null;
    File logDirectory;
    int count = 0, noteID = 1;
    private ListView list;
    //list of permissions
    private String[] permissions =
            {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };

    //LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_note, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Provide values for variables needed to be set on activity start
        Main_Window = (Main_Window) getActivity();
        context = Main_Window.getApplicationContext();
        imgRecord = view.findViewById(R.id.imgRecord);
        view.findViewById(R.id.imgCamera).setOnClickListener(this);
        view.findViewById(R.id.imgPencil).setOnClickListener(this);
        view.findViewById(R.id.imgRecord).setOnClickListener(this);
        logDirectory = new File(Main_Window.LOG_MEDIA_LOCATION + Main_Window.getLogID() + "/" + noteID);
        //for each loop to check if the permissions have been granted and if not request them
        for (String str : permissions) {
            if (Main_Window.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(permissions, 1);
                return;
            }
            //set the custom list adapter to interpret the file names so we can display them in our list view
            list = view.findViewById(R.id.listView);
            list.setAdapter(new LogNoteCustomListAdapter(Main_Window));
        }
        //Implements hardware back button to take user back to the Main Menu
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Main_Window.changeFragment("MainMenu");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.imgRecord): {
                if (count == 0) {
                    plantNoteRecorder = new MediaRecorder();
                    imgRecord.setImageResource(R.drawable.red_microphone);
                    if (logDirectory.isDirectory()) {
                        final String recordedFile = Main_Window.LOG_MEDIA_LOCATION + Main_Window.getLogID() + "/" + noteID + ".mp3";
                        plantNoteRecorder.setOutputFile(recordedFile);
                    } else {
                        logDirectory.mkdir();
                        final String recordedFile = Main_Window.LOG_MEDIA_LOCATION + Main_Window.getLogID() + "/" + noteID + ".mp3";
                        plantNoteRecorder.setOutputFile(recordedFile);
                    }

                    //Set the source of the audio pick up to the microphone
                    plantNoteRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    plantNoteRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    plantNoteRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);


                    try {
                        //Start the recorder
                        plantNoteRecorder.prepare();
                        plantNoteRecorder.start();
                    } catch (Exception e) {
                        android.util.Log.e("AudioRecorder", "Exception = " + e);
                    }
                    count++;
                } else {
                    //Set image back to colored picture to let user know they are done recording
                    imgRecord.setImageResource(R.drawable.black_microphone);

                    //Stops audio recording and releases Audio recorder resources
                    plantNoteRecorder.stop();
                    plantNoteRecorder.release();
                    Note note = new Note(Main_Window.getLogID(), "Audio", "", Main_Window.LOG_MEDIA_LOCATION + Main_Window.getLogID() + "/");
                    Main_Window.editTransaction("_insertNote", note);
                    noteID++;
                    count--;
                }
            }
            break;
            case (R.id.imgCamera): {
                //opens camera intent to allow us to take a picture
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(camera_intent);

                //Store recorded audio file in previously created directory
                if (logDirectory.isDirectory()) {
                    final String capturedFile = Main_Window.LOG_MEDIA_LOCATION + Main_Window.getLogID() + "/" + noteID + ".png";
                    File tempFile = new File(capturedFile);
                    //get photo from the putExtra method and store it into photoUri for viewing purposes
                    photoUri = FileProvider.getUriForFile(context, "com.c355_project.plannter.fileprovider", tempFile);
                    try {
                        if (photoUri == null) {
                            //Grab photo just captured
                            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(camera_intent, 1);
                        }

                    } catch (Exception e) {
                        android.util.Log.e("Photo", "Exception = " + e);
                    }
                } else {
                    logDirectory.mkdir();
                    final String capturedFile = Main_Window.LOG_MEDIA_LOCATION + Main_Window.getLogID() + "/" + noteID + ".png";
                    File tempFile = new File(capturedFile);
                    //get photo from the putExtra method and store it into photoUri for viewing purposes
//                    photoUri = FileProvider.getUriForFile(context, "com.c355_project.plannter.fileprovider", tempFile);
//                    try {
//                        if (photoUri == null) {
//
//                            //Grab photo just captured
//                            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                            startActivityForResult(camera_intent, 1);
//                        }
//
//                    } catch (Exception e) {
//                        android.util.Log.e("Photo", "Exception = " + e);
//                    }
                }

            }
            break;
        }
    }

}
