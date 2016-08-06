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
    Discovery d;
    BluetoothSocket SocketForClient;
    ServerSocket s;
    ClientSocket c;
    public static Object recieve_msg;
    Boolean bn=true;
    final long end = System.nanoTime() + 30 * 1000 * 1000 * 1000L;
    int loop = 1;
    public BluetoothManager()
    {
        Log.e("pulkit", "in constructor");
        d=new Discovery();
        s=new ServerSocket();
        c=new ClientSocket();
    }
    public void Type(String t)
    {
       Type=t;
    }
    public void scanClients()
    {
        d.onBluetooth();
        s.startConnection(d.bluetoothAdapter);
    }
    public void setObject(Object myObject)
    {
        recieve_msg=myObject;
    }
    public ArrayList<String> scanServer()
    {
        d.onBluetooth();
           while(bn)
           {
               if(d.discoverymode==true)
               {
                   bn=false;
               }
           }
        do {
            for (int i=0; i<loop; ++i) {

            }
            loop++;
        } while (System.nanoTime() < end);
          return d.list;
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
