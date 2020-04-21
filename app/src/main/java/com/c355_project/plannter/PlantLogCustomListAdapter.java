package com.c355_project.plannter;

import android.content.Context;
import android.net.Uri;
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

        //Variable Instantiation
        rowView = inflater.inflate(R.layout.plantlogcustomlistadapter, null);
        ImageButton btnVoiceMemo = rowView.findViewById(R.id.btnOpenVoiceMemo);
        ImageButton btnDeleteLogEntry = rowView.findViewById(R.id.btnDeleteLog);
        holder.imgCrop = rowView.findViewById(R.id.imgCrop);
        holder.txtLogID = rowView.findViewById(R.id.txtLogID);
        holder.txtCropName = rowView.findViewById(R.id.txtCropName);
        holder.txtPlantDate = rowView.findViewById(R.id.txtDatePlanted);
        holder.txtHarvestRange = rowView.findViewById(R.id.txtExpectedHarvestRange);

        //TODO:Set Plant Images, Plant Names, DatePlanted & Harvest Rages
        List<Log> LogList = Main_window.LogList;
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
        btnVoiceMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Open Notes Fragment
                switch (view.getId())
                {
                    case(R.id.btnOpenVoiceMemo):
                    {
                        Main_window.changeFragment("Notes");
                        Main_window.currentLogID = position + 1;
                    }
                }
            }
        });

        //Attaches onClickListener to TTS Buttons
        btnDeleteLogEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Delete Log Entry From DB
            }
        });

        return rowView;
    }
}
