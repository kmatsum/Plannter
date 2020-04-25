package com.c355_project.plannter;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
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

    BluetoothServerThread           bluetoothServerThread;
    BluetoothClientThread           bluetoothClientThread;
    BluetoothCommunicationThread    bluetoothCommunicationThread;

    Main_Window                     Main_Window_Instance;
    ProgressDialog                  clientRunningDialog;

    Fragment                        targetContext;

    Frag_addPlants                  targetFrag_addPlants;

    Plant                           passThisPlant;

    public BluetoothService (Fragment xTargetContext, String bluetoothRole) {
        System.out.println("[DEBUG]: BluetoothService(): Constructor Called!");

        targetContext = xTargetContext;

        if (bluetoothRole.equals("CLIENT")) {
            targetFrag_addPlants = (Frag_addPlants) xTargetContext;
            Main_Window_Instance = targetFrag_addPlants.Main_Window;
        } else {
            targetFrag_addPlants = null;
            Main_Window_Instance = null;
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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

    public void startBluetoothServerThread ( Plant xPlant ) {
        System.out.println("[DEBUG]: BluetoothService.startBluetoothServerThread(): Called");

        passThisPlant = xPlant;

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            System.out.println("[DEBUG]: Creating ProgressDialog...");
            ProgressDialog serverRunningDialog = new ProgressDialog(targetContext.getContext());
            serverRunningDialog.setTitle("Sharing Plant: " + passThisPlant.getPlantName());
            serverRunningDialog.setMessage("Press 'Stop Sharing' to stop...");
            serverRunningDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
            serverRunningDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Stop Sharing", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("[DEBUG]: startBluetoothServerThread.serverRunningDialog.BUTTON_ON_CLICK Called! Stopping Bluetooth Threads!");
                    stopBluetooth();
                }
            });
            System.out.println("[DEBUG]: Show ProgressDialog...");
            serverRunningDialog.show();


            bluetoothServerThread = new BluetoothServerThread();
            System.out.println("[DEBUG]: BluetoothService.bluetoothServerThread(): instantiated!");

            bluetoothServerThread.start();
            System.out.println("[DEBUG]: BluetoothService.startBluetoothServerThread(): Start the BluetoothServerThread Thread. This will make the device available for connecting");
        } else {
            System.out.println("[DEBUG]: BluetoothService.startBluetoothServerThread(): (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) returned false, Device Discovery not available");
        }
    }



    public void startBluetoothClientThread ( BluetoothDevice xBluetoothDevice ) {
        System.out.println("[DEBUG]: BluetoothService.startBluetoothClientThread(): Called");

        BluetoothDevice bluetoothDevice = xBluetoothDevice;

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            System.out.println("[DEBUG]: Creating ProgressDialog...");
            clientRunningDialog = new ProgressDialog(targetContext.getContext());
            clientRunningDialog.setTitle("Attempting to connect to " + bluetoothDevice.getName() + "...");
            clientRunningDialog.setMessage("Press 'Cancel' to stop...");
            clientRunningDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
            clientRunningDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("[DEBUG]: startBluetoothServerThread.serverRunningDialog.BUTTON_ON_CLICK Called! Stopping Bluetooth Threads!");
                    stopBluetooth();
                }
            });
            System.out.println("[DEBUG]: Show ProgressDialog...");
            clientRunningDialog.show();


            bluetoothClientThread = new BluetoothClientThread(bluetoothDevice);
            System.out.println("[DEBUG]: BluetoothService.bluetoothServerThread(): instantiated!");

            bluetoothClientThread.start();
            System.out.println("[DEBUG]: BluetoothService.startBluetoothClientThread(): Start the BluetoothClientThread Thread. This will make the device attempt to connect");
        } else {
            System.out.println("[DEBUG]: BluetoothService.startBluetoothClientThread(): (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) returned false, Device does not have Bluetooth");
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
        BluetoothDevice         bluetoothDevice;

        public BluetoothClientThread (BluetoothDevice xBluetoothDevice) {
            System.out.println("[DEBUG]: BluetoothClientThread(): constructor Called");
            System.out.println("[DEBUG]: BluetoothClientThread was instantiated!");

            bluetoothDevice = xBluetoothDevice;

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
            if (bluetoothClientSocket != null) {
                try {
                    bluetoothClientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

            //TEST
            Boolean doneCommunicating = false;

            // Keep listening to the InputStream while connected
            while (true) {
                System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): Entered WHILE Loop");

                if ( passThisPlant != null ) {
                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): While(): Writing the Plant Information into the Output Stream");
                    write("PLANT");
                } else {
                    if (doneCommunicating) {
                        write("Client");
                        System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): PLANT IS NULL: doneCommunicating was TRUE!");
                    } else {
                        System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): PLANT IS NULL: doneCommunicating was FALSE!");
                    }
                }

//                System.out.println("BluetoothCommunicationThread.write CALLED!");
//                System.out.println("Written Message: \n\t\t\t\t" + message);
//                System.out.println("Written Message in bytes: \n\t\t\t\t" + message.getBytes());

                try {
                    bytes = bluetoothInputStream.read(buffer);
                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): bluetoothInputStream read something...");
                    doneCommunicating = true;

                    if (targetFrag_addPlants != null) {
                        SerializablePlant receivedSerializablePlant = null;

                        try {
                            receivedSerializablePlant = deserialize(buffer);
                            System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): INPUT received: deserialization success!");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        if (receivedSerializablePlant != null) {
                            final SerializablePlant finalReceivedSerializablePlant = receivedSerializablePlant;
                            System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): if (receivedSerializablePlant != null) reached! Unpacking Serialized Plant");

                            Main_Window_Instance.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): if (receivedSerializablePlant != null): runOnUiThread: UNPACKING PLANT");

                                    targetFrag_addPlants.txtName.setText(finalReceivedSerializablePlant.plantName.toString());
                                    targetFrag_addPlants.txtSeedCompany.setText(finalReceivedSerializablePlant.seedCompany.toString());
                                    targetFrag_addPlants.txtFirstPlantDate.setText(finalReceivedSerializablePlant.firstPlantDate);
                                    targetFrag_addPlants.txtFirstPlantDate.setText(finalReceivedSerializablePlant.weeksToHarvest);
                                    targetFrag_addPlants.txtHarvestRange.setText(finalReceivedSerializablePlant.harvestRange);
                                    targetFrag_addPlants.txtLastPlantDate.setText(finalReceivedSerializablePlant.lastPlantDate);

                                    if (finalReceivedSerializablePlant.seedIndoorDate == 52) {
                                        targetFrag_addPlants.toggleButton.setChecked(false);
                                    } else {
                                        targetFrag_addPlants.toggleButton.setChecked(true);
                                        targetFrag_addPlants.txtSeedIndoors.setText(finalReceivedSerializablePlant.seedIndoorDate);
                                    }

                                    targetFrag_addPlants.txtSeedDistance.setText(finalReceivedSerializablePlant.distBetweenPlants);
                                    targetFrag_addPlants.txtSeedDepth.setText(Double.toString(finalReceivedSerializablePlant.seedDepth));

                                    if (finalReceivedSerializablePlant.raisedRows) {
                                        targetFrag_addPlants.rbRaisedRows.setChecked(true);
                                    } else if (finalReceivedSerializablePlant.raisedHills) {
                                        targetFrag_addPlants.rbRaisedHills.setChecked(true);
                                    } else {
                                        targetFrag_addPlants.rbFlat.setChecked(true);
                                    }

                                    targetFrag_addPlants.txtNotes.setText(finalReceivedSerializablePlant.notes.toString());

                                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): runOnUiThread(): GUI UPDATED");

                                    cancel();
                                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): runOnUiThread(): BluetoothService.cancel() called!");

                                    clientRunningDialog.dismiss();
                                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): close the Dialog Box");
                                }
                            });
                        }
                    } else {
                        final String tempReceivedMessage = new String(buffer, 0, bytes);

                        System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): IN THE WHILE(true) LOOP");
                        System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): Received Message: \n\t\t\t\t" + tempReceivedMessage);
                        System.out.println("[DEBUG]: BluetoothCommunicationThread.run(): Received Message in bytes: \n\t\t\t\t" + buffer);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(String xMessage) {
            System.out.println("[DEBUG]: BluetoothCommunicationThread.write(): Called!");


            //TODO: Figure Out How to send Plant Info
            if (xMessage.equals( "PLANT" )) {
                SerializablePlant passThisSerializablePlant = new SerializablePlant(passThisPlant.getPlantName().toCharArray(),passThisPlant.getSeedCompany().toCharArray(),passThisPlant.getFirstPlantDate(),passThisPlant.getWeeksToHarvest(),passThisPlant.getHarvestRange(),passThisPlant.getSeedIndoorDate(),passThisPlant.getLastPlantDate(),passThisPlant.getNotes().toCharArray(),"".toCharArray(),passThisPlant.isRaisedRows(),passThisPlant.isRaisedHills(),passThisPlant.getDistBetweenPlants(),passThisPlant.getSeedDepth());
                try {
                    bluetoothOutputStream.write(passThisSerializablePlant.serialize());
                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run().write(): SENT THE SERIALIZED PLANT");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    String response = xMessage + ": GOT YO MESSAGE BOIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII";
                    bluetoothOutputStream.write(response.getBytes());
                    System.out.println("[DEBUG]: BluetoothCommunicationThread.run().write(): WROTE THIS MESSAGE: " + response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancel() {
            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close();
                    bluetoothInputStream.close();
                    bluetoothOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static SerializablePlant deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (SerializablePlant) o.readObject();
    }



    public class SerializablePlant implements Serializable {
        private char[] plantName;
        private char[] seedCompany;
        private int firstPlantDate;
        private int weeksToHarvest;
        private int harvestRange;
        private int seedIndoorDate;
        private int lastPlantDate;
        private char[] notes;
        private char[] photoPath;
        private boolean raisedRows;
        private boolean raisedHills;
        private int distBetweenPlants;
        private double seedDepth;

        public SerializablePlant(char[] plantName, char[] seedCompany, int firstPlantDate,
                                 int weeksToHarvest, int harvestRange, int seedIndoorDate, int lastPlantDate,
                                 char[] notes, char[] photoPath, boolean raisedRows, boolean raisedHills,
                                 int distBetweenPlants, double seedDepth)
        {
            this.plantName = plantName;
            this.seedCompany = seedCompany;
            this.firstPlantDate = firstPlantDate;
            this.weeksToHarvest = weeksToHarvest;
            this.harvestRange = harvestRange;
            this.seedIndoorDate = seedIndoorDate;
            this.lastPlantDate = lastPlantDate;
            this.notes = notes;
            this.photoPath = photoPath;
            this.raisedRows = raisedRows;
            this.raisedHills = raisedHills;
            this.distBetweenPlants = distBetweenPlants;
            this.seedDepth = seedDepth;
        }

        public byte[] serialize() throws IOException {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArray);
            objectOutputStream.writeObject(SerializablePlant.this);
            System.out.println("[DEBUG]: serializablePlant was Serialized...");
            return byteArray.toByteArray();
        }
    }
}
