package com.c355_project.plannter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothDeviceList extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String SELECTED_DEVICE = "Selected Device";

    BluetoothAdapter            bluetoothAdapter;
    Set<BluetoothDevice>        bluetoothDeviceSet;
    ArrayList<BluetoothDevice>  bluetoothDeviceArrayList;


    ArrayAdapter<String>        bluetoothDevicesArrayAdapter;

    TextView                    txtBluetoothDeviceListOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_bluetooth_device_list);

        setResult(Activity.RESULT_CANCELED);

        txtBluetoothDeviceListOutput = findViewById(R.id.txtBluetoothDeviceListOutput);
        findViewById(R.id.btnBluetoothScanForDevices).setOnClickListener(this);

        bluetoothDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.bluetooth_device_name_list_adapter);

        ListView listViewBluetoothDevices = findViewById(R.id.listViewBluetoothDevices);
        listViewBluetoothDevices.setAdapter(bluetoothDevicesArrayAdapter);
        listViewBluetoothDevices.setOnItemClickListener(this);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(bluetoothDiscoveryBroadcastReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(bluetoothDiscoveryBroadcastReceiver, filter);

        // Get the local Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (bluetoothDeviceSet.size() > 0) {
            for (BluetoothDevice device : bluetoothDeviceSet) {
                bluetoothDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                bluetoothDeviceArrayList.add(device);
                txtBluetoothDeviceListOutput.setText("Already paired devices found...");
            }
        } else {
            txtBluetoothDeviceListOutput.setText("No paired devices found... Scan for new devices!");
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(bluetoothDiscoveryBroadcastReceiver);
    }



//EVENT METHODS ====================================================================================
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btnBluetoothScanForDevices): {

            } break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> av, View view, int arg2, long arg3) {
        // Cancel discovery because it's costly and we're about to connect
        bluetoothAdapter.cancelDiscovery();

        BluetoothDevice passThisDevice = bluetoothDeviceArrayList.get(arg2);

        //TODO: Pass a Bluetooth Device Object rather than an address for a string
        // Create the result Intent and include the MAC address
        Intent intent = new Intent();
        intent.putExtra(SELECTED_DEVICE, passThisDevice);

        // Set result and finish this Activity
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private final BroadcastReceiver bluetoothDiscoveryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice newPairingDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (newPairingDevice != null && newPairingDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                    bluetoothDevicesArrayAdapter.add(newPairingDevice.getName());
                    bluetoothDeviceArrayList.add(newPairingDevice);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
//                setTitle(R.string.select_device);
//                if (bluetoothDevicesArrayAdapter.getCount() == 0) {
//                    String noDevices = getResources().getText(R.string.none_found).toString();
//                    bluetoothDevicesArrayAdapter.add(noDevices);
//                }
            }
        }
    };
}


