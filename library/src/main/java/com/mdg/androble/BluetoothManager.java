package com.mdg.androble;

import android.content.Context;

import com.mdg.androble.listeners.ConnectionStatusListener;
import com.mdg.androble.listeners.MessageReceiveListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Pulkit Karira, Deepankar Agrawal
 *
 * This class act as main manager.
 */

public class BluetoothManager {

    public enum ConnectionType {
        CLIENT, SERVER
    }

    private ConnectionType connectionType;

    private static BluetoothManager bluetoothManager;
    private ClientSocketManager clientSocketManager;
    public static ServerSocketManager serverSocketManager;

    private MessageReceiveListener messageReceiveListener;
    private ConnectionStatusListener connectionStatusListener;


    private BluetoothManager(Context context) {
        serverSocketManager = new ServerSocketManager(context,
                connectionStatusListener, messageReceiveListener);
        clientSocketManager = new ClientSocketManager();
    }

    public static BluetoothManager getInstance(Context context) {
        if (bluetoothManager == null){
            bluetoothManager = new BluetoothManager(context);
        }
        return bluetoothManager;
    }

    /**
     *
     * @param connectionType connection type, SERVER or CLIENT
     * @param messageReceiveListener listener object to receive messages
     */

    public void init(ConnectionType connectionType, MessageReceiveListener messageReceiveListener){
        setType(connectionType);
        addOnMessageReceiveListener(messageReceiveListener);
    }

    public void setType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }


    void scanClients() {
        serverSocketManager.startConnection(BluetoothActivity.bluetoothAdapter);
    }

    public void connectTo(String s) {
        clientSocketManager.startConnection(BluetoothActivity.bluetoothAdapter, s);
    }

    public void sendText(String s) {
        if (clientSocketManager.check.equals(("connected")) && connectionType.equals(ConnectionType.CLIENT)) {
            clientSocketManager.write(ServerSocket.my_id + ":" + s);
        }
    }

    public String getId() {
        return ServerSocket.my_id;
    }

    public void sendText(String s1, int id) {
        if (id <= (serverSocketManager.getSocketCounter() + 1)) {
            serverSocketManager.write(s1, id);
        }
    }

    public void clientToClient(String s1, int id) {
        if (id <= (serverSocketManager.getSocketCounter() + 1)) {
            clientSocketManager.write("<" + id + ">" + s1);
        }
    }

    public String getAllConnectedDevices() {
        if (connectionType.equals(ConnectionType.CLIENT)) {
            clientSocketManager.write("(" + ServerSocket.my_id + ")");
            return null;
        } else {
            return ServerSocket.sb.substring(0);
        }
    }

    public String disconnect() throws IOException {
        if (connectionType.equals(ConnectionType.CLIENT)) {
            clientSocketManager.disconnectClient();
            return "DISCONNECTED";
        } else if (connectionType.equals(ConnectionType.SERVER)) {
            serverSocketManager.disconnectServer();
            return "DISCONNECTED";
        }
        return null;
    }

    public void addOnMessageReceiveListener(MessageReceiveListener messageReceiveListener){
        this.messageReceiveListener = messageReceiveListener;
    }

}
