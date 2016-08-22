package com.example.thispc.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Discovery {
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
           final String msg = ((receivemsg)observable).getMessage();
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
            if(((deviceList)observable).getContent().equals("bluetooth enabled"))
            {
                bm.scanClients();
            }else
            adapter.add(((deviceList)observable).getContent());
        }
    }
}