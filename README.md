# Distributed Computing Voting System (DC Mini Project)

## Overview

This project is a distributed online voting system designed to facilitate secure and efficient voting processes for various elections. It utilizes distributed computing principles to ensure scalability, reliability, and fault tolerance.

## Getting Started

Follow these steps to set up and run the system:

### Step 1: Start RMI Registry

Open a command prompt and start the RMI registry by running the following command: rmiregistry

### Step 2: Compile Class Files

Compile all class files using the following command. Make sure to include the MongoDB Java driver JAR file (`mongo-java-driver-3.12.14.jar`) in the classpath.


### Step 3: Run Load Balancer

Start the load balancer by running the following command: java LoadBalancer

### Step 4: Run Server

Run the server, specifying the port. For example: java -cp ".;mongo-java-driver-3.12.14.jar" Server 1100

Make sure to include the MongoDB Java driver JAR file (`mongo-java-driver-3.12.14.jar`) in the classpath.

### Step 5: Run the Client
Finally, run the client application using the following command: java Client


## Contributors

- [Nikhil Prajapati](https://github.com/nik-prajapti)








