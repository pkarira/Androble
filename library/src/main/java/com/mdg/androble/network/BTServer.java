package com.mdg.androble.network;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.mdg.androble.listeners.ConnectionStatusListener;
import com.mdg.androble.listeners.MessageReceiveListener;

import java.io.IOException;

/**
 * @author Pulkit Karira, Deepankar Agrawal
 */

public class BTServer extends BTSocket{

    private ListeningThread listeningThread;

    private int socketCounter = 0;
    private static BluetoothSocket bluetoothSockets[];
    private IOThread IOThreads[];

    private ConnectionStatusListener connectionStatusListener;
    private MessageReceiveListener messageReceiveListener;

    public BTServer(ConnectionStatusListener connectionStatusListener,
                    MessageReceiveListener messageReceiveListener){
        super();

        this.connectionStatusListener = connectionStatusListener;
        this.messageReceiveListener = messageReceiveListener;
    }

    public void connect() {
        bluetoothSockets = new BluetoothSocket[uuids.size()];
        IOThreads = new IOThread[uuids.size()];

        listeningThread = new ListeningThread();
        listeningThread.start();
    }

    @Override
    public void disconnect() {
        for (int i = 0; i < socketCounter; i++) {
            IOThreads[i].disconnect();

            //notify to main activity
            connectionStatusListener.onDisconnected(socketCounter);
        }
    }

    @Override
    public int getAllConnectedDevices() {
        String s = IOThread.sb.substring(0);
        return socketCounter;
    }

    public String getId() {
        return IOThread.myId;
    }

    public void sendText(String s1, int id) {
        if (id <= (getSocketCounter() + 1)) {
            write(s1, id);
        }
    }

    public void write(String s, int n) {
        IOThreads[n - 1].write(s.getBytes());
    }

    public synchronized BluetoothSocket connected(BluetoothSocket socket){
        return socket;
    }

    public int getSocketCounter(){
        return socketCounter;
    }

    private class ListeningThread extends Thread {
        BluetoothServerSocket bluetoothServerSocket;
        BluetoothServerSocket temp = null;

        public void run() {
            BluetoothSocket bluetoothSocket;
            boolean check = true;
            int recheckSocket = 0;

            for (int i = 0; i < uuids.size(); i++) {
                try {
                    temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("pulkit", uuids.get(i));
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
                        IOThread sm = new IOThread(bluetoothSockets[socketCounter], BTServer.this);
                        sm.start();
                        IOThreads[socketCounter] = sm;
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

}
