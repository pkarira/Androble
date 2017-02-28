package com.mdg.androble.network;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Pulkit Karira
 */

class IOThread extends Thread {

    private int mThreadId;

    private final InputStream mInStream;
    private final OutputStream mOutStream;
    private final BluetoothSocket mBluetoothSocket;
    private BTSocket mBTSocket;

    IOThread(int threadId, BluetoothSocket socket, BTSocket btSocket) {
        mThreadId = threadId;
        mBluetoothSocket = socket;
        mBTSocket = btSocket;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mInStream = tmpIn;
        mOutStream = tmpOut;
    }

    public int getThreadId(){
        return mThreadId;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {

        byte[] buffer = new byte[1024];
        int bytes1;
        int bytes2 = 0;

        // Keep listening to the InputStream while connected
        while(true) {
            try {
                String readMessage;
                bytes1 = mInStream.read(buffer);
                if (bytes1 != bytes2) {
                    readMessage = new String(buffer, 0, bytes1);
                    mBTSocket.getMessageFromThread(mThreadId, readMessage);
                    bytes2 = bytes1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                disconnect();
            }
        }
    }

    void write(byte[] buffer) {
        try {
            mOutStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void disconnect() {
        if (mInStream != null && mOutStream != null) {
            try {
                mInStream.close();
                mOutStream.close();
                mBluetoothSocket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


