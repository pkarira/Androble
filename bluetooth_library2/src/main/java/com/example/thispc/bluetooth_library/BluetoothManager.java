package com.example.thispc.bluetooth_library;

import android.bluetooth.BluetoothSocket;

/**
 * Created by this pc on 02-08-2016.
 */
public class BluetoothManager {

    String Type=null;
    Discovery d=new Discovery();
    BluetoothSocket SocketForClient;
    ServerSocket s=new ServerSocket();
    ClientSocket c=new ClientSocket();
    SocketManager sm;
    public void Type(String t)
    {
       Type=t;
    }
    public void scanDevices()
    {
        d.onBluetooth();
        if(Type.equalsIgnoreCase("server"))
        {
            s.startConnection(d.bluetoothAdapter);
        }
        if(Type.equalsIgnoreCase("client"))
        {
            //function for retuning list
        }
    }
    public void connectTo(String s)
    {
        c.startConnection(d.bluetoothAdapter, s);
    }
    public void sendText(String s)
    {
        if(c.check.equals(("connected"))&&Type.equalsIgnoreCase("client"))
        {
             c.write(s);
        }
    }
    public void sendText(String s1,int id)
    {
       if(id<=(s.a1+1))
       {
           s.write(s1,id);
       }
    }
}
