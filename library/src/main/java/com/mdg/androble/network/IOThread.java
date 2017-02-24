package com.mdg.androble.network;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Pulkit Karira
 */

class IOThread extends Thread {

    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final BluetoothSocket mBluetoothSocket;

    public static String myId = null;
    public static StringBuilder sb = new StringBuilder();
    private int playerId = 0;

    private BTServer btServer;

    public IOThread(BluetoothSocket socket, BTServer btServer) {
        mBluetoothSocket = socket;
        this.btServer = btServer;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {

        byte[] buffer = new byte[1024];
        int bytes1;
        int bytes2 = 0;
        sb.append(btServer.bluetoothAdapter.getName() + " " + "is" + " " + "SERVER" + "\n");
        // Keep listening to the InputStream while connected
        while (true) {
            try {
                String readMessage;
                bytes1 = mmInStream.read(buffer);
                if (bytes1 != bytes2) {
                    readMessage = new String(buffer, 0, bytes1);
                    if (readMessage.contains("/")) {
                        sb.append(readMessage.substring(1) + " is " + (playerId + 1) + "\n");
                        playerId++;
                        recMsg.call("Connected to " + readMessage.substring(1));
                    } else if (readMessage.contains("?")) {
                        myId = readMessage.substring(1);
                        recMsg.call("Your ID is " + readMessage.substring(1));
                    } else if (readMessage.contains("<") && readMessage.contains(">")) {
                        btServer.write(readMessage.substring(3),
                                Integer.parseInt(String.valueOf(readMessage.charAt(1))));
                    } else if (readMessage.contains("(") && readMessage.contains(")")) {
                        btServer.write(sb.substring(0),
                                Integer.parseInt(String.valueOf(readMessage.charAt(1))));
                    } else
                        recMsg.call(readMessage);
                    bytes2 = bytes1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] buffer) {
        try {
            mmOutStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        try {
            mBluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (mmInStream != null && mmOutStream != null) {
            try {
                mmInStream.close();
                mmOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


