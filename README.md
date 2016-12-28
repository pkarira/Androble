# Androble
 
 > Connect nearby devices to your server !
 
 Androble uses Android's Bluetooth API and establishes RFCOMM channels. The server listens for various clients using a unique UUID and the client having the same UUID is connected to the server. In multiple devices, server creates 4 RFCOMM channels with 4 different UUIDs and then each connection is maintained on a different thread by the server so that if anyhow connection with a particular client fails then the connection with other clients is uninterrupted.
 
 
 <h3>Dependency</h3>
 Add dependency to build.gradle of your app
 ```java
 dependencies
 {
 compile 'com.mdg.androble:library:0.1.2'
 }
 ```
 
 <h3>Reference</h3>
 
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
 public String clientToClient(String s, int id)
 ```
 Allows to send message from a client to another client specifying the id of recepient client.
 
 ```java
 public String getAllConnectedDevices()
 ```
 To fetch list of all connected devices with respective ids.
 
 ```java
 public void setMessageObject(Object myObject)
 ```
 Sets the observer object that fetches received messages.
 
 ```java
 public void setListObject(Object myObject)
 ```
 Sets the observer object that fetches list of detected devices for client side.
 
 ```java
 public String disconnect()
 ```
 To disable your connection.
 
 
<h3>Usage</h3>
 
 Extend the activity in which you want to scan for devices by Discovery instead of Activity / AppCompatActivity like:
 
 ```java
 public class MainActivity extends Discovery{}
 ```
 
 then create a class which implements Observer interface. This facilitates receiving of the list of devices scanned by Bluetooth.
 
 ```java
 class DeviceList implements Observer {
     @Override
     if(((com.mdg.androble.DeviceList)observable).getContent().equals("bluetooth enabled"))
            {
                bluetoothManager.scanClients();
            }else
            arrayAdapter.add(((com.mdg.androble.DeviceList)observable).getContent());
     }
 }
 ```
 
 then create another class, again implementing the Observer interface, for receiving text messages.
 
 ```java
 class receiveMessage implements Observer {
     @Override
     public void update(Observable observable, Object data) {
        String msg = ((receivemsg)observable).getMessage();
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
        //do whatever you want to do with received message
     }
 }
 ```
 
 Now get instance of Bluetooth manager class like :<br>
 ```java
 BluetoothManager bluetoothManager= BluetoothManager.getInstance();
 ```
 now create objects of DeviceList and receiceMessage class like:<br>
 ```java
 receiveMessage  rm = new receiveMessage();
 ```
 ```java
  DeviceList dl=new DeviceList();
  ```
  pass these objects to setMessageObject and setListObject respectively , like:<br>
  ```
      bluetoothManager.setMessageObject(receiveMessage);
      bluetoothManager.setListObject(deviceList);
  ```
   <h3>Connecting as Server:-</h3>
   
Call Type funtion of BluetoothManager class and pass "SERVER" as parameter:<br>
         ```
           bluetoothManager.Type("SERVER");
        ```<br>
To send text to any client where id is Id of client:-<br>
         ```
        bluetoothManager.sendText("your message",playerId)
        ```<br>
To get the ID's of all connected devices call ```getAllConnectedDevices()``` described above.
    
   <h3>Connecting as Client:-</h3>
   ```
      bluetoothManager.Type("CLIENT");
   ```
  //now you will be able to connect to a single server device<br>
  ```
   bluetoothManager.clientToClient("your message",id)
   ```
  where id is the id of connected devices<br>
  To get the ID's of all connected devices call ```getAllConnectedDevices()``` described above.
  <h3>Sample code for MainActivity [Wiki](https://github.com/pkarira/Androble/wiki)</h3>
<h3>App Using This Library</h3>
<img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-15-24.png" width="300">
<img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-15-43.png" width="300">
  <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-16-03.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-16-13.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-17-08.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-18-07.png" width="300">
    <img src="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/twodevice_screenshots/Screenshot_2016-06-30-00-18-17.png" width="300">
<img src ="https://raw.githubusercontent.com/sdsmdg/Mobile-Quiz/1c1d413897edc614418e063bbb01078fe75bb2ae/app/src/main/assets/multidevice_screenshots/Screenshot_2016-06-30-02-14-03.png" width="300">
  <h3>License</h3>
  
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
```
