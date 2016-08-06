package com.example.thispc.bluetooth_library;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by this pc on 02-08-2016.
 */
public class BluetoothManager{

    String Type=null;
    BluetoothSocket SocketForClient;
    ServerSocket s;
    ClientSocket c;
    public static Object recieve_msg;
    public static Object device_list;
    Boolean bn=true;
    public BluetoothManager()
    {
        Log.e("pulkit", "in constructor");
        s=new ServerSocket();
        c=new ClientSocket();
    }
    public void Type(String t)
    {
       Type=t;
    }
    public void setListObject(Object myObject)
    {
        device_list=myObject;
    }
    public void scanClients()
    {
        s.startConnection(Discovery.bluetoothAdapter);
    }
    public void setMessageObject(Object myObject)
    {
        recieve_msg=myObject;
    }
    public void connectTo(String s)
    {
        c.startConnection(Discovery.bluetoothAdapter, s);
    }

    public void sendText(String s)
    {
        if(c.check.equals(("connected"))&&Type.equalsIgnoreCase("client"))
        {
             c.write(s);
        }
    }
    public StringBuilder deviceList()
    {
         return SocketManager.sb;
    }
    public void sendText(String s1,int id)
    {
       if(id<=(s.a1+1))
       {
           s.write(s1,id);
       }
    }
}
