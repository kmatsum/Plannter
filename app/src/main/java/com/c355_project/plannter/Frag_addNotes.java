package com.c355_project.plannter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import java.io.File;

public class Frag_addNotes extends Fragment implements View.OnClickListener {

//VARIABLES ========================================================================================

    //Main_Window Activity Instantiation
    Main_Window Main_Window;
    Context context;

    // GUI Elements
    private LinearLayout viewNotes, createNote, createNoteImage, createNoteAudio;
    private RadioButton rbSimple, rbAudio, rbImage;
    private EditText txtNoteCaption;
    private ImageView imgNoteImage;
    private ImageButton imgRecording;

    // Image Note handling
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICK_IMAGE = 1999;
    Bitmap noteImage = null;

    // Audio Note Handling
    MediaRecorder noteMedia = null;

    // Temp object
    Note tempNote = null;

    // Permissions
    // Intent request codes
    private static final int    REQUEST_RECORD_AUDIO = 1;
    private static final int    REQUEST_TAKE_PICTURE = 2;
    private static final int    REQUEST_VIEW_GALLERY = 3;
    private String[] CAM_PERMISSION = {Manifest.permission.CAMERA};
    private String[] AUDIO_PERMISSION = {Manifest.permission.RECORD_AUDIO};

//LIFECYCLE METHODS ================================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Provide values for variables needed to be set on activity start
        Main_Window = (Main_Window) getActivity();
        context = Main_Window.getApplicationContext();

        // Set Up Listeners
        view.findViewById(R.id.rbAudio).setOnClickListener(this);
        view.findViewById(R.id.rbImage).setOnClickListener(this);
        view.findViewById(R.id.rbSimple).setOnClickListener(this);
        view.findViewById(R.id.btnTakeNotePicture).setOnClickListener(this);
        view.findViewById(R.id.btnNoteOpenGallery).setOnClickListener(this);
        view.findViewById(R.id.btnSaveNote).setOnClickListener(this);
        view.findViewById(R.id.btnCancelNote).setOnClickListener(this);
        view.findViewById(R.id.imgRecording).setOnClickListener(this);

        // Connect GUI Elements
        createNoteImage = view.findViewById(R.id.createNoteImage);
        createNoteAudio = view.findViewById(R.id.createNoteAudio);
        rbAudio = view.findViewById(R.id.rbAudio);
        rbImage = view.findViewById(R.id.rbImage);
        rbSimple = view.findViewById(R.id.rbSimple);
        txtNoteCaption = view.findViewById(R.id.txtNoteCaption);
        imgNoteImage = view.findViewById(R.id.imgNoteImage);
        imgRecording = view.findViewById(R.id.imgRecording);

        // Implements hardware back button to take user back to the Main Menu
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                resetGUI();
                Main_Window.changeFragment("LogNote");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

//LISTENER METHODS =================================================================================

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            // LAYOUT RESPONSE =====================================================================

            //Cancel
            case (R.id.btnCancelNote): {
                resetGUI();
                Main_Window.changeFragment("LogNote");
            } break;

            //Show audio note layout
            case (R.id.rbAudio): {

                // Show proper layout
                createNoteAudio.setVisibility(View.VISIBLE);
                createNoteImage.setVisibility(View.GONE);

            } break;

            //Show image note layout
            case (R.id.rbImage): {

                // Show proper layout
                createNoteAudio.setVisibility(View.GONE);
                createNoteImage.setVisibility(View.VISIBLE);

            } break;

            //Show simple note layout
            case (R.id.rbSimple): {

                // Show proper layout
                createNoteAudio.setVisibility(View.GONE);
                createNoteImage.setVisibility(View.GONE);

            } break;

            // IMAGE NOTE SETUP ====================================================================

