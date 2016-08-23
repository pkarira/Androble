package com.example.thispc.bluetooth_library;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by this pc on 02-08-2016.
 */
public class SocketManager extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mBluetoothSocket;
        BluetoothSocket mbluetoothSocket=null;
        public static String my_id=null;
        public static StringBuilder sb=new StringBuilder();
        int playerid=0;
        receivemsg recMsg;
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
             recMsg=new receivemsg();
             recMsg.addObserver((Observer) BluetoothManager.recieve_msg);
            byte[] buffer = new byte[1024];
            int bytes1=0;
             int bytes2=0;
            // Keep listening to the InputStream while connected
             while (true) {
                 try {
                     String readMessage ="";
                     bytes1 = mmInStream.read(buffer);
                     if(bytes1!=bytes2)
                     {
                         readMessage = new String(buffer, 0, bytes1);
                         if(readMessage.contains("/"))
                         {
                             sb.append(readMessage.substring(1)+" "+(playerid+1));
                             playerid++;
                             recMsg.call(readMessage.substring(1));}
                         else
                         if(readMessage.contains("?"))
                         {
                             my_id=readMessage.substring(1);
                             recMsg.call("Your ID is "+readMessage.substring(1));
                         }else
                         if(readMessage.contains("<"))
                         {
                             BluetoothManager.serverSocket.write(readMessage.substring(3),Integer.parseInt(String.valueOf(readMessage.charAt(1))));
                         }
                         else
                             recMsg.call(readMessage.substring(0));
                         bytes2=bytes1;
                     }
                 } catch (Exception e) {
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
    }
class receivemsg extends Observable {
    String message="";
    public void call(String s) {
        this.message=s;
        setChanged();
        notifyObservers(s);
    }
    public synchronized String getMessage() {
        return this.message;
    }
}


