# Sample Monitoring system that stores the data and connection properties of an IOT system with the help of OPC Foundation UA JAVA code stack

* To build jar or executables : 
```
mvn clean install
```
* To start the sample server : 
```
java -jar opc-ua-security-0.0.1.jar 
```
* To run sample client, pass the parameters <Protocol Type> , <IP Address Where the system is deployed> :

```
java -cp opc-ua-security-0.0.1.jar org.dfki.iot.attack.client.RoverAClient opc.tcp 10.24.0.101
```