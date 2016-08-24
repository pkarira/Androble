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

    enum ConnectionType{
        CLIENT,SERVER
    }
    ConnectionType Type;
    BluetoothSocket SocketForClient;
    public static ServerSocket serverSocket;
    ClientSocket clientSocket;
    public static Object recieve_msg;
    public static Object device_list;
    private static BluetoothManager bm=null;

    private BluetoothManager()
    {
        Log.e("pulkit", "in constructor");
        serverSocket=new ServerSocket();
        clientSocket=new ClientSocket();
    }
    public static BluetoothManager getInstance()
    {
        if(bm== null) {
            bm = new BluetoothManager();
        }
        return bm;
    }
    public void Type(String t)
    {
       Type=ConnectionType.valueOf(t);
    }
    public void setListObject(Object myObject)

    {
        device_list=myObject;
    }
    public void scanClients()

    {
        serverSocket.startConnection(Discovery.bluetoothAdapter);
    }
    public void setMessageObject(Object myObject)

    {
        recieve_msg=myObject;
    }
    public void connectTo(String s)
    {
        clientSocket.startConnection(Discovery.bluetoothAdapter, s);
    }

    public void sendText(String s)
    {
        if(clientSocket.check.equals(("connected"))&&Type.equals(ConnectionType.CLIENT))
        {
            clientSocket.write(SocketManager.my_id+":"+s);
        }
    }
    public String id()
    {
        return SocketManager.my_id ;
    }
    public void sendText(String s1,int id)
    {
       if(id<=(serverSocket.socketCounter+1))
       {
           serverSocket.write(s1,id);
       }
    }
    public void clientToClient(String s1,int id)
    {
        if(id<=(serverSocket.socketCounter+1))
        {
            clientSocket.write("<"+id+">"+s1);
        }
    }
    public String getAllConnectedDevices()
    {
        if(Type.equals(ConnectionType.CLIENT)) {
            clientSocket.write("(" + SocketManager.my_id + ")");
            return null;
        }
        else
        {
            return SocketManager.sb.substring(0);}
    }
}
