package com.example.thispc.bluetooth_library;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;

/**
 * Created by this pc on 02-08-2016.
 */
public class BluetoothManager extends Activity{

    String Type=null;
    Discovery d=new Discovery();
    BluetoothSocket SocketForClient;
    ServerSocket s=new ServerSocket();
    ClientSocket c=new ClientSocket();
    public static Object recieve_msg;
    Boolean bn=true;
    final long end = System.nanoTime() + 30 * 1000 * 1000 * 1000L;
    int loop = 1;
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
    public ArrayAdapter<String> scanServer()
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
          return d.adapter;
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
    public int totalDevices()
    {
         return s.a1;
    }
    public void sendText(String s1,int id)
    {
       if(id<=(s.a1+1))
       {
           s.write(s1,id);
       }
    }
}
