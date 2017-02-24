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

    private int socketCounter = 0;
    private static BluetoothSocket bluetoothSockets[];
    private IOThread ioThreads[];

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
        ioThreads = new IOThread[uuids.size()];

        new ListeningThread().start();
    }

    @Override
    public void disconnect() {
        for (int i = 0; i < socketCounter; i++) {
            ioThreads[i].disconnect();

            //notify to main activity
            connectionStatusListener.onDisconnected(socketCounter);
        }
    }

    @Override
    public int getAllConnectedDevices() {
        String s = IOThread.sb.substring(0);
        return socketCounter;
    }

    @Override
    void getMessageFromThread(int threadId, String message) {
        //decode the message
        sb.append(bluetoothAdapter.getName() + " " + "is" + " " + "SERVER" + "\n");
        if (message.contains("/")) {
            sb.append(message.substring(1) + " is " + (playerId + 1) + "\n");
            playerId++;
            recMsg.call("Connected to " + message.substring(1));
        } else if (message.contains("?")) {
            clientId = Integer.parseInt(message.substring(1));
            recMsg.call("Your ID is " + message.substring(1));
        } else if (message.contains("<") && message.contains(">")) {
            btServer.write(message.substring(3),
                    Integer.parseInt(String.valueOf(message.charAt(1))));
        } else if (message.contains("(") && message.contains(")")) {
            btServer.write(sb.substring(0),
                    Integer.parseInt(String.valueOf(message.charAt(1))));
        } else{
            recMsg.call(message);
        }
    }

    @Override
    void writeMessageToThread(int threadId, String message) {
        if (threadId <= (getSocketCounter() + 1)) {
            ioThreads[threadId - 1].write(message.getBytes());
        }
    }

    public String getId() {
        return IOThread.myId;
    }

    public void sendText(String s, int id) {
        writeMessageToThread(id, s);
    }


    private synchronized BluetoothSocket connected(BluetoothSocket socket){
        return socket;
    }

    private int getSocketCounter(){
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
                        IOThread sm = new IOThread(socketCounter, bluetoothSockets[socketCounter], 
                                BTServer.this);
                        sm.start();
                        ioThreads[socketCounter] = sm;
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
