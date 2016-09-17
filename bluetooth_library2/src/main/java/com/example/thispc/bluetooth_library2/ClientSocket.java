package com.example.thispc.bluetooth_library2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by this pc on 02-08-2016.
 */
public class ClientSocket {
    public ArrayList<UUID> mUuids;
    public BluetoothAdapter bluetoothAdapter;
    ConnectingThread connectingThread;
    String check = null;
    SocketManager socketManager;
    BluetoothSocket finalBluetoothSocket = null;
    receivemsg recMsg1;

    public void startConnection(BluetoothAdapter a, String s) {
        recMsg1 = new receivemsg();
        recMsg1.addObserver((Observer) BluetoothManager.recieve_msg);
        bluetoothAdapter = a;
        mUuids = new ArrayList<UUID>();
        mUuids.add(UUID.fromString("b7746a40-c758-4868-aa19-7ac6b3475dfc"));
        mUuids.add(UUID.fromString("2d64189d-5a2c-4511-a074-77f199fd0834"));
        mUuids.add(UUID.fromString("e442e09a-51f3-4a7b-91cb-f638491d1412"));
        mUuids.add(UUID.fromString("a81d6504-4536-49ee-a475-7d96d09439e4"));
        String MAC = s.substring(s.length() - 17);
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);
        for (int i = 0; i < 4; i++) {
            try {
                connectingThread = new ConnectingThread(bluetoothDevice, mUuids.get(i));
                connectingThread.start();
            } catch (Exception e) {
            }
        }
    }

    private class ConnectingThread extends Thread {
        private final BluetoothDevice bluetoothDevice;
        private final BluetoothSocket bluetoothSocket;

        public ConnectingThread(BluetoothDevice device, UUID uuid) {

            BluetoothSocket temp = null;
            bluetoothDevice = device;
            try {
                temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bluetoothSocket = temp;
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
            } catch (IOException connectException) {
                connectException.printStackTrace();
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
            }
            if (bluetoothSocket != null && bluetoothDevice != null) {
                check = "connected";
                connected(bluetoothSocket);
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

    public void connected(BluetoothSocket bluetoothSocket) {
        recMsg1.call("connected");
        socketManager = new SocketManager(bluetoothSocket);
        socketManager.start();
        socketManager.write(("/" + bluetoothAdapter.getName()).getBytes());
        finalBluetoothSocket = bluetoothSocket;
    }

    public void write(String s) {
        if (finalBluetoothSocket != null) {
            socketManager.write(s.getBytes());
        }
    }

    public void disconnectClient() {
        if (socketManager != null)
            socketManager.disconnect2();
    }
}
