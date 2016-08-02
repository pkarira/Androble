package com.example.thispc.bluetooth_library;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by this pc on 02-08-2016.
 */
public class SocketManager extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mBluetoothSocket;
        BluetoothSocket mbluetoothSocket=null;

        public SocketManager(BluetoothSocket socket) {
            mmSocket = socket;
            mBluetoothSocket=socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {

            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
         public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            String score="0";
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    String readMessage = "";
                    bytes = mmInStream.read(buffer);
                    readMessage = new String(buffer, 0, bytes);

                } catch (Exception e) {
                }
            }
        }
       public void writetoClient(byte[] buffer) {
           try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public void writetoServer(byte[] buffer) {
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
    }

