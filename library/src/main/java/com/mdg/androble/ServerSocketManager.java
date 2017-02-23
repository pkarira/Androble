package com.mdg.androble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.mdg.androble.listeners.MessageReceiveListener;
import com.mdg.androble.listeners.ConnectionStatusListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.UUID;

/**
 * @author Pulkit Karira, Deepankar Agrawal
 */

public class ServerSocketManager {

    private ArrayList<UUID> mUuids;
    private String[] mUUIDStrings;
    ListeningThread listeningThread;

    private int recheckSocket = 0;
    private int socketCounter = 0;
    private static BluetoothSocket bluetoothSockets[];
    private BluetoothAdapter bluetoothAdapter;
    private ServerSocket serverSockets[];
    private Context context;

    private ConnectionStatusListener connectionStatusListener;
    private MessageReceiveListener messageReceiveListener;

    public ServerSocketManager(Context context, ConnectionStatusListener connectionStatusListener,
                               MessageReceiveListener messageReceiveListener){
        this.context = context;
        this.connectionStatusListener = connectionStatusListener;
        this.messageReceiveListener = messageReceiveListener;
    }

    public void startConnection(BluetoothAdapter bluetoothAdapter) {

        bluetoothSockets = new BluetoothSocket[mUUIDStrings.length];

        /*  add uuid string array to list
         */
        mUuids = new ArrayList<>();
        mUUIDStrings = context.getResources().getStringArray(R.array.uuid_strings);
        for(int i=0;i<mUUIDStrings.length;i++){
            mUuids.add(UUID.fromString(mUUIDStrings[i]));
        }
        this.bluetoothAdapter = bluetoothAdapter;
        serverSockets = new ServerSocket[mUUIDStrings.length];

        listeningThread = new ListeningThread();
        listeningThread.start();
    }

    private class ListeningThread extends Thread {
        BluetoothServerSocket bluetoothServerSocket;
        BluetoothServerSocket temp = null;

        public ListeningThread() {
        }

        public void run() {
            BluetoothSocket bluetoothSocket;
            boolean check;

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
                        //notify to main activity
                        connectionStatusListener.onConnected(socketCounter);

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
            serverSockets[i].disconnect();

            //notify to main activity
            connectionStatusListener.onDisconnected(socketCounter);
        }
    }

    public int getSocketCounter(){
        return socketCounter;
    }

}
