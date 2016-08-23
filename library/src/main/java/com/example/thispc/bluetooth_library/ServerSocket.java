package com.example.thispc.bluetooth_library;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by this pc on 02-08-2016.
 */
public class ServerSocket {
    public ArrayList<UUID> mUuids;
    ListeningThread listeningThread;
    boolean check;
    int recheckSocket=0;
    int socketCounter=0;
    public static BluetoothSocket blueSocket_array[];
    public BluetoothAdapter bluetoothAdapter;
    receivemsg recMsg1;
    SocketManager arraysm[];
    public void startConnection(BluetoothAdapter bluetoothAdapter1)
    {
        recMsg1=new receivemsg();
        recMsg1.addObserver((Observer) BluetoothManager.recieve_msg);
        blueSocket_array=new BluetoothSocket[4];
        mUuids= new ArrayList<UUID>();
        mUuids.add(UUID.fromString("b7746a40-c758-4868-aa19-7ac6b3475dfc"));
        mUuids.add(UUID.fromString("2d64189d-5a2c-4511-a074-77f199fd0834"));
        mUuids.add(UUID.fromString("e442e09a-51f3-4a7b-91cb-f638491d1412"));
        mUuids.add(UUID.fromString("a81d6504-4536-49ee-a475-7d96d09439e4"));
        bluetoothAdapter=bluetoothAdapter1;
        arraysm=new SocketManager[4];
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
                        if (bluetoothSocket.equals(blueSocket_array[i]))
                            recheckSocket++;
                    }
                    if (recheckSocket == 0) {
                        recMsg1.call("Connected to"+" "+(socketCounter+1));
                        blueSocket_array[socketCounter] = bluetoothSocket;
                        check = false;
                        connected(bluetoothSocket);
                        SocketManager sm=new SocketManager(blueSocket_array[socketCounter]);
                        sm.start();
                        arraysm[socketCounter]=sm;
                        sm.write(("?"+(socketCounter+1)).getBytes());
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
    public void write(String s,int n)
    {
        arraysm[n-1].write(s.getBytes());
    }
    public synchronized BluetoothSocket connected(BluetoothSocket socket) {
        return socket;
    }
}
