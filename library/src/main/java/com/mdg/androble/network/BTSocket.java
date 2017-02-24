package com.mdg.androble.network;

import android.bluetooth.BluetoothAdapter;

import com.mdg.androble.BluetoothManager;
import com.mdg.androble.utils.UuidGenerator;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Deepankar Agrawal
 */

abstract class BTSocket {

    protected BluetoothAdapter bluetoothAdapter;
    protected ArrayList<UUID> uuids;

    BTSocket(){
        bluetoothAdapter = BluetoothManager.getInstance().bluetoothAdapter;
        uuids = UuidGenerator.generateUUIDs();
    }

    public abstract void disconnect();

    public abstract int getAllConnectedDevices();

}
