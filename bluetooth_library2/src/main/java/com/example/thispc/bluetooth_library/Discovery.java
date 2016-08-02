package com.example.thispc.bluetooth_library;

/**
 * Created by this pc on 02-08-2016.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.UUID;

public class Discovery extends Activity {
    private static final int ENABLE_BT_REQUEST_CODE = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;
    private static final int Finished_Activity = 3;
    private static final int DISCOVERABLE_DURATION = 300;
    public BluetoothAdapter bluetoothAdapter;
    public ArrayList<UUID> mUuids;
    int b;
    boolean check;
    public static BluetoothSocket a[];
    private ArrayAdapter adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1);
    public void onBluetooth()
    {
        a = new BluetoothSocket[4];
        mUuids = new ArrayList<UUID>();
        mUuids.add(UUID.fromString("b7746a40-c758-4868-aa19-7ac6b3475dfc"));
        mUuids.add(UUID.fromString("2d64189d-5a2c-4511-a074-77f199fd0834"));
        mUuids.add(UUID.fromString("e442e09a-51f3-4a7b-91cb-f638491d1412"));
        mUuids.add(UUID.fromString("a81d6504-4536-49ee-a475-7d96d09439e4"));
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BT_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK)
            {
                makeDiscoverable();
                discoverDevices();

            }
        } else if (requestCode == DISCOVERABLE_BT_REQUEST_CODE) {
            if (resultCode == DISCOVERABLE_DURATION) {
              //  return "Your device is now discoverable for Server";
            } else {
               // return "Fail to enable discoverable mode.";
            }
        } else if (resultCode == Finished_Activity) {
            bluetoothAdapter.disable();
            adapter.clear();
        }
    }
    public String discoverDevices() {
        if (bluetoothAdapter.startDiscovery()) {
            return "Discovering peers";
        } else {
            return "Discovery failed to start.";
        }
    }

    protected void makeDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
        startActivityForResult(discoverableIntent, DISCOVERABLE_BT_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                adapter.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
            }
        }
    };
}
