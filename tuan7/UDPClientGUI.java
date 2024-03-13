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
import java.awt.event.*;
import java.net.*;

public class UDPClientGUI {
    private DatagramSocket clientSocket;
    private JTextArea chatArea;
    private JTextField messageField;

    public UDPClientGUI() {
        JFrame frame = new JFrame("UDP Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        try {
            final String SERVER_IP = "127.0.0.1";
            final int SERVER_PORT = 12345;
            clientSocket = new DatagramSocket();

            // Listener thread to receive messages
            Thread receiveThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            // Receive echoed message from server
                            byte[] receiveData = new byte[1024];
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            clientSocket.receive(receivePacket);

                            // Convert byte array to string
                            String echoedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                            // Print echoed message
                            chatArea.append("Server: " + echoedMessage + "\n");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            receiveThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try {
            final String SERVER_IP = "127.0.0.1";
            final int SERVER_PORT = 12345;

            // Get message from text field
            String message = messageField.getText();

            // Send message to server
            byte[] sendData = message.getBytes();
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
            clientSocket.send(sendPacket);

            // Print sent message
            chatArea.append("You: " + message + "\n");

            // Clear message field
            messageField.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UDPClientGUI::new);
    }
}

