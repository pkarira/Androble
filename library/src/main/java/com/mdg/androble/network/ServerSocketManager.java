package com.mdg.androble.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.mdg.androble.listeners.ConnectionStatusListener;
import com.mdg.androble.listeners.MessageReceiveListener;
import com.mdg.androble.utils.UuidGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Pulkit Karira, Deepankar Agrawal
 */

public class ServerSocketManager {

    private ArrayList<UUID> mUuids;
    private ListeningThread listeningThread;

    private int socketCounter = 0;
    private static BluetoothSocket bluetoothSockets[];
    private BluetoothAdapter bluetoothAdapter;
    private ServerSocket serverSockets[];

    private ConnectionStatusListener connectionStatusListener;
    private MessageReceiveListener messageReceiveListener;

    public ServerSocketManager(ConnectionStatusListener connectionStatusListener,
                               MessageReceiveListener messageReceiveListener){

        this.connectionStatusListener = connectionStatusListener;
        this.messageReceiveListener = messageReceiveListener;

        mUuids = UuidGenerator.generateUUIDs();
    }

    public void startConnection(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;

        bluetoothSockets = new BluetoothSocket[mUuids.size()];
        serverSockets = new ServerSocket[mUuids.size()];

        listeningThread = new ListeningThread();
        listeningThread.start();
    }

    private class ListeningThread extends Thread {
        BluetoothServerSocket bluetoothServerSocket;
        BluetoothServerSocket temp = null;

        public void run() {
            BluetoothSocket bluetoothSocket;
            boolean check = true;
            int recheckSocket = 0;

            for (int i = 0; i < mUuids.size(); i++) {
                try {
                    temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("pulkit", mUuids.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bluetoothServerSocket = temp;

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