            //Take Picture
            case (R.id.btnTakeNotePicture):{
                if (Main_Window.checkSelfPermission(CAM_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(CAM_PERMISSION, REQUEST_TAKE_PICTURE);
                    System.out.println("[DEBUG] Requesting permissions.");
                } else {
                    // Send Intent to camera, response handled in onActivityResult method
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } break;

            //Download Image
            case (R.id.btnNoteOpenGallery):{
                if (Main_Window.checkSelfPermission(CAM_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(CAM_PERMISSION, REQUEST_VIEW_GALLERY);
                    System.out.println("[DEBUG] Requesting permissions.");
                } else {
                    // Send Intent to camera, response handled in onActivityResult method
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
                }
            } break;

            // AUDIO NOTE SETUP ====================================================================

            case (R.id.imgRecording): {

                if (Main_Window.checkSelfPermission(AUDIO_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(AUDIO_PERMISSION, REQUEST_RECORD_AUDIO);
                    System.out.println("[DEBUG] Requesting permissions.");
                } else {
                    // Start recording if not already started
                    if (noteMedia == null){

                        noteMedia = new MediaRecorder();
                        imgRecording.setImageResource(R.drawable.red_microphone);

                        // Store file in temp folder
                        noteMedia.setOutputFile(Main_Window.TEMP_MEDIA_LOCATION + "/tempAudio.mp3");

                        //Set the source of the audio pick up to the microphone
                        noteMedia.setAudioSource(MediaRecorder.AudioSource.MIC);
                        noteMedia.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        noteMedia.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                        try {
                            //Start the recorder
                            noteMedia.prepare();
                            noteMedia.start();
                        } catch (Exception e) {
                            android.util.Log.e("Frag_addNotes", "Exception = " + e);
                        }
                    }

                    // Else stop and save the recording
                    else {
                        //Set image back to colored picture to let user know they are done recording
                        imgRecording.setImageResource(R.drawable.black_microphone);

                        //Stops audio recording and releases Audio recorder resources
                        noteMedia.stop();
                        noteMedia.release();
                        noteMedia = null;
                    }
                }
            } break;

            // SAVE NOTE ===========================================================================

            case (R.id.btnSaveNote): {

                // Determine note type
                String noteType;

                // Save optional caption
                String noteCaption = txtNoteCaption.getText().toString();

                // Simple Note
                if (rbSimple.isChecked()){
                    if (noteCaption.equals("")){
                        Main_Window.makeToast("You must add a caption to save a simple note!");
                        return;
                    } else {
                        noteType = "Simple";
                    }
                }

                // Image Note
                else if (rbImage.isChecked()){
                    // Check that the user took an image/imported one from their gallery
                    if (noteImage == null) {
                        Main_Window.makeToast("You must add an image to save an image note!");
                        return;
                    }
                    noteType = "Image";
                }

                // Audio Note
                else {
                    // Check that the recorded something
                    if (!new File(Main_Window.TEMP_MEDIA_LOCATION + "/tempAudio.mp3").exists()){
                        Main_Window.makeToast("You must record audio to save an audio note!");
                        return;
                    }
                    noteType = "Audio";
                }

                // Save note to database
                tempNote = new Note(Main_Window.getCurrLog().getLogID(), noteType, noteCaption, "");
                Main_Window.editTransaction("InsertNote", tempNote);

                resetGUI();
            }
            break;
        }
    }

//onResult METHODS =================================================================================

    // Method to respond to intents
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Respond to the camera intent
        // Check requestCode and resultCode
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                // Unpack photo from intent
                noteImage = (Bitmap) data.getExtras().get("data");
                if (noteImage == null)
                    throw new Exception("Photo is Null");
                imgNoteImage.setImageBitmap(noteImage);
            } catch (Exception e) {
                //Display an error
                Main_Window.makeToast("Error reading image.");
                android.util.Log.e("CAMERA_REQUEST Intent", "Exception: ", e);
            }
        }

        // Respond to the gallery intent
        // Followed tutorial here: https://medium.com/@pednekarshashank33/android-10s-scoped-storage-image-picker-gallery-camera-d3dcca427bbf
        // Check requestCode and resultCode
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                // Unpack photo from intent
                Uri selectedImageUri = data.getData();
                ParcelFileDescriptor pfd = Main_Window.getContentResolver().openFileDescriptor(selectedImageUri, "r");
                noteImage = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                pfd.close();

                if (noteImage == null)
                    throw new Exception("Photo is Null");

                imgNoteImage.setImageBitmap(noteImage);
            } catch (Exception e) {
                //Display an error
                Main_Window.makeToast("Error reading selected image.");
                Log.e("PICK_IMAGE Intent", "Exception: ", e);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("[DEBUG]: onRequestPermissionResult Called!");
        if (requestCode == REQUEST_TAKE_PICTURE) {
            if (verifyPermissions(grantResults)) {
                //All Permissions Granted
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned true, ALL PERMISSIONS GRANTED");
                // Send Intent to camera, response handled in onActivityResult method
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                //Permissions Denied
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned false, ALL PERMISSIONS NOT GRANTED");
                Main_Window.makeToast("You must grant camera permissions to take a picture.");
            }
        } else if (requestCode == REQUEST_VIEW_GALLERY) {
            if (verifyPermissions(grantResults)) {
                //All Permissions Granted
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned true, ALL PERMISSIONS GRANTED");
                // Send Intent to camera, response handled in onActivityResult method
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
            } else {
                //Permissions Denied
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned false, ALL PERMISSIONS NOT GRANTED");
                Main_Window.makeToast("You must grant camera permissions to open gallery.");
            }
        } else if (requestCode == REQUEST_RECORD_AUDIO) {
            if (verifyPermissions(grantResults)) {
                //All Permissions Granted
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned true, ALL PERMISSIONS GRANTED");
                // Call On Click
                imgRecording.callOnClick();
            } else {
                //Permissions Denied
                System.out.println("[DEBUG]: onRequestPermissionResult.verifyPermissions(grantResults) returned false, ALL PERMISSIONS NOT GRANTED");
                Main_Window.makeToast("You must grant microphone permissions to record audio.");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

// METHODS =========================================================================================
    private boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1){
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void resetGUI(){
        //Resets the GUI to blank input
        rbSimple.setChecked(true);
        createNoteAudio.setVisibility(View.GONE);
        createNoteImage.setVisibility(View.GONE);
        txtNoteCaption.setText("");
    }
}
