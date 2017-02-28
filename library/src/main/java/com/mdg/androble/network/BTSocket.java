package com.mdg.androble.network;

import android.bluetooth.BluetoothAdapter;

import com.mdg.androble.BluetoothManager;
import com.mdg.androble.utils.UuidGenerator;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Deepankar Agrawal
 */

public abstract class BTSocket {

    BluetoothAdapter bluetoothAdapter;
    ArrayList<UUID> uuids;

    BTSocket(){
        bluetoothAdapter = BluetoothManager.getInstance().bluetoothAdapter;
        uuids = UuidGenerator.generateUUIDs();
    }

    public abstract void disconnect();

    public abstract int getAllConnectedDevices();

    /**
     * called by IOThread to send message to bluetooth socket
     *
     * @param threadId id of the thread to write to
     * @param message message data
     */
    abstract void getMessageFromThread(int threadId, String message);

    /**
     * converts the message into bytes array dumps to IOThread so
     * that it could be send to server or client
     *
     * @param message message data
     */
    abstract void writeMessageToThread(int threadId, String message);

}
