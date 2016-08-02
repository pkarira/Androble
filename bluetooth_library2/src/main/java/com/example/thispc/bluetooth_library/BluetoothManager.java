package com.example.thispc.bluetooth_library;

/**
 * Created by this pc on 02-08-2016.
 */
public class BluetoothManager {

    String Type=null;
    Discovery d=new Discovery();
    ServerSocket s=new ServerSocket();
    public void Type(String t)
    {
        this.Type=t;
    }
    public void switchonBluetooth()
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


    }





}
