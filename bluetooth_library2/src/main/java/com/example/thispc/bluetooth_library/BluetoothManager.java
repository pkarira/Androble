package com.example.thispc.bluetooth_library;

/**
 * Created by this pc on 02-08-2016.
 */
public class BluetoothManager {

    String Type=null;
    Discovery d=new Discovery();
    public void Type(String t)
    {
        this.Type=t;
    }
    public void switchonBluetooth()
    {
        d.onBluetooth();
    }



}
