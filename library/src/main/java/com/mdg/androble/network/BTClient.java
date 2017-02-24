package com.mdg.androble.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.mdg.androble.BluetoothManager;

import java.io.IOException;
import java.util.Observer;
import java.util.UUID;

/**
 * @author Pulkit Karira
 */

public class BTClient extends BTSocket{

    private ConnectingThread connectingThread;
    private String check = null;
    private ServerSocket serverSocket;
    private BluetoothSocket finalBluetoothSocket = null;
    private ReceiveMessage recMsg1;


    public BTClient(){
        super();
    }

    public void connect(String id) {
        recMsg1 = new ReceiveMessage();
        recMsg1.addObserver((Observer) BluetoothManager.recieve_msg);

        String MAC = id.substring(id.length() - 17);
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);
        for (int i = 0; i < uuids.size(); i++) {
            try {
                connectingThread = new ConnectingThread(bluetoothDevice, uuids.get(i));
                connectingThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void disconnect() {
        if (serverSocket != null)
            serverSocket.disconnect();
    }

    @Override
    public int getAllConnectedDevices() {
        return 0;
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
            if (bluetoothDevice != null) {
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

    private void connected(BluetoothSocket bluetoothSocket) {
        recMsg1.call("connected");
        serverSocket = new ServerSocket(bluetoothSocket);
        serverSocket.start();
        serverSocket.write(("/" + bluetoothAdapter.getName()).getBytes());
        finalBluetoothSocket = bluetoothSocket;
    }

    public void write(String s) {
        if (finalBluetoothSocket != null) {
            serverSocket.write(s.getBytes());
        }
    }
}
