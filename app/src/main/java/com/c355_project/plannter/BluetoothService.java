package com.c355_project.plannter;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    // Intent request codes
    private static final int    REQUEST_MAKE_DISCOVERABLE = 10;
    private static final int    REQUEST_ENABLE_BT = 11;
    private static final int    DISCOVERABLE_BT_REQUEST_CODE = 3;
    private static final int    DISCOVERABLE_DURATION = 300;

    public static String        EXTRA_DEVICE_ADDRESS = "device_address";

    private static final UUID   TEST_UUID = UUID.fromString("47ef049d-5347-473f-a143-2e1eed78df48");

    BluetoothAdapter                bluetoothAdapter;
    BluetoothDevice                 bluetoothDevice;

    BluetoothServerThread           bluetoothServerThread;
    BluetoothClientThread           bluetoothClientThread;
    BluetoothCommunicationThread    bluetoothCommunicationThread;

    Main_Window                     Main_Window_Instance;

    Fragment                        targetContext;

    Plant                           passThisPlant;

    public BluetoothService (Fragment xTargetContext) {
        System.out.println("[DEBUG]: BluetoothService(): Constructor Called!");

        targetContext = xTargetContext;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void setPlantToPassViaBluetooth ( Plant xPlant ) {
        passThisPlant = xPlant;
    }

    public boolean getDeviceState() {
        System.out.println("[DEBUG]: BluetoothService.getDeviceState(): Called");

        if (bluetoothAdapter == null) {
            System.out.println("[DEBUG]: BluetoothService.getDeviceState(): BluetoothAdapter == null: returning false");
            return false;
        } else {
            System.out.println("[DEBUG]: BluetoothService.getDeviceState(): BluetoothAdapter != null: Bluetooth Available: returning true");
            return true;
        }
    }

    public boolean enableBluetooth() {
        System.out.println("[DEBUG]: BluetoothService.enableBluetooth(): Called");

        if (bluetoothAdapter.isEnabled()) {
            System.out.println("[DEBUG]: BluetoothService.enableBluetooth(): Bluetooth is ENABLED, do the Next Step");
            return true;
        } else {
            System.out.println("[DEBUG]: BluetoothService.enableBluetooth(): Bluetooth NOT ENABLED, asking user to Enable Bluetooth...");
            Intent bluetoothRequestEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            targetContext.startActivityForResult(bluetoothRequestEnable, REQUEST_ENABLE_BT);
            System.out.println("[DEBUG]: BluetoothService.enableBluetooth(): targetContext.startActivityForResult(bluetoothRequestEnable, REQUEST_ENABLE_BT) called");
            return false;
        }
    }

    public void makeThisDeviceDiscoverable () {
        System.out.println("[DEBUG]: BluetoothService.makeThisDeviceDiscoverable(): Called");
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            //Makes the device Discoverable by other bluetooth devices for 300 seconds
            Intent discoverThisByBluetooth = new Intent (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverThisByBluetooth.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            targetContext.startActivityForResult(discoverThisByBluetooth, REQUEST_MAKE_DISCOVERABLE);
            System.out.println("[DEBUG]: BluetoothService.makeThisDeviceDiscoverable(): Starting the Activity-Intent to make the device discoverable to searching devices for 300 seconds");
        } else {
            System.out.println("[DEBUG]: BluetoothService.makeThisDeviceDiscoverable(): (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) returned false, Device Discovery not available");
        }
    }

    public void startBluetoothServerThread () {
        System.out.println("[DEBUG]: BluetoothService.startBluetoothServerThread(): Called");

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            ProgressDialog serverRunningDialog = new ProgressDialog(targetContext.getContext());

            serverRunningDialog.setTitle("Sharing Plant: " + passThisPlant.getPlantName());
            serverRunningDialog.setMessage("Press 'Stop Sharing' to stop...");
            serverRunningDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
            serverRunningDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Sharing", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopBluetooth();
                }
            });
            serverRunningDialog.show();


            bluetoothServerThread = new BluetoothServerThread();
            System.out.println("[DEBUG]: BluetoothService.bluetoothServerThread(): instantiated!");

            bluetoothServerThread.start();
            System.out.println("[DEBUG]: BluetoothService.startBluetoothServerThread(): Start the BluetoothServerThread Thread. This will make the device available for connecting");
        } else {
            System.out.println("[DEBUG]: BluetoothService.startBluetoothServerThread(): (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) returned false, Device Discovery not available");
        }
    }

    public void  stopBluetooth() {
        System.out.println("[DEBUG]: BluetoothService.stopBluetooth(): Called");

        if (bluetoothServerThread != null) {
            bluetoothServerThread.cancel();
            bluetoothServerThread = null;
        }
        if (bluetoothClientThread != null) {
            bluetoothClientThread.cancel();
            bluetoothClientThread = null;
        }
        if (bluetoothCommunicationThread != null) {
            bluetoothCommunicationThread.cancel();
            bluetoothCommunicationThread = null;
        }
    }





