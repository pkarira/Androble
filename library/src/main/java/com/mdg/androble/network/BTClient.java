package com.mdg.androble.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Pulkit Karira
 */

public class BTClient extends BTSocket{

    private String check = null;
    private IOThread IOThread;
    private BluetoothSocket finalBluetoothSocket = null;


    public BTClient(){
        super();
    }

    /**
     *
     * @param id id of the device which you want to connect
     */
    public void connect(String id) {
        String MAC = id.substring(id.length() - 17);
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);
        for (int i = 0; i < uuids.size(); i++) {
            try {
                ConnectingThread connectingThread = new ConnectingThread(bluetoothDevice,
                        uuids.get(i));
                connectingThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void disconnect() {
        if (IOThread != null)
            IOThread.disconnect();
    }

    @Override
    public int getAllConnectedDevices() {
        write("(" + IOThread.myId + ")");
        return 0;
    }

    public void sendText(String s) {
        if (check.equals(("connected"))) {
            write(IOThread.myId + ":" + s);
        }
    }

    public void write(String s) {
        if (finalBluetoothSocket != null) {
            IOThread.write(s.getBytes());
        }
    }

    /**
     * current implementation will not work, becauz at a time we will have only
     * one type of connection type, server or client
     */
    public void clientToClient(String s1, int id) {
//        if (id <= (btServer.getSocketCounter() + 1)) {
//            write("<" + id + ">" + s1);
//        }
    }

    private void connected(BluetoothSocket bluetoothSocket) {
        recMsg1.call("connected");
        IOThread = new IOThread(bluetoothSocket);
        IOThread.start();
        IOThread.write(("/" + bluetoothAdapter.getName()).getBytes());
        finalBluetoothSocket = bluetoothSocket;
    }

    private class ConnectingThread extends Thread {
        private final BluetoothDevice bluetoothDevice;
        private final BluetoothSocket bluetoothSocket;

        ConnectingThread(BluetoothDevice device, UUID uuid) {

            BluetoothSocket temp = null;
            bluetoothDevice = device;
            try {
                temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                e.printStackTrace();
                cancel();
            }
            bluetoothSocket = temp;
        }

        @Override
        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
            } catch (IOException connectException) {
                connectException.printStackTrace();
                cancel();
            }
            if (bluetoothDevice != null) {
                check = "connected";
                connected(bluetoothSocket);
            }
        }

        void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
