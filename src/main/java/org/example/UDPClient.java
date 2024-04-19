package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    public static void main(String[] args) throws Exception {
        // Create a DatagramSocket
        DatagramSocket socket = new DatagramSocket();

        // Prepare the data to be sent
        String message = "Hello, Netty server!";
        byte[] buffer = message.getBytes();

        // Specify the server's IP and port
        InetAddress serverIP = InetAddress.getByName("localhost");
        int serverPort = 8080;  // replace with your server's port

        // Create a DatagramPacket for sending data
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverIP, serverPort);

        // Send the packet
        socket.send(packet);

        // Prepare a DatagramPacket for receiving data
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

        // Receive the response
        socket.receive(receivePacket);

        // Print the response
        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Received from server: " + response);

        // Close the socket
        socket.close();
    }
}