//BLUETOOTH SERVER THREAD SUB-CLASS ================================================================
    private class BluetoothServerThread extends Thread {
        private BluetoothServerSocket bluetoothServerSocket;

        public BluetoothServerThread() {
            System.out.println("[DEBUG]: BluetoothServerThread(): Constructor Called");
            System.out.println("[DEBUG]: BluetoothServerThread was Instantiated!");

            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothTest", TEST_UUID);

                System.out.println("[DEBUG]: BluetoothServerThread().bluetoothAdapter.listenUsingRfcommWithServiceRecord saved as a BluetoothServerSocket.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            System.out.println("[DEBUG]: BluetoothServerThread.run(): Called");
            System.out.println("[DEBUG]: BluetoothServerThread.run(): Running the new BluetoothServerThread!");

            BluetoothSocket bluetoothSocket = null;

            while (bluetoothSocket == null) {
                System.out.println("IM LOOKING FOR A POSSIBLE BLUETOOTH CONNECTION: " + bluetoothSocket);
                try {
                    bluetoothSocket = bluetoothServerSocket.accept();
                    System.out.println("[DEBUG]: BluetoothServerThread.run().BluetoothServerSocket.accept(): called!");
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            System.out.println("[DEBUG]: BluetoothServerThread.run().BluetoothServerSocket: Passed the While Loop!");

            if (bluetoothSocket != null) {
                bluetoothCommunicationThread = new BluetoothCommunicationThread(bluetoothSocket, passThisPlant);
                System.out.println("[DEBUG]: BluetoothServerThread().BluetoothCommunicationThread() instantiated!");

                bluetoothCommunicationThread.start();
                System.out.println("[DEBUG]: BluetoothCommunicationThread.start() method Called");
            }

            System.out.println("[DEBUG]: BluetoothServerThread.run().BluetoothServerSocket: Passed the if (bluetoothServerSocket != null) !");
        }

        public void cancel() {
            System.out.println("[DEBUG]: BluetoothServerThread.cancel(): Attempting to close the BluetoothSocket Connection");
            try {
                bluetoothServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private class BluetoothClientThread extends Thread {
        private BluetoothSocket bluetoothClientSocket;

        public BluetoothClientThread () {
            System.out.println("[DEBUG]: BluetoothClientThread(): constructor Called");
            System.out.println("[DEBUG]: BluetoothClientThread was instantiated!");
            try {
                bluetoothClientSocket = bluetoothDevice.createRfcommSocketToServiceRecord(TEST_UUID);

                System.out.println("[DEBUG]: BluetoothClientThread().bluetoothAdapter.createRfcommSocketToServiceRecord() saved as a BluetoothServerSocket.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run () {
            System.out.println("[DEBUG]: BluetoothClientThread.run() method Called");
            System.out.println("[DEBUG]: BluetoothAdapter.cancelDiscovery() called");
            System.out.println("[DEBUG]: Running the new BluetoothClientThread!");

            bluetoothAdapter.cancelDiscovery();

            if (bluetoothClientSocket != null) {
                try {
                    bluetoothClientSocket.connect();

                    System.out.println("[DEBUG]: BluetoothClientThread.run().BluetoothSocket.connect() Called!");

                    //Do something for SEND / RECEIVE
                    bluetoothCommunicationThread = new BluetoothCommunicationThread(bluetoothClientSocket, null);

                    System.out.println("[DEBUG]: BluetoothClientThread().BluetoothCommunicationThread() instantiated!");
                    System.out.println("[DEBUG]: BluetoothCommunicationThread.start() method Called");

                    bluetoothCommunicationThread.start();
                } catch (IOException e) {
                    try {
                        bluetoothClientSocket.close();
                        bluetoothClientSocket = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }

        public void cancel() {
            try {
                bluetoothClientSocket.close();
            } catch (IOException e) {

            }
        }
    }





    private class BluetoothCommunicationThread extends Thread {
        private final   BluetoothSocket bluetoothSocket;
        private final   InputStream bluetoothInputStream;
        private final   OutputStream bluetoothOutputStream;

        Plant           passThisPlant;

        public BluetoothCommunicationThread(BluetoothSocket xSocket, Plant xPlant) {
            System.out.println("[DEBUG]: BluetoothCommunicationThread(): Constructor Called");

            bluetoothSocket = xSocket;
            passThisPlant = xPlant;
            InputStream tmpBluetoothInputStream = null;
            OutputStream tmpBluetoothOutputStream = null;

            try {
                tmpBluetoothInputStream = bluetoothSocket.getInputStream();
                tmpBluetoothOutputStream = bluetoothSocket.getOutputStream();

                System.out.println("[DEBUG]: BluetoothCommunicationThread(): Both Input and Output Streams were created using the provided Bluetooth Socket.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            bluetoothInputStream = tmpBluetoothInputStream;
            bluetoothOutputStream = tmpBluetoothOutputStream;
        }

        public void run() {
            System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): Called");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): Entered WHILE Loop");

                if ( passThisPlant == null ) {
                    write();
                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): While(): Writing the Plant Information into the Output Stream");
                }

//                System.out.println("BluetoothCommunicationThread.write CALLED!");
//                System.out.println("Written Message: \n\t\t\t\t" + message);
//                System.out.println("Written Message in bytes: \n\t\t\t\t" + message.getBytes());

                try {
                    bytes = bluetoothInputStream.read(buffer);

                    String tempReceivedMessage = new String(buffer, 0, bytes);

                    System.out.println("\r\n==========================================");
                    System.out.println("BluetoothCommunicationThread.run() IN THE WHILE(true) LOOP");
                    System.out.println("Received Message: \n\t\t\t\t" + tempReceivedMessage);
                    System.out.println("Received Message in bytes: \n\t\t\t\t" + buffer);
                    System.out.println("==========================================");

                    Main_Window_Instance.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write() {
            System.out.println("[DEBUG]: BluetoothCommunicationThread.write(): Called!");

            String messagePlantInfo = "";

            //TODO: Figure Out How to send Plant Info
//            try {
//                bluetoothOutputStream.write();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {

            }
        }
    }
}
