/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuan7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1";
        final int SERVER_PORT = 12345;

        try {
            // Create a DatagramSocket
            DatagramSocket clientSocket = new DatagramSocket();

            // Get user input
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Enter message: ");
                String message = reader.readLine();

                // Send message to server
                byte[] sendData = message.getBytes();
                InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
                clientSocket.send(sendPacket);

                // Receive echoed message from server
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                // Convert byte array to string
                String echoedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Print echoed message
                System.out.println("Server: " + echoedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
