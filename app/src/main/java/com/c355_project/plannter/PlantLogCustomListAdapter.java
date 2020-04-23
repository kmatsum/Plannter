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
import android.widget.TextView;
import java.util.List;

public class PlantLogCustomListAdapter extends BaseAdapter {

// CONSTRUCTOR =====================================================================================
    public PlantLogCustomListAdapter (Main_Window main_window) {
        Main_window = main_window;
        inflater = (LayoutInflater)main_window.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

// VARIABLES =======================================================================================
    // Global Variable Declarations
    private LayoutInflater inflater = null;
    Main_Window Main_window;

    // GUI Elements
    public class Holder {
        ImageView imgCrop;
        TextView txtLogID,
                txtCropName,
                txtPlantDate,
                txtHarvestRange;
        ImageButton btnDeleteLog;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Variable Declaration
        View rowView;
        Holder holder=new Holder();

        // Get current objects
        final List<Log> LogList = Main_window.LogList;
        List<Plant> PlantList = Main_window.PlantList;
        String plantImageFilePath = null;
        for (Plant currentPlant:PlantList) {
            if (LogList.get(position).getPlantID() == currentPlant.getPlantID()) {
                plantImageFilePath = currentPlant.getPhotoPath();
                break;
            }
        }

        // Inflate rowView
        rowView = inflater.inflate(R.layout.plantlogcustomlistadapter, null);

        // Connect GUI Elements
        holder.btnDeleteLog = rowView.findViewById(R.id.btnDeleteLog);
        holder.imgCrop = rowView.findViewById(R.id.imgCrop);
        holder.txtLogID = rowView.findViewById(R.id.txtLogID);
        holder.txtCropName = rowView.findViewById(R.id.txtCropName);
        holder.txtPlantDate = rowView.findViewById(R.id.txtDatePlanted);
        holder.txtHarvestRange = rowView.findViewById(R.id.txtExpectedHarvestRange);

        // Set GUI Elements
        holder.imgCrop.setImageURI(Uri.parse(plantImageFilePath));
        holder.txtLogID.setText(String.valueOf(LogList.get(position).getLogID()));
        holder.txtCropName.setText(LogList.get(position).getPlantName());
        holder.txtPlantDate.setText(LogList.get(position).getPlantDate());
        holder.txtHarvestRange.setText(LogList.get(position).getHarvestRange());

        // Attach onClickListener to rowView
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_window.setCurrLog(LogList.get(position));
                Main_window.changeFragment("LogNote");
            }
        });

        // Attach onClickListener to Delete Log Buttons
        holder.btnDeleteLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmationDialog(Main_window, LogList.get(position));
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        return Main_window.LogList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

// METHODS =========================================================================================
    private void openConfirmationDialog(Context context, final Log log) {
        new AlertDialog.Builder(context)
                .setTitle("Are you sure you want to delete log " + log.getLogID() + ": " + log.getPlantName() + "?")
                .setMessage(Html.fromHtml("This action cannot be undone."))

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Main_window.changeFragment("MainMenu");
                        Main_window.editTransaction("DeleteLog", log);
                        Main_window.makeToast("Log " + log.getLogID() + ": " + log.getPlantName() + " deleted.");
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
