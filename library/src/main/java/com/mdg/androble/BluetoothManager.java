package com.mdg.androble;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.mdg.androble.listeners.ConnectionStatusListener;
import com.mdg.androble.listeners.MessageReceiveListener;
import com.mdg.androble.network.BTClient;
import com.mdg.androble.network.BTServer;
import com.mdg.androble.network.BTSocket;

/**
 * @author Pulkit Karira, Deepankar Agrawal
 *
 * This class act as main manager for server and client sockets.
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


    private BluetoothManager() {
    }

    /**
     *
     * @return BluetoothManger instance
     *
     * returns null if BluetoothManager is not initialized.
     */
    public static BluetoothManager getInstance() {
        if (bluetoothManager == null){
            bluetoothManager = new BluetoothManager();
        }
        return bluetoothManager;
    }

    /**
     * @param connectionType type of connection SERVER or CLIENT
     *
     * @return instance of server or client that is created
     *
     * Initializes BluetoothManager and socket instance to be used in connection
     */
    public BTSocket createSocket(ConnectionType connectionType){
        return createSocket(null, connectionType);
    }

    /**
     *
     * @param context context of class
     * @param connectionType type of connection SERVER or CLIENT
     *
     * @return instance of server or client that is created
     *
     * Initializes BluetoothManager and socket instance to be used in connection
     */
    public BTSocket createSocket(Context context, ConnectionType connectionType){

        this.connectionType = connectionType;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(context!=null){
            if(context instanceof ConnectionStatusListener){
                setOnCallStatusListener((ConnectionStatusListener) context);
            }
            if(context instanceof MessageReceiveListener){
                setOnMessageReceiveListener((MessageReceiveListener) context);
            }
        }

        startConnection();

        return bluetoothManager.returnSocket();
    }

    public BTClient getBTClient() {
        return btClient;
    }

    public BTServer getBTServer() {
        return btServer;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
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

    private BTSocket returnSocket(){
        if(connectionType == ConnectionType.SERVER){
           return btServer;
        }else{
            return btClient;
        }
    }

}
