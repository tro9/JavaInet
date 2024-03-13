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
import javax.swing.*;
import java.awt.*;
import java.net.*;

public class UDPServerGUI {
    private DatagramSocket serverSocket;
    private JTextArea chatArea;

    public UDPServerGUI() {
        JFrame frame = new JFrame("UDP Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        try {
            final int SERVER_PORT = 12345;
            serverSocket = new DatagramSocket(SERVER_PORT);
            chatArea.append("Server is running...\n");

            while (true) {
                // Receive message from client
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // Convert byte array to string
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Print received message
                chatArea.append("Client: " + message + "\n");

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UDPServerGUI::new);
    }
}
