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

public class MainActivity extends BluetoothActivity {
  BluetoothManager bm;
    receiceMessage rm;
    DeviceList dl;
    String type = "";
    ListView l;
    int c=0;
    EditText et1;
    EditText et2;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bm= BluetoothManager.getInstance();
        rm = new receiceMessage();
        dl=new DeviceList();
        l = (ListView) findViewById(R.id.listView);
        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) l.getItemAtPosition(position);
                bm.connectTo(itemValue);
            }
        });
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        l.setAdapter(adapter);
    }

    public void client(View v) {
        type = "client";
        bm.Type(type);
    }

    public void server(View v) {
        type = "server";
        bm.Type(type);
    }
public void disconnect(View v)  {
    try {
        Toast.makeText(getApplicationContext(), bm.disconnect(),Toast.LENGTH_LONG).show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public void clienttoclient (View v)  {
        bm.clientToClient(et1.getText().toString(), Integer.parseInt(et2.getText().toString()));
    }
    public void devicelist(View v)
    {
        Toast.makeText(getApplicationContext(), bm.getAllConnectedDevices(),Toast.LENGTH_LONG).show();
    }
    public void start(View v) {
         bm.setMessageObject(rm);
         bm.setListObject(dl);
        if (type.equals("client")) {
            enableBluetooth();
        }
        if (type.equals("server")) {
           enableBluetooth();
        }
    }
public void send(View v)
{
    if (type.equals("client")) {
        bm.sendText(et1.getText().toString());
    }
    if (type.equals("server")) {
        bm.sendText(et1.getText().toString(),Integer.parseInt(et2.getText().toString()));
    }
}
    class receiceMessage implements Observer {
        @Override
        public void update(Observable observable, Object data) {
            Log.e("pulkit", "in received");
           final String msg = ((ReceiveMsg)observable).getMessage();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    class DeviceList implements Observer {
        @Override
        public void update(Observable observable, Object data) {
            if(((com.mdg.androble.DeviceList)observable).getContent().equals("bluetooth enabled"))
            {
                bm.scanClients();
            }else
            adapter.add(((com.mdg.androble.DeviceList)observable).getContent());
        }
    }
}