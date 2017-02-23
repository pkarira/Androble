package com.mdg.androble;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mdg.androble.testlibrary.R;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends BluetoothActivity implements MessageReceiveListener{

    private receiveMessage receiveMessage;
    private BluetoothManager.ConnectionType type;
    private ListView listView;
    private EditText et1, et2;
    private ArrayAdapter arrayAdapter;
    
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiveMessage = new receiveMessage();

        listView = (ListView) findViewById(R.id.listView);
        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                bluetoothManager.connectTo(itemValue);
            }
        });
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    public void client(View v) {
        type = BluetoothManager.ConnectionType.CLIENT;
    }

    public void server(View v) {
        type = BluetoothManager.ConnectionType.SERVER;
    }

    public void disconnect(View v)  {
        try {
            Toast.makeText(getApplicationContext(), bluetoothManager.disconnect(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientToClient(View v)  {
        bluetoothManager.clientToClient(et1.getText().toString(), Integer.parseInt(et2.getText().toString()));
    }

    public void devicelist(View v) {
        Toast.makeText(getApplicationContext(), bluetoothManager.getAllConnectedDevices(),Toast.LENGTH_LONG).show();
    }

    public void start(View v){
        bluetoothManager.init(receiveMessage,type);
        enableBluetooth();
    }

    public void send(View v) {
        if (type.equals("client")) {
            bluetoothManager.sendText(et1.getText().toString());
        }
        if (type.equals("server")) {
            bluetoothManager.sendText(et1.getText().toString(),Integer.parseInt(et2.getText().toString()));
        }
    }

    @Override
    public void onMessageReceived(int id, String message) {

    }

    private class receiveMessage implements Observer {
        @Override
        public void update(Observable observable, Object data) {
            Log.e(TAG, "in received");
           final String msg = ((ReceiveMessage)observable).getMessage();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}