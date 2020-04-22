package com.c355_project.plannter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.UUID;

public class BluetoothService {
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 3;
    private static final int DISCOVERABLE_DURATION = 300;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private static final UUID TEST_UUID = UUID.fromString("47ef049d-5347-473f-a143-2e1eed78df48");

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;

//    BluetoothServerThread bluetoothServerThread;
//    BluetoothClientThread bluetoothClientThread;
//    BluetoothCommunicationThread bluetoothClientCommunicationThread;

    Main_Window Main_Window_Instance;

    String tempReceivedMessage;

    public BluetoothService (Main_Window xMain_Window) {

    }
}
