package com.mdg.androble;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mdg.androble.listeners.MessageReceiveListener;
import com.mdg.androble.network.BTClient;
import com.mdg.androble.network.BTServer;
import com.mdg.androble.testlibrary.R;

public class MainActivity extends BluetoothActivity implements MessageReceiveListener {

    private BluetoothManager.ConnectionType type;
    private ListView listView;
    private EditText et1, et2;

    public BluetoothManager bluetoothManager;
    public BTServer btServer;
    public BTClient btClient;
    
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                btClient.connect(itemValue);
            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    public void client(View v) {
        type = BluetoothManager.ConnectionType.CLIENT;
    }

    public void server(View v) {
        type = BluetoothManager.ConnectionType.SERVER;
    }

    public void disconnect(View v)  {

        if (type.equals("client")) {
            btClient.disconnect();
        }else{
            btServer.disconnect();
        }
        Toast.makeText(getApplicationContext(), "Disconnected",Toast.LENGTH_LONG).show();
    }

    public void clientToClient(View v)  {
        btClient.clientToClient(et1.getText().toString(), Integer.parseInt(et2.getText().toString()));
    }

    public void devicelist(View v) {
        int noOfDevices;
        if (type.equals("client")) {
            noOfDevices = btClient.getAllConnectedDevices();
        }else{
            noOfDevices = btServer.getAllConnectedDevices();
        }

        Toast.makeText(getApplicationContext(), noOfDevices, Toast.LENGTH_LONG).show();
    }

    public void start(View v){
        enableBluetooth();
        bluetoothManager = BluetoothManager.getInstance();

        if (type.equals("client")) {
            btClient = (BTClient) bluetoothManager.createSocket(type);
        }else{
            btServer = (BTServer) bluetoothManager.createSocket(type);
        }

    }

    public void send(View v) {
        if (type.equals("client")) {
            btClient.sendText(et1.getText().toString());
        }
        if (type.equals("server")) {
            btServer.sendText(et1.getText().toString(), Integer.parseInt(et2.getText().toString()));
        }
    }

    @Override
    public void onReceive(int id, String message) {

    }

//    private class receiveMessage implements Observer {
//        @Override
//        public void update(Observable observable, Object data) {
//            Log.e(TAG, "in received");
//           final String msg = ((ReceiveMessage)observable).getMessage();
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    }

}