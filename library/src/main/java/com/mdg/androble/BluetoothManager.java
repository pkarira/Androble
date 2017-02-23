package com.mdg.androble;

import java.io.IOException;

/**
 * Created by this pc on 02-08-2016.
 */
public class BluetoothManager {

    public enum ConnectionType {
        CLIENT, SERVER
    }

    public ConnectionType connectionType;
    //BluetoothSocket socketForClient;
    public static SocketManger serverSocket;
    ClientSocket clientSocket;
    public static Object recieve_msg;
    public static Object device_list;
    private static BluetoothManager bManager = null;


    private BluetoothManager() {
        serverSocket = new SocketManger();
        clientSocket = new ClientSocket();
    }

    public static BluetoothManager getInstance() {
        if (bManager == null) {
            bManager = new BluetoothManager();
        }
        return bManager;
    }

    /**
     *
     * @param msgObject takes msg obj
     * @param listObject dcs
     * @param connectionType sdfd
     */
    public void init(Object msgObject,Object listObject, ConnectionType connectionType){
        setType(connectionType);
        setListObject(listObject);
        setMessageObject(msgObject);
    }

    public void setType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public void setListObject(Object myObject)

    {
        device_list = myObject;
    }

    public void setMessageObject(Object myObject) {
        recieve_msg = myObject;
    }

    void scanClients() {
        serverSocket.startConnection(BluetoothActivity.bluetoothAdapter);
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
        if (id <= (serverSocket.socketCounter + 1)) {
            serverSocket.write(s1, id);
        }
    }

    public void clientToClient(String s1, int id) {
        if (id <= (serverSocket.socketCounter + 1)) {
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
            serverSocket.disconnectServer();
            return "DISCONNECTED";
        }
        return null;
    }
}
