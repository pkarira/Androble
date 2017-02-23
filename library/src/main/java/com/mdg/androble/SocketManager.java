package com.mdg.androble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by this pc on 02-08-2016.
 */

public class SocketManager {

    public ArrayList<UUID> mUuids;
    String[] mUUIDStrings;

    ListeningThread listeningThread;
    boolean check;
    int recheckSocket = 0;
    int socketCounter = 0;
    public static BluetoothSocket bluetoothSockets[];
    public static BluetoothAdapter bluetoothAdapter;
    ReceiveMessage recMsg1;
    ServerSocket serverSockets[];

    public Context context;

    public SocketManager(Context context){
        this.context = context;
    }

    public void startConnection(BluetoothAdapter bluetoothAdapter1) {
        recMsg1 = new ReceiveMessage();
        recMsg1.addObserver((Observer) BluetoothManager.recieve_msg);
        bluetoothSockets = new BluetoothSocket[mUUIDStrings.length];

        /*
            add uuid string array to list
         */
        mUuids = new ArrayList<>();
        mUUIDStrings = context.getResources().getStringArray(R.array.uuid_strings);
        for(int i=0;i<mUUIDStrings.length;i++){
            mUuids.add(UUID.fromString(mUUIDStrings[i]));
        }
        bluetoothAdapter = bluetoothAdapter1;
        serverSockets = new ServerSocket[4];
        listeningThread = new ListeningThread();
        listeningThread.start();
    }

    private class ListeningThread extends Thread {
        BluetoothServerSocket bluetoothServerSocket;
        BluetoothServerSocket temp = null;

        public ListeningThread() {
        }

        public void run() {
            BluetoothSocket bluetoothSocket = null;
            for (int i = 0; i < mUuids.size(); i++) {
                try {
                    temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("pulkit", mUuids.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                check = true;
                bluetoothServerSocket = temp;
                recheckSocket = 0;
                while (check) {
                    try {
                        bluetoothSocket = bluetoothServerSocket.accept();
                    } catch (IOException e) {
                        break;
                    }
                    for (i = 0; i < socketCounter; i++) {
                        if (bluetoothSocket.equals(bluetoothSockets[i]))
                            recheckSocket++;
                    }
                    if (recheckSocket == 0) {
                        recMsg1.call("Connected to" + " " +(socketCounter + 1));
                        bluetoothSockets[socketCounter] = bluetoothSocket;
                        check = false;
                        connected(bluetoothSocket);
                        ServerSocket sm = new ServerSocket(bluetoothSockets[socketCounter]);
                        sm.start();
                        serverSockets[socketCounter] = sm;
                        sm.write(("?" + (socketCounter + 1)).getBytes());
                        socketCounter++;
                    }
                }
                try {
                    bluetoothServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void write(String s, int n) {
        serverSockets[n - 1].write(s.getBytes());
    }

    public synchronized BluetoothSocket connected(BluetoothSocket socket){
        return socket;
    }

    public void disconnectServer() {
        for (int i = 0; i < socketCounter; i++) {
            serverSockets[i].disconnect2();
        }
    }
}
