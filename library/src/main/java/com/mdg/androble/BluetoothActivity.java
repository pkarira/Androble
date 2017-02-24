package com.mdg.androble;

/**
 * @author Pulkit Karira
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mdg.androble.utils.BluetoothDeviceInfo;

import java.util.ArrayList;


public abstract class BluetoothActivity extends AppCompatActivity {

    private static final int ENABLE_BT_REQUEST_CODE = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;
    private static final int DISCOVERABLE_DURATION = 300;

    protected BluetoothAdapter bluetoothAdapter;
    public ArrayList<BluetoothDeviceInfo> deviceArrayList;


    protected void enableBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, ENABLE_BT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BT_REQUEST_CODE) {
            // Bluetooth successfully enabled!
            if (resultCode == Activity.RESULT_OK) {
                makeDiscoverable();
                discoverDevices();

                Toast.makeText(getApplicationContext(), R.string.bluetooth_enable,
                        Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getApplicationContext(), R.string.bluetooth_disable,
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == DISCOVERABLE_BT_REQUEST_CODE) {
            if (resultCode == DISCOVERABLE_DURATION) {
                Toast.makeText(getApplicationContext(), R.string.discoverable_enable,
                        Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(), R.string.discoverable_disable,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void makeDiscoverable(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
        startActivityForResult(discoverableIntent, DISCOVERABLE_BT_REQUEST_CODE);
    }

    protected void discoverDevices() {
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        if (!bluetoothAdapter.startDiscovery()) {
            Toast.makeText(getApplicationContext(), R.string.bluetooth_start_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        this.unregisterReceiver(broadcastReceiver);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceArrayList.add(new BluetoothDeviceInfo(bluetoothDevice.getName(),
                        bluetoothDevice.getAddress()));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
    }

}
