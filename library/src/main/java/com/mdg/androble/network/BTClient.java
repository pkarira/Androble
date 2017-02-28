package com.mdg.androble.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.mdg.androble.listeners.ConnectionStatusListener;
import com.mdg.androble.listeners.MessageReceiveListener;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Pulkit Karira
 */

public class BTClient extends BTSocket{

    private boolean connected = false;
    private IOThread ioThread;
    private BluetoothSocket finalBluetoothSocket = null;

    private int clientId;

    private ConnectionStatusListener connectionStatusListener;
    private MessageReceiveListener messageReceiveListener;

    private StringBuilder sb = new StringBuilder();
    private int playerId = 0;


    public BTClient(ConnectionStatusListener connectionStatusListener,
                    MessageReceiveListener messageReceiveListener){
        super();

        this.connectionStatusListener = connectionStatusListener;
        this.messageReceiveListener = messageReceiveListener;
    }

    public int getClientId(){
        return clientId;
    }

    /**
     * @param deviceId id of the device which you want to connect
     */
    public void connect(String deviceId) {
        String MAC = deviceId.substring(deviceId.length() - 17);
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
        if (ioThread != null)
            ioThread.disconnect();
    }

    @Override
    public int getAllConnectedDevices() {
        writeMessageToThread(0, "(" + clientId + ")");
        return 0;
    }

    public void sendText(String s) {
        if (connected) {
            writeMessageToThread(0, clientId + ":" + s);
        }
    }

    @Override
    void getMessageFromThread(int threadId, String readMessage) {
        //decode the message
        sb.append(bluetoothAdapter.getName() + " " + "is" + " " + "SERVER" + "\n");
        if (readMessage.contains("/")) {
            sb.append(readMessage.substring(1) + " is " + (playerId + 1) + "\n");
            playerId++;
            recMsg.call("Connected to " + readMessage.substring(1));
        } else if (readMessage.contains("?")) {
            clientId = Integer.parseInt(readMessage.substring(1));
            recMsg.call("Your ID is " + readMessage.substring(1));
        } else if (readMessage.contains("<") && readMessage.contains(">")) {
            btServer.write(readMessage.substring(3),
                    Integer.parseInt(String.valueOf(readMessage.charAt(1))));
        } else if (readMessage.contains("(") && readMessage.contains(")")) {
            btServer.write(sb.substring(0),
                    Integer.parseInt(String.valueOf(readMessage.charAt(1))));
        } else{
            recMsg.call(readMessage);
        }

    }

    @Override
    void writeMessageToThread(int threadId, String message) {
        if (finalBluetoothSocket != null) {
            ioThread.write(message.getBytes());
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

    private void connectSuccessful(BluetoothSocket bluetoothSocket) {
        connected = true;
        ioThread = new IOThread(0, bluetoothSocket, this);
        ioThread.start();
        ioThread.write(("/" + bluetoothAdapter.getName()).getBytes());
        finalBluetoothSocket = bluetoothSocket;

        //notify to activity
        connectionStatusListener.onConnected(0);
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
                connectSuccessful(bluetoothSocket);
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
