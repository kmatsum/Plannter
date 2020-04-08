package com.c355_project.plannter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class Frag_plantHistory extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {
//VARIABLES ========================================================================================
    //Main_Window Activity Instantiation
    Main_Window Main_Window;

    //Plant Object List
    List<Plant> plantList;
    String[]    plantNames;
    Date        FallFrostDate,
                SpringFrostDate;

    //GUI Elements
    ImageView   imageView;
    TextView    txtSpringFrost,
                txtFallFrost,
                txtPlantStart,
                txtPlantEnd,
                txtHarvestStart,
                txtHarvestEnd;
    Spinner     spnrSelectPlant;

    //Date Format
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");



//LIFECYCLE METHODS ================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Provide values for variables needed to be set on activity start
        Main_Window = (Main_Window) getActivity();
        plantList = Main_Window.getPlantList();
        SpringFrostDate = Main_Window.getLastSpringFrostDate();
        FallFrostDate = Main_Window.getFirstFallFrostDate();

        //Set all OnClickListeners needed for this View
        view.findViewById(R.id.btnBack).setOnClickListener(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.arrowPrevious).setOnClickListener(this);
        view.findViewById(R.id.arrowNext).setOnClickListener(this);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);

        //Set the required Widget variables to their respective views
        imageView = view.findViewById(R.id.imageView);
        spnrSelectPlant = view.findViewById(R.id.spnrSelectPlant);

        txtSpringFrost = view.findViewById(R.id.txtSpringFrost);
        txtFallFrost = view.findViewById(R.id.txtFallFrost);

        txtPlantStart = view.findViewById(R.id.txtPlantStart);
        txtPlantEnd = view.findViewById(R.id.txtPlantEnd);
        txtHarvestStart = view.findViewById(R.id.txtHarvestStart);
        txtHarvestEnd = view.findViewById(R.id.txtHarvestEnd);

        //Fill the spinner ArrayAdaptet contents
        plantNames = new String[plantList.size()];
        for (int i = 0; i < plantList.size(); i++){
            plantNames[i] = plantList.get(i).getPlantName();
        }

        //Attach the ArrayAdapter to the spinner, using our custom Spinner Layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(), R.layout.spinner_item, plantNames);
        spnrSelectPlant.setAdapter(adapter);

        //Attach a listener to the Spinner
        spnrSelectPlant.setOnItemSelectedListener(this);

        //Adds banner ad to UI
        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);


        //Display the stored Fall and Frost Dates
        txtSpringFrost.setText(dateFormat.format(SpringFrostDate));
        txtFallFrost.setText(dateFormat.format(FallFrostDate));

        //Call the displayResults method providing the current spinner position
        displayResults(spnrSelectPlant.getSelectedItemPosition());
        }



//LISTENER METHODS =================================================================================
    @Override
    public void onClick (View view) {
        Integer id = view.getId();

        //Go back to main menu
        if (id == R.id.btnBack) {
            Main_Window.changeFragment("MainMenu");
        }

        //Go back in spinner plant list. Do nothing if position is already the first item.
        else if (id == R.id.btnPrevious || id == R.id.arrowPrevious) {
            int position = spnrSelectPlant.getSelectedItemPosition();
            if (position > 0)
                spnrSelectPlant.setSelection(spnrSelectPlant.getSelectedItemPosition() - 1);
        }

        //Go forward in spinner plant list. Do nothing if position is already the last item.
        else if (id == R.id.btnNext || id == R.id.arrowNext) {
            int position = spnrSelectPlant.getSelectedItemPosition();
            if (position < spnrSelectPlant.getAdapter().getCount() - 1)
                spnrSelectPlant.setSelection(spnrSelectPlant.getSelectedItemPosition() + 1);
        }

        //Used for handling exceptions on if the given ViewID and the expected ViewID does not match
        else {
            //Toast Error Information
            makeToast("[ERROR] Menu parameter passed was not found, returning to main menu...");
            System.out.println("[ERROR] Menu parameter passed was not found, returning to main menu...\n");

            Main_Window.changeFragment("MainMenu");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Call the displayResults method providing the spinner position
        displayResults(position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }



//METHODS ==========================================================================================
    public void makeToast(String Message) {
        Toast toast = Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    public Date calculatePlantDate(int distanceFromFrostDate, Date xFrostDate) {
        //Create the resulting date variable and provide it a default date value
        Date resultingDate;

        //Create a Calendar Object, then cast the Date object into the calendar object.
        Calendar c = Calendar.getInstance();
        c.setTime(xFrostDate);

        //(Subtract) Add the number of weeks from the set date to get a resulting calendar date
        c.add( Calendar.WEEK_OF_YEAR, -(distanceFromFrostDate) );

        //Cast the Calendar Date to a Date to return
        resultingDate = c.getTime();

        return resultingDate;
    }

    public Date calculateHarvestDate(int xdistanceFromPlanting, Date xPlantDate) {
        //Create the resulting date variable and provide it a default date value
        Date resultingDate;

        //Create a Calendar Object, then cast the Date object into the calendar object.
        Calendar c = Calendar.getInstance();
        c.setTime(xPlantDate);

        //(Subtract) Add the number of weeks from the set date to get a resulting calendar date
        c.add( Calendar.WEEK_OF_YEAR, xdistanceFromPlanting );

        //Cast the Calendar Date to a Date to return
        resultingDate = c.getTime();

        return resultingDate;
    }

    public void displayResults (int position) {
        //Instantiate a temp currentPlant object for first-calculation use
        Plant currentPlant = plantList.get(position);

        //Set the drawing resource image from the selected Plant
        Drawable plantImage = Drawable.createFromPath(currentPlant.getPhotoPath());

        //Set the TextView text of the Planting dates
        Date tempFirstPlant = calculatePlantDate(currentPlant.getFirstPlantDate(), SpringFrostDate);
        Date tempLastPlant = calculatePlantDate(currentPlant.getLastPlantDate(), FallFrostDate);

        //Set the TextView text of the Planting dates
        txtPlantStart.setText(dateFormat.format(tempFirstPlant));
        txtPlantEnd.setText(dateFormat.format(tempLastPlant));

        //Set the imageView to the plant image
        imageView.setImageDrawable(plantImage);

        //Set the TextView text of the Harvest dates
        txtHarvestStart.setText(dateFormat.format(calculateHarvestDate(currentPlant.getWeeksToHarvest(),tempFirstPlant)));
        txtHarvestEnd.setText(dateFormat.format(calculateHarvestDate(( currentPlant.getWeeksToHarvest() + currentPlant.getHarvestRange() ),tempLastPlant)));
    }
}