# Bluetooth_Library
<h2>INTRODUCTION</h2>

 The library is capable of connecting around 4 devices to the main server device.

<h2>ABOUT CODE</h2>

The Library uses Android's Bluetooth API and establishes RFCOMM channels. The server listens for various clients using a unique UUID and the client having the same UUID is connected to the server. In Multiple Devices server creates 4 RFCOMM channels with 4 different UUIDs and then each connection is maintained on a different thread by the server so that if anyhow connection with a particular client fails then the connection with other clients is uninterrupted.




