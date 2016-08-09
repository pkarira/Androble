# Bluetooth_Library
<h2>INTRODUCTION</h2>

 The library is capable of connecting around 4 devices to the main server device.

<h2>ABOUT LIBRARY</h2>

The Library uses Android's Bluetooth API and establishes RFCOMM channels. The server listens for various clients using a unique UUID and the client having the same UUID is connected to the server. In Multiple Devices server creates 4 RFCOMM channels with 4 different UUIDs and then each connection is maintained on a different thread by the server so that if anyhow connection with a particular client fails then the connection with other clients is uninterrupted.

BluetoothManager class is the master class of this library and contains the followind functions:
<ul style="list-style-type:disc">
  <li>public void Type(String t) &nbsp&nbsp&nbsp//it sets either you want to connect as server or client</li>
  <li>public void scanClients()  &nbsp&nbsp&nbsp  //it starts scanning clients on server side</li>
  <li>public void connectTo(String s) &nbsp&nbsp&nbsp  //it lets cleint to connect to the desired server from list</li>
  <li>public void sendText(String s)  &nbsp&nbsp&nbsp  //it allows to send msg from client to connected server</li>
  <li>public void sendText(String s,int id)  &nbsp&nbsp&nbsp  //it allows to send msg from server to client specifying the id of client</li>
  <li> public StringBuilder deviceList() &nbsp&nbsp&nbsp     // server can fetch list of all connected clients with respective ids</li>
   <li>public void setMessageObject(Object myObject)&nbsp&nbsp&nbsp     //it sets the observer object that fetches received messages</li>
  <li>public void setListObject(Object myObject) &nbsp&nbsp&nbsp       //it sets the observer object that fetches list of detected devices for client side</li>
  
  <h2>HOW TO USE</h2>
</ul>





