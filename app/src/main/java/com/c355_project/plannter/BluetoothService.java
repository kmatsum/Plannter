package com.c355_project.plannter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    // Intent request codes
    private static final int    REQUEST_CONNECT_DEVICE = 1;
    private static final int    REQUEST_ENABLE_BT = 2;
    private static final int    DISCOVERABLE_BT_REQUEST_CODE = 3;
    private static final int    DISCOVERABLE_DURATION = 300;

    public static String        EXTRA_DEVICE_ADDRESS = "device_address";

    private static final UUID   TEST_UUID = UUID.fromString("47ef049d-5347-473f-a143-2e1eed78df48");

    BluetoothAdapter            bluetoothAdapter;
    BluetoothDevice             bluetoothDevice;

    BluetoothServerThread bluetoothServerThread;
    BluetoothClientThread bluetoothClientThread;
    BluetoothCommunicationThread bluetoothCommunicationThread;

    Main_Window                 Main_Window_Instance;

    public BluetoothService (Main_Window xMain_Window, String xTypeConnection) {
        System.out.println("[DEBUG]: BluetoothService Constructor Called!");

        Main_Window_Instance = xMain_Window;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void makeThisDeviceDiscoverable () {
        System.out.println("[DEBUG]: makeThisDeviceDiscoverable() Called");
        System.out.println("[DEBUG]: Starting the Activity-Intent to make the device discoverable to searching devices for 300 seconds");

        //Makes the device Discoverable by other bluetooth devices for 300 seconds
        Intent discoverThisByBluetooth = new Intent (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverThisByBluetooth.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        Main_Window_Instance.startActivity(discoverThisByBluetooth);
    }

    public void  stopBluetooth() {
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
        private BluetoothServerSocket bluetoothSocket;

        public BluetoothServerThread() {
            System.out.println("[DEBUG]: BluetoothServerThread() Constructor Called");
            System.out.println("[DEBUG]: BluetoothServerThread was Instantiated!");

            try {
                bluetoothSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothTest", TEST_UUID);

                System.out.println("[DEBUG]: BluetoothServerThread().bluetoothAdapter.listenUsingRfcommWithServiceRecord saved as a BluetoothServerSocket.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            System.out.println("[DEBUG]: BluetoothServerThread.run() method Called");
            System.out.println("[DEBUG]: Running the new BluetoothServerThread!");

            BluetoothSocket bluetoothServerSocket = null;

            while (bluetoothServerSocket == null) {
                try {
                    bluetoothServerSocket = bluetoothSocket.accept();
                    System.out.println("[DEBUG]: BluetoothServerThread.run().BluetoothServerSocket.accept() called!");
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

            if (bluetoothServerSocket != null) {
                BluetoothCommunicationThread bluetoothServerCommunicationThread = new BluetoothCommunicationThread(bluetoothServerSocket, "This message is from the Bluetooth Server");
                System.out.println("[DEBUG]: BluetoothServerThread().BluetoothCommunicationThread() instantiated!");

                bluetoothServerCommunicationThread.start();
                System.out.println("[DEBUG]: BluetoothCommunicationThread.start() method Called");
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private class BluetoothClientThread extends Thread {
        private BluetoothSocket bluetoothClientSocket;

        public BluetoothClientThread () {
            System.out.println("==========================================");
            System.out.println("BluetoothClientThread() constructor Called");
            System.out.println("BluetoothClientThread was instantiated!");
            System.out.println("==========================================");
            try {
                bluetoothClientSocket = bluetoothDevice.createRfcommSocketToServiceRecord(TEST_UUID);

                System.out.println("==========================================");
                System.out.println("BluetoothClientThread().bluetoothAdapter.createRfcommSocketToServiceRecord() saved as a BluetoothServerSocket.");
                System.out.println("==========================================");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run () {
            System.out.println("==========================================");
            System.out.println("BluetoothClientThread.run() method Called");
            System.out.println("BluetoothAdapter.cancelDiscovery() called");
            System.out.println("Running the new BluetoothClientThread!");
            System.out.println("==========================================");

            bluetoothAdapter.cancelDiscovery();

            if (bluetoothClientSocket != null) {
                try {
                    bluetoothClientSocket.connect();

                    System.out.println("==========================================");
                    System.out.println("BluetoothClientThread.run().BluetoothSocket.connect() Called!");
                    System.out.println("==========================================");

                    //Do something for SEND / RECEIVE
                    bluetoothCommunicationThread = new BluetoothCommunicationThread(bluetoothClientSocket, "This is from the Bluetooth Client");
                    System.out.println("==========================================");
                    System.out.println("BluetoothClientThread().BluetoothCommuncationThread() instantiated!");
                    System.out.println("BluetoothCommunicationThread.start() method Called");
                    System.out.println("==========================================");
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
        private final BluetoothSocket bluetoothSocket;
        private final InputStream bluetoothInputStream;
        private final OutputStream bluetoothOutputStream;
        private final String message;

        public BluetoothCommunicationThread(BluetoothSocket xSocket, String xMessage) {
            System.out.println("==========================================");
            System.out.println("BluetoothCommunicationThread() Constructor Called");
            System.out.println("BluetoothCommunicationThread was Instantiated!");
            System.out.println("==========================================");

            bluetoothSocket = xSocket;
            message = xMessage;
            InputStream tmpBluetoothInputStream = null;
            OutputStream tmpBluetoothOutputStream = null;

            try {
                System.out.println("==========================================");
                System.out.println("BluetoothCommunicationThread()");
                System.out.println("Both Input and Output Streams were created using the provided Bluetooth Socket.");
                System.out.println("==========================================");

                tmpBluetoothInputStream = bluetoothSocket.getInputStream();
                tmpBluetoothOutputStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            bluetoothInputStream = tmpBluetoothInputStream;
            bluetoothOutputStream = tmpBluetoothOutputStream;
        }

        public void run() {
            System.out.println("\r\n==========================================");
            System.out.println("BluetoothCommunicationThread.run() method Called");
            System.out.println("Running the new BluetoothCommunicationThread!");
            System.out.println("==========================================");

            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                System.out.println("\r\n==========================================");
                System.out.println("BluetoothCommunicationThread WHILE LOOPPPPPPPPPPPPP");
                System.out.println("==========================================");

                write(message.getBytes());

                System.out.println("\r\n==========================================");
                System.out.println("BluetoothCommunicationThread.write CALLED!");
                System.out.println("Written Message: \n\t\t\t\t" + message);
                System.out.println("Written Message in bytes: \n\t\t\t\t" + message.getBytes());
                System.out.println("==========================================");

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

        public void write(byte[] buffer) {
            try {
                bluetoothOutputStream.write(buffer);

                System.out.println("==========================================");
                System.out.println("BluetoothCommunicationThread.write() Method Called!");
                System.out.println("Parameter buffer: \n\t\t\t\t\t" + buffer);
                System.out.println("==========================================");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {

            }
        }
    }
}
