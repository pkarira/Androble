package com.mdg.androble;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observer;

/**
 * Created by this pc on 02-08-2016.
 */

class SocketManager extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final BluetoothSocket mBluetoothSocket;

    public static String my_id = null;
    public static StringBuilder sb = new StringBuilder();
    private int playerid = 0;
    ReceiveMsg recMsg;

    public SocketManager(BluetoothSocket socket) {
        mmSocket = socket;
        mBluetoothSocket = socket;
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
        recMsg = new ReceiveMsg();
        recMsg.addObserver((Observer) BluetoothManager.recieve_msg);
        byte[] buffer = new byte[1024];
        int bytes1 = 0;
        int bytes2 = 0;
        sb.append(ServerSocket.bluetoothAdapter.getName() + " " + "is" + " " + "SERVER" + "\n");
        // Keep listening to the InputStream while connected
        while (true) {
            try {
                String readMessage;
                bytes1 = mmInStream.read(buffer);
                if (bytes1 != bytes2) {
                    readMessage = new String(buffer, 0, bytes1);
                    if (readMessage.contains("/")) {
                        sb.append(readMessage.substring(1) + " " + "is" + " " + (playerid + 1) + "\n");
                        playerid++;
                        recMsg.call("Connected to " + readMessage.substring(1));
                    } else if (readMessage.contains("?")) {
                        my_id = readMessage.substring(1);
                        recMsg.call("Your ID is " + readMessage.substring(1));
                    } else if (readMessage.contains("<") && readMessage.contains(">")) {
                        BluetoothManager.serverSocket.write(readMessage.substring(3), Integer.parseInt(String.valueOf(readMessage.charAt(1))));
                    } else if (readMessage.contains("(") && readMessage.contains(")")) {
                        BluetoothManager.serverSocket.write(sb.substring(0), Integer.parseInt(String.valueOf(readMessage.charAt(1))));
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
            mmSocket.close();
        } catch (IOException e) {

        }
    }

    public void disconnect2() {
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

/*class ReceiveMsg extends Observable {
    String message = "";

    public void call(String s) {
        this.message = s;
        setChanged();
        notifyObservers(s);
    }

    public synchronized String getMessage() {
        return this.message;
    }
}*/


