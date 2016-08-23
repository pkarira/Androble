# Androble

> Connect nearby devices to your server !

Androble uses Android's Bluetooth API and establishes RFCOMM channels. The server listens for various clients using a unique UUID and the client having the same UUID is connected to the server. In multiple devices, server creates 4 RFCOMM channels with 4 different UUIDs and then each connection is maintained on a different thread by the server so that if anyhow connection with a particular client fails then the connection with other clients is uninterrupted.


## Reference

BluetoothManager class is the master class of this library and contains the following functions:

```java
public void Type(String t)
```
Sets either you want to connect as server or client.

```java
public void scanClients()
```
Starts scanning clients on server side.

```java
public void connectTo(String s)
```
Lets client to connect to the desired server from list.

```java
public void sendText(String s)
```
Allows to send message from client to connected server.

```java
public void sendText(String s,int id)
```
Allows to send message from server to client specifying the id of client.

```java
public StringBuilder deviceList()
```
Server can fetch list of all connected clients with respective ids.

```java
public void setMessageObject(Object myObject)
```
Sets the observer object that fetches received messages.

```java
public void setListObject(Object myObject)
```
Sets the observer object that fetches list of detected devices for client side.


## Usage

Extend the activity in which you want to scan for devices by Discovery instead of Activity / AppCompatActivity like:

```java
public class MainActivity extends Discovery{}
```

then create a class which implements Observer interface. This facilitates receiving of the list of devices scanned by Bluetooth.

```java
class DeviceList implements Observer {
    @Override
    public void update(Observable observable, Object data) {
        adapter.add(((deviceList)observable).getNewDevice());
    }
}
```

then create another class, again implementing the Observer interface, for receiving text messages.

```java
class ReceiveMessage implements Observer {
    @Override
    public void update(Observable observable, Object data) {
       String msg = ((receivemsg)observable).getMessage();
       Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
       //do whatever you want to do with received message
    }
}
```

Now get instance of Bluetooth manager classike :<br>
BluetoothManager bluetoothManager= BluetoothManager.getInstance();<br><br>
now create objects of DeviceList and receiceMessage class<br>
receiceMessage  rm = new receiceMessage();<br><br>
DeviceList dl=new DeviceList();<br><br>
<ul style="list-style-type:disc">
   <li>CONNECTING AS SERVER:-<br><br>
    Call Type funtion of BluetoothManager class and pass "server" as parameter:
    bluetoothManager.Type("server");<br>//now you will be able to connect upto 4 devices<br><br>
    pass receiveMessage Object to setMessageObject(receiceMessage rm) like:<br>
    bluetoothManager.setMessageObject(rm);<br><br>
    For sending Message to any connected client use:<br>
    bluetoothManager.sendText("your message",playerId)<br><br>
    To get the ID's of all connected client call deviceList() described above.<br><br>
    </li>
    <li>CONNECTING AS CLIENT:-<br><br>
    bluetoothManager.Type("client");//now you will be able to a single server device
    pass receiveMessage Object to setMessageObject(receiceMessage rm) like:<br>
    bluetoothManager.setMessageObject(rm);<br><br>
    pass DeviceList Object to setListObject(DeviceList dl) like:<br>
    bluetoothManager.setListObject(dl);<br><br>
    Now from the obtained list of available devices select server device you want to connect to and pass its complete string to connectTo(String s) function like:<br>
    let listView is your ListView Object , <br><br>
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {<br>
            @Override<br>
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {<br>
                String itemValue = (String) listView.getItemAtPosition(position);<br>
                bluetoothManager.connectTo(itemValue);<br><br>
    For sending Message to any connected server use:<br>
bluetoothManager.sendText("your message")<br><br>
</li>
    <h2>App Using This Library</h2>
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-15-24.png" width="300">
<img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-15-43.png" width="300">
  <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-16-03.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-16-13.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-17-08.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-18-07.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-18-17.png" width="300">
<img src ="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/multidevice_screenshots/Screenshot_2016-06-30-02-14-03.png" width="300">
# License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2016 Pulkit Karira

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

    
    
    

