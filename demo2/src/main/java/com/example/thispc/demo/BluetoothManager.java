package com.example.thispc.demo;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothManager {

    enum ConnectionType{
        client,server
    }
    ConnectionType Type;
    BluetoothSocket SocketForClient;
    ServerSocket serverSocket;
    ClientSocket clientSocket;
    public static Object recieve_msg;
    public static Object device_list;
    boolean bn=true;
    private static BluetoothManager bm=null;

    public static BluetoothManager getInstance()
    {
        if(bm== null) {
            bm = new BluetoothManager();
        }
        return bm;
    }
    private BluetoothManager()
    {
        Log.e("pulkit", "in constructor");
        serverSocket=new ServerSocket();
        clientSocket=new ClientSocket();
    }
    public void Type(String t)
    {
        Type= ConnectionType.valueOf(t);
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
            clientSocket.write(s);
    }
    public StringBuilder deviceList()
    {
        return SocketManager.sb;
    }
    public void sendText(String s1,int id)
    {
       // if(id<=(serverSocket.a1+1))
        {
            serverSocket.write(s1,id);
        }
    }
}
