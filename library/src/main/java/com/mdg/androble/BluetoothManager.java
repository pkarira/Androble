package com.mdg.androble;

import android.content.Context;

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
    private ClientSocket clientSocket;
    private SocketManager socketManager;
    private ArrayList<MessageReceiveListener> messageReceiveListeners;


    private BluetoothManager(Context context) {
        socketManager = new SocketManager(context);
        clientSocket = new ClientSocket();

        messageReceiveListeners = new ArrayList<>();
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
        socketManager.startConnection(BluetoothActivity.bluetoothAdapter);
    }

    public void connectTo(String s) {
        clientSocket.startConnection(BluetoothActivity.bluetoothAdapter, s);
    }

    public void sendText(String s) {
        if (clientSocket.check.equals(("connected")) && connectionType.equals(ConnectionType.CLIENT)) {
            clientSocket.write(ServerSocket.my_id + ":" + s);
        }
    }

    public String getId() {
        return ServerSocket.my_id;
    }

    public void sendText(String s1, int id) {
        if (id <= (socketManager.getSocketCounter() + 1)) {
            socketManager.write(s1, id);
        }
    }

    public void clientToClient(String s1, int id) {
        if (id <= (socketManager.getSocketCounter() + 1)) {
            clientSocket.write("<" + id + ">" + s1);
        }
    }

    public String getAllConnectedDevices() {
        if (connectionType.equals(ConnectionType.CLIENT)) {
            clientSocket.write("(" + ServerSocket.my_id + ")");
            return null;
        } else {
            return ServerSocket.sb.substring(0);
        }
    }

    public String disconnect() throws IOException {
        if (connectionType.equals(ConnectionType.CLIENT)) {
            clientSocket.disconnectClient();
            return "DISCONNECTED";
        } else if (connectionType.equals(ConnectionType.SERVER)) {
            socketManager.disconnectServer();
            return "DISCONNECTED";
        }
        return null;
    }

    public void addOnMessageReceiveListener(MessageReceiveListener messageReceiveListener){
        messageReceiveListeners.add(messageReceiveListener);
    }

    public void removeOnMessageReceiveListener(MessageReceiveListener messageReceiveListener){
        messageReceiveListeners.remove(messageReceiveListener);
    }

}
