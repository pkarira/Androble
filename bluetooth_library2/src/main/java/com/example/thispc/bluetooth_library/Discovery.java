package com.example.thispc.bluetooth_library;

/**
 * Created by this pc on 02-08-2016.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Observable;


public class Discovery extends Activity {
    private static final int ENABLE_BT_REQUEST_CODE = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;
    private static final int Finished_Activity = 3;
    private static final int DISCOVERABLE_DURATION = 300;
    public BluetoothAdapter bluetoothAdapter;
    Boolean discoverymode=false;
        public ArrayList<String> list;
    public receiveadapter ra;
    public void onBluetooth()
    {
        ra=new receiveadapter();
       list= new ArrayList<String>();
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
                   discoverymode=true;
            } else {
               // return "Fail to enable discoverable mode.";
            }
        } else if (resultCode == Finished_Activity) {
            bluetoothAdapter.disable();
            list.clear();
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
                list.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
                ra.call(list);
            }
        }
    };
}
class receiveadapter extends Observable
{
    public void call(ArrayList<String> s)
    {
        setChanged();
        notifyObservers(s);
    }
}
