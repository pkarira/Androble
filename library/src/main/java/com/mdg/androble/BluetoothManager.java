package com.mdg.androble;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.mdg.androble.listeners.ConnectionStatusListener;
import com.mdg.androble.listeners.MessageReceiveListener;
import com.mdg.androble.network.BTClient;
import com.mdg.androble.network.ServerSocket;
import com.mdg.androble.network.BTServer;

import java.io.IOException;

/**
 * @author Pulkit Karira, Deepankar Agrawal
 *
 * This class act as main manager.
 */

public class BluetoothManager {

    public enum ConnectionType {
        CLIENT, SERVER
    }

    public  BluetoothAdapter bluetoothAdapter;
    private BTClient btClient;
    private BTServer btServer;

    private ConnectionType connectionType;

    private MessageReceiveListener messageReceiveListener;
    private ConnectionStatusListener connectionStatusListener;

    /**
     * Singleton instance of this class
     */
    private static BluetoothManager bluetoothManager = null;


    private BluetoothManager(ConnectionType connectionType, ConnectionStatusListener
            connectionStatusListener, MessageReceiveListener messageReceiveListener) {

        this.connectionType = connectionType;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        setOnCallStatusListener(connectionStatusListener);
        setOnMessageReceiveListener(messageReceiveListener);

        startConnection();
    }

    /**
     *
     * @return BluetoothManger instance
     *
     * returns null if BluetoothManager is not initialized.
     */
    public static BluetoothManager getInstance() {
        return bluetoothManager;
    }

    /**
     * @param connectionType type of connection SERVER or CLIENT
     *
     * Initializes BluetoothManager instance
     */
    public static BluetoothManager createInstance(ConnectionType connectionType){
        if (bluetoothManager == null){
            bluetoothManager = new BluetoothManager(connectionType, null, null);
        }

        return bluetoothManager;
    }

    /**
     *
     * @param context context of class
     * @param connectionType type of connection SERVER or CLIENT
     *
     * Initializes BluetoothManager instance
     */
    public static BluetoothManager createInstance(Context context, ConnectionType connectionType){
        if (bluetoothManager == null){
            ConnectionStatusListener connection = null;
            MessageReceiveListener message = null;

            if(context instanceof ConnectionStatusListener){
               connection = (ConnectionStatusListener) context;
            }
            if(context instanceof MessageReceiveListener){
                message = (MessageReceiveListener) context;
            }
            bluetoothManager = new BluetoothManager(connectionType, connection, message);
        }

        return bluetoothManager;
    }


    public void connectTo(String id) {
        btClient.connect(id);
    }

    public void sendText(String s) {
        if (btClient.check.equals(("connected")) && connectionType.equals(ConnectionType.CLIENT)) {
            btClient.write(ServerSocket.myId + ":" + s);
        }
    }

    public String getId() {
        return ServerSocket.myId;
    }

    public void sendText(String s1, int id) {
        if (id <= (btServer.getSocketCounter() + 1)) {
            btServer.write(s1, id);
        }
    }

    public void clientToClient(String s1, int id) {
        if (id <= (btServer.getSocketCounter() + 1)) {
            btClient.write("<" + id + ">" + s1);
        }
    }

    public String getAllConnectedDevices() {
        if (connectionType.equals(ConnectionType.CLIENT)) {
            btClient.write("(" + ServerSocket.myId + ")");
            return null;
        } else {
            return ServerSocket.sb.substring(0);
        }
    }

    public String disconnect() throws IOException {
        if (connectionType.equals(ConnectionType.CLIENT)) {
            btClient.disconnect();
            return "DISCONNECTED";
        } else if (connectionType.equals(ConnectionType.SERVER)) {
            btServer.disconnect();
            return "DISCONNECTED";
        }
        return null;
    }

    /**
     *
     * @param messageReceiveListener instance of the listener class
     */
    public void setOnMessageReceiveListener(MessageReceiveListener messageReceiveListener){
        this.messageReceiveListener = messageReceiveListener;
    }

    /**
     *
     * @param connectionStatusListener instance of listener class
     */
    public void setOnCallStatusListener(ConnectionStatusListener connectionStatusListener){
        this.connectionStatusListener = connectionStatusListener;
    }

    /**
     * starts connection process depending on type of connection
     */
    private void startConnection(){
        if(connectionType == ConnectionType.SERVER){
            btServer = new BTServer(connectionStatusListener,
                    messageReceiveListener);
            btServer.connect();
        }else{
            btClient = new BTClient();
        }
    }

}
