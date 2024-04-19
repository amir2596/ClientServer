package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClient {

    public static void main(String[] args) throws Exception {
        // Create a DatagramSocket
        DatagramSocket socket = new DatagramSocket();

        // Prepare the scanner for console input
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a message to send to the server (type 'exit' to quit):");
            String message = scanner.nextLine();

            // If the user types 'exit', break the loop and close the socket
            if ("exit".equalsIgnoreCase(message)) {
                break;
            }

            // Prepare the data to be sent
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
        }

        // Close the socket
        socket.close();
    }
}
