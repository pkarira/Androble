# Androble
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
  </ul>
  <h2>HOW TO USE</h2>
  
Firstly, the activity in which you want to scan for devices,extend it by Discovery instead of Activity/AppCompatActivity like:<br>
public class MainActivity extends Discovery{}<br>
then create an observer class for receiving the list of devices scanned by bluetooth like:<br>
class DeviceList implements Observer {<br>
        @Override<br>
        public void update(Observable observable, Object data) {<br>
            adapter.add(((deviceList)observable).getNewDevice());<br>
        }<br>
    }<br>
    then create another oberver class for receiving text messages like :<br>
    class receiceMessage implements Observer {<br>
        @Override<br>
        public void update(Observable observable, Object data) {<br>
           String msg = ((receivemsg)observable).getMessage();<br>
           Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();<br>
           //do whatever you want to do with received message<br>
        }<br>
    }<br>
    Now get instance of Bluetooth manager class like :<br>
    BluetoothManager bluetoothManager= BluetoothManager.getInstance();<br>
    now create objects of DeviceList and receiceMessage class<br>
    receiceMessage  rm = new receiceMessage();<br>
    DeviceList dl=new DeviceList();<br><br>
   Connecting As Server:-<br><br>
    Call Type funtion of BluetoothManager class and pass "server" as parameter:
    bluetoothManager.Type("server");<br>//now you will be able to connect upto 4 devices<br>
    pass receiveMessage Object to setMessageObject(receiceMessage rm) like:<br>
    bluetoothManager.setMessageObject(rm);<br>
    For sending Message to any connected client use:<br>
    bluetoothManager.sendText("your message",playerId)<br>
    To get the ID's of all connected client call deviceList() described above.<br><br>
    Connecting As Client:-<br><br>
    bluetoothManager.Type("client");//now you will be able to a single server device
    pass receiveMessage Object to setMessageObject(receiceMessage rm) like:<br>
    bluetoothManager.setMessageObject(rm);<br>
    pass DeviceList Object to setListObject(DeviceList dl) like:<br>
    bluetoothManager.setListObject(dl);<br>
    Now from the obtained list of available devices select server device you want to connect to and pass its complete string to connectTo(String s) function like:<br>
    let listView is your ListView Object , <br>
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {<br>
            @Override<br>
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {<br>
                String itemValue = (String) listView.getItemAtPosition(position);<br>
                bluetoothManager.connectTo(itemValue);<br>
    For sending Message to any connected server use:<br>
bluetoothManager.sendText("your message")<br>
    <h2>App Using This Library</h2>
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-15-24.png" width="300">
<img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-15-43.png" width="300">
  <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-16-03.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-16-13.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-17-08.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-18-07.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-18-17.png" width="300">
<img src ="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/multidevice_screenshots/Screenshot_2016-06-30-02-14-03.png" width="300">
   ## License

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

    
    
    

