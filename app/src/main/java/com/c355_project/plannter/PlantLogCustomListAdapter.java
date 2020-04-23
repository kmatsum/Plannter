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

    //Global Variable Declarations
    private LayoutInflater inflater = null;
    private Context context;
    Main_Window Main_window;

    public PlantLogCustomListAdapter (Main_Window main_window) {
        context = main_window;
        Main_window = main_window;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


    /*TODO: make holder follow tutorial here: https://gist.github.com/cesarferreira/4c4ae3841fee8894ccfd*/
    public class Holder {
        ImageView imgCrop;
        TextView txtLogID;
        TextView txtCropName;
        TextView txtPlantDate;
        TextView txtHarvestRange;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Variable Declaration
        View rowView;
        Holder holder=new Holder();
        ImageButton btnDeleteLog;

        //Variable Instantiation
        rowView = inflater.inflate(R.layout.plantlogcustomlistadapter, null);
        btnDeleteLog = rowView.findViewById(R.id.btnDeleteLog);
        holder.imgCrop = rowView.findViewById(R.id.imgCrop);
        holder.txtLogID = rowView.findViewById(R.id.txtLogID);
        holder.txtCropName = rowView.findViewById(R.id.txtCropName);
        holder.txtPlantDate = rowView.findViewById(R.id.txtDatePlanted);
        holder.txtHarvestRange = rowView.findViewById(R.id.txtExpectedHarvestRange);

        final List<Log> LogList = Main_window.LogList;
        List<Plant> PlantList = Main_window.PlantList;
        String plantImageFilePath = null;
        for (Plant currentPlant:PlantList) {
            if (LogList.get(position).getPlantID() == currentPlant.getPlantID()) {
                plantImageFilePath = currentPlant.getPhotoPath();
                break;
            }
        }
        holder.imgCrop.setImageURI(Uri.parse(plantImageFilePath));
        holder.txtLogID.setText(String.valueOf(LogList.get(position).getLogID()));
        holder.txtCropName.setText(LogList.get(position).getPlantName());
        holder.txtPlantDate.setText(LogList.get(position).getPlantDate());
        holder.txtHarvestRange.setText(LogList.get(position).getHarvestRange());

        //Attaches onClickListener to Voice Memo Buttons
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main_window.setCurrLog(LogList.get(position));
                Main_window.changeFragment("Notes");
            }
        });

        //Attaches onClickListener to Delete Log Buttons
        btnDeleteLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmationDialog(context, LogList.get(position));
            }
        });

        return rowView;
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
