/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuan7;

/**
 *
 * @author Admin
 */
import java.net.*;

public class UDPServer {
    public static void main(String[] args) {
        final int SERVER_PORT = 12345;

        try {
            // Create a DatagramSocket
            DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);

            System.out.println("Server is running...");

            while (true) {
                // Receive message from client
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // Convert byte array to string
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Print received message
                System.out.println("Client: " + message);

                // Echo message back to client
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
